/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.datasource.entity.rule;

import com.alibaba.csp.sentinel.slots.block.flow.ClusterFlowConfig;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import lombok.Data;

import java.util.Date;

/**
 * 流规则实体
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Data
public class FlowRuleEntity implements RuleEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 应用名称
     */
    private String app;

    /**
     * IP 地址
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 限流应用名称
     */
    private String limitApp;

    /**
     * 资源名称
     */
    private String resource;

    /**
     * 限流规则
     */
    private Integer grade;

    /**
     * 限流阈值
     */
    private Double count;

    /**
     * 限流策略
     */
    private Integer strategy;

    /**
     * ref 资源
     */
    private String refResource;

    /**
     * 控制行为
     */
    private Integer controlBehavior;

    /**
     * 冷启动时间
     */
    private Integer warmUpPeriodSec;

    /**
     * 行为最大排队时间
     */
    private Integer maxQueueingTimeMs;

    /**
     * 是否为集群模式
     */
    private boolean clusterMode;

    /**
     * 集群模式的流规则配置
     */
    private ClusterFlowConfig clusterConfig;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 转换为流规则
     *
     * @param app  应用名称
     * @param ip   IP 地址
     * @param port 端口
     * @param rule 流规则
     * @return {@link FlowRuleEntity}
     */
    public static FlowRuleEntity fromFlowRule(String app, String ip, Integer port, FlowRule rule) {
        FlowRuleEntity entity = new FlowRuleEntity();
        entity.setApp(app);
        entity.setIp(ip);
        entity.setPort(port);
        entity.setLimitApp(rule.getLimitApp());
        entity.setResource(rule.getResource());
        entity.setGrade(rule.getGrade());
        entity.setCount(rule.getCount());
        entity.setStrategy(rule.getStrategy());
        entity.setRefResource(rule.getRefResource());
        entity.setControlBehavior(rule.getControlBehavior());
        entity.setWarmUpPeriodSec(rule.getWarmUpPeriodSec());
        entity.setMaxQueueingTimeMs(rule.getMaxQueueingTimeMs());
        entity.setClusterMode(rule.isClusterMode());
        entity.setClusterConfig(rule.getClusterConfig());
        return entity;
    }

    /**
     * 转换为流规则
     *
     * @return {@link FlowRule}
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 获取端口
     *
     * @return 端口
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * 获取应用名称
     *
     * @return 应用名称
     */
    @Override
    public String getApp() {
        return app;
    }

    /**
     * 获取主键
     *
     * @return 主键
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 转换为流规则
     *
     * @return {@link FlowRule}
     */
    @Override
    public FlowRule toRule() {
        FlowRule flowRule = new FlowRule();
        flowRule.setCount(this.count);
        flowRule.setGrade(this.grade);
        flowRule.setResource(this.resource);
        flowRule.setLimitApp(this.limitApp);
        flowRule.setRefResource(this.refResource);
        flowRule.setStrategy(this.strategy);
        if (this.controlBehavior != null) {
            flowRule.setControlBehavior(controlBehavior);
        }
        if (this.warmUpPeriodSec != null) {
            flowRule.setWarmUpPeriodSec(warmUpPeriodSec);
        }
        if (this.maxQueueingTimeMs != null) {
            flowRule.setMaxQueueingTimeMs(maxQueueingTimeMs);
        }
        flowRule.setClusterMode(clusterMode);
        flowRule.setClusterConfig(clusterConfig);
        return flowRule;
    }
}
