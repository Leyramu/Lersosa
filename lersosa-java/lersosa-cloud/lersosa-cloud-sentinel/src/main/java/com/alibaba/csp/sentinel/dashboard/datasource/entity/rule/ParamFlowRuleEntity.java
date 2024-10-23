/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.datasource.entity.rule;

import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowClusterConfig;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 参数流控制规则实体
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 0.2.1
 * @since 2024/9/3
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParamFlowRuleEntity extends AbstractRuleEntity<ParamFlowRule> {

    /**
     * 参数流控规则
     *
     * @param rule 参数流控规则
     */
    public ParamFlowRuleEntity(ParamFlowRule rule) {
        AssertUtil.notNull(rule, "Authority rule should not be null");
        this.rule = rule;
    }

    /**
     * 从参数流控规则创建实体对象
     *
     * @param app  应用名称
     * @param ip   IP 地址
     * @param port 端口
     * @param rule 参数流控规则
     * @return {@link ParamFlowRuleEntity}
     */
    public static ParamFlowRuleEntity fromParamFlowRule(String app, String ip, Integer port, ParamFlowRule rule) {
        ParamFlowRuleEntity entity = new ParamFlowRuleEntity(rule);
        entity.setApp(app);
        entity.setIp(ip);
        entity.setPort(port);
        return entity;
    }

    /**
     * 获取限流规则
     *
     * @return 限流规则
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public String getLimitApp() {
        return rule.getLimitApp();
    }

    /**
     * 获取资源名称
     *
     * @return 资源名称
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public String getResource() {
        return rule.getResource();
    }

    /**
     * 获取流控模式
     *
     * @return 流控模式
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public int getGrade() {
        return rule.getGrade();
    }

    /**
     * 获取参数索引
     *
     * @return 参数索引
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public Integer getParamIdx() {
        return rule.getParamIdx();
    }

    /**
     * 获取流控阈值
     *
     * @return 流控阈值
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public double getCount() {
        return rule.getCount();
    }

    /**
     * 获取参数流控项列表
     *
     * @return 参数流控项列表
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public List<ParamFlowItem> getParamFlowItemList() {
        return rule.getParamFlowItemList();
    }

    /**
     * 获取流控控制行为
     *
     * @return 流控控制行为
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public int getControlBehavior() {
        return rule.getControlBehavior();
    }

    /**
     * 获取最大排队时间
     *
     * @return 最大排队时间
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public int getMaxQueueingTimeMs() {
        return rule.getMaxQueueingTimeMs();
    }

    /**
     * 获取流控集群配置
     *
     * @return 流控集群配置
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public int getBurstCount() {
        return rule.getBurstCount();
    }

    /**
     * 获取流控集群配置
     *
     * @return 流控集群配置
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public long getDurationInSec() {
        return rule.getDurationInSec();
    }

    /**
     * 是否为集群模式
     *
     * @return 是否为集群模式
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isClusterMode() {
        return rule.isClusterMode();
    }

    /**
     * 获取流控集群配置
     *
     * @return 流控集群配置
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public ParamFlowClusterConfig getClusterConfig() {
        return rule.getClusterConfig();
    }
}
