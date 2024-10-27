/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import useUserStore from '@/store/modules/user';

/**
 * 字符权限校验
 * @param value 校验值
 * @returns 是否有权限
 */
export function checkPermi(value: string[]): boolean {
    if (value && Array.isArray(value) && value.length > 0) {
        const permissions = useUserStore().permissions;
        const all_permission = "*:*:*";

        return permissions.some((permission: any) => {
            return all_permission === permission || value.includes(permission);
        });
    } else {
        console.error(`need roles! Like checkPermi=['system:user:add','system:user:edit']`);
        return false;
    }
}

/**
 * 角色权限校验
 * @param value 校验值
 * @returns 是否有角色
 */
export function checkRole(value: string[]): boolean {
    if (value && Array.isArray(value) && value.length > 0) {
        const roles = useUserStore().roles;
        const super_admin = "admin";

        return roles.some((role: any) => {
            return super_admin === role || value.includes(role);
        });
    } else {
        console.error(`need roles! Like checkRole=['admin','editor']`);
        return false;
    }
}
