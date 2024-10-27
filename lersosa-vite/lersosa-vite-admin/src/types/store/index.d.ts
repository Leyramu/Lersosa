/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

// @/settings
declare module '@/settings' {
    import {Settings} from '@/settings';
    const defaultSettings: Settings;
    export default defaultSettings;
}

// @/store/modules/dict
declare module '@/store/modules/dict' {
    import {Dict} from '@/store/modules/dict';
    const useDictStore: Dict;
    export default useDictStore;
}

// @/store/modules/permission
declare module '@/store/modules/permission' {
    import {Permission} from '@/store/modules/permission';
    const usePermissionStore: Permission;
    export default usePermissionStore;
}

// @/store/modules/settings
declare module '@/store/modules/settings' {
    import {Settings} from '@/store/modules/settings';
    const useSettingsStore: Settings;
    export default useSettingsStore;
}

// @/store/modules/tagsView
declare module '@/store/modules/tagsView' {
    import {TagsView} from '@/store/modules/tagsView';
    const useTagsViewStore: TagsView;
    export default useTagsViewStore;
}

// @/store/modules/user
declare module '@/store/modules/user' {
    import {User} from '@/store/modules/user';
    const useUserStore: User;
    export default useUserStore;
}
