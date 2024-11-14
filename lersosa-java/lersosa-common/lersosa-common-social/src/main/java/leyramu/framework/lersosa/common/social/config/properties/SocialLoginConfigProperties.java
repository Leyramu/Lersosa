/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.social.config.properties;

import lombok.Data;

import java.util.List;

/**
 * 社交登录配置.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class SocialLoginConfigProperties {

    /**
     * 应用 ID.
     */
    private String clientId;

    /**
     * 应用密钥.
     */
    private String clientSecret;

    /**
     * 回调地址.
     */
    private String redirectUri;

    /**
     * 是否获取unionId.
     */
    private boolean unionId;

    /**
     * Coding 企业名称.
     */
    private String codingGroupName;

    /**
     * 支付宝公钥.
     */
    private String alipayPublicKey;

    /**
     * 企业微信应用ID.
     */
    private String agentId;

    /**
     * 堆栈溢出 API 密钥.
     */
    private String stackOverflowKey;

    /**
     * 设备ID.
     */
    private String deviceId;

    /**
     * 客户端系统类型.
     */
    private String clientOsType;

    /**
     * maxkey 服务器地址.
     */
    private String serverUrl;

    /**
     * 请求范围.
     */
    private List<String> scopes;
}
