/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
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
 * Health ControllerV2.
 *
 * @author DiligenceLai
 */
@RestController("consoleHealthV2")
@RequestMapping("/v2/console/health")
@ExtractorManager.Extractor(httpExtractor = ConsoleDefaultHttpParamExtractor.class)
public class HealthControllerV2 {

    /**
     * Whether the Nacos is in broken states or not, and cannot recover except by being restarted.
     *
     * @return HTTP code equal to 200 indicates that Nacos is in right states. HTTP code equal to 500 indicates that
     * Nacos is in broken states.
     */
    @GetMapping("/liveness")
    public Result<String> liveness() {
        return Result.success("ok");
    }

    /**
     * Ready to receive the request or not.
     *
     * @return HTTP code equal to 200 indicates that Nacos is ready. HTTP code equal to 500 indicates that Nacos is not
     * ready.
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
