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

package com.alibaba.csp.sentinel.dashboard.domain.cluster;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * 群集组实体
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterGroupEntity {

    /**
     * 机器的唯一标识
     */
    private String machineId;

    /**
     * 机器的IP地址
     */
    private String ip;

    /**
     * 机器的端口号
     */
    private Integer port;

    /**
     * 与机器连接的客户端信息集合
     */
    private Set<String> clientSet = new HashSet<>();

    /**
     * 标识机器是否属于某个应用的布尔值
     */
    private Boolean belongToApp;

    /**
     * 设置机器唯一标识
     *
     * @param machineId 机器的唯一标识
     * @return 当前的ClusterGroupEntity实例，以便进行链式调用
     */
    public ClusterGroupEntity setMachineId(String machineId) {
        this.machineId = machineId;
        return this;
    }

    /**
     * 设置机器的IP地址
     *
     * @param ip 机器的IP地址
     * @return 当前的ClusterGroupEntity实例，以便进行链式调用
     */
    public ClusterGroupEntity setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 设置机器的端口号
     *
     * @param port 机器的端口号
     * @return 当前的ClusterGroupEntity实例，以便进行链式调用
     */
    public ClusterGroupEntity setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 设置客户端集合
     *
     * @param clientSet 与机器连接的客户端信息集合
     * @return 当前的ClusterGroupEntity实例，以便进行链式调用
     */
    public ClusterGroupEntity setClientSet(Set<String> clientSet) {
        this.clientSet = clientSet;
        return this;
    }

    /**
     * 设置机器是否属于某个应用
     *
     * @param belongToApp 标识机器是否属于某个应用的布尔值
     * @return 当前的ClusterGroupEntity实例，以便进行链式调用
     */
    public ClusterGroupEntity setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }

    /**
     * 重写toString方法，用于打印ClusterGroupEntity实例的信息
     *
     * @return 当前ClusterGroupEntity实例的字符串表示形式
     */
    @Override
    public String toString() {
        return "ClusterGroupEntity{" +
               "machineId='" + machineId + '\'' +
               ", ip='" + ip + '\'' +
               ", port=" + port +
               ", clientSet=" + clientSet +
               ", belongToApp=" + belongToApp +
               '}';
    }
}
