/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request';

// 定义一些基本的类型
interface QueryParams {
    [key: string]: any;
}

interface GenTableData {
    [key: string]: any;
}

// 查询生成表数据
export function listTable(query: QueryParams): Promise<any> {
    return request({
        url: '/code/gen/list',
        method: 'get',
        params: query
    });
}

// 查询db数据库列表
export function listDbTable(query: QueryParams): Promise<any> {
    return request({
        url: '/code/gen/db/list',
        method: 'get',
        params: query
    });
}

// 查询表详细信息
export function getGenTable(tableId: string): Promise<any> {
    return request({
        url: '/code/gen/' + tableId,
        method: 'get'
    });
}

// 修改代码生成信息
export function updateGenTable(data: GenTableData): Promise<any> {
    return request({
        url: '/code/gen',
        method: 'put',
        data: data
    });
}

// 导入表
export function importTable(data: QueryParams): Promise<any> {
    return request({
        url: '/code/gen/importTable',
        method: 'post',
        params: data
    });
}

// 预览生成代码
export function previewTable(tableId: string): Promise<any> {
    return request({
        url: '/code/gen/preview/' + tableId,
        method: 'get'
    });
}

// 删除表数据
export function delTable(tableId: string): Promise<any> {
    return request({
        url: '/code/gen/' + tableId,
        method: 'delete'
    });
}

// 生成代码（自定义路径）
export function genCode(tableName: string): Promise<any> {
    return request({
        url: '/code/gen/genCode/' + tableName,
        method: 'get'
    });
}

// 同步数据库
export function synchDb(tableName: string): Promise<any> {
    return request({
        url: '/code/gen/synchDb/' + tableName,
        method: 'get'
    });
}
