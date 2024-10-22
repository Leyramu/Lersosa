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

package com.alibaba.csp.sentinel.dashboard.datasource.entity;

import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import lombok.Data;

import java.util.Date;

/**
 * 机器实体
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Data
public class MachineEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 应用名称
     */
    private String app;

    /**
     * IP 地址
     */
    private String ip;

    /**
     * 主机名称
     */
    private String hostname;

    /**
     * 最后心跳时间
     */
    private Date timestamp;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 转换为 MachineInfo 对象
     *
     * @return MachineInfo 对象
     */
    public MachineInfo toMachineInfo() {
        MachineInfo machineInfo = new MachineInfo();

        machineInfo.setApp(app);
        machineInfo.setHostname(hostname);
        machineInfo.setIp(ip);
        machineInfo.setPort(port);
        machineInfo.setLastHeartbeat(timestamp.getTime());
        machineInfo.setHeartbeatVersion(timestamp.getTime());

        return machineInfo;
    }

    /**
     * 转换为 String
     *
     * @return String
     */
    @Override
    public String toString() {
        return "MachineEntity{" +
               "id=" + id +
               ", gmtCreate=" + gmtCreate +
               ", gmtModified=" + gmtModified +
               ", app='" + app + '\'' +
               ", ip='" + ip + '\'' +
               ", hostname='" + hostname + '\'' +
               ", timestamp=" + timestamp +
               ", port=" + port +
               '}';
    }
}
