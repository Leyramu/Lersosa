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

package com.alibaba.csp.sentinel.dashboard.domain.cluster.config;

import lombok.Getter;

/**
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterClientConfig {

    /**
     * 服务器主机
     */
    private String serverHost;

    /**
     * 服务器端口
     */
    private Integer serverPort;

    /**
     * 请求超时时间
     */
    private Integer requestTimeout;

    /**
     * 连接超时时间
     */
    private Integer connectTimeout;

    /**
     * 设置服务器主机
     *
     * @param serverHost 服务器主机
     * @return this
     */
    public ClusterClientConfig setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }

    /**
     * 设置服务器端口
     *
     * @param serverPort 服务器端口
     * @return this
     */
    public ClusterClientConfig setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    /**
     * 设置请求超时时间
     *
     * @param requestTimeout 请求超时时间
     * @return this
     */
    public ClusterClientConfig setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    /**
     * 设置连接超时时间
     *
     * @param connectTimeout 连接超时时间
     * @return this
     */
    public ClusterClientConfig setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 获取主机和端口的组合字符串
     *
     * @return 主机和端口的组合字符串
     */
    @Override
    public String toString() {
        return "ClusterClientConfig{" +
               "serverHost='" + serverHost + '\'' +
               ", serverPort=" + serverPort +
               ", requestTimeout=" + requestTimeout +
               ", connectTimeout=" + connectTimeout +
               '}';
    }
}
