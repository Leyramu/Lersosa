/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.client;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.command.CommandConstants;
import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.SentinelVersion;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.*;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterClientInfoVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.ClusterServerStateVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.ClusterStateSimpleEntity;
import com.alibaba.csp.sentinel.dashboard.util.AsyncUtils;
import com.alibaba.csp.sentinel.dashboard.util.VersionUtils;
import com.alibaba.csp.sentinel.slots.block.Rule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 与 Sentinel 客户端通信.
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
@Slf4j
@Component
public class SentinelApiClient {

    /**
     * 默认编码.
     */
    private static final Charset DEFAULT_CHARSET = Charset.forName(SentinelConfig.charset());

    /**
     * HTTP 头 Content-Type.
     */
    private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

    /**
     * HTTP 头 Content-Type URLEncoded.
     */
    private static final String HTTP_HEADER_CONTENT_TYPE_URLENCODED = ContentType.create(URLEncodedUtils.CONTENT_TYPE).toString();

    /**
     * 资源路径.
     */
    private static final String RESOURCE_URL_PATH = "jsonTree";

    /**
     * 集群节点.
     */
    private static final String CLUSTER_NODE_PATH = "clusterNode";

    /**
     * 获取规则.
     */
    private static final String GET_RULES_PATH = "getRules";

    /**
     * 设置规则.
     */
    private static final String SET_RULES_PATH = "setRules";

    /**
     * 获取系统规则.
     */
    private static final String GET_PARAM_RULE_PATH = "getParamFlowRules";

    /**
     * 设置参数规则.
     */
    private static final String SET_PARAM_RULE_PATH = "setParamFlowRules";

    /**
     * 获取集群模式.
     */
    private static final String FETCH_CLUSTER_MODE_PATH = "getClusterMode";

    /**
     * 设置集群模式.
     */
    private static final String MODIFY_CLUSTER_MODE_PATH = "setClusterMode";

    /**
     * 集群客户端配置.
     */
    private static final String FETCH_CLUSTER_CLIENT_CONFIG_PATH = "cluster/client/fetchConfig";

    /**
     * 集群客户端配置.
     */
    private static final String MODIFY_CLUSTER_CLIENT_CONFIG_PATH = "cluster/client/modifyConfig";

    /**
     * 集群服务器配置.
     */
    private static final String FETCH_CLUSTER_SERVER_BASIC_INFO_PATH = "cluster/server/info";

    /**
     * 集群服务器配置.
     */
    private static final String MODIFY_CLUSTER_SERVER_TRANSPORT_CONFIG_PATH = "cluster/server/modifyTransportConfig";

    /**
     * 集群服务器配置.
     */
    private static final String MODIFY_CLUSTER_SERVER_FLOW_CONFIG_PATH = "cluster/server/modifyFlowConfig";

    /**
     * 集群服务器命名空间设置.
     */
    private static final String MODIFY_CLUSTER_SERVER_NAMESPACE_SET_PATH = "cluster/server/modifyNamespaceSet";

    /**
     * 获取网关API.
     */
    private static final String FETCH_GATEWAY_API_PATH = "gateway/getApiDefinitions";

    /**
     * 更新网关规则.
     */
    private static final String MODIFY_GATEWAY_API_PATH = "gateway/updateApiDefinitions";

    /**
     * 获取网关流控规则.
     */
    private static final String FETCH_GATEWAY_FLOW_RULE_PATH = "gateway/getRules";

    /**
     * 更新网关流控规则.
     */
    private static final String MODIFY_GATEWAY_FLOW_RULE_PATH = "gateway/updateRules";

    /**
     * 规则类型.
     */
    private static final String FLOW_RULE_TYPE = "flow";

    /**
     * 降级规则类型.
     */
    private static final String DEGRADE_RULE_TYPE = "degrade";

    /**
     * 系统规则类型.
     */
    private static final String SYSTEM_RULE_TYPE = "system";

    /**
     * 授权规则类型.
     */
    private static final String AUTHORITY_TYPE = "authority";

    /**
     * 1.6.0版本.
     */
    private static final SentinelVersion VERSION160 = new SentinelVersion(1, 6, 0);

    /**
     * 1.7.1版本.
     */
    private static final SentinelVersion VERSION171 = new SentinelVersion(1, 7, 1);

    /**
     * HTTP 客户端.
     */
    private final CloseableHttpAsyncClient httpClient;

    /**
     * 应用管理.
     */
    private final AppManagement appManagement;

    /**
     * 构造函数.
     *
     * @param appManagement 应用管理
     */
    @Autowired
    public SentinelApiClient(AppManagement appManagement) {
        this.appManagement = appManagement;
        IOReactorConfig ioConfig = IOReactorConfig.custom().setConnectTimeout(3000).setSoTimeout(10000)
            .setIoThreadCount(Runtime.getRuntime().availableProcessors() * 2).build();
        httpClient = HttpAsyncClients.custom().setRedirectStrategy(new DefaultRedirectStrategy() {

            /**
             * 禁用重定向.
             */
            @Override
            protected boolean isRedirectable(final String method) {
                return false;
            }
        }).setMaxConnTotal(4000).setMaxConnPerRoute(1000).setDefaultIOReactorConfig(ioConfig).build();
        httpClient.start();
    }

    /**
     * 以 POST 方式构建 'HttpUriRequest'.
     *
     * @param url                        URL
     * @param params                     POST 参数
     * @param supportEnhancedContentType see {@link #isSupportEnhancedContentType(String, String, int)}
     * @return 请求
     */
    protected static HttpUriRequest postRequest(String url, Map<String, String> params, boolean supportEnhancedContentType) {
        HttpPost httpPost = new HttpPost(url);
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> list = new ArrayList<>(params.size());
            for (Entry<String, String> entry : params.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(list, Consts.UTF_8));
            if (!supportEnhancedContentType) {
                httpPost.setHeader(HTTP_HEADER_CONTENT_TYPE, HTTP_HEADER_CONTENT_TYPE_URLENCODED);
            }
        }
        return httpPost;
    }

    /**
     * 判断HTTP请求是否成功.
     *
     * @param statusCode HTTP状态码
     * @return 如果状态码在成功范围内则返回true，否则返回false
     */
    private boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    /**
     * 判断错误信息是否表明命令未找到.
     *
     * @param statusCode HTTP状态码
     * @param body       响应体字符串
     * @return 如果满足条件则返回true，否则返回false
     */
    private boolean isCommandNotFound(int statusCode, String body) {
        return statusCode == 400 && StringUtil.isNotEmpty(body) && body.contains(CommandConstants.MSG_UNKNOWN_COMMAND_PREFIX);
    }

    /**
     * 判断给定的应用、IP和端口是否支持POST请求.
     *
     * @param app  应用名称
     * @param ip   机器IP地址
     * @param port 机器端口号
     * @return 如果满足支持POST请求的条件则返回true，否则返回false
     */
    protected boolean isSupportPost(String app, String ip, int port) {
        return StringUtil.isNotEmpty(app) && Optional.ofNullable(appManagement.getDetailApp(app))
            .flatMap(e -> e.getMachine(ip, port))
            .flatMap(m -> VersionUtils.parseVersion(m.getVersion())
                .map(v -> v.greaterOrEqual(VERSION160)))
            .orElse(false);
    }

    /**
     * 检查目标实例.
     *
     * @param app  Target 应用程序名称
     * @param ip   目标节点的地址
     * @param port 目标节点的端口
     */
    protected boolean isSupportEnhancedContentType(String app, String ip, int port) {
        return StringUtil.isNotEmpty(app) && Optional.ofNullable(appManagement.getDetailApp(app))
            .flatMap(e -> e.getMachine(ip, port))
            .flatMap(m -> VersionUtils.parseVersion(m.getVersion())
                .map(v -> v.greaterOrEqual(VERSION171)))
            .orElse(false);
    }

    /**
     * 将参数映射转换为URL查询字符串.
     *
     * @param params 包含查询参数的映射
     * @return 包含查询字符串的StringBuilder对象
     */
    private StringBuilder queryString(Map<String, String> params) {
        StringBuilder queryStringBuilder = new StringBuilder();
        for (Entry<String, String> entry : params.entrySet()) {
            if (StringUtil.isEmpty(entry.getValue())) {
                continue;
            }
            String name = urlEncode(entry.getKey());
            String value = urlEncode(entry.getValue());
            if (name != null && value != null) {
                if (!queryStringBuilder.isEmpty()) {
                    queryStringBuilder.append('&');
                }
                queryStringBuilder.append(name).append('=').append(value);
            }
        }
        return queryStringBuilder;
    }

    /**
     * 对字符串进行URL编码.
     *
     * @param str 需要编码的字符串
     * @return 编码后的字符串
     */
    private String urlEncode(String str) {
        return URLEncoder.encode(str, DEFAULT_CHARSET);
    }

    /**
     * 获取HTTP响应体内容.
     *
     * @param response HTTP响应对象
     * @return 响应体内容字符串
     * @throws Exception 如果解析响应体时发生错误
     */
    private String getBody(HttpResponse response) throws Exception {
        Charset charset = null;
        try {
            String contentTypeStr = response.getFirstHeader(HTTP_HEADER_CONTENT_TYPE).getValue();
            if (StringUtil.isNotEmpty(contentTypeStr)) {
                ContentType contentType = ContentType.parse(contentTypeStr);
                charset = contentType.getCharset();
            }
        } catch (Exception ignore) {
        }
        return EntityUtils.toString(response.getEntity(), charset != null ? charset : DEFAULT_CHARSET);
    }

    /**
     * 执行命令，不带参数.
     *
     * @param ip   目标IP地址
     * @param port 目标端口号
     * @param api  API路径
     * @return 一个CompletableFuture对象，包含命令执行结果
     */
    private CompletableFuture<String> executeCommand(String ip, int port, String api) {
        return executeCommand(ip, port, api, null);
    }

    /**
     * 执行命令，不指定应用，强制使用GET方法.
     *
     * @param ip     目标IP地址
     * @param port   目标端口号
     * @param api    API路径
     * @param params 请求参数
     * @return 一个CompletableFuture对象，包含命令执行结果
     */
    private CompletableFuture<String> executeCommand(String ip, int port, String api, Map<String, String> params) {
        return executeCommand(null, ip, port, api, params, false);
    }

    /**
     * 执行命令，优先使用POST方法.
     *
     * @param app    应用名称
     * @param ip     目标IP地址
     * @param port   目标端口号
     * @param api    API路径
     * @param params 请求参数
     * @return 一个CompletableFuture对象，包含命令执行结果
     */
    private CompletableFuture<String> executeCommand(String app, String ip, int port, String api, Map<String, String> params, boolean useHttpPost) {
        CompletableFuture<String> future = new CompletableFuture<>();
        if (StringUtil.isBlank(ip) || StringUtil.isBlank(api)) {
            future.completeExceptionally(new IllegalArgumentException("无效的URL或命令名称"));
            return future;
        }
        if (!InetAddressUtils.isIPv4Address(ip) && !InetAddressUtils.isIPv6Address(ip)) {
            future.completeExceptionally(new IllegalArgumentException("无效的IP地址"));
            return future;
        }
        if (!StringUtil.isEmpty(app) && appManagement.isValidMachineOfApp(app, ip)) {
            future.completeExceptionally(new IllegalArgumentException("给定的IP地址不属于给定的应用"));
            return future;
        }
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://");
        urlBuilder.append(ip).append(':').append(port).append('/').append(api);
        if (params == null) {
            params = Collections.emptyMap();
        }
        if (!useHttpPost || !isSupportPost(app, ip, port)) {
            if (!params.isEmpty()) {
                if (urlBuilder.indexOf("?") == -1) {
                    urlBuilder.append('?');
                } else {
                    urlBuilder.append('&');
                }
                urlBuilder.append(queryString(params));
            }
            return executeCommand(new HttpGet(urlBuilder.toString()));
        } else {
            return executeCommand(
                postRequest(urlBuilder.toString(), params, isSupportEnhancedContentType(app, ip, port)));
        }
    }

    /**
     * 执行HTTP请求，并返回一个CompletableFuture对象，包含HTTP响应结果.
     *
     * @param request HTTP请求对象
     * @return 一个CompletableFuture对象，包含HTTP响应结果
     */
    private CompletableFuture<String> executeCommand(HttpUriRequest request) {
        CompletableFuture<String> future = new CompletableFuture<>();
        httpClient.execute(request, new FutureCallback<>() {

            /**
             * 请求成功时调用.
             *
             * @param response HTTP响应对象
             */
            @Override
            public void completed(final HttpResponse response) {
                int statusCode = response.getStatusLine().getStatusCode();
                try {
                    String value = getBody(response);
                    if (isSuccess(statusCode)) {
                        future.complete(value);
                    } else {
                        if (isCommandNotFound(statusCode, value)) {
                            future.completeExceptionally(new CommandNotFoundException(request.getURI().getPath()));
                        } else {
                            future.completeExceptionally(new CommandFailedException(value));
                        }
                    }
                } catch (Exception ex) {
                    future.completeExceptionally(ex);
                    log.error("HTTP request failed: {}", request.getURI().toString(), ex);
                }
            }

            /**
             * 请求失败时调用.
             * @param ex 异常对象
             */
            @Override
            public void failed(final Exception ex) {
                future.completeExceptionally(ex);
                log.error("HTTP request failed: {}", request.getURI().toString(), ex);
            }

            /**
             * 请求被取消时调用.
             */
            @Override
            public void cancelled() {
                future.complete(null);
            }
        });
        return future;
    }

    /**
     * 关闭HTTP客户端.
     *
     * @throws Exception 异常
     */
    @SuppressWarnings("unused")
    public void close() throws Exception {
        httpClient.close();
    }

    /**
     * 从指定机器上获取指定类型的规则列表.
     *
     * @param ip       目标IP地址
     * @param port     目标端口号
     * @param type     规则类型
     * @param ruleType 规则类型
     * @param <T>      规则类型
     * @return 规则列表
     */
    @Nullable
    private <T> CompletableFuture<List<T>> fetchItemsAsync(String ip, int port, String api, String type, Class<T> ruleType) {
        AssertUtil.notEmpty(ip, "Bad machine IP");
        AssertUtil.isTrue(port > 0, "Bad machine port");
        Map<String, String> params = null;
        if (StringUtil.isNotEmpty(type)) {
            params = new HashMap<>(1);
            params.put("type", type);
        }
        return executeCommand(ip, port, api, params)
            .thenApply(json -> JSON.parseArray(json, ruleType));
    }

    /**
     * 从指定机器上获取指定类型的规则列表.
     *
     * @param ip       目标IP地址
     * @param port     目标端口号
     * @param type     规则类型
     * @param ruleType 规则类型
     * @param <T>      规则类型
     * @return 规则列表
     */
    @Nullable
    private <T> List<T> fetchItems(String ip, int port, String api, String type, Class<T> ruleType) {
        try {
            AssertUtil.notEmpty(ip, "Bad machine IP");
            AssertUtil.isTrue(port > 0, "Bad machine port");
            @SuppressWarnings("all")
            Map<String, String> params;
            if (StringUtil.isNotEmpty(type)) {
                params = new HashMap<>(1);
                params.put("type", type);
            }
            return Objects.requireNonNull(fetchItemsAsync(ip, port, api, type, ruleType)).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error when fetching items from api: {} -> {}", api, type, e);
            return null;
        } catch (Exception e) {
            log.error("Error when fetching items: {} -> {}", api, type, e);
            return null;
        }
    }

    /**
     * 从指定机器上获取指定类型的规则列表.
     *
     * @param ip       目标IP地址
     * @param port     目标端口号
     * @param type     规则类型
     * @param ruleType 规则类型
     * @param <T>      规则类型
     * @return 规则列表
     */
    private <T extends Rule> List<T> fetchRules(String ip, int port, String type, Class<T> ruleType) {
        return fetchItems(ip, port, GET_RULES_PATH, type, ruleType);
    }

    /**
     * 设置规则同步方法.
     *
     * @param app      应用名称，不能为空
     * @param ip       机器IP地址，不能为空
     * @param port     机器端口号，必须大于0
     * @param type     规则类型
     * @param entities 规则实体列表，不能为空
     * @return 如果设置成功返回true，否则返回false
     */
    private boolean setRules(String app, String ip, int port, String type, List<? extends RuleEntity> entities) {
        if (entities == null) {
            return true;
        }
        try {
            AssertUtil.notEmpty(app, "Bad app name");
            AssertUtil.notEmpty(ip, "Bad machine IP");
            AssertUtil.isTrue(port > 0, "Bad machine port");
            String data = JSON.toJSONString(
                entities.stream().map(RuleEntity::toRule).collect(Collectors.toList()));
            Map<String, String> params = new HashMap<>(2);
            params.put("type", type);
            params.put("data", data);
            String result = executeCommand(app, ip, port, SET_RULES_PATH, params, true).get();
            log.info("setRules result: {}, type={}", result, type);
            return true;
        } catch (InterruptedException e) {
            log.warn("setRules API failed: {}", type, e);
            return false;
        } catch (ExecutionException e) {
            log.warn("setRules API failed: {}", type, e.getCause());
            return false;
        } catch (Exception e) {
            log.error("setRules API failed, type={}", type, e);
            return false;
        }
    }

    /**
     * 设置规则异步方法.
     *
     * @param app      应用名称，不能为空
     * @param ip       机器IP地址，不能为空
     * @param port     机器端口号，必须大于0
     * @param entities 规则实体列表，不能为空
     * @return 返回一个CompletableFuture对象，表示异步操作
     */
    private CompletableFuture<Void> setRulesAsync(String app, String ip, int port, List<? extends RuleEntity> entities) {
        try {
            AssertUtil.notNull(entities, "rules cannot be null");
            AssertUtil.notEmpty(app, "Bad app name");
            AssertUtil.notEmpty(ip, "Bad machine IP");
            AssertUtil.isTrue(port > 0, "Bad machine port");
            String data = JSON.toJSONString(
                entities.stream().map(RuleEntity::toRule).collect(Collectors.toList()));
            Map<String, String> params = new HashMap<>(2);
            params.put("type", SentinelApiClient.FLOW_RULE_TYPE);
            params.put("data", data);
            return executeCommand(app, ip, port, SET_RULES_PATH, params, true)
                .thenCompose(r -> {
                    if ("success".equalsIgnoreCase(r.trim())) {
                        return CompletableFuture.completedFuture(null);
                    }
                    return AsyncUtils.newFailedFuture(new CommandFailedException(r));
                });
        } catch (Exception e) {
            log.error("setRulesAsync API failed, type={}", SentinelApiClient.FLOW_RULE_TYPE, e);
            return AsyncUtils.newFailedFuture(e);
        }
    }

    /**
     * 获取指定机器上指定资源名的调用统计数据.
     *
     * @param ip   目标IP地址
     * @param port 目标端口号
     * @return 调用统计数据
     */
    public List<NodeVo> fetchResourceOfMachine(String ip, int port, String type) {
        return fetchItems(ip, port, RESOURCE_URL_PATH, type, NodeVo.class);
    }

    /**
     * 获取集群节点.
     *
     * @param ip          要获取的IP地址
     * @param port        IP地址的端口号
     * @param includeZero 是否在结果列表中包含零值
     * @return 节点列表
     */
    public List<NodeVo> fetchClusterNodeOfMachine(String ip, int port, boolean includeZero) {
        String type = "notZero";
        if (includeZero) {
            type = "zero";
        }
        return fetchItems(ip, port, CLUSTER_NODE_PATH, type, NodeVo.class);
    }

    /**
     * 获取指定机器上的流量规则.
     *
     * @param app  应用名称
     * @param ip   机器客户端IP
     * @param port 机器客户端端口
     * @return 流量规则列表
     */
    public List<FlowRuleEntity> fetchFlowRuleOfMachine(String app, String ip, int port) {
        List<FlowRule> rules = fetchRules(ip, port, FLOW_RULE_TYPE, FlowRule.class);
        if (rules != null) {
            return rules.stream().map(rule -> FlowRuleEntity.fromFlowRule(app, ip, port, rule))
                .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /**
     * 获取指定机器上的降级规则.
     *
     * @param app  应用名称
     * @param ip   机器客户端IP
     * @param port 机器客户端端口
     * @return 降级规则列表
     */
    public List<DegradeRuleEntity> fetchDegradeRuleOfMachine(String app, String ip, int port) {
        List<DegradeRule> rules = fetchRules(ip, port, DEGRADE_RULE_TYPE, DegradeRule.class);
        if (rules != null) {
            return rules.stream().map(rule -> DegradeRuleEntity.fromDegradeRule(app, ip, port, rule))
                .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /**
     * 获取指定机器上的系统规则.
     *
     * @param app  应用名称
     * @param ip   机器客户端IP
     * @param port 机器客户端端口
     * @return 系统规则列表
     */
    public List<SystemRuleEntity> fetchSystemRuleOfMachine(String app, String ip, int port) {
        List<SystemRule> rules = fetchRules(ip, port, SYSTEM_RULE_TYPE, SystemRule.class);
        if (rules != null) {
            return rules.stream().map(rule -> SystemRuleEntity.fromSystemRule(app, ip, port, rule))
                .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /**
     * 从指定机器获取所有参数流量规则.
     *
     * @param app  应用名称
     * @param ip   机器客户端IP
     * @param port 机器客户端端口
     * @return 所有检索到的参数流量规则
     */
    public CompletableFuture<List<ParamFlowRuleEntity>> fetchParamFlowRulesOfMachine(String app, String ip, int port) {
        try {
            AssertUtil.notEmpty(app, "Bad app name");
            AssertUtil.notEmpty(ip, "Bad machine IP");
            AssertUtil.isTrue(port > 0, "Bad machine port");
            return Objects.requireNonNull(fetchItemsAsync(ip, port, GET_PARAM_RULE_PATH, null, ParamFlowRule.class))
                .thenApply(rules -> rules.stream()
                    .map(e -> ParamFlowRuleEntity.fromParamFlowRule(app, ip, port, e))
                    .collect(Collectors.toList())
                );
        } catch (Exception e) {
            log.error("Error when fetching parameter flow rules", e);
            return AsyncUtils.newFailedFuture(e);
        }
    }

    /**
     * 从指定机器获取所有授权规则.
     *
     * @param app  应用名称
     * @param ip   机器客户端IP
     * @param port 机器客户端端口
     * @return 所有检索到的授权规则
     */
    public List<AuthorityRuleEntity> fetchAuthorityRulesOfMachine(String app, String ip, int port) {
        AssertUtil.notEmpty(app, "Bad app name");
        AssertUtil.notEmpty(ip, "Bad machine IP");
        AssertUtil.isTrue(port > 0, "Bad machine port");
        @SuppressWarnings("all")
        Map<String, String> params = new HashMap<>(1);
        params.put("type", AUTHORITY_TYPE);
        List<AuthorityRule> rules = fetchRules(ip, port, AUTHORITY_TYPE, AuthorityRule.class);
        return Optional.ofNullable(rules).map(r -> r.stream()
            .map(e -> AuthorityRuleEntity.fromAuthorityRule(app, ip, port, e))
            .collect(Collectors.toList())
        ).orElse(null);
    }

    /**
     * 设置机器的规则.
     *
     * @param app   应用名称
     * @param ip    机器客户端IP
     * @param port  机器客户端端口
     * @param rules 规则列表
     */
    public void setFlowRuleOfMachine(String app, String ip, int port, List<FlowRuleEntity> rules) {
        setRules(app, ip, port, FLOW_RULE_TYPE, rules);
    }

    /**
     * 设置机器的规则.
     *
     * @param app   应用名称
     * @param ip    机器客户端IP
     * @param port  机器客户端端口
     * @param rules 规则列表
     */
    public CompletableFuture<Void> setFlowRuleOfMachineAsync(String app, String ip, int port, List<FlowRuleEntity> rules) {
        return setRulesAsync(app, ip, port, rules);
    }

    /**
     * 设置机器的降级规则.
     *
     * @param app   应用名称
     * @param ip    机器 IP 地址
     * @param port  机器端口
     * @param rules 降级规则列表
     * @return 是否成功设置规则
     */
    public boolean setDegradeRuleOfMachine(String app, String ip, int port, List<DegradeRuleEntity> rules) {
        return setRules(app, ip, port, DEGRADE_RULE_TYPE, rules);
    }

    /**
     * 设置机器的系统规则.
     *
     * @param app   应用名称
     * @param ip    机器 IP 地址
     * @param port  机器端口
     * @param rules 系统规则列表
     * @return 是否成功设置规则
     */
    public boolean setSystemRuleOfMachine(String app, String ip, int port, List<SystemRuleEntity> rules) {
        return setRules(app, ip, port, SYSTEM_RULE_TYPE, rules);
    }

    /**
     * 设置机器的权限规则.
     *
     * @param app   应用名称
     * @param ip    机器 IP 地址
     * @param port  机器端口
     * @param rules 权限规则列表
     * @return 是否成功设置规则
     */
    public boolean setAuthorityRuleOfMachine(String app, String ip, int port, List<AuthorityRuleEntity> rules) {
        return setRules(app, ip, port, AUTHORITY_TYPE, rules);
    }

    /**
     * 设置机器的参数流控规则.
     *
     * @param app   应用名称
     * @param ip    机器 IP 地址
     * @param port  机器端口
     * @param rules 参数流控规则列表
     * @return 一个表示操作结果的 CompletableFuture
     */
    public CompletableFuture<Void> setParamFlowRuleOfMachine(String app, String ip, int port, List<ParamFlowRuleEntity> rules) {
        if (rules == null) {
            return CompletableFuture.completedFuture(null);
        }
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        try {
            String data = JSON.toJSONString(
                rules.stream().map(ParamFlowRuleEntity::getRule).collect(Collectors.toList())
            );
            Map<String, String> params = new HashMap<>(1);
            params.put("data", data);
            return executeCommand(app, ip, port, SET_PARAM_RULE_PATH, params, true)
                .thenCompose(e -> {
                    if (CommandConstants.MSG_SUCCESS.equals(e)) {
                        return CompletableFuture.completedFuture(null);
                    } else {
                        log.warn("Push parameter flow rules to client failed: {}", e);
                        return AsyncUtils.newFailedFuture(new RuntimeException(e));
                    }
                });
        } catch (Exception ex) {
            log.warn("Error when setting parameter flow rule", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 获取集群模式.
     *
     * @param ip   机器 IP 地址
     * @param port 机器端口
     * @return 一个表示集群模式的 CompletableFuture
     */
    public CompletableFuture<ClusterStateSimpleEntity> fetchClusterMode(String ip, int port) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        try {
            return executeCommand(ip, port, FETCH_CLUSTER_MODE_PATH)
                .thenApply(r -> JSON.parseObject(r, ClusterStateSimpleEntity.class));
        } catch (Exception ex) {
            log.warn("Error when fetching cluster mode", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 修改集群模式.
     *
     * @param ip   机器 IP 地址
     * @param port 机器端口
     * @param mode 集群模式
     * @return 一个表示操作结果的 CompletableFuture
     */
    public CompletableFuture<Void> modifyClusterMode(String ip, int port, int mode) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        try {
            Map<String, String> params = new HashMap<>(1);
            params.put("mode", String.valueOf(mode));
            return executeCommand(ip, port, MODIFY_CLUSTER_MODE_PATH, params)
                .thenCompose(e -> {
                    if (CommandConstants.MSG_SUCCESS.equals(e)) {
                        return CompletableFuture.completedFuture(null);
                    } else {
                        log.warn("Error when modifying cluster mode: {}", e);
                        return AsyncUtils.newFailedFuture(new RuntimeException(e));
                    }
                });
        } catch (Exception ex) {
            log.warn("Error when modifying cluster mode", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 异步获取集群客户端信息和配置.
     *
     * @param ip   集群客户端的 IP 地址
     * @param port 集群客户端的端口号
     * @return 包含集群客户端信息和配置的 CompletableFuture
     */
    public CompletableFuture<ClusterClientInfoVO> fetchClusterClientInfoAndConfig(String ip, int port) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        try {
            return executeCommand(ip, port, FETCH_CLUSTER_CLIENT_CONFIG_PATH)
                .thenApply(r -> JSON.parseObject(r, ClusterClientInfoVO.class));
        } catch (Exception ex) {
            log.warn("Error when fetching cluster client config", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 异步修改集群客户端配置.
     *
     * @param app    应用名称
     * @param ip     集群客户端的 IP 地址
     * @param port   集群客户端的端口号
     * @param config 要修改的集群客户端配置
     * @return 表示操作结果的 CompletableFuture
     */
    public CompletableFuture<Void> modifyClusterClientConfig(String app, String ip, int port, ClusterClientConfig config) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        try {
            Map<String, String> params = new HashMap<>(1);
            params.put("data", JSON.toJSONString(config));
            return executeCommand(app, ip, port, MODIFY_CLUSTER_CLIENT_CONFIG_PATH, params, true)
                .thenCompose(e -> {
                    if (CommandConstants.MSG_SUCCESS.equals(e)) {
                        return CompletableFuture.completedFuture(null);
                    } else {
                        log.warn("Error when modifying cluster client config: {}", e);
                        return AsyncUtils.newFailedFuture(new RuntimeException(e));
                    }
                });
        } catch (Exception ex) {
            log.warn("Error when modifying cluster client config", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 异步修改集群服务器流量配置.
     *
     * @param app    应用名称
     * @param ip     集群客户端的 IP 地址
     * @param port   集群客户端的端口号
     * @param config 要修改的集群服务器流量配置
     * @return 表示操作结果的 CompletableFuture
     */
    public CompletableFuture<Void> modifyClusterServerFlowConfig(String app, String ip, int port, ServerFlowConfig config) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        try {
            Map<String, String> params = new HashMap<>(1);
            params.put("data", JSON.toJSONString(config));
            return executeCommand(app, ip, port, MODIFY_CLUSTER_SERVER_FLOW_CONFIG_PATH, params, true)
                .thenCompose(e -> {
                    if (CommandConstants.MSG_SUCCESS.equals(e)) {
                        return CompletableFuture.completedFuture(null);
                    } else {
                        log.warn("Error when modifying cluster server flow config: {}", e);
                        return AsyncUtils.newFailedFuture(new RuntimeException(e));
                    }
                });
        } catch (Exception ex) {
            log.warn("Error when modifying cluster server flow config", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 修改集群服务器的传输配置.
     *
     * @param app    应用名称
     * @param ip     服务器IP地址
     * @param port   服务器端口号
     * @param config 新的服务器传输配置
     * @return 表示异步操作完成的CompletableFuture
     */
    public CompletableFuture<Void> modifyClusterServerTransportConfig(String app, String ip, int port, ServerTransportConfig config) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        try {
            Map<String, String> params = new HashMap<>(2);
            params.put("port", config.getPort().toString());
            params.put("idleSeconds", config.getIdleSeconds().toString());
            return executeCommand(app, ip, port, MODIFY_CLUSTER_SERVER_TRANSPORT_CONFIG_PATH, params, false)
                .thenCompose(e -> {
                    if (CommandConstants.MSG_SUCCESS.equals(e)) {
                        return CompletableFuture.completedFuture(null);
                    } else {
                        log.warn("Error when modifying cluster server transport config: {}", e);
                        return AsyncUtils.newFailedFuture(new RuntimeException(e));
                    }
                });
        } catch (Exception ex) {
            log.warn("Error when modifying cluster server transport config", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 修改集群服务器的命名空间集合.
     *
     * @param app  应用名称
     * @param ip   服务器IP地址
     * @param port 服务器端口号
     * @param set  新的命名空间集合
     * @return 表示异步操作完成的CompletableFuture
     */
    public CompletableFuture<Void> modifyClusterServerNamespaceSet(String app, String ip, int port, Set<String> set) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        try {
            Map<String, String> params = new HashMap<>(1);
            params.put("data", JSON.toJSONString(set));
            return executeCommand(app, ip, port, MODIFY_CLUSTER_SERVER_NAMESPACE_SET_PATH, params, true)
                .thenCompose(e -> {
                    if (CommandConstants.MSG_SUCCESS.equals(e)) {
                        return CompletableFuture.completedFuture(null);
                    } else {
                        log.warn("Error when modifying cluster server NamespaceSet: {}", e);
                        return AsyncUtils.newFailedFuture(new RuntimeException(e));
                    }
                });
        } catch (Exception ex) {
            log.warn("Error when modifying cluster server NamespaceSet", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 获取集群服务器的基本信息.
     *
     * @param ip   服务器IP地址
     * @param port 服务器端口号
     * @return 包含服务器基本信息的CompletableFuture
     */
    public CompletableFuture<ClusterServerStateVO> fetchClusterServerBasicInfo(String ip, int port) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }
        try {
            return executeCommand(ip, port, FETCH_CLUSTER_SERVER_BASIC_INFO_PATH)
                .thenApply(r -> JSON.parseObject(r, ClusterServerStateVO.class));
        } catch (Exception ex) {
            log.warn("Error when fetching cluster sever all config and basic info", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 获取集群服务器上的API定义.
     *
     * @param app  应用名称
     * @param ip   服务器IP地址
     * @param port 服务器端口号
     * @return 包含API定义列表的CompletableFuture
     */
    public CompletableFuture<List<ApiDefinitionEntity>> fetchApis(String app, String ip, int port) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }

        try {
            return executeCommand(ip, port, FETCH_GATEWAY_API_PATH)
                .thenApply(r -> {
                    List<ApiDefinitionEntity> entities = JSON.parseArray(r, ApiDefinitionEntity.class);
                    if (entities != null) {
                        for (ApiDefinitionEntity entity : entities) {
                            entity.setApp(app);
                            entity.setIp(ip);
                            entity.setPort(port);
                        }
                    }
                    return entities;
                });
        } catch (Exception ex) {
            log.warn("Error when fetching gateway apis", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 修改API定义信息.
     *
     * @param app  应用名称
     * @param ip   机器IP地址
     * @param port 机器端口号
     * @param apis API定义实体列表
     * @return 修改是否成功
     */
    public boolean modifyApis(String app, String ip, int port, List<ApiDefinitionEntity> apis) {
        if (apis == null) {
            return true;
        }

        try {
            AssertUtil.notEmpty(app, "Bad app name");
            AssertUtil.notEmpty(ip, "Bad machine IP");
            AssertUtil.isTrue(port > 0, "Bad machine port");
            String data = JSON.toJSONString(
                apis.stream().map(ApiDefinitionEntity::toApiDefinition).collect(Collectors.toList()));
            Map<String, String> params = new HashMap<>(2);
            params.put("data", data);
            String result = executeCommand(app, ip, port, MODIFY_GATEWAY_API_PATH, params, true).get();
            log.info("Modify gateway apis: {}", result);
            return true;
        } catch (Exception e) {
            log.warn("Error when modifying gateway apis", e);
            return false;
        }
    }

    /**
     * 异步获取网关流量规则.
     *
     * @param app  应用名称
     * @param ip   机器IP地址
     * @param port 机器端口号
     * @return 网关流量规则实体列表的异步结果
     */
    public CompletableFuture<List<GatewayFlowRuleEntity>> fetchGatewayFlowRules(String app, String ip, int port) {
        if (StringUtil.isBlank(ip) || port <= 0) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("Invalid parameter"));
        }

        try {
            return executeCommand(ip, port, FETCH_GATEWAY_FLOW_RULE_PATH)
                .thenApply(r -> {
                    List<GatewayFlowRule> gatewayFlowRules = JSON.parseArray(r, GatewayFlowRule.class);
                    return gatewayFlowRules.stream().map(rule -> GatewayFlowRuleEntity.fromGatewayFlowRule(app, ip, port, rule)).collect(Collectors.toList());
                });
        } catch (Exception ex) {
            log.warn("Error when fetching gateway flow rules", ex);
            return AsyncUtils.newFailedFuture(ex);
        }
    }

    /**
     * 修改网关流量规则.
     *
     * @param app   应用名称
     * @param ip    机器IP地址
     * @param port  机器端口号
     * @param rules 网关流量规则实体列表
     * @return 修改是否成功
     */
    public boolean modifyGatewayFlowRules(String app, String ip, int port, List<GatewayFlowRuleEntity> rules) {
        if (rules == null) {
            return true;
        }

        try {
            AssertUtil.notEmpty(app, "Bad app name");
            AssertUtil.notEmpty(ip, "Bad machine IP");
            AssertUtil.isTrue(port > 0, "Bad machine port");
            String data = JSON.toJSONString(
                rules.stream().map(GatewayFlowRuleEntity::toGatewayFlowRule).collect(Collectors.toList()));
            Map<String, String> params = new HashMap<>(2);
            params.put("data", data);
            String result = executeCommand(app, ip, port, MODIFY_GATEWAY_FLOW_RULE_PATH, params, true).get();
            log.info("Modify gateway flow rules: {}", result);
            return true;
        } catch (Exception e) {
            log.warn("Error when modifying gateway apis", e);
            return false;
        }
    }
}
