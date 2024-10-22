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

import java.util.List;

/**
 * 集群组连接信息
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ConnectionGroupVO {

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 连接描述符集合
     */
    private List<ConnectionDescriptorVO> connectionSet;

    /**
     * 已连接数量
     */
    private Integer connectedCount;

    /**
     * 设置命名空间方法
     *
     * @param namespace 命名空间字符串
     * @return 当前对象实例，支持链式调用
     */
    public ConnectionGroupVO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * 设置连接集合方法
     *
     * @param connectionSet 连接描述符列表
     * @return 当前对象实例，支持链式调用
     */
    public ConnectionGroupVO setConnectionSet(List<ConnectionDescriptorVO> connectionSet) {
        this.connectionSet = connectionSet;
        return this;
    }

    /**
     * 设置已连接数量方法
     *
     * @param connectedCount 已连接数量
     * @return 当前对象实例，支持链式调用
     */
    public ConnectionGroupVO setConnectedCount(Integer connectedCount) {
        this.connectedCount = connectedCount;
        return this;
    }

    /**
     * 重写toString方法，用于打印对象信息
     *
     * @return 对象信息的字符串表示
     */
    @Override
    public String toString() {
        return "ConnectionGroupVO{" +
               "namespace='" + namespace + '\'' +
               ", connectionSet=" + connectionSet +
               ", connectedCount=" + connectedCount +
               '}';
    }
}
