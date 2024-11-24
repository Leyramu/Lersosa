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

import useUserStore from '@/store/modules/user';

const authPermission = (permission: string): boolean => {
  const all_permission = '*:*:*';
  const permissions: string[] = useUserStore().permissions;
  if (permission && permission.length > 0) {
    return permissions.some((v) => {
      return all_permission === v || v === permission;
    });
  } else {
    return false;
  }
};

const authRole = (role: string): boolean => {
  const super_admin = 'admin';
  const roles = useUserStore().roles;
  if (role && role.length > 0) {
    return roles.some((v) => {
      return super_admin === v || v === role;
    });
  } else {
    return false;
  }
};

export default {
  // 验证用户是否具备某权限
  hasPermi(permission: string): boolean {
    return authPermission(permission);
  },
  // 验证用户是否含有指定权限，只需包含其中一个
  hasPermiOr(permissions: string[]): boolean {
    return permissions.some((item) => {
      return authPermission(item);
    });
  },
  // 验证用户是否含有指定权限，必须全部拥有
  hasPermiAnd(permissions: string[]): boolean {
    return permissions.every((item) => {
      return authPermission(item);
    });
  },
  // 验证用户是否具备某角色
  hasRole(role: string): boolean {
    return authRole(role);
  },
  // 验证用户是否含有指定角色，只需包含其中一个
  hasRoleOr(roles: string[]): boolean {
    return roles.some((item) => {
      return authRole(item);
    });
  },
  // 验证用户是否含有指定角色，必须全部拥有
  hasRoleAnd(roles: string[]): boolean {
    return roles.every((item) => {
      return authRole(item);
    });
  }
};
