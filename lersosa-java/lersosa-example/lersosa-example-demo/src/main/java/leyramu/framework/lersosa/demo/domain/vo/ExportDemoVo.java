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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import leyramu.framework.lersosa.common.core.enums.UserStatus;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import leyramu.framework.lersosa.common.core.validate.EditGroup;
import leyramu.framework.lersosa.common.excel.annotation.ExcelDictFormat;
import leyramu.framework.lersosa.common.excel.annotation.ExcelEnumFormat;
import leyramu.framework.lersosa.common.excel.convert.ExcelDictConvert;
import leyramu.framework.lersosa.common.excel.convert.ExcelEnumConvert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 带有下拉选的Excel导出
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ExcelIgnoreUnannotated
@AllArgsConstructor
@NoArgsConstructor
public class ExportDemoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户名", index = 0)
    @NotEmpty(message = "用户名不能为空", groups = AddGroup.class)
    private String nickName;

    /**
     * 用户类型
     * </p>
     * 使用ExcelEnumFormat注解需要进行下拉选的部分
     */
    @ExcelProperty(value = "用户类型", index = 1, converter = ExcelEnumConvert.class)
    @ExcelEnumFormat(enumClass = UserStatus.class, textField = "info")
    @NotEmpty(message = "用户类型不能为空", groups = AddGroup.class)
    private String userStatus;

    /**
     * 性别
     * <p>
     * 使用ExcelDictFormat注解需要进行下拉选的部分
     */
    @ExcelProperty(value = "性别", index = 2, converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_user_sex")
    @NotEmpty(message = "性别不能为空", groups = AddGroup.class)
    private String gender;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号", index = 3)
    @NotEmpty(message = "手机号不能为空", groups = AddGroup.class)
    private String phoneNumber;

    /**
     * Email
     */
    @ExcelProperty(value = "Email", index = 4)
    @NotEmpty(message = "Email不能为空", groups = AddGroup.class)
    private String email;

    /**
     * 省
     * <p>
     * 级联下拉，仅判断是否选了
     */
    @ExcelProperty(value = "省", index = 5)
    @NotNull(message = "省不能为空", groups = AddGroup.class)
    private String province;

    /**
     * 数据库中的省ID
     * </p>
     * 处理完毕后再判断是否市正确的值
     */
    @NotNull(message = "请勿手动输入", groups = EditGroup.class)
    private Integer provinceId;

    /**
     * 市
     * <p>
     * 级联下拉
     */
    @ExcelProperty(value = "市", index = 6)
    @NotNull(message = "市不能为空", groups = AddGroup.class)
    private String city;

    /**
     * 数据库中的市ID
     */
    @NotNull(message = "请勿手动输入", groups = EditGroup.class)
    private Integer cityId;

    /**
     * 县
     * <p>
     * 级联下拉
     */
    @ExcelProperty(value = "县", index = 7)
    @NotNull(message = "县不能为空", groups = AddGroup.class)
    private String area;

    /**
     * 数据库中的县ID
     */
    @NotNull(message = "请勿手动输入", groups = EditGroup.class)
    private Integer areaId;
}