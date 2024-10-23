/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.nacos.console.controller;

import com.alibaba.nacos.console.paramcheck.ConsoleDefaultHttpParamExtractor;
import com.alibaba.nacos.core.cluster.health.ModuleHealthCheckerHolder;
import com.alibaba.nacos.core.cluster.health.ReadinessResult;
import com.alibaba.nacos.core.paramcheck.ExtractorManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 健康控制器
 *
 * @author <a href="mailto:huangxiaoyu1018@gmail.com">hxy1991</a>
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.2.0
 * @since 2024/7/31
 */
@RestController("consoleHealth")
@RequestMapping("/v1/console/health")
@ExtractorManager.Extractor(httpExtractor = ConsoleDefaultHttpParamExtractor.class)
public class HealthController {

    /**
     * 无论 Nacos 是否处于损坏状态，除非处于损坏状态，否则无法恢复重新启动
     *
     * @return HTTP 代码等于 200 表示 Nacos 处于正确状态。HTTP 代码等于 500 表示 Nacos 处于损坏状态
     * @apiNote 健康检查
     */
    @GetMapping("/liveness")
    public ResponseEntity<String> liveness() {
        return ResponseEntity.ok().body("OK");
    }

    /**
     * 准备好接收请求与否
     *
     * @return HTTP 代码等于 200 表示 Nacos 已准备就绪。HTTP 代码等于 500 表示 Nacos 未准备就绪
     * @apiNote 健康检查
     */
    @GetMapping("/readiness")
    public ResponseEntity<String> readiness(HttpServletRequest request) {
        ReadinessResult result = ModuleHealthCheckerHolder.getInstance().checkReadiness();
        if (result.isSuccess()) {
            return ResponseEntity.ok().body("OK");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.getResultMessage());
    }
}
