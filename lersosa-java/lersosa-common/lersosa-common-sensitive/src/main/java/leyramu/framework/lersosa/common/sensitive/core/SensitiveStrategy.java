/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.sensitive.core;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.AllArgsConstructor;

import java.util.function.Function;

/**
 * 脱敏策略.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@AllArgsConstructor
public enum SensitiveStrategy {

    /**
     * 身份证脱敏.
     */
    ID_CARD(s -> DesensitizedUtil.idCardNum(s, 3, 4)),

    /**
     * 手机号脱敏.
     */
    PHONE(DesensitizedUtil::mobilePhone),

    /**
     * 地址脱敏.
     */
    ADDRESS(s -> DesensitizedUtil.address(s, 8)),

    /**
     * 邮箱脱敏.
     */
    EMAIL(DesensitizedUtil::email),

    /**
     * 银行卡.
     */
    BANK_CARD(DesensitizedUtil::bankCard),

    /**
     * 中文名.
     */
    CHINESE_NAME(DesensitizedUtil::chineseName),

    /**
     * 固定电话.
     */
    FIXED_PHONE(DesensitizedUtil::fixedPhone),

    /**
     * 用户ID.
     */
    USER_ID(_ -> String.valueOf(DesensitizedUtil.userId())),

    /**
     * 密码.
     */
    PASSWORD(DesensitizedUtil::password),

    /**
     * ipv4.
     */
    IPV4(DesensitizedUtil::ipv4),

    /**
     * ipv6.
     */
    IPV6(DesensitizedUtil::ipv6),

    /**
     * 中国大陆车牌，包含普通车辆、新能源车辆.
     */
    CAR_LICENSE(DesensitizedUtil::carLicense),

    /**
     * 只显示第一个字符.
     */
    FIRST_MASK(DesensitizedUtil::firstMask),

    /**
     * 清空为null.
     */
    CLEAR(_ -> DesensitizedUtil.clear()),

    /**
     * 清空为"".
     */
    CLEAR_TO_NULL(_ -> DesensitizedUtil.clearToNull());

    //可自行添加其他脱敏策略

    private final Function<String, String> desensitizer;

    public Function<String, String> desensitizer() {
        return desensitizer;
    }
}
