/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import useTagsViewStore from '@/store/modules/tagsView'
import router from '@/router'

export default {
    // 刷新当前tab页签
    refreshPage(obj) {
        const {path, query, matched} = router.currentRoute.value;
        if (obj === undefined) {
            matched.forEach((m) => {
                if (m.components && m.components.default && m.components.default.name) {
                    if (!['Layout', 'ParentView'].includes(m.components.default.name)) {
                        obj = {name: m.components.default.name, path: path, query: query};
                    }
                }
            });
        }
        return useTagsViewStore().delCachedView(obj).then(() => {
            const {path, query} = obj
            router.replace({
                path: '/redirect' + path,
                query: query
            })
        })
    },
    // 关闭当前tab页签，打开新页签
    closeOpenPage(obj) {
        useTagsViewStore().delView(router.currentRoute.value);
        if (obj !== undefined) {
            return router.push(obj);
        }
    },
    // 关闭指定tab页签
    closePage(obj) {
        if (obj === undefined) {
            return useTagsViewStore().delView(router.currentRoute.value).then(({visitedViews}) => {
                const latestView = visitedViews.slice(-1)[0]
                if (latestView) {
                    return router.push(latestView.fullPath)
                }
                return router.push('/');
            });
        }
        return useTagsViewStore().delView(obj);
    },
    // 关闭所有tab页签
    closeAllPage() {
        return useTagsViewStore().delAllViews();
    },
    // 关闭左侧tab页签
    closeLeftPage(obj) {
        return useTagsViewStore().delLeftTags(obj || router.currentRoute.value);
    },
    // 关闭右侧tab页签
    closeRightPage(obj) {
        return useTagsViewStore().delRightTags(obj || router.currentRoute.value);
    },
    // 关闭其他tab页签
    closeOtherPage(obj) {
        return useTagsViewStore().delOthersViews(obj || router.currentRoute.value);
    },
    // 打开tab页签
    openPage(url) {
        return router.push(url);
    },
    // 修改tab页签
    updatePage(obj) {
        return useTagsViewStore().updateVisitedView(obj);
    }
}
