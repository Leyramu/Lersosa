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

package com.alibaba.nacos.console.controller.v2;

import com.alibaba.nacos.api.model.v2.Result;
import com.alibaba.nacos.console.paramcheck.ConsoleDefaultHttpParamExtractor;
import com.alibaba.nacos.core.cluster.health.ModuleHealthCheckerHolder;
import com.alibaba.nacos.core.cluster.health.ReadinessResult;
import com.alibaba.nacos.core.paramcheck.ExtractorManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 健康控制器 V2
 *
 * @author DiligenceLai
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.2.0
 * @since 2024/7/31
 */
@RestController("consoleHealthV2")
@RequestMapping("/v2/console/health")
@ExtractorManager.Extractor(httpExtractor = ConsoleDefaultHttpParamExtractor.class)
public class HealthControllerV2 {

    /**
     * 无论 Nacos 是否处于损坏状态，除非处于损坏状态，否则无法恢复重新启动
     *
     * @return HTTP 代码等于 200 表示 Nacos 处于正确状态。HTTP 代码等于 500 表示 Nacos 处于损坏状态
     * @apiNote 健康检查
     */
    @GetMapping("/liveness")
    public Result<String> liveness() {
        return Result.success("ok");
    }

    /**
     * 准备好接收请求或不接收请求
     *
     * @return HTTP 代码等于 200 表示 Nacos 已准备就绪。HTTP 代码等于500 表示 Nacos 未准备就绪
     * @apiNote 健康检查
     */
    @GetMapping("/readiness")
    public Result<String> readiness(HttpServletRequest request) {
        ReadinessResult result = ModuleHealthCheckerHolder.getInstance().checkReadiness();
        if (result.isSuccess()) {
            return Result.success("ok");
        }
        return Result.failure(result.getResultMessage());
    }
}
