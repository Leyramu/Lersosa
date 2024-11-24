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

package com.alibaba.csp.sentinel.dashboard.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.lang.NonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Dashboard 本地配置支持.
 *
 * @author jason
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
public class DashboardConfig {

    /**
     * 默认机器健康超时时间（毫秒）.
     */
    public static final int DEFAULT_MACHINE_HEALTHY_TIMEOUT_MS = 60_000;

    /**
     * 登录用户名配置键.
     */
    public static final String CONFIG_AUTH_USERNAME = "sentinel.dashboard.auth.username";

    /**
     * 登录密码配置键.
     */
    public static final String CONFIG_AUTH_PASSWORD = "sentinel.dashboard.auth.password";

    /**
     * 当应用在指定时间内没有健康机器时，在侧边栏中隐藏应用名称的配置键.
     */
    public static final String CONFIG_HIDE_APP_NO_MACHINE_MILLIS = "sentinel.dashboard.app.hideAppNoMachineMillis";

    /**
     * 当应用在指定时间内没有健康机器时，移除应用的配置键.
     */
    public static final String CONFIG_REMOVE_APP_NO_MACHINE_MILLIS = "sentinel.dashboard.removeAppNoMachineMillis";

    /**
     * 机器不健康的超时时间配置键.
     */
    public static final String CONFIG_UNHEALTHY_MACHINE_MILLIS = "sentinel.dashboard.unhealthyMachineMillis";

    /**
     * 自动移除不健康机器的时间配置键.
     */
    public static final String CONFIG_AUTO_REMOVE_MACHINE_MILLIS = "sentinel.dashboard.autoRemoveMachineMillis";

    /**
     * 缓存配置项的映射表.
     */
    private static final ConcurrentMap<String, Object> CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取配置项的值.
     *
     * @param name 配置项名称
     * @return 配置项的值，如果未找到则返回空字符串
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
     * 获取配置项的字符串值，并缓存结果.
     *
     * @param name 配置项名称
     * @return 配置项的值，如果未找到或为空则返回null
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
     * 获取配置项的整数值，并缓存结果.
     *
     * @param name       配置项名称
     * @param defaultVal 默认值
     * @param minVal     最小值
     * @return 配置项的值，如果未找到或解析失败则返回默认值，且确保值不小于最小值
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
     * 获取登录用户名.
     *
     * @return 登录用户名
     */
    public static String getAuthUsername() {
        return getConfigStr(CONFIG_AUTH_USERNAME);
    }

    /**
     * 获取登录密码.
     *
     * @return 登录密码
     */
    public static String getAuthPassword() {
        return getConfigStr(CONFIG_AUTH_PASSWORD);
    }

    /**
     * 获取隐藏无健康机器的应用的时间（毫秒）.
     *
     * @return 时间（毫秒）
     */
    public static int getHideAppNoMachineMillis() {
        return getConfigInt(CONFIG_HIDE_APP_NO_MACHINE_MILLIS, 0, 60000);
    }

    /**
     * 获取移除无健康机器的应用的时间（毫秒）.
     *
     * @return 时间（毫秒）
     */
    public static int getRemoveAppNoMachineMillis() {
        return getConfigInt(CONFIG_REMOVE_APP_NO_MACHINE_MILLIS, 0, 120000);
    }

    /**
     * 获取自动移除不健康机器的时间（毫秒）.
     *
     * @return 时间（毫秒）
     */
    public static int getAutoRemoveMachineMillis() {
        return getConfigInt(CONFIG_AUTO_REMOVE_MACHINE_MILLIS, 0, 300000);
    }

    /**
     * 获取机器不健康的超时时间（毫秒）.
     *
     * @return 超时时间（毫秒）
     */
    public static int getUnhealthyMachineMillis() {
        return getConfigInt(CONFIG_UNHEALTHY_MACHINE_MILLIS, DEFAULT_MACHINE_HEALTHY_TIMEOUT_MS, 30000);
    }

    /**
     * 清除所有缓存的配置项.
     */
    @SuppressWarnings("unused")
    public static void clearCache() {
        CACHE_MAP.clear();
    }
}
