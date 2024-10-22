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

package com.alibaba.csp.sentinel.dashboard.datasource.entity;

import lombok.Data;

import java.util.Date;

/**
 * 度量位置实体
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Data
public class MetricPositionEntity {

    /**
     * 主键
     */
    private long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 应用名称
     */
    private String app;

    /**
     * IP 地址
     */
    private String ip;

    /**
     * Sentinel在该应用上使用的端口
     */
    private int port;

    /**
     * 机器名，冗余字段
     */
    private String hostname;

    /**
     * 上一次拉取的最晚时间戳
     */
    private Date lastFetch;

    /**
     * 转换为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "MetricPositionEntity{" +
               "id=" + id +
               ", gmtCreate=" + gmtCreate +
               ", gmtModified=" + gmtModified +
               ", app='" + app + '\'' +
               ", ip='" + ip + '\'' +
               ", port=" + port +
               ", hostname='" + hostname + '\'' +
               ", lastFetch=" + lastFetch +
               '}';
    }
}
