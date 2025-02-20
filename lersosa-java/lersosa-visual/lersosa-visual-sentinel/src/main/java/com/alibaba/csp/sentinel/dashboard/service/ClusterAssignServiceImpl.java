/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
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
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 集群分配服务实现.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClusterAssignServiceImpl implements ClusterAssignService {

    private final SentinelApiClient sentinelApiClient;

    private final ClusterConfigService clusterConfigService;

    private boolean isMachineInApp(String machineId) {
        return machineId.contains(":");
    }

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
            log.error("Failed to unbind machine <{}>", machineId, e);
            failedSet.add(machineId);
        }
        return new ClusterAppAssignResultVO()
            .setFailedClientSet(failedSet)
            .setFailedServerSet(new HashSet<>());
    }

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
            log.error("Failed to unbind machine <{}>", machineId, e);
            failedSet.add(machineId);
        }
        return new ClusterAppAssignResultVO()
            .setFailedClientSet(failedSet)
            .setFailedServerSet(new HashSet<>());
    }

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

    @Override
    public ClusterAppAssignResultVO applyAssignToApp(
        String app,
        List<ClusterAppAssignMap> clusterMap,
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

    private void applyAllRemainingMachineSet(String ignoredApp, Set<String> remainingSet, Set<String> failedSet) {
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

    private void handleFutureSync(Tuple2<String, CompletableFuture<Void>> t, Set<String> failedSet) {
        try {
            t.r2.get(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            if (ex instanceof ExecutionException) {
                log.error("Request for <{}> failed", t.r1, ex.getCause());
            } else {
                log.error("Request for <{}> failed", t.r1, ex);
            }
            failedSet.add(t.r1);
        }
    }

    private CompletableFuture<Void> applyServerConfigChange(String app, String ip, int commandPort,
                                                            ClusterAppAssignMap assignMap) {
        ServerTransportConfig transportConfig = new ServerTransportConfig()
            .setPort(assignMap.getPort())
            .setIdleSeconds(600);
        return sentinelApiClient.modifyClusterServerTransportConfig(app, ip, commandPort, transportConfig)
            .thenCompose(v -> applyServerFlowConfigChange(app, ip, commandPort, assignMap))
            .thenCompose(v -> applyServerNamespaceSetConfig(app, ip, commandPort, assignMap));
    }

    private CompletableFuture<Void> applyServerFlowConfigChange(String app, String ip, int commandPort,
                                                                ClusterAppAssignMap assignMap) {
        Double maxAllowedQps = assignMap.getMaxAllowedQps();
        if (maxAllowedQps == null || maxAllowedQps <= 0 || maxAllowedQps > 20_0000) {
            return CompletableFuture.completedFuture(null);
        }
        return sentinelApiClient.modifyClusterServerFlowConfig(app, ip, commandPort,
            new ServerFlowConfig().setMaxAllowedQps(maxAllowedQps));
    }

    private CompletableFuture<Void> applyServerNamespaceSetConfig(String app, String ip, int commandPort,
                                                                  ClusterAppAssignMap assignMap) {
        Set<String> namespaceSet = assignMap.getNamespaceSet();
        if (namespaceSet == null || namespaceSet.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        return sentinelApiClient.modifyClusterServerNamespaceSet(app, ip, commandPort, namespaceSet);
    }

    private CompletableFuture<Void> modifyMode(String ip, int port, int mode) {
        return sentinelApiClient.modifyClusterMode(ip, port, mode);
    }

    private int parsePort(ClusterAppAssignMap assignMap) {
        return MachineUtils.parseCommandPort(assignMap.getMachineId())
            .orElse(ServerTransportConfig.DEFAULT_PORT);
    }
}
