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

package leyramu.framework.lersosa.common.redis.manager;

import leyramu.framework.lersosa.common.redis.utils.RedisUtils;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 重写 cacheName 处理方法 支持多参数.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/6
 */
@NoArgsConstructor
public class PlusSpringCacheManager implements CacheManager {

    /**
     * 动态创建 catch.
     */
    private boolean dynamic = true;

    /**
     * 允许 null 值.
     */
    @Setter
    private boolean allowNullValues = true;

    /**
     * 事务感知.
     */
    @Setter
    private boolean transactionAware = true;

    /**
     * 按 catch 名称映射的 catch 配置.
     */
    Map<String, CacheConfig> configMap = new ConcurrentHashMap<>();

    /**
     * 按 catch 名称映射的 catch 实例.
     */
    ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<>();

    /**
     * 定义 'fixed' 缓存名称.
     *
     * @param names 缓存数量
     */
    public void setCacheNames(Collection<String> names) {
        if (names != null) {
            for (String name : names) {
                getCache(name);
            }
            dynamic = false;
        } else {
            dynamic = true;
        }
    }

    /**
     * 设置由 catch 映射的缓存配置，名为.
     *
     * @param config object
     */
    @SuppressWarnings("unchecked")
    public void setConfig(Map<String, ? extends CacheConfig> config) {
        this.configMap = (Map<String, CacheConfig>) config;
    }

    /**
     * 创建默认的 CacheConfig.
     *
     * @return CacheConfig
     */
    protected CacheConfig createDefaultConfig() {
        return new CacheConfig();
    }

    /**
     * 获取缓存实例.
     *
     * @param name 缓存名称
     * @return Cache
     */
    @Override
    public Cache getCache(@Nullable String name) {
        // 重写 cacheName 支持多参数
        String[] array = StringUtils.delimitedListToStringArray(name, "#");
        name = array[0];

        Cache cache = instanceMap.get(name);
        if (cache != null) {
            return cache;
        }
        if (!dynamic) {
            return cache;
        }

        CacheConfig config = configMap.get(name);
        if (config == null) {
            config = createDefaultConfig();
            configMap.put(name, config);
        }

        if (array.length > 1) {
            config.setTTL(DurationStyle.detectAndParse(array[1]).toMillis());
        }
        if (array.length > 2) {
            config.setMaxIdleTime(DurationStyle.detectAndParse(array[2]).toMillis());
        }
        if (array.length > 3) {
            config.setMaxSize(Integer.parseInt(array[3]));
        }

        if (config.getMaxIdleTime() == 0 && config.getTTL() == 0 && config.getMaxSize() == 0) {
            return createMap(name, config);
        }

        return createMapCache(name, config);
    }

    /**
     * 创建 Map 实例.
     *
     * @param name          缓存名称
     * @param ignoredConfig 缓存配置
     * @return Cache
     */
    private Cache createMap(String name, CacheConfig ignoredConfig) {
        RMap<Object, Object> map = RedisUtils.getClient().getMap(name);

        Cache cache = new CaffeineCacheDecorator(name, new RedissonCache(map, allowNullValues));
        if (transactionAware) {
            cache = new TransactionAwareCacheDecorator(cache);
        }
        Cache oldCache = instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        }
        return cache;
    }

    /**
     * 创建 MapCache 实例.
     *
     * @param name   缓存名称
     * @param config 缓存配置
     * @return Cache
     */
    private Cache createMapCache(String name, CacheConfig config) {
        RMapCache<Object, Object> map = RedisUtils.getClient().getMapCache(name);

        Cache cache = new CaffeineCacheDecorator(name, new RedissonCache(map, config, allowNullValues));
        if (transactionAware) {
            cache = new TransactionAwareCacheDecorator(cache);
        }
        Cache oldCache = instanceMap.putIfAbsent(name, cache);
        if (oldCache != null) {
            cache = oldCache;
        } else {
            map.setMaxSize(config.getMaxSize());
        }
        return cache;
    }

    /**
     * 获取缓存名称集合.
     *
     * @return Collection
     */
    @Override
    @NonNull
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(configMap.keySet());
    }
}
