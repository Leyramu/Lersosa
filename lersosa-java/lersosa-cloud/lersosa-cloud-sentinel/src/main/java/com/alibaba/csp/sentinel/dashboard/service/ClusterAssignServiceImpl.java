/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterAppAssignResultVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterGroupEntity;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterAppAssignMap;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.ClusterUniversalStatePairVO;
import com.alibaba.csp.sentinel.dashboard.util.MachineUtils;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 集群配置服务实现
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Service
@RequiredArgsConstructor
public class ClusterAssignServiceImpl implements ClusterAssignService {

    /**
     * 日志记录器
     */
    private final Logger LOGGER = LoggerFactory.getLogger(ClusterAssignServiceImpl.class);

    /**
     * Sentinel API 客户端
     */
    private final SentinelApiClient sentinelApiClient;

    /**
     * 集群配置服务
     */
    private final ClusterConfigService clusterConfigService;

    /**
     * 获取集群配置服务
     *
     * @return 集群配置服务
     */
    private boolean isMachineInApp(String machineId) {
        return machineId.contains(":");
    }

    /**
     * 处理未在应用中的集群服务器解绑
     *
     * @param app       应用名称
     * @param machineId 有效的计算机 ID （{@code host@commandPort}）
     * @return 分配结果
     */
    private ClusterAppAssignResultVO handleUnbindClusterServerNotInApp(String app, String machineId) {
        Set<String> failedSet = new HashSet<>();
        try {
            List<ClusterUniversalStatePairVO> list = clusterConfigService.getClusterUniversalState(app)
                    .get(10, TimeUnit.SECONDS);
            Set<String> toModifySet = list.stream()
                    .filter(e -> e.getState().getStateInfo().getMode() == ClusterStateManager.CLUSTER_CLIENT)
                    .filter(e -> machineId.equals(e.getState().getClient().getClientConfig().getServerHost() + ':' +
                                                  e.getState().getClient().getClientConfig().getServerPort()))
                    .map(e -> e.getIp() + '@' + e.getCommandPort())
                    .collect(Collectors.toSet());
            modifyToNonStarted(toModifySet, failedSet);
        } catch (Exception ex) {
            Throwable e = ex instanceof ExecutionException ? ex.getCause() : ex;
            LOGGER.error("Failed to unbind machine <{}>", machineId, e);
            failedSet.add(machineId);
        }
        return new ClusterAppAssignResultVO()
                .setFailedClientSet(failedSet)
                .setFailedServerSet(new HashSet<>());
    }

    /**
     * 修改模式为非启动状态
     *
     * @param toModifySet 需要修改的模式为非启动状态的机器集合
     * @param failedSet   失败的机器集合
     */
    private void modifyToNonStarted(Set<String> toModifySet, Set<String> failedSet) {
        toModifySet.parallelStream()
                .map(MachineUtils::parseCommandIpAndPort)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(e -> {
                    CompletableFuture<Void> f = modifyMode(e.r1, e.r2, ClusterStateManager.CLUSTER_NOT_STARTED);
                    return Tuple2.of(e.r1 + '@' + e.r2, f);
                })
                .forEach(f -> handleFutureSync(f, failedSet));
    }

    /**
     * 解绑
     *
     * @param app       应用名称
     * @param machineId 有效的计算机 ID （{@code host@commandPort}）
     * @return 分配结果
     */
    @Override
    public ClusterAppAssignResultVO unbindClusterServer(String app, String machineId) {
        AssertUtil.assertNotBlank(app, "app cannot be blank");
        AssertUtil.assertNotBlank(machineId, "machineId cannot be blank");

        if (isMachineInApp(machineId)) {
            return handleUnbindClusterServerNotInApp(app, machineId);
        }
        Set<String> failedSet = new HashSet<>();
        try {
            ClusterGroupEntity entity = clusterConfigService.getClusterUniversalStateForAppMachine(app, machineId)
                    .get(10, TimeUnit.SECONDS);
            Set<String> toModifySet = new HashSet<>();
            toModifySet.add(machineId);
            if (entity.getClientSet() != null) {
                toModifySet.addAll(entity.getClientSet());
            }
            modifyToNonStarted(toModifySet, failedSet);
        } catch (Exception ex) {
            Throwable e = ex instanceof ExecutionException ? ex.getCause() : ex;
            LOGGER.error("Failed to unbind machine <{}>", machineId, e);
            failedSet.add(machineId);
        }
        return new ClusterAppAssignResultVO()
                .setFailedClientSet(failedSet)
                .setFailedServerSet(new HashSet<>());
    }

    /**
     * 解绑一组集群服务器及其客户端
     *
     * @param app          应用名称
     * @param machineIdSet 一组有效的计算机 ID （{@code host@commandPort}）
     * @return 分配结果
     */
    @Override
    public ClusterAppAssignResultVO unbindClusterServers(String app, Set<String> machineIdSet) {
        AssertUtil.assertNotBlank(app, "app cannot be blank");
        AssertUtil.isTrue(machineIdSet != null && !machineIdSet.isEmpty(), "machineIdSet cannot be empty");
        ClusterAppAssignResultVO result = new ClusterAppAssignResultVO()
                .setFailedClientSet(new HashSet<>())
                .setFailedServerSet(new HashSet<>());
        for (String machineId : machineIdSet) {
            ClusterAppAssignResultVO resultVO = unbindClusterServer(app, machineId);
            result.getFailedClientSet().addAll(resultVO.getFailedClientSet());
            result.getFailedServerSet().addAll(resultVO.getFailedServerSet());
        }
        return result;
    }

    /**
     * 应用集群分配到应用
     *
     * @param app          应用名称
     * @param clusterMap   集群分配映射
     * @param remainingSet 剩余的未分配的机器集合
     * @return 分配结果
     */
    @Override
    public ClusterAppAssignResultVO applyAssignToApp(String app, List<ClusterAppAssignMap> clusterMap,
                                                     Set<String> remainingSet) {
        AssertUtil.assertNotBlank(app, "app cannot be blank");
        AssertUtil.notNull(clusterMap, "clusterMap cannot be null");
        Set<String> failedServerSet = new HashSet<>();
        Set<String> failedClientSet = new HashSet<>();

        clusterMap.stream()
                .filter(Objects::nonNull)
                .filter(ClusterAppAssignMap::getBelongToApp)
                .map(e -> {
                    String ip = e.getIp();
                    int commandPort = parsePort(e);
                    CompletableFuture<Void> f = modifyMode(ip, commandPort, ClusterStateManager.CLUSTER_SERVER)
                            .thenCompose(v -> applyServerConfigChange(app, ip, commandPort, e));
                    return Tuple2.of(e.getMachineId(), f);
                })
                .forEach(t -> handleFutureSync(t, failedServerSet));

        clusterMap.parallelStream()
                .filter(Objects::nonNull)
                .forEach(e -> applyAllClientConfigChange(app, e, failedClientSet));

        applyAllRemainingMachineSet(app, remainingSet, failedClientSet);

        return new ClusterAppAssignResultVO()
                .setFailedClientSet(failedClientSet)
                .setFailedServerSet(failedServerSet);
    }

    /**
     * 应用所有剩余的未分配的机器
     *
     * @param app          应用名称
     * @param remainingSet 剩余的未分配的机器集合
     * @param failedSet    失败的机器集合
     */
    private void applyAllRemainingMachineSet(String app, Set<String> remainingSet, Set<String> failedSet) {
        if (remainingSet == null || remainingSet.isEmpty()) {
            return;
        }
        remainingSet.parallelStream()
                .filter(Objects::nonNull)
                .map(MachineUtils::parseCommandIpAndPort)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ipPort -> {
                    String ip = ipPort.r1;
                    int commandPort = ipPort.r2;
                    CompletableFuture<Void> f = modifyMode(ip, commandPort, ClusterStateManager.CLUSTER_NOT_STARTED);
                    return Tuple2.of(ip + '@' + commandPort, f);
                })
                .forEach(t -> handleFutureSync(t, failedSet));
    }

    /**
     * 应用所有客户端配置更改
     *
     * @param app       应用名称
     * @param assignMap 集群分配映射
     * @param failedSet 失败的机器集合
     */
    private void applyAllClientConfigChange(String app, ClusterAppAssignMap assignMap,
                                            Set<String> failedSet) {
        Set<String> clientSet = assignMap.getClientSet();
        if (clientSet == null || clientSet.isEmpty()) {
            return;
        }
        final String serverIp = assignMap.getIp();
        final int serverPort = assignMap.getPort();
        clientSet.stream()
                .map(MachineUtils::parseCommandIpAndPort)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ipPort -> {
                    CompletableFuture<Void> f = sentinelApiClient
                            .modifyClusterMode(ipPort.r1, ipPort.r2, ClusterStateManager.CLUSTER_CLIENT)
                            .thenCompose(v -> sentinelApiClient.modifyClusterClientConfig(app, ipPort.r1, ipPort.r2,
                                    new ClusterClientConfig().setRequestTimeout(20)
                                            .setServerHost(serverIp)
                                            .setServerPort(serverPort)
                            ));
                    return Tuple2.of(ipPort.r1 + '@' + ipPort.r2, f);
                })
                .forEach(t -> handleFutureSync(t, failedSet));
    }

    /**
     * 处理 Future 同步
     *
     * @param t         元组
     * @param failedSet 失败的机器集合
     */
    private void handleFutureSync(Tuple2<String, CompletableFuture<Void>> t, Set<String> failedSet) {
        try {
            t.r2.get(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            if (ex instanceof ExecutionException) {
                LOGGER.error("Request for <{}> failed", t.r1, ex.getCause());
            } else {
                LOGGER.error("Request for <{}> failed", t.r1, ex);
            }
            failedSet.add(t.r1);
        }
    }

    /**
     * 应用服务器配置更改
     *
     * @param app         应用名称
     * @param ip          IP 地址
     * @param commandPort 命令端口
     * @param assignMap   集群分配映射
     * @return CompletableFuture
     */
    private CompletableFuture<Void> applyServerConfigChange(String app, String ip, int commandPort,
                                                            ClusterAppAssignMap assignMap) {
        ServerTransportConfig transportConfig = new ServerTransportConfig()
                .setPort(assignMap.getPort())
                .setIdleSeconds(600);
        return sentinelApiClient.modifyClusterServerTransportConfig(app, ip, commandPort, transportConfig)
                .thenCompose(v -> applyServerFlowConfigChange(app, ip, commandPort, assignMap))
                .thenCompose(v -> applyServerNamespaceSetConfig(app, ip, commandPort, assignMap));
    }

    /**
     * 应用服务器流量配置更改
     *
     * @param app         应用名称
     * @param ip          IP 地址
     * @param commandPort 命令端口
     * @param assignMap   集群分配映射
     * @return CompletableFuture
     */
    private CompletableFuture<Void> applyServerFlowConfigChange(String app, String ip, int commandPort,
                                                                ClusterAppAssignMap assignMap) {
        Double maxAllowedQps = assignMap.getMaxAllowedQps();
        if (maxAllowedQps == null || maxAllowedQps <= 0 || maxAllowedQps > 20_0000) {
            return CompletableFuture.completedFuture(null);
        }
        return sentinelApiClient.modifyClusterServerFlowConfig(app, ip, commandPort,
                new ServerFlowConfig().setMaxAllowedQps(maxAllowedQps));
    }

    /**
     * 应用服务器命名空间配置更改
     *
     * @param app         应用名称
     * @param ip          IP 地址
     * @param commandPort 命令端口
     * @param assignMap   集群分配映射
     * @return CompletableFuture
     */
    private CompletableFuture<Void> applyServerNamespaceSetConfig(String app, String ip, int commandPort,
                                                                  ClusterAppAssignMap assignMap) {
        Set<String> namespaceSet = assignMap.getNamespaceSet();
        if (namespaceSet == null || namespaceSet.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return sentinelApiClient.modifyClusterServerNamespaceSet(app, ip, commandPort, namespaceSet);
    }

    /**
     * 修改集群模式
     *
     * @param ip   IP 地址
     * @param port 端口
     * @param mode 模式
     * @return CompletableFuture
     */
    private CompletableFuture<Void> modifyMode(String ip, int port, int mode) {
        return sentinelApiClient.modifyClusterMode(ip, port, mode);
    }

    /**
     * 解析端口
     *
     * @param assignMap 集群分配映射
     * @return 端口
     */
    private int parsePort(ClusterAppAssignMap assignMap) {
        return MachineUtils.parseCommandPort(assignMap.getMachineId())
                .orElse(ServerTransportConfig.DEFAULT_PORT);
    }
}
