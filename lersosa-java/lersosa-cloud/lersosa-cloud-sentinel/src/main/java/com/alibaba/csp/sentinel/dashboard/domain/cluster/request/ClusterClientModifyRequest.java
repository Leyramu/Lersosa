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

package com.alibaba.csp.sentinel.dashboard.domain.cluster.request;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ClusterClientConfig;
import lombok.Getter;

/**
 * 集群配置修改请求
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterClientModifyRequest implements ClusterModifyRequest {

    /**
     * 应用名称
     */
    private String app;

    /**
     * 应用IP
     */
    private String ip;

    /**
     * 应用端口
     */
    private Integer port;

    /**
     * 集群模式
     */
    private Integer mode;

    /**
     * 客户端配置
     */
    private ClusterClientConfig clientConfig;

    /**
     * 获取应用名称
     *
     * @return 应用名称
     */
    @Override
    public String getApp() {
        return app;
    }

    /**
     * 设置应用名称
     *
     * @param app 应用名称
     * @return 当前对象
     */
    public ClusterClientModifyRequest setApp(String app) {
        this.app = app;
        return this;
    }

    /**
     * 获取应用IP
     *
     * @return 应用IP
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 设置应用IP
     *
     * @param ip 应用IP
     * @return 当前对象
     */
    public ClusterClientModifyRequest setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 获取应用端口
     *
     * @return 应用端口
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * 设置应用端口
     *
     * @param port 应用端口
     * @return 当前对象
     */
    public ClusterClientModifyRequest setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 获取集群模式
     *
     * @return 集群模式
     */
    @Override
    public Integer getMode() {
        return mode;
    }

    /**
     * 设置集群模式
     *
     * @param mode 集群模式
     * @return 当前对象
     */
    public ClusterClientModifyRequest setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 设置客户端配置
     *
     * @return 客户端配置
     */
    public ClusterClientModifyRequest setClientConfig(
            ClusterClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        return this;
    }
}
