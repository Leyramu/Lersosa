/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.dict.utils;

import leyramu.framework.lersosa.common.core.constant.CacheNames;
import leyramu.framework.lersosa.common.redis.utils.CacheUtils;
import leyramu.framework.lersosa.system.api.domain.vo.RemoteDictDataVo;

import java.util.List;

/**
 * 字典工具类.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@SuppressWarnings("unused")
public class DictUtils {

    /**
     * 设置字典缓存.
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<RemoteDictDataVo> dictDatas) {
        CacheUtils.put(CacheNames.SYS_DICT, key, dictDatas);
    }

    /**
     * 获取字典缓存.
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<RemoteDictDataVo> getDictCache(String key) {
        return CacheUtils.get(CacheNames.SYS_DICT, key);
    }

    /**
     * 删除指定字典缓存.
     *
     * @param key 字典键
     */
    public static void removeDictCache(String key) {
        CacheUtils.evict(CacheNames.SYS_DICT, key);
    }

    /**
     * 清空字典缓存.
     */
    public static void clearDictCache() {
        CacheUtils.clear(CacheNames.SYS_DICT);
    }
}
