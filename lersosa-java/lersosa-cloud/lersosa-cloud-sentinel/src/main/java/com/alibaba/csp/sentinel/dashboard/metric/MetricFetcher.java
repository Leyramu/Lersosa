/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.csp.sentinel.dashboard.metric;

import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.MetricEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppInfo;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.repository.metric.MetricsRepository;
import com.alibaba.csp.sentinel.node.metric.MetricNode;
import com.alibaba.csp.sentinel.util.StringUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 获取计算机的度量
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Component
public class MetricFetcher {

    /**
     * 无指标
     */
    public static final String NO_METRICS = "No metrics";

    /**
     * HTTP 状态码：200
     */
    private static final int HTTP_OK = 200;

    /**
     * 最后一次获取的间隔时间
     */
    private static final long MAX_LAST_FETCH_INTERVAL_MS = 1000 * 15;

    /**
     * 获取间隔时间
     */
    private static final long FETCH_INTERVAL_SECOND = 6;

    /**
     * 默认字符集
     */
    private static final Charset DEFAULT_CHARSET = Charset.forName(SentinelConfig.charset());

    /**
     * 指标 URL 路径
     */
    private final static String METRIC_URL_PATH = "metric";

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(MetricFetcher.class);

    /**
     * 最后一次获取时间
     */
    private final Map<String, AtomicLong> appLastFetchTime = new ConcurrentHashMap<>();

    /**
     * 存储指标的仓库
     */
    private final MetricsRepository<MetricEntity> metricStore;

    /**
     * 应用管理
     */
    private final AppManagement appManagement;

    /**
     * HTTP 客户端
     */
    private final CloseableHttpAsyncClient httpclient;

    /**
     * 调度服务
     */
    @SuppressWarnings("PMD.ThreadPoolCreationRule")
    private final ScheduledExecutorService fetchScheduleService = Executors.newScheduledThreadPool(1,
            new NamedThreadFactory("sentinel-dashboard-metrics-fetch-task", true));
    private final ExecutorService fetchService;
    private final ExecutorService fetchWorker;

    /**
     * 构造方法
     *
     * @param metricStore   存储指标的仓库
     * @param appManagement 应用管理
     */
    @Autowired
    public MetricFetcher(MetricsRepository<MetricEntity> metricStore, AppManagement appManagement) {
        this.metricStore = metricStore;
        this.appManagement = appManagement;
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 0;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new DiscardPolicy();
        fetchService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("sentinel-dashboard-metrics-fetchService", true), handler);
        fetchWorker = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("sentinel-dashboard-metrics-fetchWorker", true), handler);
        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(3000)
                .setSoTimeout(3000)
                .setIoThreadCount(Runtime.getRuntime().availableProcessors() * 2)
                .build();

        httpclient = HttpAsyncClients.custom()
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    protected boolean isRedirectable(final String method) {
                        return false;
                    }
                }).setMaxConnTotal(4000)
                .setMaxConnPerRoute(1000)
                .setDefaultIOReactorConfig(ioConfig)
                .build();
        httpclient.start();
        start();
    }

    /**
     * 开始任务
     */
    private void start() {
        long intervalSecond = 1;
        fetchScheduleService.scheduleAtFixedRate(() -> {
            try {
                fetchAllApp();
            } catch (Exception e) {
                logger.info("fetchAllApp error:", e);
            }
        }, 10, intervalSecond, TimeUnit.SECONDS);
    }

    /**
     * 写入指标
     *
     * @param map 指标数据
     */
    private void writeMetric(Map<String, MetricEntity> map) {
        if (map.isEmpty()) {
            return;
        }
        Date date = new Date();
        for (MetricEntity entity : map.values()) {
            entity.setGmtCreate(date);
            entity.setGmtModified(date);
        }
        metricStore.saveAll(map.values());
    }

    /**
     * 遍历每个 APP，然后提取该 APP 的所有计算机的指标
     */
    private void fetchAllApp() {
        List<String> apps = appManagement.getAppNames();
        if (apps == null) {
            return;
        }
        for (final String app : apps) {
            fetchService.submit(() -> {
                try {
                    doFetchAppMetric(app);
                } catch (Exception e) {
                    logger.error("fetchAppMetric error", e);
                }
            });
        }
    }

    /**
     * 获取 [startTime， endTime] 之间的指标，包括两端
     */
    private void fetchOnce(String app, long startTime, long endTime) {
        AppInfo appInfo = appManagement.getDetailApp(app);
        if (appInfo.isDead()) {
            logger.info("Dead app removed: {}", app);
            appManagement.removeApp(app);
            return;
        }
        Set<MachineInfo> machines = appInfo.getMachines();
        logger.debug("enter fetchOnce({}), machines.size()={}, time intervalMs [{}, {}]", app, machines.size(), startTime, endTime);
        if (machines.isEmpty()) {
            return;
        }
        final String msg = "fetch";
        AtomicLong unhealthy = new AtomicLong();
        final AtomicLong success = new AtomicLong();
        final AtomicLong fail = new AtomicLong();

        System.currentTimeMillis();
        final Map<String, MetricEntity> metricMap = new ConcurrentHashMap<>(16);
        final CountDownLatch latch = new CountDownLatch(machines.size());
        for (final MachineInfo machine : machines) {
            if (machine.isDead()) {
                latch.countDown();
                appManagement.getDetailApp(app).removeMachine(machine.getIp(), machine.getPort());
                logger.info("Dead machine removed: {}:{} of {}", machine.getIp(), machine.getPort(), app);
                continue;
            }
            if (!machine.isHealthy()) {
                latch.countDown();
                unhealthy.incrementAndGet();
                continue;
            }
            final String url = "http://" + machine.getIp() + ":" + machine.getPort() + "/" + METRIC_URL_PATH
                               + "?startTime=" + startTime + "&endTime=" + endTime + "&refetch=" + false;
            final HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
            httpclient.execute(httpGet, new FutureCallback<>() {

                /**
                 * 响应处理成功
                 */
                @Override
                public void completed(final HttpResponse response) {
                    try {
                        handleResponse(response, machine, metricMap);
                        success.incrementAndGet();
                    } catch (Exception e) {
                        logger.error(msg + " metric {} error:", url, e);
                    } finally {
                        latch.countDown();
                    }
                }

                /**
                 * 响应处理失败，例如连接超时等
                 */
                @Override
                public void failed(final Exception ex) {
                    latch.countDown();
                    fail.incrementAndGet();
                    httpGet.abort();
                    if (ex instanceof SocketTimeoutException) {
                        logger.error("Failed to fetch metric from <{}>: socket timeout", url);
                    } else if (ex instanceof ConnectException) {
                        logger.error("Failed to fetch metric from <{}> (ConnectionException: {})", url, ex.getMessage());
                    } else {
                        logger.error(msg + " metric {} error", url, ex);
                    }
                }

                /**
                 * 响应处理被取消，例如请求超时等
                 */
                @Override
                public void cancelled() {
                    latch.countDown();
                    fail.incrementAndGet();
                    httpGet.abort();
                }
            });
        }
        try {
            boolean await = latch.await(5, TimeUnit.SECONDS);
            if (!await) {
                logger.warn("Timeout when fetching metric for app {}", app);
            }
        } catch (Exception e) {
            logger.info(msg + " metric, wait http client error:", e);
        }
        writeMetric(metricMap);
    }

    /**
     * 处理响应
     */
    private void doFetchAppMetric(final String app) {
        long now = System.currentTimeMillis();
        long lastFetchMs = now - MAX_LAST_FETCH_INTERVAL_MS;
        if (appLastFetchTime.containsKey(app)) {
            lastFetchMs = Math.max(lastFetchMs, appLastFetchTime.get(app).get() + 1000);
        }
        lastFetchMs = lastFetchMs / 1000 * 1000;
        long endTime = lastFetchMs + FETCH_INTERVAL_SECOND * 1000;
        if (endTime > now - 1000 * 2) {
            return;
        }
        appLastFetchTime.computeIfAbsent(app, a -> new AtomicLong()).set(endTime);
        final long finalLastFetchMs = lastFetchMs;
        final long finalEndTime = endTime;
        try {
            fetchWorker.submit(() -> {
                try {
                    fetchOnce(app, finalLastFetchMs, finalEndTime);
                } catch (Exception e) {
                    logger.info("fetchOnce({}) error", app, e);
                }
            });
        } catch (Exception e) {
            logger.info("submit fetchOnce({}) fail, intervalMs [{}, {}]", app, lastFetchMs, endTime, e);
        }
    }

    /**
     * 处理响应
     */
    private void handleResponse(final HttpResponse response, MachineInfo machine,
                                Map<String, MetricEntity> metricMap) throws Exception {
        int code = response.getStatusLine().getStatusCode();
        if (code != HTTP_OK) {
            return;
        }
        Charset charset = null;
        try {
            String contentTypeStr = response.getFirstHeader("Content-type").getValue();
            if (StringUtil.isNotEmpty(contentTypeStr)) {
                ContentType contentType = ContentType.parse(contentTypeStr);
                charset = contentType.getCharset();
            }
        } catch (Exception ignore) {
        }
        String body = EntityUtils.toString(response.getEntity(), charset != null ? charset : DEFAULT_CHARSET);
        if (StringUtil.isEmpty(body) || body.startsWith(NO_METRICS)) {
            return;
        }
        String[] lines = body.split("\n");
        handleBody(lines, machine, metricMap);
    }

    /**
     * 处理响应内容
     */
    private void handleBody(String[] lines, MachineInfo machine, Map<String, MetricEntity> map) {
        if (lines.length < 1) {
            return;
        }

        for (String line : lines) {
            try {
                MetricNode node = MetricNode.fromThinString(line);
                if (shouldFilterOut(node.getResource())) {
                    continue;
                }
                String key = buildMetricKey(machine.getApp(), node.getResource(), node.getTimestamp());

                MetricEntity metricEntity = map.computeIfAbsent(key, s -> {
                    MetricEntity initMetricEntity = new MetricEntity();
                    initMetricEntity.setApp(machine.getApp());
                    initMetricEntity.setTimestamp(new Date(node.getTimestamp()));
                    initMetricEntity.setPassQps(0L);
                    initMetricEntity.setBlockQps(0L);
                    initMetricEntity.setRtAndSuccessQps(0, 0L);
                    initMetricEntity.setExceptionQps(0L);
                    initMetricEntity.setCount(0);
                    initMetricEntity.setResource(node.getResource());
                    return initMetricEntity;
                });
                metricEntity.addPassQps(node.getPassQps());
                metricEntity.addBlockQps(node.getBlockQps());
                metricEntity.addRtAndSuccessQps(node.getRt(), node.getSuccessQps());
                metricEntity.addExceptionQps(node.getExceptionQps());
                metricEntity.addCount(1);
            } catch (Exception e) {
                logger.warn("handleBody line exception, machine: {}, line: {}", machine.toLogString(), line);
            }
        }
    }

    /**
     * 构造 key
     */
    private String buildMetricKey(String app, String resource, long timestamp) {
        return app + "__" + resource + "__" + (timestamp / 1000);
    }

    /**
     * 是否过滤掉
     */
    private boolean shouldFilterOut(String resource) {
        return RES_EXCLUSION_SET.contains(resource);
    }

    /**
     * 默认过滤掉的资源
     */
    private static final Set<String> RES_EXCLUSION_SET = new HashSet<>() {{
        add(Constants.TOTAL_IN_RESOURCE_NAME);
        add(Constants.SYSTEM_LOAD_RESOURCE_NAME);
        add(Constants.CPU_USAGE_RESOURCE_NAME);
    }};
}
