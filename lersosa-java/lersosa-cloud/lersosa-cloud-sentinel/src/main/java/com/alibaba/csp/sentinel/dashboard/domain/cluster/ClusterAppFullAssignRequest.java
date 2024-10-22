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

import java.util.List;
import java.util.Set;

/**
 * 集群客户端的完全分配请求
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterAppFullAssignRequest {

    /**
     * 集群应用分配映射列表
     */
    private List<ClusterAppAssignMap> clusterMap;

    /**
     * 剩余项集合
     */
    private Set<String> remainingList;

    /**
     * 设置集群应用分配映射列表
     *
     * @param clusterMap 集群应用分配映射列表
     * @return 当前实例，方便链式调用
     */
    public ClusterAppFullAssignRequest setClusterMap(List<ClusterAppAssignMap> clusterMap) {
        this.clusterMap = clusterMap;
        return this;
    }

    /**
     * 设置剩余项集合
     *
     * @param remainingList 剩余项的集合
     * @return 当前实例，方便链式调用
     */
    public ClusterAppFullAssignRequest setRemainingList(Set<String> remainingList) {
        this.remainingList = remainingList;
        return this;
    }

    /**
     * 获取当前实例的字符串表示形式。
     *
     * @return 当前实例的字符串表示形式
     */
    @Override
    public String toString() {
        return "ClusterAppFullAssignRequest{" +
               "clusterMap=" + clusterMap +
               ", remainingList=" + remainingList +
               '}';
    }
}
