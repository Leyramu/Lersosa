/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
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
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.slots.block.Rule;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * {@link GatewayFlowRule} 的实体.
 *
 * @author cdfive
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class GatewayFlowRuleEntity implements RuleEntity {

    /**
     * 间隔单位 0-秒.
     */
    public static final int INTERVAL_UNIT_SECOND = 0;

    /**
     * 间隔单位 1-分.
     */
    public static final int INTERVAL_UNIT_MINUTE = 1;

    /**
     * 间隔单位 2-时.
     */
    public static final int INTERVAL_UNIT_HOUR = 2;

    /**
     * 间隔单位 3-天.
     */
    public static final int INTERVAL_UNIT_DAY = 3;

    /**
     * 规则ID.
     */
    private Long id;

    /**
     * 应用名称.
     */
    private String app;

    /**
     * ip.
     */
    private String ip;

    /**
     * 端口.
     */
    private Integer port;

    /**
     * 创建时间.
     */
    private Date gmtCreate;

    /**
     * 修改时间.
     */
    private Date gmtModified;

    /**
     * 资源名称.
     */
    private String resource;

    /**
     * 资源模式.
     */
    private Integer resourceMode;

    /**
     * 0-QPS, 1-线程数.
     */
    private Integer grade;

    /**
     * QPS/线程数.
     */
    private Double count;

    /**
     * 时间间隔（秒）.
     */
    private Long interval;

    /**
     * 间隔单位.
     */
    private Integer intervalUnit;

    /**
     * 控制行为.
     */
    private Integer controlBehavior;

    /**
     * 突发流控.
     */
    private Integer burst;

    /**
     * 队列超时时间（毫秒）.
     */
    private Integer maxQueueingTimeoutMs;

    /**
     * 参数流控项.
     */
    private GatewayParamFlowItemEntity paramItem;

    /**
     * 计算时间间隔（秒）.
     *
     * @param interval     时间间隔（秒）
     * @param intervalUnit 时间间隔单位
     * @return 时间间隔（秒）
     */
    public static Long calIntervalSec(Long interval, Integer intervalUnit) {
        return switch (intervalUnit) {
            case INTERVAL_UNIT_SECOND -> interval;
            case INTERVAL_UNIT_MINUTE -> interval * 60;
            case INTERVAL_UNIT_HOUR -> interval * 60 * 60;
            case INTERVAL_UNIT_DAY -> interval * 60 * 60 * 24;
            default -> throw new IllegalArgumentException("Invalid intervalUnit: " + intervalUnit);
        };
    }

    /**
     * 解析时间间隔.
     *
     * @param intervalSec 时间间隔（秒）
     * @return [时间间隔（秒），时间间隔单位]
     */
    public static Object[] parseIntervalSec(Long intervalSec) {
        if (intervalSec % (60 * 60 * 24) == 0) {
            return new Object[]{intervalSec / (60 * 60 * 24), INTERVAL_UNIT_DAY};
        }

        if (intervalSec % (60 * 60) == 0) {
            return new Object[]{intervalSec / (60 * 60), INTERVAL_UNIT_HOUR};
        }

        if (intervalSec % 60 == 0) {
            return new Object[]{intervalSec / 60, INTERVAL_UNIT_MINUTE};
        }

        return new Object[]{intervalSec, INTERVAL_UNIT_SECOND};
    }

    /**
     * 从 {@link GatewayFlowRule} 转换为 {@link GatewayFlowRuleEntity}.
     *
     * @param app  应用名称
     * @param ip   ip
     * @param port 端口
     * @param rule {@link GatewayFlowRule}
     * @return {@link GatewayFlowRuleEntity}
     */
    public static GatewayFlowRuleEntity fromGatewayFlowRule(String app, String ip, Integer port, GatewayFlowRule rule) {
        GatewayFlowRuleEntity entity = new GatewayFlowRuleEntity();
        entity.setApp(app);
        entity.setIp(ip);
        entity.setPort(port);

        entity.setResource(rule.getResource());
        entity.setResourceMode(rule.getResourceMode());

        entity.setGrade(rule.getGrade());
        entity.setCount(rule.getCount());
        Object[] intervalSecResult = parseIntervalSec(rule.getIntervalSec());
        entity.setInterval((Long) intervalSecResult[0]);
        entity.setIntervalUnit((Integer) intervalSecResult[1]);

        entity.setControlBehavior(rule.getControlBehavior());
        entity.setBurst(rule.getBurst());
        entity.setMaxQueueingTimeoutMs(rule.getMaxQueueingTimeoutMs());

        GatewayParamFlowItem paramItem = rule.getParamItem();
        if (paramItem != null) {
            GatewayParamFlowItemEntity itemEntity = new GatewayParamFlowItemEntity();
            entity.setParamItem(itemEntity);
            itemEntity.setParseStrategy(paramItem.getParseStrategy());
            itemEntity.setFieldName(paramItem.getFieldName());
            itemEntity.setPattern(paramItem.getPattern());
            itemEntity.setMatchStrategy(paramItem.getMatchStrategy());
        }

        return entity;
    }

    /**
     * 转换为 {@link GatewayFlowRule}.
     */
    public GatewayFlowRule toGatewayFlowRule() {
        GatewayFlowRule rule = new GatewayFlowRule();
        rule.setResource(resource);
        rule.setResourceMode(resourceMode);

        rule.setGrade(grade);
        rule.setCount(count);
        rule.setIntervalSec(calIntervalSec(interval, intervalUnit));

        rule.setControlBehavior(controlBehavior);

        if (burst != null) {
            rule.setBurst(burst);
        }

        if (maxQueueingTimeoutMs != null) {
            rule.setMaxQueueingTimeoutMs(maxQueueingTimeoutMs);
        }

        if (paramItem != null) {
            GatewayParamFlowItem ruleItem = new GatewayParamFlowItem();
            rule.setParamItem(ruleItem);
            ruleItem.setParseStrategy(paramItem.getParseStrategy());
            ruleItem.setFieldName(paramItem.getFieldName());
            ruleItem.setPattern(paramItem.getPattern());

            if (paramItem.getMatchStrategy() != null) {
                ruleItem.setMatchStrategy(paramItem.getMatchStrategy());
            }
        }

        return rule;
    }

    /**
     * 获取规则ID.
     *
     * @return 规则ID
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * 设置规则ID.
     *
     * @param id 规则ID
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取应用名称.
     *
     * @return 应用名称
     */
    @Override
    public String getApp() {
        return app;
    }

    /**
     * 获取IP地址.
     *
     * @return IP地址
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 获取端口号.
     *
     * @return 端口号
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * 获取创建时间.
     *
     * @return 创建时间
     */
    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 将当前实体转换为规则对象.
     *
     * @return 规则对象
     */
    @Override
    public Rule toRule() {
        return null;
    }

    /**
     * 重写equals方法以自定义对象相等性比较.
     *
     * @param o 要与当前对象进行比较的对象
     * @return 如果两个对象相等则返回true，否则返回false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GatewayFlowRuleEntity that = (GatewayFlowRuleEntity) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(app, that.app) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(port, that.port) &&
            Objects.equals(gmtCreate, that.gmtCreate) &&
            Objects.equals(gmtModified, that.gmtModified) &&
            Objects.equals(resource, that.resource) &&
            Objects.equals(resourceMode, that.resourceMode) &&
            Objects.equals(grade, that.grade) &&
            Objects.equals(count, that.count) &&
            Objects.equals(interval, that.interval) &&
            Objects.equals(intervalUnit, that.intervalUnit) &&
            Objects.equals(controlBehavior, that.controlBehavior) &&
            Objects.equals(burst, that.burst) &&
            Objects.equals(maxQueueingTimeoutMs, that.maxQueueingTimeoutMs) &&
            Objects.equals(paramItem, that.paramItem);
    }


    /**
     * 重写hashCode方法以自定义对象的哈希码生成算法.
     *
     * @return 当前对象的哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, app, ip, port, gmtCreate, gmtModified, resource, resourceMode, grade, count, interval, intervalUnit, controlBehavior, burst, maxQueueingTimeoutMs, paramItem);
    }


    /**
     * 重写toString方法以自定义对象的字符串表示形式.
     *
     * @return 当前对象的字符串表示
     */
    @Override
    public String toString() {
        return "GatewayFlowRuleEntity{" +
            "id=" + id +
            ", app='" + app + '\'' +
            ", ip='" + ip + '\'' +
            ", port=" + port +
            ", gmtCreate=" + gmtCreate +
            ", gmtModified=" + gmtModified +
            ", resource='" + resource + '\'' +
            ", resourceMode=" + resourceMode +
            ", grade=" + grade +
            ", count=" + count +
            ", interval=" + interval +
            ", intervalUnit=" + intervalUnit +
            ", controlBehavior=" + controlBehavior +
            ", burst=" + burst +
            ", maxQueueingTimeoutMs=" + maxQueueingTimeoutMs +
            ", paramItem=" + paramItem +
            '}';
    }
}
