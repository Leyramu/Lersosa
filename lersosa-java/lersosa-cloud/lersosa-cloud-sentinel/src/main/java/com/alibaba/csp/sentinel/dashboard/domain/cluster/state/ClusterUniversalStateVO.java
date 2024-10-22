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
 * 群集通用状态视图对象
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterUniversalStateVO {

    /**
     * 集群状态信息
     */
    private ClusterStateSimpleEntity stateInfo;

    /**
     * Client 端状态信息
     */
    private ClusterClientStateVO client;

    /**
     * Server 端状态信息
     */
    private ClusterServerStateVO server;

    /**
     * 设置Client端状态信息
     *
     * @param client Client端的状态信息
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterUniversalStateVO setClient(ClusterClientStateVO client) {
        this.client = client;
        return this;
    }

    /**
     * 设置Server端状态信息
     *
     * @param server Server端的状态信息
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterUniversalStateVO setServer(ClusterServerStateVO server) {
        this.server = server;
        return this;
    }

    /**
     * 设置集群状态信息
     *
     * @param stateInfo 集群的基本状态信息
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterUniversalStateVO setStateInfo(
            ClusterStateSimpleEntity stateInfo) {
        this.stateInfo = stateInfo;
        return this;
    }

    /**
     * 将当前对象转换为字符串表示形式
     *
     * @return 当前对象的字符串表示形式，包括状态信息、Client端状态信息、Server端状态信息
     */
    @Override
    public String toString() {
        return "ClusterUniversalStateVO{" +
               "stateInfo=" + stateInfo +
               ", client=" + client +
               ", server=" + server +
               '}';
    }
}
