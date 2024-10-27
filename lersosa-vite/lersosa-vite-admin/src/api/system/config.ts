/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request';

interface Config {
    id: number;
    configName: string;
    configKey: string;
    configValue: string;
    configType: string;
    createBy: string;
    createTime: string;
    updateBy: string;
    updateTime: string;
    remark: string;
}

interface ConfigListResponse {
    total: number;
    list: Config[];
}

// 查询参数列表
export function listConfig(query: { [key: string]: any }): Promise<ConfigListResponse> {
    return request({
        url: '/system/config/list',
        method: 'get',
        params: query
    });
}

// 查询参数详细
export function getConfig(configId: string): Promise<Config> {
    return request({
        url: '/system/config/' + configId,
        method: 'get'
    });
}

// 根据参数键名查询参数值
export function getConfigKey(configKey: string): Promise<string> {
    return request({
        url: '/system/config/configKey/' + configKey,
        method: 'get'
    });
}

// 新增参数配置
export function addConfig(data: Config): Promise<{ success: boolean }> {
    return request({
        url: '/system/config',
        method: 'post',
        data: data
    });
}

// 修改参数配置
export function updateConfig(data: Config): Promise<{ success: boolean }> {
    return request({
        url: '/system/config',
        method: 'put',
        data: data
    });
}

// 删除参数配置
export function delConfig(configId: string): Promise<{ success: boolean }> {
    return request({
        url: '/system/config/' + configId,
        method: 'delete'
    });
}

// 刷新参数缓存
export function refreshCache(): Promise<{ success: boolean }> {
    return request({
        url: '/system/config/refreshCache',
        method: 'delete'
    });
}
