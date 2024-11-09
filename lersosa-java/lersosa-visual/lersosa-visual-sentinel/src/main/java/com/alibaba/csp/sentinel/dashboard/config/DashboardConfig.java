/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.lang.NonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>Dashboard local config support.</p>
 * <p>
 * Dashboard supports configuration loading by several ways by order:<br>
 * 1. System.properties<br>
 * 2. Env
 * </p>
 *
 * @author jason
 * @since 1.5.0
 */
public class DashboardConfig {

    public static final int DEFAULT_MACHINE_HEALTHY_TIMEOUT_MS = 60_000;

    /**
     * Login username
     */
    public static final String CONFIG_AUTH_USERNAME = "sentinel.dashboard.auth.username";

    /**
     * Login password
     */
    public static final String CONFIG_AUTH_PASSWORD = "sentinel.dashboard.auth.password";

    /**
     * Hide application name in sidebar when it has no healthy machines after specific period in millisecond.
     */
    public static final String CONFIG_HIDE_APP_NO_MACHINE_MILLIS = "sentinel.dashboard.app.hideAppNoMachineMillis";
    /**
     * Remove application when it has no healthy machines after specific period in millisecond.
     */
    public static final String CONFIG_REMOVE_APP_NO_MACHINE_MILLIS = "sentinel.dashboard.removeAppNoMachineMillis";
    /**
     * Timeout
     */
    public static final String CONFIG_UNHEALTHY_MACHINE_MILLIS = "sentinel.dashboard.unhealthyMachineMillis";
    /**
     * Auto remove unhealthy machine after specific period in millisecond.
     */
    public static final String CONFIG_AUTO_REMOVE_MACHINE_MILLIS = "sentinel.dashboard.autoRemoveMachineMillis";

    private static final ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<>();

    @NonNull
    private static String getConfig(String name) {
        // env
        String val = System.getenv(name);
        if (StringUtils.isNotEmpty(val)) {
            return val;
        }
        // properties
        val = System.getProperty(name);
        if (StringUtils.isNotEmpty(val)) {
            return val;
        }
        return "";
    }

    protected static String getConfigStr(String name) {
        if (cacheMap.containsKey(name)) {
            return (String) cacheMap.get(name);
        }

        String val = getConfig(name);

        if (StringUtils.isBlank(val)) {
            return null;
        }

        cacheMap.put(name, val);
        return val;
    }

    protected static int getConfigInt(String name, int defaultVal, int minVal) {
        if (cacheMap.containsKey(name)) {
            return (int) cacheMap.get(name);
        }
        int val = NumberUtils.toInt(getConfig(name));
        if (val == 0) {
            val = defaultVal;
        } else if (val < minVal) {
            val = minVal;
        }
        cacheMap.put(name, val);
        return val;
    }

    public static String getAuthUsername() {
        return getConfigStr(CONFIG_AUTH_USERNAME);
    }

    public static String getAuthPassword() {
        return getConfigStr(CONFIG_AUTH_PASSWORD);
    }

    public static int getHideAppNoMachineMillis() {
        return getConfigInt(CONFIG_HIDE_APP_NO_MACHINE_MILLIS, 0, 60000);
    }

    public static int getRemoveAppNoMachineMillis() {
        return getConfigInt(CONFIG_REMOVE_APP_NO_MACHINE_MILLIS, 0, 120000);
    }

    public static int getAutoRemoveMachineMillis() {
        return getConfigInt(CONFIG_AUTO_REMOVE_MACHINE_MILLIS, 0, 300000);
    }

    public static int getUnhealthyMachineMillis() {
        return getConfigInt(CONFIG_UNHEALTHY_MACHINE_MILLIS, DEFAULT_MACHINE_HEALTHY_TIMEOUT_MS, 30000);
    }

    public static void clearCache() {
        cacheMap.clear();
    }
}
