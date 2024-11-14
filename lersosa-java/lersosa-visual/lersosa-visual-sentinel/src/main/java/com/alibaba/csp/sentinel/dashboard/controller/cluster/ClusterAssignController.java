/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.controller.cluster;

import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterAppAssignResultVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterAppFullAssignRequest;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterAppSingleServerAssignRequest;
import com.alibaba.csp.sentinel.dashboard.service.ClusterAssignService;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

/**
 * 集群服务器分配控制器，用于处理集群服务器分配请求.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cluster/assign")
public class ClusterAssignController {

    /**
     * 用于处理集群服务器分配请求的服务接口.
     */
    private final ClusterAssignService clusterAssignService;

    /**
     * 处理为应用分配所有集群服务器的请求.
     *
     * @param app           应用名称，用于标识特定的应用
     * @param assignRequest 包含集群分配信息的请求体，包括集群映射和剩余列表
     * @return 分配结果封装在Result对象中，包括分配结果数据和状态码
     */
    @PostMapping("/all_server/{app}")
    public Result<ClusterAppAssignResultVO> apiAssignAllClusterServersOfApp(
        @PathVariable String app,
        @RequestBody ClusterAppFullAssignRequest assignRequest) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app cannot be null or empty");
        }
        if (assignRequest == null || assignRequest.getClusterMap() == null
            || assignRequest.getRemainingList() == null) {
            return Result.ofFail(-1, "bad request body");
        }
        try {
            return Result.ofSuccess(clusterAssignService.applyAssignToApp(app, assignRequest.getClusterMap(),
                assignRequest.getRemainingList()));
        } catch (Throwable throwable) {
            log.error("Error when assigning full cluster servers for app: {}", app, throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }

    /**
     * 处理为应用分配单个集群服务器的请求.
     *
     * @param app           应用名称，用于标识特定的应用
     * @param assignRequest 包含单个集群服务器分配信息的请求体，包括集群映射和可选的剩余列表
     * @return 分配结果封装在Result对象中，包括分配结果数据和状态码
     */
    @PostMapping("/single_server/{app}")
    public Result<ClusterAppAssignResultVO> apiAssignSingleClusterServersOfApp(
        @PathVariable String app,
        @RequestBody ClusterAppSingleServerAssignRequest assignRequest) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app cannot be null or empty");
        }
        if (assignRequest == null || assignRequest.getClusterMap() == null) {
            return Result.ofFail(-1, "bad request body");
        }
        try {
            return Result.ofSuccess(clusterAssignService.applyAssignToApp(app, Collections.singletonList(assignRequest.getClusterMap()),
                assignRequest.getRemainingList()));
        } catch (Throwable throwable) {
            log.error("Error when assigning single cluster servers for app: {}", app, throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }

    /**
     * 处理解除应用绑定的集群服务器请求.
     *
     * @param app        应用名称，用于标识特定的应用
     * @param machineIds 需要解除绑定的集群服务器ID集合
     * @return 解绑结果封装在Result对象中，包括解绑结果数据和状态码
     */
    @PostMapping("/unbind_server/{app}")
    public Result<ClusterAppAssignResultVO> apiUnbindClusterServersOfApp(
        @PathVariable String app,
        @RequestBody Set<String> machineIds) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app cannot be null or empty");
        }
        if (machineIds == null || machineIds.isEmpty()) {
            return Result.ofFail(-1, "bad request body");
        }
        try {
            return Result.ofSuccess(clusterAssignService.unbindClusterServers(app, machineIds));
        } catch (Throwable throwable) {
            log.error("Error when unbinding cluster server {} for app <{}>", machineIds, app, throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }
}
