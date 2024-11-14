/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 用户信息.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@NoArgsConstructor
public class LoginUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID.
     */
    private String tenantId;

    /**
     * 用户ID.
     */
    private Long userId;

    /**
     * 部门ID.
     */
    private Long deptId;

    /**
     * 部门类别编码.
     */
    private String deptCategory;

    /**
     * 部门名.
     */
    private String deptName;

    /**
     * 用户唯一标识.
     */
    private String token;

    /**
     * 用户类型.
     */
    private String userType;

    /**
     * 登录时间.
     */
    private Long loginTime;

    /**
     * 过期时间.
     */
    private Long expireTime;

    /**
     * 登录IP地址.
     */
    private String ipaddr;

    /**
     * 登录地点.
     */
    private String loginLocation;

    /**
     * 浏览器类型.
     */
    private String browser;

    /**
     * 操作系统.
     */
    private String os;

    /**
     * 菜单权限.
     */
    private Set<String> menuPermission;

    /**
     * 角色权限.
     */
    private Set<String> rolePermission;

    /**
     * 用户名.
     */
    private String username;

    /**
     * 用户昵称.
     */
    private String nickname;

    /**
     * 密码.
     */
    private String password;

    /**
     * 角色对象.
     */
    private List<RoleDTO> roles;

    /**
     * 数据权限 当前角色ID.
     */
    private Long roleId;

    /**
     * 客户端.
     */
    private String clientKey;

    /**
     * 设备类型.
     */
    private String deviceType;

    /**
     * 获取登录id.
     */
    public String getLoginId() {
        if (userType == null) {
            throw new IllegalArgumentException("用户类型不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return userType + ":" + userId;
    }
}
