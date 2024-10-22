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

package com.alibaba.csp.sentinel.dashboard.datasource.entity.rule;

import com.alibaba.csp.sentinel.slots.block.Rule;

import java.util.Date;

/**
 * 规则实体接口
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
public interface RuleEntity {

    /**
     * 获取主键ID
     *
     * @return 主键ID
     */
    Long getId();

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    void setId(Long id);

    /**
     * 获取应用名称
     *
     * @return 应用名称
     */
    String getApp();

    /**
     * 设置应用名称
     *
     * @return 应用名称
     */
    String getIp();

    /**
     * 获取端口
     *
     * @return 端口
     */
    Integer getPort();

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    Date getGmtCreate();

    /**
     * 获取修改时间
     *
     * @return 修改时间
     */
    Rule toRule();
}
