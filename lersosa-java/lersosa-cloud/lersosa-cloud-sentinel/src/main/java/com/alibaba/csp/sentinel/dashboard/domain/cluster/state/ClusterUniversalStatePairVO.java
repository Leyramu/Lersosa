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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 集群通用状态对
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClusterUniversalStatePairVO {

    /**
     * IP 地址
     */
    private String ip;

    /**
     * 命令端口
     */
    private Integer commandPort;

    /**
     * 状态信息
     */
    private ClusterUniversalStateVO state;

    /**
     * 设置IP地址
     *
     * @param ip 要设置的IP地址
     * @return 当前实例，支持链式调用
     */
    public ClusterUniversalStatePairVO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 设置命令端口
     *
     * @param commandPort 要设置的命令端口号
     * @return 当前实例，支持链式调用
     */
    public ClusterUniversalStatePairVO setCommandPort(Integer commandPort) {
        this.commandPort = commandPort;
        return this;
    }

    /**
     * 设置状态信息
     *
     * @param state 要设置的状态信息
     * @return 当前实例，支持链式调用
     */
    public ClusterUniversalStatePairVO setState(ClusterUniversalStateVO state) {
        this.state = state;
        return this;
    }

    /**
     * 重写toString方法，方便打印对象状态
     *
     * @return 对象的状态信息字符串
     */
    @Override
    public String toString() {
        return "ClusterUniversalStatePairVO{"
               + "ip='" + ip + '\''
               + ", commandPort=" + commandPort
               + ", state=" + state
               + '}';
    }
}
