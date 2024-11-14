/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import leyramu.framework.lersosa.common.core.validate.EditGroup;
import leyramu.framework.lersosa.common.mybatis.core.domain.BaseEntity;
import leyramu.framework.lersosa.system.domain.SysTenant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 租户业务对象 sys_tenant.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysTenant.class, reverseConvertGenerate = false)
public class SysTenantBo extends BaseEntity {

    /**
     * id.
     */
    @NotNull(message = "id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 租户编号.
     */
    private String tenantId;

    /**
     * 联系人.
     */
    @NotBlank(message = "联系人不能为空", groups = {AddGroup.class, EditGroup.class})
    private String contactUserName;

    /**
     * 联系电话.
     */
    @NotBlank(message = "联系电话不能为空", groups = {AddGroup.class, EditGroup.class})
    private String contactPhone;

    /**
     * 企业名称.
     */
    @NotBlank(message = "企业名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String companyName;

    /**
     * 用户名（创建系统用户）.
     */
    @NotBlank(message = "用户名不能为空", groups = {AddGroup.class})
    private String username;

    /**
     * 密码（创建系统用户）.
     */
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class})
    private String password;

    /**
     * 统一社会信用代码.
     */
    private String licenseNumber;

    /**
     * 地址.
     */
    private String address;

    /**
     * 域名.
     */
    private String domain;

    /**
     * 企业简介.
     */
    private String intro;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 租户套餐编号.
     */
    @NotNull(message = "租户套餐不能为空", groups = {AddGroup.class})
    private Long packageId;

    /**
     * 过期时间.
     */
    private Date expireTime;

    /**
     * 用户数量（-1不限制）.
     */
    private Long accountCount;

    /**
     * 租户状态（0正常 1停用）.
     */
    private String status;
}
