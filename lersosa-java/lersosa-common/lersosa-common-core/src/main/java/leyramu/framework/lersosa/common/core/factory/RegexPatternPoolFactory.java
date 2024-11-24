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

package leyramu.framework.lersosa.common.core.factory;

import cn.hutool.core.lang.PatternPool;
import leyramu.framework.lersosa.common.core.constant.RegexConstants;

import java.util.regex.Pattern;

/**
 * 正则表达式模式池工厂.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class RegexPatternPoolFactory extends PatternPool {

    /**
     * 字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）.
     */
    public static final Pattern DICTIONARY_TYPE = get(RegexConstants.DICTIONARY_TYPE);

    /**
     * 身份证号码（后6位）.
     */
    public static final Pattern ID_CARD_LAST_6 = get(RegexConstants.ID_CARD_LAST_6);

    /**
     * QQ号码.
     */
    public static final Pattern QQ_NUMBER = get(RegexConstants.QQ_NUMBER);

    /**
     * 邮政编码.
     */
    public static final Pattern POSTAL_CODE = get(RegexConstants.POSTAL_CODE);

    /**
     * 注册账号.
     */
    public static final Pattern ACCOUNT = get(RegexConstants.ACCOUNT);

    /**
     * 密码：包含至少8个字符，包括大写字母、小写字母、数字和特殊字符.
     */
    public static final Pattern PASSWORD = get(RegexConstants.PASSWORD);

    /**
     * 通用状态（0表示正常，1表示停用）.
     */
    public static final Pattern STATUS = get(RegexConstants.STATUS);
}
