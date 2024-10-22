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

/**
 * 集群客户端信息视图对象
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterClientInfoVO {

    /**
     * 服务器主机地址
     */
    private String serverHost;

    /**
     * 服务器端口
     */
    private Integer serverPort;

    /**
     * 客户端状态
     */
    private Integer clientState;

    /**
     * 请求超时时间
     */
    private Integer requestTimeout;

    /**
     * 设置服务器主机地址
     *
     * @param serverHost 服务器主机地址
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterClientInfoVO setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }

    /**
     * 设置服务器端口
     *
     * @param serverPort 服务器端口
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterClientInfoVO setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    /**
     * 设置客户端状态
     *
     * @param clientState 客户端状态
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterClientInfoVO setClientState(Integer clientState) {
        this.clientState = clientState;
        return this;
    }

    /**
     * 设置请求超时时间
     *
     * @param requestTimeout 请求超时时间
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterClientInfoVO setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    /**
     * 重写toString方法，用于打印对象信息
     *
     * @return 对象信息的字符串表示
     */
    @Override
    public String toString() {
        return "ClusterClientInfoVO{" +
               "serverHost='" + serverHost + '\'' +
               ", serverPort=" + serverPort +
               ", clientState=" + clientState +
               ", requestTimeout=" + requestTimeout +
               '}';
    }
}
