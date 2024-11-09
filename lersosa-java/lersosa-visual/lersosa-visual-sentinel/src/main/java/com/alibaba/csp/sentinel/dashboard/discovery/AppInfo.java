/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.discovery;

import com.alibaba.csp.sentinel.dashboard.config.DashboardConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AppInfo {

    private String app = "";

    private Integer appType = 0;

    private Set<MachineInfo> machines = ConcurrentHashMap.newKeySet();

    public AppInfo() {
    }

    public AppInfo(String app) {
        this.app = app;
    }

    public AppInfo(String app, Integer appType) {
        this.app = app;
        this.appType = appType;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    /**
     * Get the current machines.
     *
     * @return a new copy of the current machines.
     */
    public Set<MachineInfo> getMachines() {
        return new HashSet<>(machines);
    }

    @Override
    public String toString() {
        return "AppInfo{" + "app='" + app + ", machines=" + machines + '}';
    }

    public boolean addMachine(MachineInfo machineInfo) {
        machines.remove(machineInfo);
        return machines.add(machineInfo);
    }

    public synchronized boolean removeMachine(String ip, int port) {
        Iterator<MachineInfo> it = machines.iterator();
        while (it.hasNext()) {
            MachineInfo machine = it.next();
            if (machine.getIp().equals(ip) && machine.getPort() == port) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public Optional<MachineInfo> getMachine(String ip, int port) {
        return machines.stream()
            .filter(e -> e.getIp().equals(ip) && e.getPort().equals(port))
            .findFirst();
    }

    public Optional<MachineInfo> getMachine(String ip) {
        return machines.stream()
            .filter(e -> e.getIp().equals(ip))
            .findFirst();
    }

    private boolean heartbeatJudge(final int threshold) {
        if (machines.size() == 0) {
            return false;
        }
        if (threshold > 0) {
            long healthyCount = machines.stream()
                .filter(MachineInfo::isHealthy)
                .count();
            if (healthyCount == 0) {
                // No healthy machines.
                return machines.stream()
                    .max(Comparator.comparingLong(MachineInfo::getLastHeartbeat))
                    .map(e -> System.currentTimeMillis() - e.getLastHeartbeat() < threshold)
                    .orElse(false);
            }
        }
        return true;
    }

    /**
     * Check whether current application has no healthy machines and should not be displayed.
     *
     * @return true if the application should be displayed in the sidebar, otherwise false
     */
    public boolean isShown() {
        return heartbeatJudge(DashboardConfig.getHideAppNoMachineMillis());
    }

    /**
     * Check whether current application has no healthy machines and should be removed.
     *
     * @return true if the application is dead and should be removed, otherwise false
     */
    public boolean isDead() {
        return !heartbeatJudge(DashboardConfig.getRemoveAppNoMachineMillis());
    }
}
