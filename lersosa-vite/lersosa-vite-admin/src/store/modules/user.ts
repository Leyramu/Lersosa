/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import {defineStore} from 'pinia';
import {getInfo, login, logout} from '@/api/login';
import {getToken, removeToken, setToken} from '@/utils/auth';
import defAva from '@/assets/images/profile.jpg';

interface UserState {
    token: string;
    id: string;
    name: string;
    avatar: string;
    roles: string[];
    permissions: string[];
}

interface LoginResponse {
    data: {
        access_token: string;
    };
}

interface InfoResponse {
    user: {
        userId: string;
        userName: string;
        avatar?: string;
    };
    roles: string[];
    permissions: string[];
}

const useUserStore = defineStore('user', {
    state: (): UserState => ({
        token: getToken() || '',
        id: '',
        name: '',
        avatar: '',
        roles: [],
        permissions: []
    }),
    actions: {
        // 登录
        login(userInfo: { username: string; password: string; code: string; uuid: string }) {
            const {username, password, code, uuid} = userInfo;
            return new Promise((resolve, reject) => {
                login(username.trim(), password, code, uuid)
                    .then((res: LoginResponse) => {
                        const {access_token} = res.data;
                        setToken(access_token);
                        this.token = access_token;
                        resolve(null);
                    })
                    .catch((error: unknown) => {
                        reject(error);
                    });
            });
        },
        // 获取用户信息
        getInfo() {
            return new Promise((resolve, reject) => {
                getInfo()
                    .then((res: InfoResponse) => {
                        const {user} = res;
                        const avatar = user.avatar ? user.avatar : defAva;

                        if (res.roles && res.roles.length > 0) {
                            this.roles = res.roles;
                            this.permissions = res.permissions;
                        } else {
                            this.roles = ['ROLE_DEFAULT'];
                        }

                        this.id = user.userId;
                        this.name = user.userName;
                        this.avatar = avatar;
                        resolve(res);
                    })
                    .catch((error: unknown) => {
                        reject(error);
                    });
            });
        },
        // 退出系统
        logOut() {
            return new Promise((resolve, reject) => {
                logout(this.token)
                    .then(() => {
                        this.token = '';
                        this.roles = [];
                        this.permissions = [];
                        removeToken();
                        resolve(null);
                    })
                    .catch((error: unknown) => {
                        reject(error);
                    });
            });
        }
    }
});

export default useUserStore;
