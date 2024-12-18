/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.dict.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import leyramu.framework.lersosa.common.core.constant.CacheConstants;
import leyramu.framework.lersosa.common.core.service.DictService;
import leyramu.framework.lersosa.common.core.utils.StreamUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.system.api.RemoteDictService;
import leyramu.framework.lersosa.system.api.domain.vo.RemoteDictDataVo;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典服务服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final Cache<Object, Object> ceffeine;

    @DubboReference
    private RemoteDictService remoteDictService;

    /**
     * 根据字典类型和字典值获取字典标签.
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    @SuppressWarnings("unchecked")
    @Override
    public String getDictLabel(String dictType, String dictValue, String separator) {
        // 优先从本地缓存获取
        List<RemoteDictDataVo> datas = (List<RemoteDictDataVo>) ceffeine.get(CacheConstants.SYS_DICT_KEY + "remote:" + dictType, _ -> remoteDictService.selectDictDataByType(dictType));
        Map<String, String> map = StreamUtils.toMap(datas, RemoteDictDataVo::getDictValue, RemoteDictDataVo::getDictLabel);
        if (StringUtils.containsAny(dictValue, separator)) {
            return Arrays.stream(dictValue.split(separator))
                .map(v -> map.getOrDefault(v, StringUtils.EMPTY))
                .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictValue, StringUtils.EMPTY);
        }
    }

    /**
     * 根据字典类型和字典标签获取字典值.
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    @SuppressWarnings("unchecked")
    @Override
    public String getDictValue(String dictType, String dictLabel, String separator) {
        // 优先从本地缓存获取
        List<RemoteDictDataVo> datas = (List<RemoteDictDataVo>) ceffeine.get(CacheConstants.SYS_DICT_KEY + "remote:" + dictType, _ -> remoteDictService.selectDictDataByType(dictType));
        Map<String, String> map = StreamUtils.toMap(datas, RemoteDictDataVo::getDictLabel, RemoteDictDataVo::getDictValue);
        if (StringUtils.containsAny(dictLabel, separator)) {
            return Arrays.stream(dictLabel.split(separator))
                .map(l -> map.getOrDefault(l, StringUtils.EMPTY))
                .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictLabel, StringUtils.EMPTY);
        }
    }

    @Override
    public Map<String, String> getAllDictByDictType(String dictType) {
        List<RemoteDictDataVo> list = remoteDictService.selectDictDataByType(dictType);
        return StreamUtils.toMap(list, RemoteDictDataVo::getDictValue, RemoteDictDataVo::getDictLabel);
    }
}
