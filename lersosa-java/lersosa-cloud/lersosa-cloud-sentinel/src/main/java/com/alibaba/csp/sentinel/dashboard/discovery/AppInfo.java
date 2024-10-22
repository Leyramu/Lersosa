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

package com.alibaba.csp.sentinel.dashboard.discovery;

import com.alibaba.csp.sentinel.dashboard.config.DashboardConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sentinel
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppInfo {

    /**
     * 应用名称
     */
    private String app = "";

    /**
     * 应用类型
     */
    private Integer appType = 0;

    /**
     * 应用机器信息
     */
    private final Set<MachineInfo> machines = ConcurrentHashMap.newKeySet();

    /**
     * 构造函数
     *
     * @param app 应用名称
     */
    public AppInfo(String app) {
        this.app = app;
    }

    /**
     * 获取当前计算机
     *
     * @return 当前计算机的新副本
     */
    public Set<MachineInfo> getMachines() {
        return new HashSet<>(machines);
    }

    /**
     * 获取当前计算机
     *
     * @return 当前计算机
     */
    @Override
    public String toString() {
        return "AppInfo{" + "app='" + app + ", machines=" + machines + '}';
    }

    /**
     * 添加机器
     *
     * @param machineInfo 机器信息
     */
    public void addMachine(MachineInfo machineInfo) {
        machines.remove(machineInfo);
        machines.add(machineInfo);
    }

    /**
     * 删除机器
     *
     * @param ip   ip地址
     * @param port 端口号
     * @return 是否删除成功
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
     * 获取机器
     *
     * @param ip   ip地址
     * @param port 端口号
     * @return 机器信息
     */
    public Optional<MachineInfo> getMachine(String ip, int port) {
        return machines.stream()
                .filter(e -> e.getIp().equals(ip) && e.getPort().equals(port))
                .findFirst();
    }

    /**
     * 获取机器
     *
     * @param ip ip地址
     * @return 机器信息
     */
    public Optional<MachineInfo> getMachine(String ip) {
        return machines.stream()
                .filter(e -> e.getIp().equals(ip))
                .findFirst();
    }

    /**
     * 心跳判断
     *
     * @param threshold 心跳间隔
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
     * 检查当前应用程序是否没有健康的机器，不应显示在侧边栏中
     *
     * @return 如果应用程序应显示在侧边栏中，则为 true，否则为 false
     */
    public boolean isShown() {
        return heartbeatJudge(DashboardConfig.getHideAppNoMachineMillis());
    }

    /**
     * 检查当前应用程序是否没有健康的机器，是否应该删除
     *
     * @return 如果应用程序已死亡且应删除，则为 true，否则为 false
     */
    public boolean isDead() {
        return !heartbeatJudge(DashboardConfig.getRemoveAppNoMachineMillis());
    }
}
