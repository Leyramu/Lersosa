/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
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

package leyramu.framework.lersosa.auth.web;

import jakarta.servlet.http.HttpServletRequest;
import leyramu.framework.lersosa.auth.api.TokenServiceI;
import leyramu.framework.lersosa.auth.dto.TokenAuthBindingCmd;
import leyramu.framework.lersosa.auth.dto.co.LoginCo;
import leyramu.framework.lersosa.auth.dto.co.TenantCo;
import leyramu.framework.lersosa.common.core.domain.Result;
import leyramu.framework.lersosa.common.encrypt.annotation.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * token 控制.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {

    /**
     * 令牌服务.
     */
    private final TokenServiceI tokenServiceI;

    /**
     * 登录方法.
     *
     * @param body 登录信息
     * @return 结果
     */
    @ApiEncrypt
    @PostMapping("/login")
    public Result<LoginCo> login(@RequestBody String body) {
        return tokenServiceI.login(body);
    }

    /**
     * 第三方登录请求.
     *
     * @param source 登录来源
     * @return 结果
     */
    @GetMapping("/binding/{source}")
    public Result<String> authBinding(@PathVariable("source") String source, @ModelAttribute TokenAuthBindingCmd cmd) {
        return tokenServiceI.authBinding(source, cmd);
    }

    /**
     * 第三方登录回调业务处理 绑定授权.
     *
     * @param body 回调信息
     * @return 结果
     */
    @PostMapping("/social/callback")
    public Result<Void> socialCallback(@RequestBody String body) {
        return tokenServiceI.socialCallback(body);
    }

    /**
     * 取消授权.
     *
     * @param socialId socialId
     * @return 结果
     */
    @DeleteMapping(value = "/unlock/{socialId}")
    public Result<Void> unlockSocial(@PathVariable Long socialId) {
        return tokenServiceI.unlockSocial(socialId);
    }

    /**
     * 登出方法.
     *
     * @return 结果
     */
    @PostMapping("logout")
    public Result<Void> logout() {
        return tokenServiceI.logout();
    }

    /**
     * 用户注册.
     */
    @ApiEncrypt
    @PostMapping("register")
    public Result<Void> register(@RequestBody String body) {
        return tokenServiceI.register(body);
    }

    /**
     * 登录页面租户下拉框.
     *
     * @param request 请求信息
     * @return 租户列表
     * @throws Exception 异常
     */
    @GetMapping("/tenant/list")
    public Result<TenantCo> tenantList(HttpServletRequest request) throws Exception {
        return tokenServiceI.tenantList(request);
    }
}
