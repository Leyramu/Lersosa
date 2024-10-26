/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request'

// 查询公告列表
export function listNotice(query) {
    return request({
        url: '/system/notice/list',
        method: 'get',
        params: query
    })
}

// 查询公告详细
export function getNotice(noticeId) {
    return request({
        url: '/system/notice/' + noticeId,
        method: 'get'
    })
}

// 新增公告
export function addNotice(data) {
    return request({
        url: '/system/notice',
        method: 'post',
        data: data
    })
}

// 修改公告
export function updateNotice(data) {
    return request({
        url: '/system/notice',
        method: 'put',
        data: data
    })
}

// 删除公告
export function delNotice(noticeId) {
    return request({
        url: '/system/notice/' + noticeId,
        method: 'delete'
    })
}
