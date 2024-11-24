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

package com.alibaba.csp.sentinel.dashboard.auth;

import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 用于基于特权的授权的 Web 侦听器.
 *
 * @author lkxiaolou
 * @author wxq
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
@RequiredArgsConstructor
public class DefaultAuthorizationInterceptor implements AuthorizationInterceptor {

    /**
     * 认证服务.
     */
    private final AuthService<HttpServletRequest> authService;

    /**
     * 预处理.
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 是否继续处理
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Object handler)
        throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            Method method = ((HandlerMethod) handler).getMethod();

            AuthAction authAction = method.getAnnotation(AuthAction.class);
            if (authAction != null) {
                AuthService.AuthUser authUser = authService.getAuthUser(request);
                if (authUser == null) {
                    responseNoPrivilegeMsg(response, authAction.message());
                    return false;
                }
                String target = request.getParameter(authAction.targetName());

                if (!authUser.authTarget(target, authAction.value())) {
                    responseNoPrivilegeMsg(response, authAction.message());
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 响应无权限信息.
     *
     * @param response 响应
     * @param message  信息
     * @throws IOException 异常
     */
    private void responseNoPrivilegeMsg(HttpServletResponse response, String message) throws IOException {
        Result<Object> result = Result.ofFail(-1, message);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.getOutputStream().write(JSON.toJSONBytes(result));
    }
}
