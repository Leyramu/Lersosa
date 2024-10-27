/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request';

// 查询定时任务调度列表
export function listJob(query: { [key: string]: any }): Promise<any> {
    return request({
        url: '/schedule/job/list',
        method: 'get',
        params: query
    });
}

// 查询定时任务调度详细
export function getJob(jobId: number | string): Promise<any> {
    return request({
        url: '/schedule/job/' + jobId,
        method: 'get'
    });
}

// 新增定时任务调度
export function addJob(data: { [key: string]: any }): Promise<any> {
    return request({
        url: '/schedule/job',
        method: 'post',
        data: data
    });
}

// 修改定时任务调度
export function updateJob(data: { [key: string]: any }): Promise<any> {
    return request({
        url: '/schedule/job',
        method: 'put',
        data: data
    });
}

// 删除定时任务调度
export function delJob(jobId: number | string): Promise<any> {
    return request({
        url: '/schedule/job/' + jobId,
        method: 'delete'
    });
}

// 任务状态修改
export function changeJobStatus(jobId: number | string, status: string): Promise<any> {
    const data = {
        jobId,
        status
    };
    return request({
        url: '/schedule/job/changeStatus',
        method: 'put',
        data: data
    });
}

// 定时任务立即执行一次
export function runJob(jobId: number | string, jobGroup: string): Promise<any> {
    const data = {
        jobId,
        jobGroup
    };
    return request({
        url: '/schedule/job/run',
        method: 'put',
        data: data
    });
}
