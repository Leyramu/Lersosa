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

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import leyramu.framework.lersosa.auth.dto.TokenAuthBindingCmd;
import leyramu.framework.lersosa.common.core.domain.Result;
import leyramu.framework.lersosa.common.json.utils.JsonUtils;
import leyramu.framework.lersosa.common.social.config.properties.SocialLoginConfigProperties;
import leyramu.framework.lersosa.common.social.config.properties.SocialProperties;
import leyramu.framework.lersosa.common.social.utils.SocialUtils;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 令牌认证命令执行器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/1
 */
@Component
@RequiredArgsConstructor
public class AuthBindingCmdExe {

    /**
     * 社交登录配置.
     */
    private final SocialProperties socialProperties;

    /**
     * 执行认证命令.
     *
     * @param source 第三方平台标识
     * @param cmd    认证命令
     * @return 认证结果
     */
    public Result<String> execute(String source, TokenAuthBindingCmd cmd) {
        SocialLoginConfigProperties obj = socialProperties.getType().get(source);
        if (ObjectUtil.isNull(obj)) {
            return Result.fail(source + "平台账号暂不支持");
        }
        AuthRequest authRequest = SocialUtils.getAuthRequest(source, socialProperties);
        Map<String, String> map = new HashMap<>();
        map.put("tenantId", cmd.getTenantId());
        map.put("domain", cmd.getDomain());
        map.put("state", AuthStateUtils.createState());
        String authorizeUrl = authRequest.authorize(Base64.encode(JsonUtils.toJsonString(map), StandardCharsets.UTF_8));
        return Result.ok("操作成功", authorizeUrl);
    }
}
