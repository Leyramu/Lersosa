/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request';

// 角色接口数据类型
interface Role {
    roleId: string;
    userIds: string[];
    roleName: string;
    roleKey: string;
    roleSort: number;
    status: string;
    createBy: string;
    createTime: string;
    updateBy: string;
    updateTime: string;
    remark: string;
}

interface RoleListResponse {
    total: number;
    list: Role[];
}

interface DeptTree {
    id: string;
    label: string;
    children: DeptTree[];
}

// 查询角色列表
export function listRole(query: { [key: string]: any }): Promise<RoleListResponse> {
    return request({
        url: '/system/role/list',
        method: 'get',
        params: query
    });
}

// 查询角色详细
export function getRole(roleId: string): Promise<Role> {
    return request({
        url: '/system/role/' + roleId,
        method: 'get'
    });
}

// 新增角色
export function addRole(data: Role): Promise<{ success: boolean }> {
    return request({
        url: '/system/role',
        method: 'post',
        data: data
    });
}

// 修改角色
export function updateRole(data: Role): Promise<{ success: boolean }> {
    return request({
        url: '/system/role',
        method: 'put',
        data: data
    });
}

// 角色数据权限
export function dataScope(data: any | {
    deptCheckStrictly: boolean,
    roleId: undefined,
    menuCheckStrictly: boolean,
    roleName: undefined,
    roleKey: undefined,
    remark: undefined,
    deptIds: any[],
    menuIds: any[],
    roleSort: number,
    status: string
}): Promise<{ success: boolean }> {
    return request({
        url: '/system/role/dataScope',
        method: 'put',
        data: data
    });
}

// 角色状态修改
export function changeRoleStatus(roleId: string, status: string): Promise<{ success: boolean }> {
    const data = {
        roleId,
        status
    };
    return request({
        url: '/system/role/changeStatus',
        method: 'put',
        data: data
    });
}

// 删除角色
export function delRole(roleId: string): Promise<{ success: boolean }> {
    return request({
        url: '/system/role/' + roleId,
        method: 'delete'
    });
}

// 查询角色已授权用户列表
export function allocatedUserList(query: { [key: string]: any }): Promise<{ total: number; list: any[] }> {
    return request({
        url: '/system/role/authUser/allocatedList',
        method: 'get',
        params: query
    });
}

// 查询角色未授权用户列表
export function unallocatedUserList(query: { [key: string]: any }): Promise<{ total: number; list: any[] }> {
    return request({
        url: '/system/role/authUser/unallocatedList',
        method: 'get',
        params: query
    });
}

// 取消用户授权角色
export function authUserCancel(data: any): Promise<{ success: boolean }> {
    return request({
        url: '/system/role/authUser/cancel',
        method: 'put',
        data: data
    });
}

// 批量取消用户授权角色
export function authUserCancelAll(data: any): Promise<{ success: boolean }> {
    return request({
        url: '/system/role/authUser/cancelAll',
        method: 'put',
        data: data
    });
}

// 授权用户选择
export function authUserSelectAll(data: any): Promise<{
    success: boolean
}> {
    return request({
        url: '/system/role/authUser/selectAll',
        method: 'put',
        data: data
    });
}

// 根据角色ID查询部门树结构
export function deptTreeSelect(roleId: string): Promise<DeptTree[]> {
    return request({
        url: '/system/role/deptTree/' + roleId,
        method: 'get'
    });
}

