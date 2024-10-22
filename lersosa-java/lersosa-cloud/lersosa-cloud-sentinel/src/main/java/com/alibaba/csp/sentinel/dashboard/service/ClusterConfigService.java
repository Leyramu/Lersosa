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

package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.discovery.AppInfo;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterGroupEntity;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterClientModifyRequest;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterServerModifyRequest;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.ClusterClientStateVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.ClusterUniversalStatePairVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.ClusterUniversalStateVO;
import com.alibaba.csp.sentinel.dashboard.util.AsyncUtils;
import com.alibaba.csp.sentinel.dashboard.util.ClusterEntityUtils;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 集群配置服务
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Service
@RequiredArgsConstructor
public class ClusterConfigService {

    /**
     * Sentinel API 客户端
     */
    private final SentinelApiClient sentinelApiClient;

    /**
     * 应用管理
     */
    private final AppManagement appManagement;

    /**
     * 修改集群客户端的客户端配置
     *
     * @param request 客户端配置请求
     * @return {@link CompletableFuture}
     */
    public CompletableFuture<Void> modifyClusterClientConfig(ClusterClientModifyRequest request) {
        if (notClientRequestValid(request)) {
            throw new IllegalArgumentException("Invalid request");
        }
        String app = request.getApp();
        String ip = request.getIp();
        int port = request.getPort();
        return sentinelApiClient.modifyClusterClientConfig(app, ip, port, request.getClientConfig())
                .thenCompose(v -> sentinelApiClient.modifyClusterMode(ip, port, ClusterStateManager.CLUSTER_CLIENT));
    }

    /**
     * 校验客户端配置请求
     *
     * @param request 客户端配置请求
     * @return 如果有效，则为 {@code true}，否则为 {@code false}
     */
    private boolean notClientRequestValid(ClusterClientModifyRequest request) {
        ClusterClientConfig config = request.getClientConfig();
        return config == null || StringUtil.isEmpty(config.getServerHost())
               || config.getServerPort() == null || config.getServerPort() <= 0
               || config.getRequestTimeout() == null || config.getRequestTimeout() <= 0;
    }

    /**
     * 修改集群服务器的服务端配置
     *
     * @param request 服务端配置请求
     * @return {@link CompletableFuture}
     */
    public CompletableFuture<Void> modifyClusterServerConfig(ClusterServerModifyRequest request) {
        ServerTransportConfig transportConfig = request.getTransportConfig();
        ServerFlowConfig flowConfig = request.getFlowConfig();
        Set<String> namespaceSet = request.getNamespaceSet();
        if (invalidTransportConfig(transportConfig)) {
            throw new IllegalArgumentException("Invalid transport config in request");
        }
        if (invalidFlowConfig(flowConfig)) {
            throw new IllegalArgumentException("Invalid flow config in request");
        }
        if (namespaceSet == null) {
            throw new IllegalArgumentException("namespace set cannot be null");
        }
        String app = request.getApp();
        String ip = request.getIp();
        int port = request.getPort();
        return sentinelApiClient.modifyClusterServerNamespaceSet(app, ip, port, namespaceSet)
                .thenCompose(v -> sentinelApiClient.modifyClusterServerTransportConfig(app, ip, port, transportConfig))
                .thenCompose(v -> sentinelApiClient.modifyClusterServerFlowConfig(app, ip, port, flowConfig))
                .thenCompose(v -> sentinelApiClient.modifyClusterMode(ip, port, ClusterStateManager.CLUSTER_SERVER));
    }

    /**
     * 获取所提供应用程序的所有可用计算机的集群状态列表
     *
     * @param app 应用程序名称
     * @return 应用程序的所有可用计算机的 cluster state 列表
     */
    public CompletableFuture<List<ClusterUniversalStatePairVO>> getClusterUniversalState(String app) {
        if (StringUtil.isBlank(app)) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("app cannot be empty"));
        }
        AppInfo appInfo = appManagement.getDetailApp(app);
        if (appInfo == null || appInfo.getMachines() == null) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }

        List<CompletableFuture<ClusterUniversalStatePairVO>> futures = appInfo.getMachines().stream()
                .filter(MachineInfo::isHealthy)
                .map(machine -> getClusterUniversalState(app, machine.getIp(), machine.getPort())
                        .thenApply(e -> new ClusterUniversalStatePairVO(machine.getIp(), machine.getPort(), e)))
                .collect(Collectors.toList());

        return AsyncUtils.sequenceSuccessFuture(futures);
    }

    /**
     * 获取所提供应用程序的所有可用计算机的集群状态列表
     *
     * @param app 应用程序名称
     * @return 应用程序的所有可用计算机的 cluster state 列表
     */
    public CompletableFuture<ClusterGroupEntity> getClusterUniversalStateForAppMachine(String app, String machineId) {
        if (StringUtil.isBlank(app)) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("app cannot be empty"));
        }
        AppInfo appInfo = appManagement.getDetailApp(app);
        if (appInfo == null || appInfo.getMachines() == null) {
            return AsyncUtils.newFailedFuture(new IllegalArgumentException("app does not have machines"));
        }

        boolean machineOk = appInfo.getMachines().stream()
                .filter(MachineInfo::isHealthy)
                .map(e -> e.getIp() + '@' + e.getPort())
                .anyMatch(e -> e.equals(machineId));
        if (!machineOk) {
            return AsyncUtils.newFailedFuture(new IllegalStateException("machine does not exist or disconnected"));
        }

        return getClusterUniversalState(app)
                .thenApply(ClusterEntityUtils::wrapToClusterGroup)
                .thenCompose(e -> e.stream()
                        .filter(e1 -> e1.getMachineId().equals(machineId))
                        .findAny()
                        .map(CompletableFuture::completedFuture)
                        .orElse(AsyncUtils.newFailedFuture(new IllegalStateException("not a server: " + machineId)))
                );
    }

    /**
     * 获取集群状态
     *
     * @param ignoredApp 应用名称
     * @param ip         IP 地址
     * @param port       端口
     * @return 集群状态
     */
    public CompletableFuture<ClusterUniversalStateVO> getClusterUniversalState(String ignoredApp, String ip, int port) {
        return sentinelApiClient.fetchClusterMode(ip, port)
                .thenApply(e -> new ClusterUniversalStateVO().setStateInfo(e))
                .thenCompose(vo -> {
                    if (vo.getStateInfo().getClientAvailable()) {
                        return sentinelApiClient.fetchClusterClientInfoAndConfig(ip, port)
                                .thenApply(cc -> vo.setClient(new ClusterClientStateVO().setClientConfig(cc)));
                    } else {
                        return CompletableFuture.completedFuture(vo);
                    }
                }).thenCompose(vo -> {
                    if (vo.getStateInfo().getServerAvailable()) {
                        return sentinelApiClient.fetchClusterServerBasicInfo(ip, port)
                                .thenApply(vo::setServer);
                    } else {
                        return CompletableFuture.completedFuture(vo);
                    }
                });
    }

    /**
     * 验证 transportConfig 是否有效
     *
     * @param transportConfig transportConfig
     * @return transportConfig 是否有效
     */
    private boolean invalidTransportConfig(ServerTransportConfig transportConfig) {
        return transportConfig == null || transportConfig.getPort() == null || transportConfig.getPort() <= 0
               || transportConfig.getIdleSeconds() == null || transportConfig.getIdleSeconds() <= 0;
    }

    /**
     * 验证 flowConfig 是否有效
     *
     * @param flowConfig flowConfig
     * @return flowConfig 是否有效
     */
    private boolean invalidFlowConfig(ServerFlowConfig flowConfig) {
        return flowConfig == null || flowConfig.getSampleCount() == null || flowConfig.getSampleCount() <= 0
               || flowConfig.getIntervalMs() == null || flowConfig.getIntervalMs() <= 0
               || flowConfig.getIntervalMs() % flowConfig.getSampleCount() != 0
               || flowConfig.getMaxAllowedQps() == null || flowConfig.getMaxAllowedQps() < 0;
    }
}
