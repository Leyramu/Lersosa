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

package leyramu.framework.lersosa.demo.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 测试单表业务对象 test_demo.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class TestDemoImportVo {

    /**
     * 部门id.
     */
    @NotNull(message = "部门id不能为空")
    @ExcelProperty(value = "部门id")
    private Long deptId;

    /**
     * 用户id.
     */
    @NotNull(message = "用户id不能为空")
    @ExcelProperty(value = "用户id")
    private Long userId;

    /**
     * 排序号.
     */
    @NotNull(message = "排序号不能为空")
    @ExcelProperty(value = "排序号")
    private Long orderNum;

    /**
     * key键.
     */
    @NotBlank(message = "key键不能为空")
    @ExcelProperty(value = "key键")
    private String testKey;

    /**
     * 值.
     */
    @NotBlank(message = "值不能为空")
    @ExcelProperty(value = "值")
    private String value;
}
