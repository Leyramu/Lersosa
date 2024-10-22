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
 * 群集状态简单实体
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterStateSimpleEntity {

    /**
     * 集群模式
     */
    private Integer mode;

    /**
     * 上次修改时间戳
     */
    private Long lastModified;

    /**
     * 客户端可用性
     */
    private Boolean clientAvailable;

    /**
     * 服务端可用性
     */
    private Boolean serverAvailable;

    /**
     * 设置集群模式
     *
     * @param mode 集群的模式
     * @return 当前的集群状态实体对象，支持链式调用
     */
    public ClusterStateSimpleEntity setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 设置上次修改时间
     *
     * @param lastModified 上次修改的时间戳
     * @return 当前的集群状态实体对象，支持链式调用
     */
    public ClusterStateSimpleEntity setLastModified(Long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    /**
     * 设置客户端可用性
     *
     * @param clientAvailable 客户端是否可用
     * @return 当前的集群状态实体对象，支持链式调用
     */
    public ClusterStateSimpleEntity setClientAvailable(Boolean clientAvailable) {
        this.clientAvailable = clientAvailable;
        return this;
    }

    /**
     * 设置服务器端可用性
     *
     * @param serverAvailable 服务器是否可用
     * @return 当前的集群状态实体对象，支持链式调用
     */
    public ClusterStateSimpleEntity setServerAvailable(Boolean serverAvailable) {
        this.serverAvailable = serverAvailable;
        return this;
    }

    /**
     * 重写toString方法，方便打印和展示集群状态实体的信息
     *
     * @return 包含集群状态信息的字符串
     */
    @Override
    public String toString() {
        return "ClusterStateSimpleEntity{"
               + "mode=" + mode
               + ", lastModified=" + lastModified
               + ", clientAvailable=" + clientAvailable
               + ", serverAvailable=" + serverAvailable
               + '}';
    }
}
