/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import {defineStore} from 'pinia';
import Cookies from 'js-cookie';

interface SidebarState {
    opened: boolean;
    withoutAnimation: boolean;
    hide: boolean;
}

interface AppState {
    sidebar: SidebarState;
    device: string;
    size: string;
}

const useAppStore = defineStore('app', {
    state: (): AppState => ({
        sidebar: {
            opened: Cookies.get('sidebarStatus') !== undefined ? !!+Cookies.get('sidebarStatus')! : true,
            withoutAnimation: false,
            hide: false
        },
        device: 'desktop',
        size: Cookies.get('size') || 'default'
    }),
    actions: {
        toggleSideBar(withoutAnimation: boolean = false) {
            if (this.sidebar.hide) {
                return false;
            }
            this.sidebar.opened = !this.sidebar.opened;
            this.sidebar.withoutAnimation = withoutAnimation;
            if (this.sidebar.opened) {
                Cookies.set('sidebarStatus', '1');
            } else {
                Cookies.set('sidebarStatus', '0');
            }
        },
        closeSideBar({withoutAnimation}: { withoutAnimation: boolean }) {
            Cookies.set('sidebarStatus', '0');
            this.sidebar.opened = false;
            this.sidebar.withoutAnimation = withoutAnimation;
        },
        toggleDevice(device: string) {
            this.device = device;
        },
        setSize(size: string) {
            this.size = size;
            Cookies.set('size', size);
        },
        toggleSideBarHide(status: boolean) {
            this.sidebar.hide = status;
        }
    }
});

export default useAppStore;
