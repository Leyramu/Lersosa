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

package com.alibaba.csp.sentinel.dashboard.domain.cluster.state;

import lombok.Getter;

/**
 * 集群服务器状态包装器
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class AppClusterServerStateWrapVO {

    /**
     * 服务器的唯一标识符
     */
    private String id;

    /**
     * 服务器 IP 地址
     */
    private String ip;

    /**
     * 服务器端口号
     */
    private Integer port;

    /**
     * 与服务器的连接数
     */
    private Integer connectedCount;

    /**
     * 是否属于应用
     */
    private Boolean belongToApp;

    /**
     * 服务器状态信息
     */
    private ClusterServerStateVO state;

    /**
     * 设置服务器的身份 ID
     *
     * @param id 服务器的身份 ID
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 设置服务器的 IP 地址
     *
     * @param ip 服务器的 IP 地址
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 设置服务器的端口号
     *
     * @param port 服务器的端口号
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 设置服务器是否属于应用程序
     *
     * @param belongToApp 表示服务器是否属于该 App
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }

    /**
     * 设置与服务器的连接数
     *
     * @param connectedCount 与服务器的连接数
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setConnectedCount(Integer connectedCount) {
        this.connectedCount = connectedCount;
        return this;
    }

    /**
     * 设置服务器的状态信息
     *
     * @param state 服务器的状态信息
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setState(ClusterServerStateVO state) {
        this.state = state;
        return this;
    }

    /**
     * 重写 toString 方法，以字符串格式显示 AppClusterServerStateWrapVO 对象的信息
     *
     * @return 对象信息的字符串表示形式
     */
    @Override
    public String toString() {
        return "AppClusterServerStateWrapVO{" +
               "id='" + id + '\'' +
               ", ip='" + ip + '\'' +
               ", port='" + port + '\'' +
               ", belongToApp=" + belongToApp +
               ", state=" + state +
               '}';
    }
}
