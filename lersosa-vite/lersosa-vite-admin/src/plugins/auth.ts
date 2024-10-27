/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import useUserStore from '@/store/modules/user';

// 定义类型
type Permission = string;
type Role = string;

// 验证用户是否具备某权限
function authPermission(permission: Permission): boolean {
    const all_permission = "*:*:*";
    const permissions = useUserStore().permissions;
    if (permission && permission.length > 0) {
        return permissions.some((v: string | undefined) => all_permission === v || v === permission);
    } else {
        return false;
    }
}

// 验证用户是否具备某角色
function authRole(role: Role): boolean {
    const super_admin = "admin";
    const roles = useUserStore().roles;
    if (role && role.length > 0) {
        return roles.some((v: string | undefined) => super_admin === v || v === role);
    } else {
        return false;
    }
}

export default {
    // 验证用户是否具备某权限
    hasPermi(permission: Permission): boolean {
        return authPermission(permission);
    },
    // 验证用户是否含有指定权限，只需包含其中一个
    hasPermiOr(permissions: Permission[]): boolean {
        return permissions.some(item => authPermission(item));
    },
    // 验证用户是否含有指定权限，必须全部拥有
    hasPermiAnd(permissions: Permission[]): boolean {
        return permissions.every(item => authPermission(item));
    },
    // 验证用户是否具备某角色
    hasRole(role: Role): boolean {
        return authRole(role);
    },
    // 验证用户是否含有指定角色，只需包含其中一个
    hasRoleOr(roles: Role[]): boolean {
        return roles.some(item => authRole(item));
    },
    // 验证用户是否含有指定角色，必须全部拥有
    hasRoleAnd(roles: Role[]): boolean {
        return roles.every(item => authRole(item));
    }
};

