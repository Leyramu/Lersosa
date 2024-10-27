/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request';

interface Menu {
    menuId: string;
    menuName: string;
    parentId: string;
    orderNum: number;
    path: string;
    component: string;
    isFrame: string;
    menuType: string;
    visible: string;
    status: string;
    perms: string;
    icon: string;
    createBy: string;
    createTime: string;
    updateBy: string;
    updateTime: string;
    remark: string;
}

interface MenuListResponse {
    total: number;
    list: Menu[];
}

interface MenuTree {
    menuId: string;
    menuName: string;
    children: MenuTree[];
}

// 查询菜单列表
export function listMenu(query: { [key: string]: any }): Promise<MenuListResponse> {
    return request({
        url: '/system/menu/list',
        method: 'get',
        params: query
    });
}

// 查询菜单详细
export function getMenu(menuId: string): Promise<Menu> {
    return request({
        url: '/system/menu/' + menuId,
        method: 'get'
    });
}

// 查询菜单下拉树结构
export function treeselect(): Promise<MenuTree[]> {
    return request({
        url: '/system/menu/treeselect',
        method: 'get'
    });
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId: string): Promise<MenuTree[]> {
    return request({
        url: '/system/menu/roleMenuTreeselect/' + roleId,
        method: 'get'
    });
}

// 新增菜单
export function addMenu(data: Menu): Promise<{ success: boolean }> {
    return request({
        url: '/system/menu',
        method: 'post',
        data: data
    });
}

// 修改菜单
export function updateMenu(data: Menu): Promise<{ success: boolean }> {
    return request({
        url: '/system/menu',
        method: 'put',
        data: data
    });
}

// 删除菜单
export function delMenu(menuId: string): Promise<{ success: boolean }> {
    return request({
        url: '/system/menu/' + menuId,
        method: 'delete'
    });
}

