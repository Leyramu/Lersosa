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

package leyramu.framework.lersosa.system.domain.bo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import leyramu.framework.lersosa.common.core.constant.RegexConstants;
import leyramu.framework.lersosa.common.core.xss.Xss;
import leyramu.framework.lersosa.common.mybatis.core.domain.BaseEntity;
import leyramu.framework.lersosa.common.sensitive.annotation.Sensitive;
import leyramu.framework.lersosa.common.sensitive.core.SensitiveStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 个人信息业务处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysUserProfileBo extends BaseEntity {

    /**
     * 用户昵称.
     */
    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(max = 30, message = "用户昵称长度不能超过{max}个字符")
    private String nickName;

    /**
     * 用户邮箱.
     */
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过{max}个字符")
    private String email;

    /**
     * 手机号码.
     */
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    @Pattern(regexp = RegexConstants.MOBILE, message = "手机号格式不正确")
    private String phonenumber;

    /**
     * 用户性别（0男 1女 2未知）.
     */
    private String sex;
}
