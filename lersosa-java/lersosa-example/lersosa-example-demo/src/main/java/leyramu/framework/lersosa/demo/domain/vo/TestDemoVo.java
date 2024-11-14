/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import leyramu.framework.lersosa.demo.domain.TestDemo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 测试单表视图对象 test_demo.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TestDemo.class)
public class TestDemoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 部门id.
     */
    @ExcelProperty(value = "部门id")
    private Long deptId;

    /**
     * 用户id.
     */
    @ExcelProperty(value = "用户id")
    private Long userId;

    /**
     * 排序号.
     */
    @ExcelProperty(value = "排序号")
    private Integer orderNum;

    /**
     * key键.
     */
    @ExcelProperty(value = "key键")
    private String testKey;

    /**
     * 值.
     */
    @ExcelProperty(value = "值")
    private String value;

    /**
     * 创建时间.
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建人.
     */
    @ExcelProperty(value = "创建人")
    private String createBy;

    /**
     * 更新时间.
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 更新人.
     */
    @ExcelProperty(value = "更新人")
    private String updateBy;
}
