/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.social.utils;

import cn.hutool.core.util.ObjectUtil;
import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.social.config.properties.SocialLoginConfigProperties;
import leyramu.framework.lersosa.common.social.config.properties.SocialProperties;
import leyramu.framework.lersosa.common.social.maxkey.AuthMaxKeyRequest;
import leyramu.framework.lersosa.common.social.topiam.AuthTopIamRequest;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;

/**
 * 认证授权工具类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class SocialUtils {

    private static final AuthRedisStateCache STATE_CACHE = SpringUtils.getBean(AuthRedisStateCache.class);

    @SuppressWarnings("unchecked")
    public static AuthResponse<AuthUser> loginAuth(String source, String code, String state, SocialProperties socialProperties) throws AuthException {
        AuthRequest authRequest = getAuthRequest(source, socialProperties);
        AuthCallback callback = new AuthCallback();
        callback.setCode(code);
        callback.setState(state);
        return authRequest.login(callback);
    }

    public static AuthRequest getAuthRequest(String source, SocialProperties socialProperties) throws AuthException {
        SocialLoginConfigProperties obj = socialProperties.getType().get(source);
        if (ObjectUtil.isNull(obj)) {
            throw new AuthException("不支持的第三方登录类型");
        }
        AuthConfig.AuthConfigBuilder builder = AuthConfig.builder()
            .clientId(obj.getClientId())
            .clientSecret(obj.getClientSecret())
            .redirectUri(obj.getRedirectUri())
            .scopes(obj.getScopes());
        return switch (source.toLowerCase()) {
            case "dingtalk" -> new AuthDingTalkRequest(builder.build(), STATE_CACHE);
            case "baidu" -> new AuthBaiduRequest(builder.build(), STATE_CACHE);
            case "github" -> new AuthGithubRequest(builder.build(), STATE_CACHE);
            case "gitee" -> new AuthGiteeRequest(builder.build(), STATE_CACHE);
            case "weibo" -> new AuthWeiboRequest(builder.build(), STATE_CACHE);
            case "coding" -> new AuthCodingRequest(builder.build(), STATE_CACHE);
            case "oschina" -> new AuthOschinaRequest(builder.build(), STATE_CACHE);
            // 支付宝在创建回调地址时，不允许使用localhost或者127.0.0.1，所以这儿的回调地址使用的局域网内的ip
            case "alipay_wallet" ->
                new AuthAlipayRequest(builder.build(), socialProperties.getType().get("alipay_wallet").getAlipayPublicKey(), STATE_CACHE);
            case "qq" -> new AuthQqRequest(builder.build(), STATE_CACHE);
            case "wechat_open" -> new AuthWeChatOpenRequest(builder.build(), STATE_CACHE);
            case "taobao" -> new AuthTaobaoRequest(builder.build(), STATE_CACHE);
            case "douyin" -> new AuthDouyinRequest(builder.build(), STATE_CACHE);
            case "linkedin" -> new AuthLinkedinRequest(builder.build(), STATE_CACHE);
            case "microsoft" -> new AuthMicrosoftRequest(builder.build(), STATE_CACHE);
            case "renren" -> new AuthRenrenRequest(builder.build(), STATE_CACHE);
            case "stack_overflow" -> new AuthStackOverflowRequest(builder.build(), STATE_CACHE);
            case "huawei" -> new AuthHuaweiRequest(builder.build(), STATE_CACHE);
            case "wechat_enterprise" -> new AuthWeChatEnterpriseQrcodeRequest(builder.build(), STATE_CACHE);
            case "gitlab" -> new AuthGitlabRequest(builder.build(), STATE_CACHE);
            case "wechat_mp" -> new AuthWeChatMpRequest(builder.build(), STATE_CACHE);
            case "aliyun" -> new AuthAliyunRequest(builder.build(), STATE_CACHE);
            case "maxkey" -> new AuthMaxKeyRequest(builder.build(), STATE_CACHE);
            case "topiam" -> new AuthTopIamRequest(builder.build(), STATE_CACHE);
            default -> throw new AuthException("未获取到有效的Auth配置");
        };
    }
}

