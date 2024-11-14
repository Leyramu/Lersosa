/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.api.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 社会化关系视图对象 sys_social.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class RemoteSocialVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 用户ID.
     */
    private Long userId;

    /**
     * 租户ID.
     */
    private String tenantId;

    /**
     * 认证唯一ID.
     */
    private String authId;

    /**
     * 用户来源.
     */
    private String source;

    /**
     * 用户的授权令牌.
     */
    private String accessToken;

    /**
     * 用户的授权令牌的有效期，部分平台可能没有.
     */
    private int expireIn;

    /**
     * 刷新令牌，部分平台可能没有.
     */
    private String refreshToken;

    /**
     * 用户的 open id.
     */
    private String openId;

    /**
     * 授权的第三方账号.
     */
    private String userName;

    /**
     * 授权的第三方昵称.
     */
    private String nickName;

    /**
     * 授权的第三方邮箱.
     */
    private String email;

    /**
     * 授权的第三方头像地址.
     */
    private String avatar;


    /**
     * 平台的授权信息，部分平台可能没有.
     */
    private String accessCode;

    /**
     * 用户的 unionid.
     */
    private String unionId;

    /**
     * 授予的权限，部分平台可能没有.
     */
    private String scope;

    /**
     * 个别平台的授权信息，部分平台可能没有.
     */
    private String tokenType;

    /**
     * id token，部分平台可能没有.
     */
    private String idToken;

    /**
     * 小米平台用户的附带属性，部分平台可能没有.
     */
    private String macAlgorithm;

    /**
     * 小米平台用户的附带属性，部分平台可能没有.
     */
    private String macKey;

    /**
     * 用户的授权code，部分平台可能没有.
     */
    private String code;

    /**
     * Twitter平台用户的附带属性，部分平台可能没有.
     */
    private String oauthToken;

    /**
     * Twitter平台用户的附带属性，部分平台可能没有.
     */
    private String oauthTokenSecret;
}
