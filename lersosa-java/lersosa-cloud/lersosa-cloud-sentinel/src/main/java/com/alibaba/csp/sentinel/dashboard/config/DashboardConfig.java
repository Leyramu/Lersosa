/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
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
 */

package com.alibaba.csp.sentinel.dashboard.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.lang.NonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Dashboard 本地配置支持
 *
 * @author jason
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.5.0
 * @since 2024/9/3
 */
public class DashboardConfig {

    /**
     * 默认计算机运行状况超时（以毫秒为单位）
     */
    public static final int DEFAULT_MACHINE_HEALTHY_TIMEOUT_MS = 60_000;

    /**
     * 登录用户名
     */
    public static final String CONFIG_AUTH_USERNAME = "sentinel.dashboard.auth.username";

    /**
     * 登录密码
     */
    public static final String CONFIG_AUTH_PASSWORD = "sentinel.dashboard.auth.password";

    /**
     * 当应用程序在特定时间段（以毫秒为单位）后没有健康的机器时，在侧边栏中隐藏应用程序名称
     */
    public static final String CONFIG_HIDE_APP_NO_MACHINE_MILLIS = "sentinel.dashboard.app.hideAppNoMachineMillis";

    /**
     * 当应用程序在特定时间段（毫秒）后没有运行状况良好的计算机时删除应用程序
     */
    public static final String CONFIG_REMOVE_APP_NO_MACHINE_MILLIS = "sentinel.dashboard.removeAppNoMachineMillis";

    /**
     * 超时值
     */
    public static final String CONFIG_UNHEALTHY_MACHINE_MILLIS = "sentinel.dashboard.unhealthyMachineMillis";

    /**
     * 在特定时间段（毫秒）后自动删除不健康的计算机
     */
    public static final String CONFIG_AUTO_REMOVE_MACHINE_MILLIS = "sentinel.dashboard.autoRemoveMachineMillis";

    /**
     * 缓存
     */
    private static final ConcurrentMap<String, Object> CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取配置
     *
     * @param name 配置名称
     * @return 配置值
     */
    @NonNull
    private static String getConfig(String name) {
        String val = System.getenv(name);
        if (StringUtils.isNotEmpty(val)) {
            return val;
        }
        val = System.getProperty(name);
        if (StringUtils.isNotEmpty(val)) {
            return val;
        }
        return "";
    }

    /**
     * 获取配置
     *
     * @param name 配置名称
     * @return 配置值
     */
    protected static String getConfigStr(String name) {
        if (CACHE_MAP.containsKey(name)) {
            return (String) CACHE_MAP.get(name);
        }

        String val = getConfig(name);

        if (StringUtils.isBlank(val)) {
            return null;
        }

        CACHE_MAP.put(name, val);
        return val;
    }

    /**
     * 获取配置
     *
     * @param name       配置名称
     * @param defaultVal 默认值
     * @param minVal     最小值
     * @return 配置值
     */
    protected static int getConfigInt(String name, int defaultVal, int minVal) {
        if (CACHE_MAP.containsKey(name)) {
            return (int) CACHE_MAP.get(name);
        }
        int val = NumberUtils.toInt(getConfig(name));
        if (val == 0) {
            val = defaultVal;
        } else if (val < minVal) {
            val = minVal;
        }
        CACHE_MAP.put(name, val);
        return val;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public static String getAuthUsername() {
        return getConfigStr(CONFIG_AUTH_USERNAME);
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    public static String getAuthPassword() {
        return getConfigStr(CONFIG_AUTH_PASSWORD);
    }

    /**
     * 获取隐藏无健康机器时间
     *
     * @return 隐藏无健康机器时间
     */
    public static int getHideAppNoMachineMillis() {
        return getConfigInt(CONFIG_HIDE_APP_NO_MACHINE_MILLIS, 0, 60000);
    }

    /**
     * 获取删除无健康机器时间
     *
     * @return 删除无健康机器时间
     */
    public static int getRemoveAppNoMachineMillis() {
        return getConfigInt(CONFIG_REMOVE_APP_NO_MACHINE_MILLIS, 0, 120000);
    }

    /**
     * 获取自动删除无健康机器时间
     *
     * @return 自动删除无健康机器时间
     */
    public static int getAutoRemoveMachineMillis() {
        return getConfigInt(CONFIG_AUTO_REMOVE_MACHINE_MILLIS, 0, 300000);
    }

    /**
     * 获取无健康机器时间
     *
     * @return 无健康机器时间
     */
    public static int getUnhealthyMachineMillis() {
        return getConfigInt(CONFIG_UNHEALTHY_MACHINE_MILLIS, DEFAULT_MACHINE_HEALTHY_TIMEOUT_MS, 30000);
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        CACHE_MAP.clear();
    }
}
