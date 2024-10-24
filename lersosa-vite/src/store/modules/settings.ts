/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import defaultSettings from '@/settings'
import {useDynamicTitle} from '@/utils/dynamicTitle'

const {sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle} = defaultSettings

const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''

const useSettingsStore = defineStore(
    'settings',
    {
        state: () => ({
            title: '',
            theme: storageSetting.theme || '#409EFF',
            sideTheme: storageSetting.sideTheme || sideTheme,
            showSettings: showSettings,
            topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
            tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
            fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
            sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
            dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle
        }),
        actions: {
            // 修改布局设置
            changeSetting(data) {
                const {key, value} = data
                if (this.hasOwnProperty(key)) {
                    this[key] = value
                }
            },
            // 设置网页标题
            setTitle(title) {
                this.title = title
                useDynamicTitle();
            }
        }
    })

export default useSettingsStore
