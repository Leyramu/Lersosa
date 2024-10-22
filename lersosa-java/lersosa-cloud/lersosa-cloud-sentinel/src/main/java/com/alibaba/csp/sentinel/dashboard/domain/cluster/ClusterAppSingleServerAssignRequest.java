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

import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterAppAssignMap;
import lombok.Getter;

import java.util.Set;

/**
 * 集群应用单服务器分配请求
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterAppSingleServerAssignRequest {

    /**
     * 集群应用分配映射，表示分配给单个服务器的应用信息
     */
    private ClusterAppAssignMap clusterMap;

    /**
     * 剩余服务器列表，表示未分配的应用服务器
     */
    private Set<String> remainingList;

    /**
     * 设置集群应用分配映射的方法
     *
     * @param clusterMap 集群应用分配映射
     * @return 当前的ClusterAppSingleServerAssignRequest实例，以便进行链式调用
     */
    public ClusterAppSingleServerAssignRequest setClusterMap(ClusterAppAssignMap clusterMap) {
        this.clusterMap = clusterMap;
        return this;
    }

    /**
     * 设置剩余服务器列表的方法
     *
     * @param remainingList 服务器的集合，表示剩余可用的服务器
     * @return 当前的ClusterAppSingleServerAssignRequest实例，以便进行链式调用
     */
    public ClusterAppSingleServerAssignRequest setRemainingList(Set<String> remainingList) {
        this.remainingList = remainingList;
        return this;
    }

    /**
     * 重写toString方法，用于打印ClusterAppSingleServerAssignRequest对象的信息
     *
     * @return 包含clusterMap和remainingList信息的字符串
     */
    @Override
    public String toString() {
        return "ClusterAppSingleServerAssignRequest{" +
               "clusterMap=" + clusterMap +
               ", remainingList=" + remainingList +
               '}';
    }
}
