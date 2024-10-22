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
 * 集群请求限制信息
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterRequestLimitVO {

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 当前QPS
     */
    private Double currentQps;

    /**
     * 最大允许QPS
     */
    private Double maxAllowedQps;

    /**
     * 设置命名空间
     *
     * @param namespace 命名空间字符串，用于区分不同的服务或分组
     * @return 当前实例，方便链式调用
     */
    public ClusterRequestLimitVO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * 设置当前QPS
     *
     * @param currentQps 当前QPS值，表示正在处理的请求数量
     * @return 当前实例，方便链式调用
     */
    public ClusterRequestLimitVO setCurrentQps(Double currentQps) {
        this.currentQps = currentQps;
        return this;
    }

    /**
     * 设置允许的最大QPS
     *
     * @param maxAllowedQps 最大QPS值，用于限制请求速率
     * @return 当前实例，方便链式调用
     */
    public ClusterRequestLimitVO setMaxAllowedQps(Double maxAllowedQps) {
        this.maxAllowedQps = maxAllowedQps;
        return this;
    }

    /**
     * 重写toString方法，方便打印和查看集群请求限制的详细信息
     *
     * @return 包含命名空间、当前QPS和允许的最大QPS信息的字符串表示
     */
    @Override
    public String toString() {
        return "ClusterRequestLimitVO{" +
               "namespace='" + namespace + '\'' +
               ", currentQps=" + currentQps +
               ", maxAllowedQps=" + maxAllowedQps +
               '}';
    }
}
