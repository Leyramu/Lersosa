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

import java.util.Set;

/**
 * 集群应用程序分配结果
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterAppAssignResultVO {

    /**
     * 失败的服务器集合
     */
    private Set<String> failedServerSet;

    /**
     * 失败的客户端集合
     */
    private Set<String> failedClientSet;

    /**
     * 总数量
     */
    private Integer totalCount;

    /**
     * 设置失败的服务器集合
     *
     * @param failedServerSet 失败的服务器集合
     * @return 当前的 ClusterAppAssignResultVO 实例
     */
    public ClusterAppAssignResultVO setFailedServerSet(Set<String> failedServerSet) {
        this.failedServerSet = failedServerSet;
        return this;
    }

    /**
     * 设置失败的客户端集合
     *
     * @param failedClientSet 失败的客户端集合
     * @return 当前的 ClusterAppAssignResultVO 实例
     */
    public ClusterAppAssignResultVO setFailedClientSet(Set<String> failedClientSet) {
        this.failedClientSet = failedClientSet;
        return this;
    }

    /**
     * 设置总数量
     *
     * @param totalCount 总数量
     * @return 当前的 ClusterAppAssignResultVO 实例
     */
    public ClusterAppAssignResultVO setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    /**
     * 将当前对象转换为字符串表示
     *
     * @return 当前对象的字符串表示
     */
    @Override
    public String toString() {
        return "ClusterAppAssignResultVO{" +
               "failedServerSet=" + failedServerSet +
               ", failedClientSet=" + failedClientSet +
               ", totalCount=" + totalCount +
               '}';
    }
}
