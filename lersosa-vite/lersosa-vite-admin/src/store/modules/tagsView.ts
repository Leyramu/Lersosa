/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import {defineStore} from 'pinia';

interface View {
    path: string;
    name: string;
    meta: {
        title?: string;
        affix?: boolean;
        noCache?: boolean;
        link?: string;
    };
}

interface State {
    visitedViews: View[];
    cachedViews: string[];
    iframeViews: View[];
}

const useTagsViewStore = defineStore(
    'tags-view',
    {
        state: (): State => ({
            visitedViews: [],
            cachedViews: [],
            iframeViews: []
        }),
        actions: {
            addView(view: View) {
                this.addVisitedView(view);
                this.addCachedView(view);
            },
            addIframeView(view: View) {
                if (this.iframeViews.some(v => v.path === view.path)) return;
                this.iframeViews.push(
                    Object.assign({}, view, {
                        title: view.meta.title || 'no-name'
                    })
                );
            },
            addVisitedView(view: View) {
                if (this.visitedViews.some(v => v.path === view.path)) return;
                this.visitedViews.push(
                    Object.assign({}, view, {
                        title: view.meta.title || 'no-name'
                    })
                );
            },
            addCachedView(view: View) {
                if (this.cachedViews.includes(view.name)) return;
                if (!view.meta.noCache) {
                    this.cachedViews.push(view.name);
                }
            },
            delView(view: View): Promise<{ visitedViews: View[]; cachedViews: string[] }> {
                return new Promise(resolve => {
                    this.delVisitedView(view).then(() => {
                    });
                    this.delCachedView(view).then(() => {
                    });
                    resolve({
                        visitedViews: [...this.visitedViews],
                        cachedViews: [...this.cachedViews]
                    });
                });
            },
            delVisitedView(view: View): Promise<View[]> {
                return new Promise(resolve => {
                    for (const [i, v] of this.visitedViews.entries()) {
                        if (v.path === view.path) {
                            this.visitedViews.splice(i, 1);
                            break;
                        }
                    }
                    this.iframeViews = this.iframeViews.filter(item => item.path !== view.path);
                    resolve([...this.visitedViews]);
                });
            },
            delIframeView(view: View): Promise<View[]> {
                return new Promise(resolve => {
                    this.iframeViews = this.iframeViews.filter(item => item.path !== view.path);
                    resolve([...this.iframeViews]);
                });
            },
            delCachedView(view: View): Promise<string[]> {
                return new Promise(resolve => {
                    const index = this.cachedViews.indexOf(view.name);
                    if (index > -1) {
                        this.cachedViews.splice(index, 1);
                    }
                    resolve([...this.cachedViews]);
                });
            },
            delOthersViews(view: View): Promise<{ visitedViews: View[]; cachedViews: string[] }> {
                return new Promise(resolve => {
                    this.delOthersVisitedViews(view).then(() => {
                    });
                    this.delOthersCachedViews(view).then(() => {
                    });
                    resolve({
                        visitedViews: [...this.visitedViews],
                        cachedViews: [...this.cachedViews]
                    });
                });
            },
            delOthersVisitedViews(view: View): Promise<View[]> {
                return new Promise(resolve => {
                    this.visitedViews = this.visitedViews.filter(v => {
                        return v.meta.affix || v.path === view.path;
                    });
                    this.iframeViews = this.iframeViews.filter(item => item.path === view.path);
                    resolve([...this.visitedViews]);
                });
            },
            delOthersCachedViews(view: View): Promise<string[]> {
                return new Promise(resolve => {
                    const index = this.cachedViews.indexOf(view.name);
                    if (index > -1) {
                        this.cachedViews = this.cachedViews.slice(index, index + 1);
                    } else {
                        this.cachedViews = [];
                    }
                    resolve([...this.cachedViews]);
                });
            },
            delAllViews(view: View): Promise<{ visitedViews: View[]; cachedViews: string[] }> {
                return new Promise(resolve => {
                    this.delAllVisitedViews(view).then(() => {
                    });
                    this.delAllCachedViews(view).then(() => {
                    });
                    resolve({
                        visitedViews: [...this.visitedViews],
                        cachedViews: [...this.cachedViews]
                    });
                });
            },
            delAllVisitedViews(_view: View): Promise<View[]> {
                return new Promise(resolve => {
                    this.visitedViews = this.visitedViews.filter(tag => tag.meta.affix);
                    this.iframeViews = [];
                    resolve([...this.visitedViews]);
                });
            },
            delAllCachedViews(_view: View): Promise<string[]> {
                return new Promise(resolve => {
                    this.cachedViews = [];
                    resolve([...this.cachedViews]);
                });
            },
            updateVisitedView(view: View) {
                for (let v of this.visitedViews) {
                    if (v.path === view.path) {
                        v = Object.assign(v, view);
                        break;
                    }
                }
            },
            delRightTags(view: View): Promise<View[]> {
                return new Promise(resolve => {
                    const index = this.visitedViews.findIndex(v => v.path === view.path);
                    if (index === -1) {
                        return;
                    }
                    this.visitedViews = this.visitedViews.filter((item, idx) => {
                        if (idx <= index || (item.meta && item.meta.affix)) {
                            return true;
                        }
                        const i = this.cachedViews.indexOf(item.name);
                        if (i > -1) {
                            this.cachedViews.splice(i, 1);
                        }
                        if (item.meta.link) {
                            const fi = this.iframeViews.findIndex(v => v.path === item.path);
                            this.iframeViews.splice(fi, 1);
                        }
                        return false;
                    });
                    resolve([...this.visitedViews]);
                });
            },
            delLeftTags(view: View): Promise<View[]> {
                return new Promise(resolve => {
                    const index = this.visitedViews.findIndex(v => v.path === view.path);
                    if (index === -1) {
                        return;
                    }
                    this.visitedViews = this.visitedViews.filter((item, idx) => {
                        if (idx >= index || (item.meta && item.meta.affix)) {
                            return true;
                        }
                        const i = this.cachedViews.indexOf(item.name);
                        if (i > -1) {
                            this.cachedViews.splice(i, 1);
                        }
                        if (item.meta.link) {
                            const fi = this.iframeViews.findIndex(v => v.path === item.path);
                            this.iframeViews.splice(fi, 1);
                        }
                        return false;
                    });
                    resolve([...this.visitedViews]);
                });
            }
        }
    }
);

export default useTagsViewStore;
