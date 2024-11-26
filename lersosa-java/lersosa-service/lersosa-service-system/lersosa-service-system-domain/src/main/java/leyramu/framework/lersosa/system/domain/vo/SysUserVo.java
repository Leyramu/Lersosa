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

package leyramu.framework.lersosa.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.linpeilie.annotations.AutoMapper;
import leyramu.framework.lersosa.common.sensitive.annotation.Sensitive;
import leyramu.framework.lersosa.common.sensitive.core.SensitiveStrategy;
import leyramu.framework.lersosa.common.translation.annotation.Translation;
import leyramu.framework.lersosa.common.translation.constant.TransConstant;
import leyramu.framework.lersosa.system.domain.SysUser;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户信息视图对象 sys_user.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@AutoMapper(target = SysUser.class)
public class SysUserVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID.
     */
    private Long userId;

    /**
     * 租户ID.
     */
    private String tenantId;

    /**
     * 部门ID.
     */
    private Long deptId;

    /**
     * 用户账号.
     */
    private String userName;

    /**
     * 用户昵称.
     */
    private String nickName;

    /**
     * 用户类型（sys_user系统用户）.
     */
    private String userType;

    /**
     * 用户邮箱.
     */
    @Sensitive(strategy = SensitiveStrategy.EMAIL, perms = "system:user:edit")
    private String email;

    /**
     * 手机号码.
     */
    @Sensitive(strategy = SensitiveStrategy.PHONE, perms = "system:user:edit")
    private String phonenumber;

    /**
     * 用户性别（0男 1女 2未知）.
     */
    private String sex;

    /**
     * 头像地址.
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL)
    private Long avatar;

    /**
     * 密码.
     */
    @JsonIgnore
    @JsonProperty
    private String password;

    /**
     * 帐号状态（0正常 1停用）.
     */
    private String status;

    /**
     * 最后登录IP.
     */
    private String loginIp;

    /**
     * 最后登录时间.
     */
    private Date loginDate;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 部门名.
     */
    @Translation(type = TransConstant.DEPT_ID_TO_NAME, mapper = "deptId")
    private String deptName;

    /**
     * 角色对象.
     */
    private List<SysRoleVo> roles;

    /**
     * 角色组.
     */
    private Long[] roleIds;

    /**
     * 岗位组.
     */
    private Long[] postIds;

    /**
     * 数据权限 当前角色ID.
     */
    private Long roleId;
}
