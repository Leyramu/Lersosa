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

/**
 * 集群客户端修改请求
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
public interface ClusterModifyRequest {

    /**
     * 获取应用名称
     *
     * @return 应用名称
     */
    String getApp();

    /**
     * 获取IP地址
     *
     * @return IP地址
     */
    String getIp();

    /**
     * 获取端口号
     *
     * @return 端口号
     */
    Integer getPort();

    /**
     * 获取客户端模式
     *
     * @return 客户端模式
     */
    Integer getMode();
}
