/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.constant;

import cn.hutool.core.lang.RegexPool;

/**
 * 常用正则表达式字符串
 * <p>
 * 常用正则表达式集合，更多正则见: https://any86.github.io/any-rule/
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface RegexConstants extends RegexPool {

    /**
     * 字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）
     */
    String DICTIONARY_TYPE = "^[a-z][a-z0-9_]*$";

    /**
     * 权限标识必须符合 tool:build:list 格式，或者空字符串
     */
    String PERMISSION_STRING = "^(|^[a-zA-Z0-9_]+:[a-zA-Z0-9_]+:[a-zA-Z0-9_]+)$";

    /**
     * 身份证号码（后6位）
     */
    String ID_CARD_LAST_6 = "^(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    /**
     * QQ号码
     */
    String QQ_NUMBER = "^[1-9][0-9]\\d{4,9}$";

    /**
     * 邮政编码
     */
    String POSTAL_CODE = "^[1-9]\\d{5}$";

    /**
     * 注册账号
     */
    String ACCOUNT = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    /**
     * 密码：包含至少8个字符，包括大写字母、小写字母、数字和特殊字符
     */
    String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    /**
     * 通用状态（0表示正常，1表示停用）
     */
    String STATUS = "^[01]$";

}
