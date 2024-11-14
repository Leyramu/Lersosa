/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import leyramu.framework.lersosa.common.excel.annotation.ExcelDictFormat;
import leyramu.framework.lersosa.common.excel.convert.ExcelDictConvert;
import leyramu.framework.lersosa.system.domain.SysTenant;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 租户视图对象 sys_tenant.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysTenant.class)
public class SysTenantVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id.
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 租户编号.
     */
    @ExcelProperty(value = "租户编号")
    private String tenantId;

    /**
     * 联系人.
     */
    @ExcelProperty(value = "联系人")
    private String contactUserName;

    /**
     * 联系电话.
     */
    @ExcelProperty(value = "联系电话")
    private String contactPhone;

    /**
     * 企业名称.
     */
    @ExcelProperty(value = "企业名称")
    private String companyName;

    /**
     * 统一社会信用代码.
     */
    @ExcelProperty(value = "统一社会信用代码")
    private String licenseNumber;

    /**
     * 地址.
     */
    @ExcelProperty(value = "地址")
    private String address;

    /**
     * 域名.
     */
    @ExcelProperty(value = "域名")
    private String domain;

    /**
     * 企业简介.
     */
    @ExcelProperty(value = "企业简介")
    private String intro;

    /**
     * 备注.
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 租户套餐编号.
     */
    @ExcelProperty(value = "租户套餐编号")
    private Long packageId;

    /**
     * 过期时间.
     */
    @ExcelProperty(value = "过期时间")
    private Date expireTime;

    /**
     * 用户数量（-1不限制）.
     */
    @ExcelProperty(value = "用户数量")
    private Long accountCount;

    /**
     * 租户状态（0正常 1停用）.
     */
    @ExcelProperty(value = "租户状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;
}
