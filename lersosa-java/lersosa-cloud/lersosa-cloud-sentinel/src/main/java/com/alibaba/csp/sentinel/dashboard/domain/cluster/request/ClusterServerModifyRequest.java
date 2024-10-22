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

import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerTransportConfig;
import lombok.Getter;

import java.util.Set;

/**
 * 集群服务器修改请求实体
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterServerModifyRequest implements ClusterModifyRequest {

    /**
     * 应用名称
     */
    private String app;

    /**
     * 应用IP
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 集群模式
     */
    private Integer mode;

    /**
     * 流控配置
     */
    private ServerFlowConfig flowConfig;

    /**
     * 传输配置
     */
    private ServerTransportConfig transportConfig;

    /**
     * 命名空间集合
     */
    private Set<String> namespaceSet;

    /**
     * 获取当前请求中的app值
     *
     * @return 当前的app值
     */
    @Override
    public String getApp() {
        return app;
    }

    /**
     * 设置当前请求中的app值
     *
     * @param app 要设置的app值
     * @return 当前的ClusterServerModifyRequest对象，支持链式调用
     */
    public ClusterServerModifyRequest setApp(String app) {
        this.app = app;
        return this;
    }

    /**
     * 获取当前请求中的ip值
     *
     * @return 当前的ip值
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 设置当前请求中的ip值
     *
     * @param ip 要设置的ip值
     * @return 当前的ClusterServerModifyRequest对象，支持链式调用
     */
    public ClusterServerModifyRequest setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 获取当前请求中的port值
     *
     * @return 当前的port值
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * 设置当前请求中的port值
     *
     * @param port 要设置的port值
     * @return 当前的ClusterServerModifyRequest对象，支持链式调用
     */
    public ClusterServerModifyRequest setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 获取当前请求中的mode值
     *
     * @return 当前的mode值
     */
    @Override
    public Integer getMode() {
        return mode;
    }

    /**
     * 设置当前请求中的mode值
     *
     * @param mode 要设置的mode值
     * @return 当前的ClusterServerModifyRequest对象，支持链式调用
     */
    public ClusterServerModifyRequest setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 设置当前请求中的flowConfig值
     *
     * @param flowConfig 要设置的flowConfig值
     * @return 当前的ClusterServerModifyRequest对象，支持链式调用
     */
    public ClusterServerModifyRequest setFlowConfig(ServerFlowConfig flowConfig) {
        this.flowConfig = flowConfig;
        return this;
    }

    /**
     * 设置当前请求中的transportConfig值
     *
     * @param transportConfig 要设置的transportConfig值
     * @return 当前的ClusterServerModifyRequest对象，支持链式调用
     */
    public ClusterServerModifyRequest setTransportConfig(ServerTransportConfig transportConfig) {
        this.transportConfig = transportConfig;
        return this;
    }

    /**
     * 设置当前请求中的namespaceSet值
     *
     * @param namespaceSet 要设置的namespaceSet值
     * @return 当前的ClusterServerModifyRequest对象，支持链式调用
     */
    public ClusterServerModifyRequest setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }

    /**
     * 重写toString方法，用于打印ClusterServerModifyRequest对象的所有信息
     *
     * @return 包含所有属性值的字符串表示
     */
    @Override
    public String toString() {
        return "ClusterServerModifyRequest{"
               + "app='" + app + '\''
               + ", ip='" + ip + '\''
               + ", port=" + port
               + ", mode=" + mode
               + ", flowConfig=" + flowConfig
               + ", transportConfig=" + transportConfig
               + ", namespaceSet=" + namespaceSet
               + '}';
    }

}
