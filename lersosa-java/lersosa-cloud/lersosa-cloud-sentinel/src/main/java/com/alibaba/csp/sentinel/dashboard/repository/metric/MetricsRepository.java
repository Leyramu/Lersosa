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

package com.alibaba.csp.sentinel.dashboard.repository.metric;

import java.util.List;

/**
 * 用于聚合指标数据的存储库界面
 *
 * @param <T> 指标类型
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
public interface MetricsRepository<T> {

    /**
     * 将指标保存到存储库
     *
     * @param metric 要保存的指标数据
     */
    void save(T metric);

    /**
     * 将所有指标保存到存储库
     *
     * @param metrics 要保存的指标
     */
    void saveAll(Iterable<T> metrics);

    /**
     * 通过 {@code appName} 和 {@code resourceName} 获取一段时间内的所有指标。
     *
     * @param app       Sentinel 的应用程序名称
     * @param resource  资源名称
     * @param startTime 开始时间戳
     * @param endTime   结束时间戳
     * @return 查询条件中的所有指标
     */
    List<T> queryByAppAndResourceBetween(String app, String resource, long startTime, long endTime);

    /**
     * 列出提供的应用程序名称的资源名称
     *
     * @param app 应用程序名称
     * @return 资源列表
     */
    List<String> listResourcesOfApp(String app);
}
