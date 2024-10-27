/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import {defineStore} from 'pinia';
import auth from '@/plugins/auth'
import router, {constantRoutes, dynamicRoutes} from '@/router'
import {getRouters} from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

interface RouteConfig {
    path: string;
    component: any;
    children?: RouteConfig[];
    redirect?: string;
    permissions?: string[];
    roles?: string[];
}

interface PermissionState {
    routes: RouteConfig[];
    addRoutes: RouteConfig[];
    defaultRoutes: RouteConfig[];
    topbarRouters: RouteConfig[];
    sidebarRouters: RouteConfig[];
}

const usePermissionStore = defineStore(
    'permission',
    {
        state: (): PermissionState => ({
            routes: [],
            addRoutes: [],
            defaultRoutes: [],
            topbarRouters: [],
            sidebarRouters: []
        }),
        actions: {
            setRoutes(routes: RouteConfig[]) {
                this.addRoutes = routes
                this.routes = constantRoutes.concat(routes)
            },
            setDefaultRoutes(routes: RouteConfig[]) {
                this.defaultRoutes = constantRoutes.concat(routes)
            },
            setTopbarRoutes(routes: RouteConfig[]) {
                this.topbarRouters = routes
            },
            setSidebarRouters(routes: RouteConfig[]) {
                this.sidebarRouters = routes
            },
            generateRoutes(_roles: string[]): Promise<RouteConfig[]> {
                return new Promise((resolve) => {
                    // 向后端请求路由数据
                    getRouters().then((res: any) => {
                        const sdata = JSON.parse(JSON.stringify(res.data));
                        const rdata = JSON.parse(JSON.stringify(res.data));
                        const defaultData = JSON.parse(JSON.stringify(res.data));
                        const sidebarRoutes = filterAsyncRouter(sdata);
                        const rewriteRoutes = filterAsyncRouter(rdata, false, true);
                        const defaultRoutes = filterAsyncRouter(defaultData);
                        const asyncRoutes = filterDynamicRoutes(dynamicRoutes);
                        asyncRoutes.forEach((route) => {
                            router.addRoute(route);
                        });
                        this.setRoutes(rewriteRoutes);
                        this.setSidebarRouters(constantRoutes.concat(sidebarRoutes));
                        this.setDefaultRoutes(sidebarRoutes);
                        this.setTopbarRoutes(defaultRoutes);
                        resolve(rewriteRoutes);
                    });
                });
            }
        }
    })

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap: RouteConfig[], lastRouter: RouteConfig | boolean = false, type: boolean = false): RouteConfig[] {
    return asyncRouterMap.filter(route => {
        if (type && route.children) {
            route.children = filterChildren(route.children, lastRouter as RouteConfig);
        }
        if (route.component) {
            // Layout ParentView 组件特殊处理
            if (route.component === 'Layout') {
                route.component = Layout;
            } else if (route.component === 'ParentView') {
                route.component = ParentView;
            } else if (route.component === 'InnerLink') {
                route.component = InnerLink;
            } else {
                route.component = loadView(route.component);
            }
        }
        if (route.children != null && route.children && route.children.length) {
            route.children = filterAsyncRouter(route.children, route, type);
        } else {
            delete route['children'];
            delete route['redirect'];
        }
        return true;
    });
}

function filterChildren(childrenMap: RouteConfig[], lastRouter: RouteConfig | false = false): RouteConfig[] {
    const children: RouteConfig[] = [];
    childrenMap.forEach((el: RouteConfig, _index) => {
        if (el.children && el.children.length) {
            if (el.component === 'ParentView' && !lastRouter) {
                el.children.forEach((c: RouteConfig) => {
                    c.path = el.path + '/' + c.path;
                    if (c.children && c.children.length) {
                        children.push(...filterChildren(c.children, c));
                        return;
                    }
                    children.push(c);
                });
                return;
            }
        }
        if (lastRouter) {
            el.path = lastRouter.path + '/' + el.path;
            if (el.children && el.children.length) {
                children.push(...filterChildren(el.children, el));
                return;
            }
        }
        children.push(el);
    });
    return children;
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes: RouteConfig[]): RouteConfig[] {
    const res: RouteConfig[] = [];
    routes.forEach((route) => {
        if (route.permissions) {
            if (auth.hasPermiOr(route.permissions)) {
                res.push(route);
            }
        } else if (route.roles) {
            if (auth.hasRoleOr(route.roles)) {
                res.push(route);
            }
        }
    });
    return res;
}

export const loadView = (view: string): (() => Promise<any>) => {
    let res: (() => Promise<any>) | undefined;
    for (const path in modules) {
        const dir = path.split('views/')[1].split('.vue')[0];
        if (dir === view) {
            res = () => modules[path]();
            break;
        }
    }
    return res || (() => Promise.reject(new Error(`Module ${view} not found`)));
};

export default usePermissionStore
