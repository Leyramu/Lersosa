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

package leyramu.framework.lersosa.common.core.constant;

/**
 * 通用常量信息.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface Constants {

    /**
     * UTF-8 字符集.
     */
    String UTF8 = "UTF-8";

    /**
     * GBK 字符集.
     */
    @SuppressWarnings("unused")
    String GBK = "GBK";

    /**
     * www主域.
     */
    String WWW = "www.";

    /**
     * http请求.
     */
    String HTTP = "http://";

    /**
     * https请求.
     */
    String HTTPS = "https://";

    /**
     * 通用成功标识.
     */
    String SUCCESS = "0";

    /**
     * 通用失败标识.
     */
    String FAIL = "1";

    /**
     * 登录成功.
     */
    String LOGIN_SUCCESS = "Success";

    /**
     * 注销.
     */
    String LOGOUT = "Logout";

    /**
     * 注册.
     */
    String REGISTER = "Register";

    /**
     * 登录失败.
     */
    String LOGIN_FAIL = "Error";

    /**
     * 验证码有效期（分钟）.
     */
    Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌.
     */
    @SuppressWarnings("unused")
    String TOKEN = "token";

    /**
     * 顶级部门id.
     */
    Long TOP_PARENT_ID = 0L;
}
