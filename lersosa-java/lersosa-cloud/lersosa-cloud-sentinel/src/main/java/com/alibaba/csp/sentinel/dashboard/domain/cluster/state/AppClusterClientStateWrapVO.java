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
 * 群集客户端状态包装器
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class AppClusterClientStateWrapVO {

    /**
     * 客户端的唯一标识符
     */
    private String id;

    /**
     * 客户端的命令端口
     */
    private Integer commandPort;

    /**
     * 客户端的 IP 地址
     */
    private String ip;

    /**
     * 客户端的状态信息
     */
    private ClusterClientStateVO state;

    /**
     * 设置客户端的 ID。
     *
     * @param id 客户端的 ID 设置
     * @return AppClusterClientStateWrapVO 的当前实例进行链式调用
     */
    public AppClusterClientStateWrapVO setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 设置客户端的 IP 地址
     *
     * @param ip 客户端的 IP 地址进行设置
     * @return AppClusterClientStateWrapVO 的当前实例进行链式调用
     */
    public AppClusterClientStateWrapVO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 设置客户端的状态
     *
     * @param state 要设置的客户端状态信息
     * @return 用于链调用的 AppClusterClientStateWrapVO 的当前实例
     */
    public AppClusterClientStateWrapVO setState(ClusterClientStateVO state) {
        this.state = state;
        return this;
    }

    /**
     * 设置客户端的命令端口
     *
     * @param commandPort 客户端的命令端口设置
     * @return AppClusterClientStateWrapVO 的当前实例进行链式调用
     */
    public AppClusterClientStateWrapVO setCommandPort(Integer commandPort) {
        this.commandPort = commandPort;
        return this;
    }

    /**
     * 重写 toString 方法，以便于调试和记录客户端的状态信息
     *
     * @return Client 端状态信息的字符串表示形式
     */
    @Override
    public String toString() {
        return "AppClusterClientStateWrapVO{" +
               "id='" + id + '\'' +
               ", commandPort=" + commandPort +
               ", ip='" + ip + '\'' +
               ", state=" + state +
               '}';
    }
}
