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
 * A fake AuthService implementation, which will pass all user auth checking.
 *
 * @author Carpenter Lee
 * @since 1.5.0
 */
public class FakeAuthServiceImpl implements AuthService<HttpServletRequest> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public FakeAuthServiceImpl() {
        this.logger.warn("there is no auth, use {} by implementation {}", AuthService.class, this.getClass());
    }

    @Override
    public AuthUser getAuthUser(HttpServletRequest request) {
        return new AuthUserImpl();
    }

    static final class AuthUserImpl implements AuthUser {

        @Override
        public boolean authTarget(String target, PrivilegeType privilegeType) {
            // fake implementation, always return true
            return true;
        }

        @Override
        public boolean isSuperUser() {
            // fake implementation, always return true
            return true;
        }

        @Override
        public String getNickName() {
            return "FAKE_NICK_NAME";
        }

        @Override
        public String getLoginName() {
            return "FAKE_LOGIN_NAME";
        }

        @Override
        public String getId() {
            return "FAKE_EMP_ID";
        }
    }
}
