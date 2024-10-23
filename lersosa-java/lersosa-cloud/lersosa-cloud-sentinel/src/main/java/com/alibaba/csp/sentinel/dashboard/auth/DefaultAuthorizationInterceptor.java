/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.auth;

import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 用于基于特权的授权的 Web 侦听器
 *
 * @author lkxiaolou
 * @author wxq
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.7.1
 * @since 2024/9/3
 */
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class DefaultAuthorizationInterceptor implements AuthorizationInterceptor {

    /**
     * 用于获取用户信息的授权服务
     */
    private final AuthService<HttpServletRequest> authService;

    /**
     * 预处理请求，检查用户是否具有访问该资源的特权
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param handler  处理程序，可以是控制器方法、资源处理器等
     * @return 如果用户具有访问资源的特权，则返回 true；否则返回 false
     * @throws Exception 如果发生错误，将抛出异常
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
     * 响应没有特权的消息
     *
     * @param response HTTP 响应
     * @param message  错误消息
     * @throws IOException 如果发生错误，将抛出 IOException
     */
    private void responseNoPrivilegeMsg(HttpServletResponse response, String message) throws IOException {
        Result<Object> result = Result.ofFail(-1, message);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        response.getOutputStream().write(JSON.toJSONBytes(result));
    }
}
