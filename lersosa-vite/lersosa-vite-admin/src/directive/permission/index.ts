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

import { Directive, DirectiveBinding } from 'vue';
import useUserStore from '@/store/modules/user';
/**
 * 操作权限处理
 */
export const hasPermi: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { permissions } = useUserStore();
    // 「其他角色」按钮权限校验
    const { value } = binding;
    if (value && value instanceof Array && value.length > 0) {
      const hasPermission = permissions.some((permi: string) => {
        return permi === '*:*:*' || value.includes(permi);
      });
      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el);
        return false;
      }
    } else {
      throw new Error("check perms! Like v-has-permi=\"['system:user:add','system:user:edit']\"");
    }
  }
};

/**
 * 角色权限处理
 */
export const hasRoles: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { value } = binding;
    const { roles } = useUserStore();
    if (value && value instanceof Array && value.length > 0) {
      const hasRole = roles.some((role: string) => {
        return role === 'admin' || value.includes(role);
      });
      if (!hasRole) {
        el.parentNode && el.parentNode.removeChild(el);
        return false;
      }
    } else {
      throw new Error("check roles! Like v-has-roles=\"['admin','test']\"");
    }
  }
};
