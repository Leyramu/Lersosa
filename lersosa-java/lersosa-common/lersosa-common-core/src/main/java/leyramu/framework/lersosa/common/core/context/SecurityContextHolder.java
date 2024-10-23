/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import leyramu.framework.lersosa.common.core.constant.SecurityConstants;
import leyramu.framework.lersosa.common.core.text.Convert;
import leyramu.framework.lersosa.common.core.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量信息
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class SecurityContextHolder {

    /**
     * 线程本地变量
     */
    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 设置缓存对象
     *
     * @param key   键值
     * @param value 值
     */
    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    /**
     * 获取缓存对象
     *
     * @param key 键值
     * @return 缓存对象
     */
    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, StringUtils.EMPTY));
    }

    /**
     * 获取缓存对象
     *
     * @param key   键值
     * @param clazz 类型
     * @return 缓存对象
     */
    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return StringUtils.cast(map.getOrDefault(key, null));
    }

    /**
     * 获取当前线程的缓存对象
     *
     * @return 缓存对象
     */
    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    /**
     * 设置当前线程的缓存对象
     *
     * @param threadLocalMap 缓存对象
     */
    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public static Long getUserId() {
        return Convert.toLong(get(SecurityConstants.DETAILS_USER_ID), 0L);
    }

    /**
     * 设置用户ID
     *
     * @param account 用户ID
     */
    public static void setUserId(String account) {
        set(SecurityConstants.DETAILS_USER_ID, account);
    }

    /**
     * 获取用户名称
     *
     * @return 用户名称
     */
    public static String getUserName() {
        return get(SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 设置用户名称
     *
     * @param username 用户名称
     */
    public static void setUserName(String username) {
        set(SecurityConstants.DETAILS_USERNAME, username);
    }

    /**
     * 获取用户key
     *
     * @return 用户key
     */
    public static String getUserKey() {
        return get(SecurityConstants.USER_KEY);
    }

    /**
     * 设置用户key
     *
     * @param userKey 用户key
     */
    public static void setUserKey(String userKey) {
        set(SecurityConstants.USER_KEY, userKey);
    }

    /**
     * 获取权限
     *
     * @return 权限
     */
    public static String getPermission() {
        return get(SecurityConstants.ROLE_PERMISSION);
    }

    /**
     * 设置权限
     *
     * @param permissions 权限
     */
    public static void setPermission(String permissions) {
        set(SecurityConstants.ROLE_PERMISSION, permissions);
    }

    /**
     * 清理当前线程缓存
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
