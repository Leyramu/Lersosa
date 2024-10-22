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
