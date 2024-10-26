/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import useDictStore from '@/store/modules/dict'
import {getDicts} from '@/api/system/dict/data'

/**
 * 获取字典数据
 */
export function useDict(...args) {
    const res = ref({});
    return (() => {
        args.forEach((dictType, index) => {
            res.value[dictType] = [];
            const dicts = useDictStore().getDict(dictType);
            if (dicts) {
                res.value[dictType] = dicts;
            } else {
                getDicts(dictType).then(resp => {
                    res.value[dictType] = resp.data.map(p => ({
                        label: p.dictLabel,
                        value: p.dictValue,
                        elTagType: p.listClass,
                        elTagClass: p.cssClass
                    }))
                    useDictStore().setDict(dictType, res.value[dictType]);
                })
            }
        })
        return toRefs(res.value);
    })()
}
