/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request'

// 查询登录日志列表
export function list(query) {
    return request({
        url: '/system/logininfor/list',
        method: 'get',
        params: query
    })
}

// 删除登录日志
export function delLogininfor(infoId) {
    return request({
        url: '/system/logininfor/' + infoId,
        method: 'delete'
    })
}

// 解锁用户登录状态
export function unlockLogininfor(userName) {
    return request({
        url: '/system/logininfor/unlock/' + userName,
        method: 'get'
    })
}

// 清空登录日志
export function cleanLogininfor() {
    return request({
        url: '/system/logininfor/clean',
        method: 'delete'
    })
}
