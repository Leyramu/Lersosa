/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router';
import Layout from '@/layout';

interface RouteMeta {
    noCache?: boolean;
    title?: string;
    icon?: string;
    breadcrumb?: boolean;
    activeMenu?: string;
    affix?: boolean;
}

interface CustomRoute {
    path: string;
    component: any;
    children?: CustomRoute[];
    hidden?: boolean;
    alwaysShow?: boolean;
    redirect?: string;
    name?: string;
    query?: string;
    roles?: string[];
    permissions?: string[];
    meta?: RouteMeta;
}

// 公共路由
export const constantRoutes: CustomRoute[] = [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('@/views/redirect/index.vue')
            }
        ]
    },
    {
        path: '/login',
        component: () => import('@/views/login'),
        hidden: true
    },
    {
        path: '/register',
        component: () => import('@/views/register'),
        hidden: true
    },
    {
        path: '/:pathMatch(.*)*',
        component: () => import('@/views/error/404'),
        hidden: true
    },
    {
        path: '/401',
        component: () => import('@/views/error/401'),
        hidden: true
    },
    {
        path: '',
        component: Layout,
        redirect: '/index',
        children: [
            {
                path: '/index',
                component: () => import('@/views/index'),
                name: 'Index',
                meta: {title: '首页', icon: 'dashboard', affix: true}
            }
        ]
    },
    {
        path: '/user',
        component: Layout,
        hidden: true,
        redirect: 'noredirect',
        children: [
            {
                path: 'profile',
                component: () => import('@/views/system/user/profile/index'),
                name: 'Profile',
                meta: {title: '个人中心', icon: 'user'}
            }
        ]
    }
];

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes: CustomRoute[] = [
    {
        path: '/system/user-auth',
        component: Layout,
        hidden: true,
        permissions: ['system:user:edit'],
        children: [
            {
                path: 'role/:userId(\\d+)',
                component: () => import('@/views/system/user/authRole'),
                name: 'AuthRole',
                meta: {title: '分配角色', activeMenu: '/system/user'}
            }
        ]
    },
    {
        path: '/system/role-auth',
        component: Layout,
        hidden: true,
        permissions: ['system:role:edit'],
        children: [
            {
                path: 'user/:roleId(\\d+)',
                component: () => import('@/views/system/role/authUser'),
                name: 'AuthUser',
                meta: {title: '分配用户', activeMenu: '/system/role'}
            }
        ]
    },
    {
        path: '/system/dict-data',
        component: Layout,
        hidden: true,
        permissions: ['system:dict:list'],
        children: [
            {
                path: 'index/:dictId(\\d+)',
                component: () => import('@/views/system/dict/data'),
                name: 'Data',
                meta: {title: '字典数据', activeMenu: '/system/dict'}
            }
        ]
    },
    {
        path: '/monitor/job-log',
        component: Layout,
        hidden: true,
        permissions: ['monitor:job:list'],
        children: [
            {
                path: 'index/:jobId(\\d+)',
                component: () => import('@/views/monitor/job/log'),
                name: 'JobLog',
                meta: {title: '调度日志', activeMenu: '/monitor/job'}
            }
        ]
    },
    {
        path: '/tool/gen-edit',
        component: Layout,
        hidden: true,
        permissions: ['tool:gen:edit'],
        children: [
            {
                path: 'index/:tableId(\\d+)',
                component: () => import('@/views/tool/gen/editTable'),
                name: 'GenEdit',
                meta: {title: '修改生成配置', activeMenu: '/tool/gen'}
            }
        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes: [...constantRoutes, ...dynamicRoutes] as RouteRecordRaw[],
    scrollBehavior(_to, _from, savedPosition) {
        if (savedPosition) {
            return savedPosition;
        } else {
            return {top: 0};
        }
    }
});

export default router;
