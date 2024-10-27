/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request';
import {parseStrEmpty} from '@/utils/lersosa';

// 用户接口数据类型
interface User {
    userId: string;
    userName: string;
    nickName: string;
    email: string;
    phonenumber: string;
    sex: string;
    avatar: string;
    password: string;
    status: string;
    delFlag: string;
    loginIp: string;
    loginDate: string;
    createBy: string;
    createTime: string;
    updateBy: string;
    updateTime: string;
    remark: string;
}

interface UserListResponse {
    total: number;
    list: User[];
}

interface ResetPwdData {
    userId: string;
    password: string;
}

interface ChangeStatusData {
    userId: string;
    status: string;
}

interface UpdatePwdData {
    oldPassword: string;
    newPassword: string;
}

// 查询用户列表
export function listUser(query: { [key: string]: any }): Promise<UserListResponse> {
    return request({
        url: '/system/user/list',
        method: 'get',
        params: query
    });
}

// 查询用户详细
export function getUser(userId: string): Promise<User> {
    return request({
        url: '/system/user/' + parseStrEmpty(userId),
        method: 'get'
    });
}

// 新增用户
export function addUser(data: User): Promise<{ success: boolean }> {
    return request({
        url: '/system/user',
        method: 'post',
        data: data
    });
}

// 修改用户
export function updateUser(data: User): Promise<{ success: boolean }> {
    return request({
        url: '/system/user',
        method: 'put',
        data: data
    });
}

// 删除用户
export function delUser(userId: string): Promise<{ success: boolean }> {
    return request({
        url: '/system/user/' + userId,
        method: 'delete'
    });
}

// 用户密码重置
export function resetUserPwd(userId: string, password: string): Promise<{ success: boolean }> {
    const data: ResetPwdData = {
        userId,
        password
    };
    return request({
        url: '/system/user/resetPwd',
        method: 'put',
        data: data
    });
}

// 用户状态修改
export function changeUserStatus(userId: string, status: string): Promise<{ success: boolean }> {
    const data: ChangeStatusData = {
        userId,
        status
    };
    return request({
        url: '/system/user/changeStatus',
        method: 'put',
        data: data
    });
}

// 查询用户个人信息
export function getUserProfile(): Promise<User> {
    return request({
        url: '/system/user/profile',
        method: 'get'
    });
}

// 修改用户个人信息
export function updateUserProfile(data: User): Promise<{ success: boolean }> {
    return request({
        url: '/system/user/profile',
        method: 'put',
        data: data
    });
}

// 用户密码重置
export function updateUserPwd(oldPassword: string, newPassword: string): Promise<{ success: boolean }> {
    const data: UpdatePwdData = {
        oldPassword,
        newPassword
    };
    return request({
        url: '/system/user/profile/updatePwd',
        method: 'put',
        params: data
    });
}

// 用户头像上传
export function uploadAvatar(data: any): Promise<{ success: boolean }> {
    return request({
        url: '/system/user/profile/avatar',
        method: 'post',
        data: data
    });
}

// 查询授权角色
export function getAuthRole(userId: string): Promise<{ roleIds: string[] }> {
    return request({
        url: '/system/user/authRole/' + userId,
        method: 'get'
    });
}

// 保存授权角色
export function updateAuthRole(data: any): Promise<{ success: boolean }> {
    return request({
        url: '/system/user/authRole',
        method: 'put',
        params: data
    });
}

// 查询部门下拉树结构
export function deptTreeSelect(): Promise<DeptTree[]> {
    return request({
        url: '/system/user/deptTree',
        method: 'get'
    });
}

// 部门树结构类型
interface DeptTree {
    id: string;
    label: string;
    children: DeptTree[];
}

