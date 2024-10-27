/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request';

interface Dept {
    id: number;
    parentId: number;
    deptName: string;
    orderNum: number;
    leader: string;
    phone: string;
    email: string;
    status: string;
    createBy: string;
    createTime: string;
    updateBy: string;
    updateTime: string;
    remark: string;
}

interface DeptListResponse {
    total: number;
    list: Dept[];
}

// 查询部门列表
export function listDept(query: { [key: string]: any }): Promise<DeptListResponse> {
    return request({
        url: '/system/dept/list',
        method: 'get',
        params: query
    });
}

// 查询部门列表（排除节点）
export function listDeptExcludeChild(deptId: string): Promise<DeptListResponse> {
    return request({
        url: '/system/dept/list/exclude/' + deptId,
        method: 'get'
    });
}

// 查询部门详细
export function getDept(deptId: string): Promise<Dept> {
    return request({
        url: '/system/dept/' + deptId,
        method: 'get'
    });
}

// 新增部门
export function addDept(data: Dept): Promise<{ success: boolean }> {
    return request({
        url: '/system/dept',
        method: 'post',
        data: data
    });
}

// 修改部门
export function updateDept(data: Dept): Promise<{ success: boolean }> {
    return request({
        url: '/system/dept',
        method: 'put',
        data: data
    });
}

// 删除部门
export function delDept(deptId: string): Promise<{ success: boolean }> {
    return request({
        url: '/system/dept/' + deptId,
        method: 'delete'
    });
}
