/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import router from './router/index';
import {ElMessage} from 'element-plus';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import {getToken} from '@/utils/auth';
import {isHttp} from '@/utils/validate';
import {isRelogin} from '@/utils/request';
import useUserStore from '@/store/modules/user';
import useSettingsStore from '@/store/modules/settings';
import usePermissionStore from '@/store/modules/permission';

NProgress.configure({showSpinner: false});

const whiteList = ['/login', '/register'];

router.beforeEach(async (to: any, _from: any, next: any) => {
    NProgress.start();

    const token = getToken();
    if (token && token.includes('.')) { // 检查 token 是否有效
        if (to.meta.title) {
            useSettingsStore().setTitle(to.meta.title);
        }

        if (to.path === '/login') {
            next({path: '/'});
            NProgress.done();
        } else if (whiteList.includes(to.path)) {
            next();
            NProgress.done();
        } else {
            const userStore = useUserStore();
            if (userStore.roles.length === 0) {
                isRelogin.show = true;
                try {
                    await userStore.getInfo();
                    isRelogin.show = false;
                    const permissionStore = usePermissionStore();
                    const accessRoutes = await permissionStore.generateRoutes();
                    accessRoutes.forEach((route: any) => {
                        if (!isHttp(route.path)) {
                            router.addRoute(route); // 动态添加可访问路由表
                        }
                    });
                    next({...to, replace: true}); // hack方法 确保addRoutes已完成
                    NProgress.done();
                } catch (err: any) {
                    await userStore.logOut();
                    ElMessage.error(String(err));
                    next({path: '/'});
                    NProgress.done();
                }
            } else {
                next();
                NProgress.done();
            }
        }
    } else {
        if (whiteList.includes(to.path)) {
            next();
            NProgress.done();
        } else {
            next(`/login?redirect=${to.fullPath}`); // 否则全部重定向到登录页
            NProgress.done();
        }
    }
});

router.afterEach(() => {
    NProgress.done();
});

