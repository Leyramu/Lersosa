/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
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
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.social.topiam;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.xkcoding.http.support.HttpHeader;
import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.json.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.utils.HttpUtils;
import me.zhyd.oauth.utils.UrlBuilder;

import java.util.Objects;

import static leyramu.framework.lersosa.common.social.topiam.AuthTopiamSource.TOPIAM;

/**
 * TopIAM 认证请求.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
public class AuthTopIamRequest extends AuthDefaultRequest {

    public static final String SERVER_URL = SpringUtils.getProperty("justauth.type.topiam.server-url");

    /**
     * 设定归属域.
     */
    @SuppressWarnings("unused")
    public AuthTopIamRequest(AuthConfig config) {
        super(config, TOPIAM);
    }

    public AuthTopIamRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, TOPIAM, authStateCache);
    }

    public static void checkResponse(Dict object) {
        // oauth/token 验证异常
        if (object.containsKey("error")) {
            throw new AuthException(object.getStr("error_description"));
        }
        // user 验证异常
        if (object.containsKey("message")) {
            throw new AuthException(object.getStr("message"));
        }
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String body = doPostAuthorizationCode(authCallback.getCode());
        Dict object = JsonUtils.parseMap(body);
        checkResponse(Objects.requireNonNull(object));
        return AuthToken.builder()
            .accessToken(object.getStr("access_token"))
            .refreshToken(object.getStr("refresh_token"))
            .idToken(object.getStr("id_token"))
            .tokenType(object.getStr("token_type"))
            .scope(object.getStr("scope"))
            .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String body = doGetUserInfo(authToken);
        Dict object = JsonUtils.parseMap(body);
        checkResponse(Objects.requireNonNull(object));
        return AuthUser.builder()
            .uuid(object.getStr("sub"))
            .username(object.getStr("preferred_username"))
            .nickname(object.getStr("nickname"))
            .avatar(object.getStr("picture"))
            .email(object.getStr("email"))
            .token(authToken)
            .source(source.toString())
            .build();
    }

    @Override
    protected String doGetUserInfo(AuthToken authToken) {
        return new HttpUtils(config.getHttpConfig()).get(source.userInfo(), null, new HttpHeader()
            .add("Content-Type", "application/json")
            .add("Authorization", "Bearer " + authToken.getAccessToken()), false).getBody();
    }

    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(super.authorize(state))
            .queryParam("scope", StrUtil.join("%20", config.getScopes()))
            .build();
    }
}
