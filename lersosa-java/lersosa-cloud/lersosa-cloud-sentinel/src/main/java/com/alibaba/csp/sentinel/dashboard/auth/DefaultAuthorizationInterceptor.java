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
@RequiredArgsConstructor
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
