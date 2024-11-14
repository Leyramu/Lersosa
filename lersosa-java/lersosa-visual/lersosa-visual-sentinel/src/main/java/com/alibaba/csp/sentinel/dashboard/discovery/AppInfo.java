/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.discovery;

import com.alibaba.csp.sentinel.dashboard.config.DashboardConfig;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用信息.
 *
 * @author Sentinel
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class AppInfo {

    /**
     * 应用程序名称.
     */
    private final String app;

    /**
     * 应用程序类型.
     */
    private Integer appType = 0;

    /**
     * 存储应用程序中的所有机器.
     */
    private final Set<MachineInfo> machines = ConcurrentHashMap.newKeySet();

    /**
     * 创建一个具有指定应用程序名称的新 AppInfo 对象.
     *
     * @param app 应用程序名称
     */
    @SuppressWarnings("unused")
    public AppInfo(String app) {
        this.app = app;
    }

    /**
     * 创建一个具有指定应用程序名称和应用程序类型（0 或 1）的新 AppInfo 对象.
     *
     * @param app     应用程序名称
     * @param appType 应用程序类型，0 或 1
     */
    public AppInfo(String app, Integer appType) {
        this.app = app;
        this.appType = appType;
    }

    /**
     * 获取当前计算机.
     *
     * @return 当前计算机的新副本.
     */
    public Set<MachineInfo> getMachines() {
        return new HashSet<>(machines);
    }

    /**
     * 返回对象的字符串表示形式.
     *
     * @return 对象的字符串表示形式.
     */
    @Override
    public String toString() {
        return "AppInfo{" + "app='" + app + ", machines=" + machines + '}';
    }

    /**
     * 添加一个新机器.
     *
     * @param machineInfo 要添加的新机器信息
     */
    public void addMachine(MachineInfo machineInfo) {
        machines.remove(machineInfo);
        machines.add(machineInfo);
    }

    /**
     * 删除具有指定 IP 和端口号的计算机.
     *
     * @param ip   IP地址
     * @param port 端口号
     * @return 如果成功删除则返回 true，否则返回 false
     */
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

    /**
     * 获取具有指定 IP 和端口号的计算机.
     *
     * @param ip   IP地址
     * @param port 端口号
     * @return 具有指定 IP 和端口号的计算机，如果未找到则为空
     */
    public Optional<MachineInfo> getMachine(String ip, int port) {
        return machines.stream()
            .filter(e -> e.getIp().equals(ip) && e.getPort().equals(port))
            .findFirst();
    }

    /**
     * 获取具有指定 IP 的计算机.
     *
     * @param ip IP地址
     * @return 具有指定 IP 的计算机，如果未找到则为空
     */
    public Optional<MachineInfo> getMachine(String ip) {
        return machines.stream()
            .filter(e -> e.getIp().equals(ip))
            .findFirst();
    }

    /**
     * 心跳判断.
     *
     * @param threshold 心跳时间阈值
     * @return 心跳是否正常
     */
    private boolean heartbeatJudge(final int threshold) {
        if (machines.isEmpty()) {
            return false;
        }
        if (threshold > 0) {
            long healthyCount = machines.stream()
                .filter(MachineInfo::isHealthy)
                .count();
            if (healthyCount == 0) {
                return machines.stream()
                    .max(Comparator.comparingLong(MachineInfo::getLastHeartbeat))
                    .map(e -> System.currentTimeMillis() - e.getLastHeartbeat() < threshold)
                    .orElse(false);
            }
        }
        return true;
    }

    /**
     * 检查当前应用程序是否没有健康的机器，不应显示.
     *
     * @return 如果应用程序应显示在侧边栏中，则为 true，否则为 false
     */
    @SuppressWarnings("unused")
    public boolean isShown() {
        return heartbeatJudge(DashboardConfig.getHideAppNoMachineMillis());
    }

    /**
     * 检查当前应用程序是否没有健康的机器，是否应该删除.
     *
     * @return 如果应用程序已死亡且应删除，则为 true，否则为 false
     */
    public boolean isDead() {
        return !heartbeatJudge(DashboardConfig.getRemoveAppNoMachineMillis());
    }
}
