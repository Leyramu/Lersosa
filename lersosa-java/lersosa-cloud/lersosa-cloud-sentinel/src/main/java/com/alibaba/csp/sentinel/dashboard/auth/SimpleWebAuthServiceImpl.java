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

import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 简单的 Web 身份验证服务实施
 *
 * @author cdfive
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.6.0
 * @since 2024/9/3
 */
public class SimpleWebAuthServiceImpl implements AuthService<HttpServletRequest> {

    /**
     * 默认的 Web 身份验证会话键
     */
    public static final String WEB_SESSION_KEY = "session_sentinel_admin";

    /**
     * 获取当前用户信息
     *
     * @param request 请求
     * @return 用户信息
     */
    @Override
    public AuthUser getAuthUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object sentinelUserObj = session.getAttribute(SimpleWebAuthServiceImpl.WEB_SESSION_KEY);
        if (sentinelUserObj instanceof AuthUser) {
            return (AuthUser) sentinelUserObj;
        }

        return null;
    }

    /**
     * 默认的 Web 身份验证用户实现
     */
    @AllArgsConstructor
    public static final class SimpleWebAuthUserImpl implements AuthUser {

        /**
         * 用户名
         */
        private final String username;

        /**
         * 检查目标权限
         *
         * @param target        要检查的目标
         * @param privilegeType 要检查的权限类型
         * @return 如果目标具有特定权限，则返回 true，否则返回 false
         */
        @Override
        public boolean authTarget(String target, PrivilegeType privilegeType) {
            return true;
        }

        /**
         * 是否为超级用户
         *
         * @return 如果为超级用户，则返回 true，否则返回 false
         */
        @Override
        public boolean isSuperUser() {
            return true;
        }

        /**
         * 获取用户昵称
         *
         * @return 用户昵称
         */
        @Override
        public String getNickName() {
            return username;
        }

        /**
         * 获取用户登录名
         *
         * @return 用户登录名
         */
        @Override
        public String getLoginName() {
            return username;
        }

        /**
         * 获取用户ID
         *
         * @return 用户ID
         */
        @Override
        public String getId() {
            return username;
        }
    }
}
