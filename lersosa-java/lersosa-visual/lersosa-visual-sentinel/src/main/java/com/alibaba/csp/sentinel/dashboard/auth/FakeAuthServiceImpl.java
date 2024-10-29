/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * 一个伪的 AuthService 实现，它将通过所有用户的身份验证检查
 *
 * @author Carpenter Lee
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.5.0
 * @since 2024/9/3
 */
public class FakeAuthServiceImpl implements AuthService<HttpServletRequest> {

    /**
     * 构造函数，打印警告信息
     */
    public FakeAuthServiceImpl() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.warn("there is no auth, use {} by implementation {}", AuthService.class, this.getClass());
    }

    /**
     * 获取 AuthUser 对象，该对象代表当前用户，当用户不合法时，会返回 null 值
     *
     * @param request 请求包含用户信息
     * @return auth 用户代表当前用户，当用户不合法时，会返回 null 值
     */
    @Override
    public AuthUser getAuthUser(HttpServletRequest request) {
        return new AuthUserImpl();
    }

    /**
     * 伪的 AuthUser 实现，它总是返回 true
     */
    static final class AuthUserImpl implements AuthUser {

        /**
         * 伪的鉴权方法，总是返回 true
         *
         * @param target        目标资源
         * @param privilegeType 权限类型
         * @return true
         */
        @Override
        public boolean authTarget(String target, PrivilegeType privilegeType) {
            return true;
        }

        /**
         * 伪的判断是否为超级管理员的方法，总是返回 true
         *
         * @return true
         */
        @Override
        public boolean isSuperUser() {
            return true;
        }

        /**
         * 伪的获取用户昵称的方法，总是返回 "FAKE_NICK_NAME"
         *
         * @return "FAKE_NICK_NAME"
         */
        @Override
        public String getNickName() {
            return "FAKE_NICK_NAME";
        }

        /**
         * 伪的获取用户登录名的方法，总是返回 "FAKE_LOGIN_NAME"
         *
         * @return "FAKE_LOGIN_NAME"
         */
        @Override
        public String getLoginName() {
            return "FAKE_LOGIN_NAME";
        }

        /**
         * 伪的获取用户 ID 的方法，总是返回 "FAKE_EMP_ID"
         *
         * @return "FAKE_EMP_ID"
         */
        @Override
        public String getId() {
            return "FAKE_EMP_ID";
        }
    }
}
