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

import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import lombok.NonNull;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * Cache 装饰器模式(用于扩展 Caffeine 一级缓存).
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class CaffeineCacheDecorator implements Cache {

    /**
     * Caffeine 缓存.
     */
    private static final com.github.benmanes.caffeine.cache.Cache<Object, Object>
        CAFFEINE = SpringUtils.getBean("caffeine");

    /**
     * 缓存名称.
     */
    private final String name;

    /**
     * Spring 缓存.
     */
    private final Cache cache;

    /**
     * 构造函数.
     *
     * @param name  缓存名称
     * @param cache Spring 缓存
     */
    public CaffeineCacheDecorator(String name, Cache cache) {
        this.name = name;
        this.cache = cache;
    }

    /**
     * 获取缓存名称.
     *
     * @return 缓存名称
     */
    @Override
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * 获取原生缓存.
     *
     * @return 原生缓存
     */
    @Override
    @NonNull
    public Object getNativeCache() {
        return cache.getNativeCache();
    }

    /**
     * 获取唯一键.
     *
     * @param key 键
     * @return 唯一键
     */
    public String getUniqueKey(Object key) {
        return name + ":" + key;
    }

    /**
     * 获取缓存值.
     *
     * @param key 键
     * @return 缓存值
     */
    @Override
    public ValueWrapper get(@NonNull Object key) {
        Object o = CAFFEINE.get(getUniqueKey(key), _ -> cache.get(key));
        return (ValueWrapper) o;
    }

    /**
     * 获取缓存值.
     *
     * @param key  键
     * @param type 类型
     * @param <T>  泛型
     * @return 缓存值
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(@NonNull Object key, Class<T> type) {
        Object o = CAFFEINE.get(getUniqueKey(key), _ -> cache.get(key, type));
        return (T) o;
    }

    /**
     * 设置缓存值.
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void put(@NonNull Object key, Object value) {
        CAFFEINE.invalidate(getUniqueKey(key));
        cache.put(key, value);
    }

    /**
     * 设置缓存值.
     *
     * @param key   键
     * @param value 值
     * @return 值包装器
     */
    @Override
    public ValueWrapper putIfAbsent(@NonNull Object key, Object value) {
        CAFFEINE.invalidate(getUniqueKey(key));
        return cache.putIfAbsent(key, value);
    }

    /**
     * 删除缓存值.
     *
     * @param key 键
     */
    @Override
    public void evict(@NonNull Object key) {
        evictIfPresent(key);
    }

    /**
     * 删除缓存值.
     *
     * @param key 键
     * @return 是否删除成功
     */
    @Override
    public boolean evictIfPresent(@NonNull Object key) {
        boolean b = cache.evictIfPresent(key);
        if (b) {
            CAFFEINE.invalidate(getUniqueKey(key));
        }
        return b;
    }

    /**
     * 清空缓存.
     */
    @Override
    public void clear() {
        cache.clear();
    }

    /**
     * 清空缓存.
     *
     * @return 是否成功
     */
    @Override
    public boolean invalidate() {
        return cache.invalidate();
    }

    /**
     * 获取缓存值.
     *
     * @param key         键
     * @param valueLoader 值加载器
     * @param <T>         泛型
     * @return 缓存值
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
        Object o = CAFFEINE.get(getUniqueKey(key), _ -> cache.get(key, valueLoader));
        return (T) o;
    }
}
