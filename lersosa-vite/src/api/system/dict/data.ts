/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request'

// 查询字典数据列表
export function listData(query) {
    return request({
        url: '/system/dict/data/list',
        method: 'get',
        params: query
    })
}

// 查询字典数据详细
export function getData(dictCode) {
    return request({
        url: '/system/dict/data/' + dictCode,
        method: 'get'
    })
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
    return request({
        url: '/system/dict/data/type/' + dictType,
        method: 'get'
    })
}

// 新增字典数据
export function addData(data) {
    return request({
        url: '/system/dict/data',
        method: 'post',
        data: data
    })
}

// 修改字典数据
export function updateData(data) {
    return request({
        url: '/system/dict/data',
        method: 'put',
        data: data
    })
}

// 删除字典数据
export function delData(dictCode) {
    return request({
        url: '/system/dict/data/' + dictCode,
        method: 'delete'
    })
}
