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

import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterAppAssignResultVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterAppAssignMap;

import java.util.List;
import java.util.Set;

/**
 * 群集分配服务
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
public interface ClusterAssignService {

    /**
     * 解绑特定集群服务器及其客户端
     *
     * @param app       应用名称
     * @param machineId 有效的计算机 ID （{@code host@commandPort}）
     * @return 分配结果
     */
    ClusterAppAssignResultVO unbindClusterServer(String app, String machineId);

    /**
     * 解绑一组集群服务器及其客户端
     *
     * @param app          应用名称
     * @param machineIdSet 一组有效的计算机 ID （{@code host@commandPort}）
     * @return 分配结果
     */
    ClusterAppAssignResultVO unbindClusterServers(String app, Set<String> machineIdSet);

    /**
     * 为提供的应用程序应用群集服务器和客户端分配
     *
     * @param app          应用名称
     * @param clusterMap   集群分配映射（服务器 -> 客户端）
     * @param remainingSet 未分配的计算机 ID 集
     * @return 分配结果
     */
    ClusterAppAssignResultVO applyAssignToApp(String app, List<ClusterAppAssignMap> clusterMap,
                                              Set<String> remainingSet);
}
