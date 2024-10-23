/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.api.factory;

import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.system.api.RemoteUserService;
import leyramu.framework.lersosa.system.api.domain.SysUser;
import leyramu.framework.lersosa.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/22
 */
@Slf4j
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {

    /**
     * 调用失败处理
     *
     * @param throwable Throwable
     * @return RemoteUserService
     */
    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {

            /**
             * 获取用户信息
             *
             * @param username 用户名
             * @param source   请求来源
             * @return 结果
             */
            @Override
            public R<LoginUser> getUserInfo(String username, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            /**
             * 注册用户信息
             *
             * @param sysUser 用户信息
             * @param source  请求来源
             * @return 结果
             */
            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }

            /**
             * 记录登录信息
             *
             * @param sysUser 用户信息
             * @param source  请求来源
             * @return 结果
             */
            @Override
            public R<Boolean> recordUserLogin(SysUser sysUser, String source) {
                return R.fail("记录用户登录信息失败:" + throwable.getMessage());
            }
        };
    }
}
