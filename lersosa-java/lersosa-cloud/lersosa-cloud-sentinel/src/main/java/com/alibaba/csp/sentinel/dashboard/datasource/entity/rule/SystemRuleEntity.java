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

import com.alibaba.csp.sentinel.slots.system.SystemRule;
import lombok.Data;

import java.util.Date;

/**
 * 系统规则实体
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Data
public class SystemRuleEntity implements RuleEntity {

    /**
     * 规则ID
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
     * 系统规则
     */
    private Double highestSystemLoad;

    /**
     * 平均响应时间
     */
    private Long avgRt;

    /**
     * 最大线程数
     */
    private Long maxThread;

    /**
     * QPS
     */
    private Double qps;

    /**
     * CPU使用率
     */
    private Double highestCpuUsage;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 转换为系统规则
     *
     * @param app  应用名称
     * @param ip   IP 地址
     * @param port 端口
     * @param rule 系统规则
     * @return 系统规则实体
     */
    public static SystemRuleEntity fromSystemRule(String app, String ip, Integer port, SystemRule rule) {
        SystemRuleEntity entity = new SystemRuleEntity();
        entity.setApp(app);
        entity.setIp(ip);
        entity.setPort(port);
        entity.setHighestSystemLoad(rule.getHighestSystemLoad());
        entity.setHighestCpuUsage(rule.getHighestCpuUsage());
        entity.setAvgRt(rule.getAvgRt());
        entity.setMaxThread(rule.getMaxThread());
        entity.setQps(rule.getQps());
        return entity;
    }

    /**
     * 获取应用名称
     *
     * @return 应用名称
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
     * 获取应用名称
     *
     * @return 应用名称
     */
    @Override
    public String getApp() {
        return app;
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
     * 转换为规则
     *
     * @return 系统规则
     */
    @Override
    public SystemRule toRule() {
        SystemRule rule = new SystemRule();
        rule.setHighestSystemLoad(highestSystemLoad);
        rule.setAvgRt(avgRt);
        rule.setMaxThread(maxThread);
        rule.setQps(qps);
        rule.setHighestCpuUsage(highestCpuUsage);
        return rule;
    }
}
