/*
 * Copyright (c) 2025 Leyramu Group. All rights reserved.
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

package leyramu.framework.lersosa.common.purge.aspect;

import leyramu.framework.lersosa.common.purge.annotation.NgxCacheCls;
import leyramu.framework.lersosa.common.purge.enmu.ExecutionTimeEnum;
import leyramu.framework.lersosa.common.purge.handler.NgxCacheHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * Nginx 缓存切面.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/17
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class NgxCacheAspect {

    /**
     * Nginx 缓存服务.
     */
    private final NgxCacheHandler nginxCacheHandler;

    /**
     * SpEL表达式解析器.
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 切面逻辑.
     *
     * @param joinPoint   切点
     * @param ngxCacheCls 注解
     * @return Object
     * @throws Throwable 异常
     */
    @Around("@annotation(ngxCacheCls)")
    public Object processCacheOperation(ProceedingJoinPoint joinPoint, NgxCacheCls ngxCacheCls) throws Throwable {
        // BEFORE 逻辑处理
        if (ngxCacheCls.when() == ExecutionTimeEnum.BEFORE) {
            executeCacheClear(joinPoint, ngxCacheCls);
        }

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            // AFTER 异常处理
            if (ngxCacheCls.when() == ExecutionTimeEnum.AFTER) {
                executeCacheClear(joinPoint, ngxCacheCls);
            }
            throw e;
        }

        // AFTER_SUCCESS/AFTER 逻辑处理
        if (ngxCacheCls.when() == ExecutionTimeEnum.AFTER_SUCCESS
            || ngxCacheCls.when() == ExecutionTimeEnum.AFTER) {
            executeCacheClear(joinPoint, ngxCacheCls);
        }

        return result;
    }

    /**
     * 执行缓存清除操作.
     *
     * @param joinPoint  切点
     * @param annotation 注解
     */
    private void executeCacheClear(ProceedingJoinPoint joinPoint, NgxCacheCls annotation) {
        String pathTemplate = annotation.path();

        if (StringUtils.equals(pathTemplate, "/purge_all")) {
            nginxCacheHandler.clearCache(pathTemplate);
            return;
        }

        // SpEL动态解析路径
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        context.setVariable("result", joinPoint.getTarget());

        try {
            Expression exp = parser.parseExpression(pathTemplate);
            String path = exp.getValue(context, String.class);
            nginxCacheHandler.clearCache(path);
        } catch (Exception e) {
            // 记录解析错误但继续执行
            log.error("缓存路径解析失败: {}", pathTemplate, e);
        }
    }
}
