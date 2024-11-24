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

package leyramu.framework.lersosa.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import leyramu.framework.lersosa.workflow.domain.TestLeave;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 请假视图对象 test_leave.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TestLeave.class)
public class TestLeaveVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 请假类型.
     */
    @ExcelProperty(value = "请假类型")
    private String leaveType;

    /**
     * 开始时间.
     */
    @ExcelProperty(value = "开始时间")
    private Date startDate;

    /**
     * 结束时间.
     */
    @ExcelProperty(value = "结束时间")
    private Date endDate;

    /**
     * 请假天数.
     */
    @ExcelProperty(value = "请假天数")
    private Integer leaveDays;

    /**
     * 备注.
     */
    @ExcelProperty(value = "请假原因")
    private String remark;

    /**
     * 状态.
     */
    @ExcelProperty(value = "状态")
    private String status;
}
