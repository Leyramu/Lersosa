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

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import leyramu.framework.lersosa.common.mybatis.core.domain.BaseEntity;
import leyramu.framework.lersosa.system.domain.SysConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 参数配置业务对象 sys_config.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysConfig.class, reverseConvertGenerate = false)
public class SysConfigBo extends BaseEntity {

    /**
     * 参数主键.
     */
    private Long configId;

    /**
     * 参数名称.
     */
    @NotBlank(message = "参数名称不能为空")
    @Size(max = 100, message = "参数名称不能超过{max}个字符")
    private String configName;

    /**
     * 参数键名.
     */
    @NotBlank(message = "参数键名不能为空")
    @Size(max = 100, message = "参数键名长度不能超过{max}个字符")
    private String configKey;

    /**
     * 参数键值.
     */
    @NotBlank(message = "参数键值不能为空")
    @Size(max = 500, message = "参数键值长度不能超过{max}个字符")
    private String configValue;

    /**
     * 系统内置（Y是 N否）.
     */
    private String configType;

    /**
     * 备注.
     */
    private String remark;
}
