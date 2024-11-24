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

import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { MenuQuery, MenuVO, MenuForm, MenuTreeOption, RoleMenuTree } from './types';

// 查询菜单列表
export const listMenu = (query?: MenuQuery): AxiosPromise<MenuVO[]> => {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params: query
  });
};

// 查询菜单详细
export const getMenu = (menuId: string | number): AxiosPromise<MenuVO> => {
  return request({
    url: '/system/menu/' + menuId,
    method: 'get'
  });
};

// 查询菜单下拉树结构
export const treeselect = (): AxiosPromise<MenuTreeOption[]> => {
  return request({
    url: '/system/menu/treeselect',
    method: 'get'
  });
};

// 根据角色ID查询菜单下拉树结构
export const roleMenuTreeselect = (roleId: string | number): AxiosPromise<RoleMenuTree> => {
  return request({
    url: '/system/menu/roleMenuTreeselect/' + roleId,
    method: 'get'
  });
};

// 根据角色ID查询菜单下拉树结构
export const tenantPackageMenuTreeselect = (packageId: string | number): AxiosPromise<RoleMenuTree> => {
  return request({
    url: '/system/menu/tenantPackageMenuTreeselect/' + packageId,
    method: 'get'
  });
};

// 新增菜单
export const addMenu = (data: MenuForm) => {
  return request({
    url: '/system/menu',
    method: 'post',
    data: data
  });
};

// 修改菜单
export const updateMenu = (data: MenuForm) => {
  return request({
    url: '/system/menu',
    method: 'put',
    data: data
  });
};

// 删除菜单
export const delMenu = (menuId: string | number) => {
  return request({
    url: '/system/menu/' + menuId,
    method: 'delete'
  });
};
