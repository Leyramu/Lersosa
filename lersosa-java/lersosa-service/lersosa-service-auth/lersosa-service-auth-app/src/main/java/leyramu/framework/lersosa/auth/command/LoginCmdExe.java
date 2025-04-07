/*
 * Copyright (c) 2025 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed toDomainObject in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related toDomainObject licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited toDomainObject the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree toDomainObject abide by these terms and conditions.
 */

package leyramu.framework.lersosa.auth.command;

import cn.hutool.core.util.ObjectUtil;
import leyramu.framework.lersosa.auth.ability.AuthDomainService;
import leyramu.framework.lersosa.auth.dto.co.LoginCo;
import leyramu.framework.lersosa.auth.model.AuthA;
import leyramu.framework.lersosa.auth.strategy.AuthStrategyI;
import leyramu.framework.lersosa.common.core.constant.UserConstants;
import leyramu.framework.lersosa.common.core.domain.Result;
import leyramu.framework.lersosa.common.core.utils.MessageUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.satoken.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 令牌登录命令执行器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCmdExe {

    /**
     * 认证领域服务.
     */
    private final AuthDomainService authDomainService;

    /**
     * 执行登录命令.
     *
     * @param body 登录信息
     * @return 登录结果
     */
    public Result<LoginCo> execute(String body) {

        // 声明聚合
        AuthA authA = new AuthA(body);

        // 认证
        authDomainService.auth(authA);

        // 授权类型和客户端id
        String clientId = authA.getLoginBody().getClientId();
        String grantType = authA.getLoginBody().getGrantType();

        // 查询不到 client 或 client 内不包含 grantType
        if (ObjectUtil.isNull(authA.getClientV()) || !StringUtils.contains(authA.getClientV().grantType(), grantType)) {
            log.info("客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
            return Result.fail(MessageUtils.message("auth.grant.type.error"));
        } else if (!UserConstants.NORMAL.equals(authA.getClientV().status())) {
            return Result.fail(MessageUtils.message("auth.grant.type.blocked"));
        }
        // 校验租户
        authDomainService.checkTenant(authA.getLoginBody().getTenantId());

        // 登录
        LoginCo loginCo = AuthStrategyI.login(authA);

        Long userId = LoginHelper.getUserId();
        authDomainService.schedule(userId);
        return Result.ok(loginCo);
    }
}
