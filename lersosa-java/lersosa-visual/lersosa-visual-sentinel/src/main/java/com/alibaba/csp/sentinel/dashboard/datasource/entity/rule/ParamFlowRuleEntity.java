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

package com.alibaba.csp.sentinel.dashboard.datasource.entity.rule;

import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowClusterConfig;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 参数流控制规则实体.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@NoArgsConstructor
public class ParamFlowRuleEntity extends AbstractRuleEntity<ParamFlowRule> {

    /**
     * 从规则创建.
     *
     * @param rule 规则
     */
    public ParamFlowRuleEntity(ParamFlowRule rule) {
        AssertUtil.notNull(rule, "Authority rule should not be null");
        this.rule = rule;
    }

    /**
     * 从实体创建.
     *
     * @param app  应用名称
     * @param ip   ip 地址
     * @param port 端口
     * @param rule 规则
     */
    public static ParamFlowRuleEntity fromParamFlowRule(String app, String ip, Integer port, ParamFlowRule rule) {
        ParamFlowRuleEntity entity = new ParamFlowRuleEntity(rule);
        entity.setApp(app);
        entity.setIp(ip);
        entity.setPort(port);
        return entity;
    }

    /**
     * 获取应用名称.
     *
     * @return 应用名称
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public String getLimitApp() {
        return rule.getLimitApp();
    }

    /**
     * 获取资源名称.
     *
     * @return 资源名称
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public String getResource() {
        return rule.getResource();
    }

    /**
     * 获取流控模式.
     *
     * @return 流控模式
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public int getGrade() {
        return rule.getGrade();
    }

    /**
     * 获取参数索引.
     *
     * @return 参数索引
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public Integer getParamIdx() {
        return rule.getParamIdx();
    }

    /**
     * 获取流控阈值.
     *
     * @return 流控阈值
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public double getCount() {
        return rule.getCount();
    }

    /**
     * 获取参数流配置.
     *
     * @return 参数流配置
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @SuppressWarnings("unused")
    public List<ParamFlowItem> getParamFlowItemList() {
        return rule.getParamFlowItemList();
    }

    /**
     * 获取流控策略.
     *
     * @return 流控策略
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public int getControlBehavior() {
        return rule.getControlBehavior();
    }

    /**
     * 获取等待时长.
     *
     * @return 等待时长
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @SuppressWarnings("unused")
    public int getMaxQueueingTimeMs() {
        return rule.getMaxQueueingTimeMs();
    }

    /**
     * 获取流控 burst count.
     *
     * @return burst count
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @SuppressWarnings("unused")
    public int getBurstCount() {
        return rule.getBurstCount();
    }

    /**
     * 获取统计时长.
     *
     * @return 统计时长
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public long getDurationInSec() {
        return rule.getDurationInSec();
    }

    /**
     * 是否为集群流控.
     *
     * @return 是否为集群流控
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isClusterMode() {
        return rule.isClusterMode();
    }

    /**
     * 获取集群配置.
     *
     * @return 集群配置
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public ParamFlowClusterConfig getClusterConfig() {
        return rule.getClusterConfig();
    }
}
