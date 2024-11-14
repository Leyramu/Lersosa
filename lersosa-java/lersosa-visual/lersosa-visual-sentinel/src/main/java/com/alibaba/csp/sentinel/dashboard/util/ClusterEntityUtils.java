/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.util;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterGroupEntity;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.ConnectionGroupVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.*;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * 集群实体实用程序.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@NoArgsConstructor
public final class ClusterEntityUtils {

    public static List<AppClusterServerStateWrapVO> wrapToAppClusterServerState(
        List<ClusterUniversalStatePairVO> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, AppClusterServerStateWrapVO> map = new HashMap<>();
        Set<String> tokenServerSet = new HashSet<>();
        for (ClusterUniversalStatePairVO stateVO : list) {
            int mode = stateVO.getState().getStateInfo().getMode();

            if (mode == ClusterStateManager.CLUSTER_SERVER) {
                String ip = stateVO.getIp();
                String serverId = ip + '@' + stateVO.getCommandPort();
                ClusterServerStateVO serverStateVO = stateVO.getState().getServer();
                map.computeIfAbsent(serverId, v -> new AppClusterServerStateWrapVO()
                    .setId(serverId)
                    .setIp(ip)
                    .setPort(serverStateVO.getPort())
                    .setState(serverStateVO)
                    .setBelongToApp(true)
                    .setConnectedCount(serverStateVO.getConnection().stream()
                        .mapToInt(ConnectionGroupVO::getConnectedCount)
                        .sum()
                    )
                );
                tokenServerSet.add(ip + ":" + serverStateVO.getPort());
            }
        }
        for (ClusterUniversalStatePairVO stateVO : list) {
            int mode = stateVO.getState().getStateInfo().getMode();

            if (mode == ClusterStateManager.CLUSTER_CLIENT) {
                ClusterClientStateVO clientState = stateVO.getState().getClient();
                if (clientState == null) {
                    continue;
                }
                String serverIp = clientState.getClientConfig().getServerHost();
                int serverPort = clientState.getClientConfig().getServerPort();
                if (tokenServerSet.contains(serverIp + ":" + serverPort)) {
                    continue;
                }
                String serverId = String.format("%s:%d", serverIp, serverPort);
                map.computeIfAbsent(serverId, v -> new AppClusterServerStateWrapVO()
                    .setId(serverId)
                    .setIp(serverIp)
                    .setPort(serverPort)
                    .setBelongToApp(false)
                );
            }
        }
        return new ArrayList<>(map.values());
    }

    public static List<AppClusterClientStateWrapVO> wrapToAppClusterClientState(
        List<ClusterUniversalStatePairVO> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, AppClusterClientStateWrapVO> map = new HashMap<>();
        for (ClusterUniversalStatePairVO stateVO : list) {
            int mode = stateVO.getState().getStateInfo().getMode();

            if (mode == ClusterStateManager.CLUSTER_CLIENT) {
                String ip = stateVO.getIp();
                String clientId = ip + '@' + stateVO.getCommandPort();
                ClusterClientStateVO clientStateVO = stateVO.getState().getClient();
                map.computeIfAbsent(clientId, v -> new AppClusterClientStateWrapVO()
                    .setId(clientId)
                    .setIp(ip)
                    .setState(clientStateVO)
                    .setCommandPort(stateVO.getCommandPort())
                );
            }
        }
        return new ArrayList<>(map.values());
    }

    public static List<ClusterGroupEntity> wrapToClusterGroup(List<ClusterUniversalStatePairVO> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, ClusterGroupEntity> map = new HashMap<>();
        for (ClusterUniversalStatePairVO stateVO : list) {
            int mode = stateVO.getState().getStateInfo().getMode();
            String ip = stateVO.getIp();
            if (mode == ClusterStateManager.CLUSTER_SERVER) {
                String serverAddress = getIp(ip);
                int port = stateVO.getState().getServer().getPort();
                String targetAddress = serverAddress + ":" + port;
                map.computeIfAbsent(targetAddress, v -> new ClusterGroupEntity()
                    .setBelongToApp(true).setMachineId(ip + '@' + stateVO.getCommandPort())
                    .setIp(ip).setPort(port)
                );
            }
        }
        for (ClusterUniversalStatePairVO stateVO : list) {
            int mode = stateVO.getState().getStateInfo().getMode();
            String ip = stateVO.getIp();
            if (mode == ClusterStateManager.CLUSTER_CLIENT) {
                String targetServer = stateVO.getState().getClient().getClientConfig().getServerHost();
                Integer targetPort = stateVO.getState().getClient().getClientConfig().getServerPort();
                if (StringUtil.isBlank(targetServer) || targetPort == null || targetPort <= 0) {
                    continue;
                }
                String targetAddress = targetServer + ":" + targetPort;
                ClusterGroupEntity group = map.computeIfAbsent(targetAddress,
                    v -> new ClusterGroupEntity()
                        .setBelongToApp(true).setMachineId(targetServer)
                        .setIp(targetServer).setPort(targetPort)
                );
                group.getClientSet().add(ip + '@' + stateVO.getCommandPort());
            }
        }
        return new ArrayList<>(map.values());
    }

    private static String getIp(String str) {
        if (str.contains(":")) {
            return str.split(":")[0];
        }
        return str;
    }
}
