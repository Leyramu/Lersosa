/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.discovery;

import com.alibaba.csp.sentinel.dashboard.config.DashboardConfig;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.Data;
import lombok.NonNull;

import java.util.Objects;

/**
 * @author Sentinel
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Data
public class MachineInfo implements Comparable<MachineInfo> {

    /**
     * 应用名称
     */
    private String app = "";

    /**
     * 应用类型
     */
    private Integer appType = 0;

    /**
     * 主机名
     */
    private String hostname = "";

    /**
     * IP 地址
     */
    private String ip = "";

    /**
     * 端口号
     */
    private Integer port = -1;

    /**
     * 上次心跳时间戳
     */
    private long lastHeartbeat;

    /**
     * 心跳版本号
     */
    private long heartbeatVersion;

    /**
     * 指示 Sentinel 客户端的版本（自 0.2.0 起）
     */
    private String version;

    /**
     * 创建 MachineInfo 对象
     *
     * @param app  应用名称
     * @param ip   IP 地址
     * @param port 端口号
     * @return MachineInfo 对象
     */
    public static MachineInfo of(String app, String ip, Integer port) {
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setApp(app);
        machineInfo.setIp(ip);
        machineInfo.setPort(port);
        return machineInfo;
    }

    /**
     * 获取主机名和端口号的组合字符串
     *
     * @return 主机和端口号的组合字符串
     */
    public String toHostPort() {
        return ip + ":" + port;
    }

    /**
     * 判断是否健康
     *
     * @return 是否健康
     */
    public boolean isHealthy() {
        long delta = System.currentTimeMillis() - lastHeartbeat;
        return delta < DashboardConfig.getUnhealthyMachineMillis();
    }

    /**
     * 是否应移除 dead
     *
     * @return 是否应移除
     */
    public boolean isDead() {
        if (DashboardConfig.getAutoRemoveMachineMillis() > 0) {
            long delta = System.currentTimeMillis() - lastHeartbeat;
            return delta > DashboardConfig.getAutoRemoveMachineMillis();
        }
        return false;
    }

    /**
     * 比较两个 MachineInfo 对象
     *
     * @param o 比较的对象
     * @return 比较结果
     */
    @Override
    public int compareTo(@NonNull MachineInfo o) {
        if (this == o) {
            return 0;
        }
        if (!port.equals(o.getPort())) {
            return port.compareTo(o.getPort());
        }
        if (!StringUtil.equals(app, o.getApp())) {
            return app.compareToIgnoreCase(o.getApp());
        }
        return ip.compareToIgnoreCase(o.getIp());
    }

    /**
     * 转换为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "MachineInfo {" +
                "app='" + app + '\'' +
                ",appType='" + appType + '\'' +
                ", hostname='" + hostname + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", heartbeatVersion=" + heartbeatVersion +
                ", lastHeartbeat=" + lastHeartbeat +
                ", version='" + version + '\'' +
                ", healthy=" + isHealthy() +
                '}';
    }

    /**
     * 判断两个 MachineInfo 对象是否相等
     *
     * @param o 比较的对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MachineInfo that)) {
            return false;
        }
        return Objects.equals(app, that.app) &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port);
    }

    /**
     * 计算对象的哈希值
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hash(app, ip, port);
    }

    /**
     * 日志信息
     *
     * @return 日志字符串
     */
    public String toLogString() {
        return app + "|" + ip + "|" + port + "|" + version;
    }
}
