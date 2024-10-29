/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.vo;

import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算机信息视图对象
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Data
public class MachineInfoVo {

    /**
     * 应用名称
     */
    private String app;

    /**
     * 主机名称
     */
    private String hostname;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 端口号
     */
    private int port;

    /**
     * 心跳版本号
     */
    private long heartbeatVersion;

    /**
     * 最后一次心跳时间
     */
    private long lastHeartbeat;

    /**
     * 是否健康
     */
    private boolean healthy;

    /**
     * 版本号
     */
    private String version;

    /**
     * 从机器信息列表中创建机器信息视图对象列表
     *
     * @param machines 机器信息列表
     * @return 机器信息视图对象列表
     */
    public static List<MachineInfoVo> fromMachineInfoList(List<MachineInfo> machines) {
        List<MachineInfoVo> list = new ArrayList<>();
        for (MachineInfo machine : machines) {
            list.add(fromMachineInfo(machine));
        }
        return list;
    }

    /**
     * 从机器信息中创建机器信息视图对象
     *
     * @param machine 机器信息
     * @return 机器信息视图对象
     */
    public static MachineInfoVo fromMachineInfo(MachineInfo machine) {
        MachineInfoVo vo = new MachineInfoVo();
        vo.setApp(machine.getApp());
        vo.setHostname(machine.getHostname());
        vo.setIp(machine.getIp());
        vo.setPort(machine.getPort());
        vo.setLastHeartbeat(machine.getLastHeartbeat());
        vo.setHeartbeatVersion(machine.getHeartbeatVersion());
        vo.setVersion(machine.getVersion());
        vo.setHealthy(machine.isHealthy());
        return vo;
    }
}
