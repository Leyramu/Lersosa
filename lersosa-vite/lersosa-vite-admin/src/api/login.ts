/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
    return request({
        url: '/auth/login',
        headers: {
            isToken: false,
            repeatSubmit: false
        },
        method: 'post',
        data: {username, password, code, uuid}
    })
}

// 注册方法
export function register(data) {
    return request({
        url: '/auth/register',
        headers: {
            isToken: false
        },
        method: 'post',
        data: data
    })
}

// 刷新方法
export function refreshToken() {
    return request({
        url: '/auth/refresh',
        method: 'post'
    })
}

// 获取用户详细信息
export function getInfo() {
    return request({
        url: '/system/user/getInfo',
        method: 'get'
    })
}

// 退出方法
export function logout() {
    return request({
        url: '/auth/logout',
        method: 'delete'
    })
}

// 获取验证码
export function getCodeImg() {
    return request({
        url: '/code',
        headers: {
            isToken: false
        },
        method: 'get',
        timeout: 20000
    })
}
