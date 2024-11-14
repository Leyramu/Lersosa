/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.auth;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 一个伪的 AuthService 实现，它将通过所有用户的身份验证检查.
 *
 * @author Carpenter Lee
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
@Slf4j
public class FakeAuthServiceImpl implements AuthService<HttpServletRequest> {

    /**
     * 构造函数.
     */
    public FakeAuthServiceImpl() {
        log.warn("there is no auth, use {} by implementation {}", AuthService.class, this.getClass());
    }

    /**
     * 获取用户信息.
     *
     * @param request 请求
     * @return 用户信息
     */
    @Override
    public AuthUser getAuthUser(HttpServletRequest request) {
        return new AuthUserImpl();
    }

    /**
     * 静态内部类，实现AuthUser接口.
     */
    static final class AuthUserImpl implements AuthUser {

        /**
         * 获取用户权限.
         *
         * @param target        目标
         * @param privilegeType 权限类型
         * @return 用户权限
         */
        @Override
        public boolean authTarget(String target, PrivilegeType privilegeType) {
            return true;
        }

        /**
         * 是否是超级用户.
         *
         * @return 是否是超级用户
         */
        @Override
        public boolean isSuperUser() {
            return true;
        }

        /**
         * 获取用户昵称.
         *
         * @return 用户昵称
         */
        @Override
        public String getNickName() {
            return "FAKE_NICK_NAME";
        }

        /**
         * 获取用户登录名.
         *
         * @return 用户登录名
         */
        @Override
        public String getLoginName() {
            return "FAKE_LOGIN_NAME";
        }

        /**
         * 获取用户ID.
         *
         * @return 用户ID
         */
        @Override
        public String getId() {
            return "FAKE_EMP_ID";
        }
    }
}
