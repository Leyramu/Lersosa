/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.controller.cluster;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.dashboard.client.CommandNotFoundException;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.SentinelVersion;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterClientModifyRequest;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterModifyRequest;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterServerModifyRequest;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.AppClusterClientStateWrapVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.AppClusterServerStateWrapVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.ClusterUniversalStatePairVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.state.ClusterUniversalStateVO;
import com.alibaba.csp.sentinel.dashboard.service.ClusterConfigService;
import com.alibaba.csp.sentinel.dashboard.util.ClusterEntityUtils;
import com.alibaba.csp.sentinel.dashboard.util.VersionUtils;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@RestController
@RequestMapping(value = "/cluster")
@RequiredArgsConstructor
public class ClusterConfigController {

    /**
     * 日志记录器
     */
    private final Logger logger = LoggerFactory.getLogger(ClusterConfigController.class);

    /**
     * Sentinel 版本 1.4.0
     */
    private final SentinelVersion version140 = new SentinelVersion().setMajorVersion(1).setMinorVersion(4);

    /**
     * 集群配置的key
     */
    private static final String KEY_MODE = "mode";

    /**
     * 应用管理
     */
    private final AppManagement appManagement;

    /**
     * 集群配置服务
     */
    private final ClusterConfigService clusterConfigService;

    /**
     * 修改集群配置
     *
     * @param payload 集群配置信息
     * @return 集群配置信息
     */
    @PostMapping("/config/modify_single")
    public Result<Boolean> apiModifyClusterConfig(@RequestBody String payload) {
        if (StringUtil.isBlank(payload)) {
            return Result.ofFail(-1, "empty request body");
        }
        try {
            JSONObject body = JSON.parseObject(payload);
            if (body.containsKey(KEY_MODE)) {
                int mode = body.getInteger(KEY_MODE);
                switch (mode) {
                    case ClusterStateManager.CLUSTER_CLIENT:
                        ClusterClientModifyRequest data = JSON.parseObject(payload, ClusterClientModifyRequest.class);
                        Result<Boolean> res = checkValidRequest(data);
                        if (res != null) {
                            return res;
                        }
                        clusterConfigService.modifyClusterClientConfig(data).get();
                        return Result.ofSuccess(true);
                    case ClusterStateManager.CLUSTER_SERVER:
                        ClusterServerModifyRequest d = JSON.parseObject(payload, ClusterServerModifyRequest.class);
                        Result<Boolean> r = checkValidRequest(d);
                        if (r != null) {
                            return r;
                        }
                        clusterConfigService.modifyClusterServerConfig(d).get();
                        return Result.ofSuccess(true);
                    default:
                        return Result.ofFail(-1, "invalid mode");
                }
            }
            return Result.ofFail(-1, "invalid parameter");
        } catch (ExecutionException ex) {
            logger.error("Error when modifying cluster config", ex.getCause());
            return errorResponse(ex);
        } catch (Throwable ex) {
            logger.error("Error when modifying cluster config", ex);
            return Result.ofFail(-1, ex.getMessage());
        }
    }

    /**
     * 错误响应
     *
     * @param ex  异常
     * @param <T> 泛型
     * @return 错误响应
     */
    private <T> Result<T> errorResponse(ExecutionException ex) {
        if (isNotSupported(ex.getCause())) {
            return unsupportedVersion();
        } else {
            return Result.ofThrowable(-1, ex.getCause());
        }
    }

    /**
     * 获取集群状态
     *
     * @param app  应用名称
     * @param ip   ip地址
     * @param port 端口
     * @return 集群状态
     */
    @GetMapping("/state_single")
    public Result<ClusterUniversalStateVO> apiGetClusterState(
            @RequestParam String app,
            @RequestParam String ip,
            @RequestParam Integer port) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app cannot be null or empty");
        }
        if (StringUtil.isEmpty(ip)) {
            return Result.ofFail(-1, "ip cannot be null or empty");
        }
        if (port == null || port <= 0) {
            return Result.ofFail(-1, "Invalid parameter: port");
        }
        if (checkIfSupported(app, ip, port)) {
            return unsupportedVersion();
        }
        try {
            return clusterConfigService.getClusterUniversalState(app, ip, port)
                    .thenApply(Result::ofSuccess)
                    .get();
        } catch (ExecutionException ex) {
            logger.error("Error when fetching cluster state", ex.getCause());
            return errorResponse(ex);
        } catch (Throwable throwable) {
            logger.error("Error when fetching cluster state", throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }

    /**
     * 获取集群状态
     *
     * @param app 应用名称
     * @return 集群状态
     */
    @GetMapping("/server_state/{app}")
    public Result<List<AppClusterServerStateWrapVO>> apiGetClusterServerStateOfApp(@PathVariable String app) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app cannot be null or empty");
        }
        try {
            return clusterConfigService.getClusterUniversalState(app)
                    .thenApply(ClusterEntityUtils::wrapToAppClusterServerState)
                    .thenApply(Result::ofSuccess)
                    .get();
        } catch (ExecutionException ex) {
            logger.error("Error when fetching cluster server state of app: {}", app, ex.getCause());
            return errorResponse(ex);
        } catch (Throwable throwable) {
            logger.error("Error when fetching cluster server state of app: {}", app, throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }

    /**
     * 获取集群状态
     *
     * @param app 应用名称
     * @return 集群状态
     */
    @GetMapping("/client_state/{app}")
    public Result<List<AppClusterClientStateWrapVO>> apiGetClusterClientStateOfApp(@PathVariable String app) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app cannot be null or empty");
        }
        try {
            return clusterConfigService.getClusterUniversalState(app)
                    .thenApply(ClusterEntityUtils::wrapToAppClusterClientState)
                    .thenApply(Result::ofSuccess)
                    .get();
        } catch (ExecutionException ex) {
            logger.error("Error when fetching cluster token client state of app: {}", app, ex.getCause());
            return errorResponse(ex);
        } catch (Throwable throwable) {
            logger.error("Error when fetching cluster token client state of app: {}", app, throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }

    /**
     * 获取集群状态
     *
     * @param app 应用名称
     * @return 集群状态
     */
    @GetMapping("/state/{app}")
    public Result<List<ClusterUniversalStatePairVO>> apiGetClusterStateOfApp(@PathVariable String app) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app cannot be null or empty");
        }
        try {
            return clusterConfigService.getClusterUniversalState(app)
                    .thenApply(Result::ofSuccess)
                    .get();
        } catch (ExecutionException ex) {
            logger.error("Error when fetching cluster state of app: {}", app, ex.getCause());
            return errorResponse(ex);
        } catch (Throwable throwable) {
            logger.error("Error when fetching cluster state of app: {}", app, throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }

    /**
     * 不支持的版本
     *
     * @return 不支持的版本
     */
    private boolean isNotSupported(Throwable ex) {
        return ex instanceof CommandNotFoundException;
    }


    /**
     * 检查是否支持
     *
     * @param app  应用名称
     * @param ip   ip地址
     * @param port 端口
     * @return 是否支持
     */
    private boolean checkIfSupported(String app, String ip, int port) {
        try {
            return Optional.ofNullable(appManagement.getDetailApp(app))
                    .flatMap(e -> e.getMachine(ip, port))
                    .flatMap(m -> VersionUtils.parseVersion(m.getVersion())
                            .map(v -> v.greaterOrEqual(version140)))
                    .orElse(true);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 检查请求是否合法
     *
     * @param request 请求
     * @return 请求是否合法
     */
    private Result<Boolean> checkValidRequest(ClusterModifyRequest request) {
        if (StringUtil.isEmpty(request.getApp())) {
            return Result.ofFail(-1, "app cannot be empty");
        }
        if (StringUtil.isEmpty(request.getIp())) {
            return Result.ofFail(-1, "ip cannot be empty");
        }
        if (request.getPort() == null || request.getPort() < 0) {
            return Result.ofFail(-1, "invalid port");
        }
        if (request.getMode() == null || request.getMode() < 0) {
            return Result.ofFail(-1, "invalid mode");
        }
        if (checkIfSupported(request.getApp(), request.getIp(), request.getPort())) {
            return unsupportedVersion();
        }
        return null;
    }

    /**
     * 不支持的版本
     *
     * @return 不支持的版本
     */
    private <R> Result<R> unsupportedVersion() {
        return Result.ofFail(4041, "Sentinel client not supported for cluster flow control (unsupported version or dependency absent)");
    }
}
