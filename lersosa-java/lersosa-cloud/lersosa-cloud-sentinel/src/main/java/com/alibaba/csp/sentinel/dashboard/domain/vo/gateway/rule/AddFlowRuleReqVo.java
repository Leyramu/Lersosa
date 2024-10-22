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

package com.alibaba.csp.sentinel.dashboard.domain.vo.gateway.rule;

import lombok.Data;

/**
 * 添加网关流规则的值对象
 *
 * @author cdfive
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.7.0
 * @since 2024/9/3
 */
@Data
public class AddFlowRuleReqVo {

    /**
     * 应用名称
     */
    private String app;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 资源名称
     */
    private String resource;

    /**
     * 资源类型
     */
    private Integer resourceMode;

    /**
     * 限流阈值类型
     */
    private Integer grade;

    /**
     * 限流阈值
     */
    private Double count;

    /**
     * 统计时间窗口
     */
    private Long interval;

    /**
     * 时间窗口单位
     */
    private Integer intervalUnit;

    /**
     * 流控效果
     */
    private Integer controlBehavior;

    /**
     * 预热时间
     */
    private Integer burst;

    /**
     * 队列超时时间
     */
    private Integer maxQueueingTimeoutMs;

    /**
     * 匹配规则
     */
    private GatewayParamFlowItemVo paramItem;
}
