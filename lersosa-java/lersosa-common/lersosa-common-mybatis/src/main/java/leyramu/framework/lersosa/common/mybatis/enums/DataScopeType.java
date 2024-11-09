/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.mybatis.enums;

import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.mybatis.helper.DataPermissionHelper;
import leyramu.framework.lersosa.system.api.model.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限类型枚举
 * <p>
 * 支持使用 SpEL 模板表达式定义 SQL 查询条件
 * 内置数据：
 * - {@code user}: 当前登录用户信息，参考 {@link LoginUser}
 * 内置服务：
 * - {@code sdss}: 系统数据权限服务，参考 SysDataScopeService
 * 如需扩展数据，可以通过 {@link DataPermissionHelper} 进行操作
 * 如需扩展服务，可以通过 SysDataScopeService 自行编写
 * </p>
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Getter
@AllArgsConstructor
public enum DataScopeType {

    /**
     * 全部数据权限
     */
    ALL("1", "", ""),

    /**
     * 自定数据权限
     */
    CUSTOM("2", " #{#deptName} IN ( #{@sdss.getRoleCustom( #user.roleId )} ) ", " 1 = 0 "),

    /**
     * 部门数据权限
     */
    DEPT("3", " #{#deptName} = #{#user.deptId} ", " 1 = 0 "),

    /**
     * 部门及以下数据权限
     */
    DEPT_AND_CHILD("4", " #{#deptName} IN ( #{@sdss.getDeptAndChild( #user.deptId )} )", " 1 = 0 "),

    /**
     * 仅本人数据权限
     */
    SELF("5", " #{#userName} = #{#user.userId} ", " 1 = 0 ");

    private final String code;

    /**
     * SpEL 模板表达式，用于构建 SQL 条件
     */
    private final String sqlTemplate;

    /**
     * 如果不满足 {@code sqlTemplate} 的条件，则使用此默认 SQL 表达式
     */
    private final String elseSql;

    /**
     * 根据枚举代码查找对应的枚举值
     *
     * @param code 枚举代码
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static DataScopeType findCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (DataScopeType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
