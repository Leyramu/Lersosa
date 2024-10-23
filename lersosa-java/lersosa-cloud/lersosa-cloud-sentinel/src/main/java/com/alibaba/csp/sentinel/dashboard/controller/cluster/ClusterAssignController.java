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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

/**
 * 集群分配控制器
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@RestController
@RequestMapping("/cluster/assign")
@RequiredArgsConstructor
public class ClusterAssignController {

    /**
     * 日志记录器
     */
    private final Logger logger = LoggerFactory.getLogger(ClusterAssignController.class);

    /**
     * 集群分配服务
     */
    private final ClusterAssignService clusterAssignService;

    /**
     * 集群分配全量服务
     *
     * @param app           应用名称
     * @param assignRequest 集群分配请求
     * @return 集群分配结果
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
            logger.error("Error when assigning full cluster servers for app: {}", app, throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }

    /**
     * 集群分配单服务
     *
     * @param app           应用名称
     * @param assignRequest 集群分配请求
     * @return 集群分配结果
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
            logger.error("Error when assigning single cluster servers for app: {}", app, throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }

    /**
     * 集群解绑服务
     *
     * @param app        应用名称
     * @param machineIds 机器ID集合
     * @return 集群解绑结果
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
            logger.error("Error when unbinding cluster server {} for app <{}>", machineIds, app, throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }
}
