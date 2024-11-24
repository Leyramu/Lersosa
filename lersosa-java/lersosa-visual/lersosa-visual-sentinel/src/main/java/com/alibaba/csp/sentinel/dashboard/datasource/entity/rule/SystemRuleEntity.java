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

import com.alibaba.csp.sentinel.slots.system.SystemRule;
import lombok.Data;

import java.util.Date;

/**
 * 系统规则实体.
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class SystemRuleEntity implements RuleEntity {

    /**
     * 规则ID.
     */
    private Long id;

    /**
     * 应用名称.
     */
    private String app;

    /**
     * IP地址.
     */
    private String ip;

    /**
     * 端口号.
     */
    private Integer port;

    /**
     * 系统负载.
     */
    private Double highestSystemLoad;

    /**
     * 平均响应时间.
     */
    private Long avgRt;

    /**
     * 最大线程数.
     */
    private Long maxThread;

    /**
     * QPS.
     */
    private Double qps;

    /**
     * CPU使用率.
     */
    private Double highestCpuUsage;

    /**
     * 创建时间.
     */
    private Date gmtCreate;

    /**
     * 修改时间.
     */
    private Date gmtModified;

    /**
     * 从系统规则转换成实体.
     *
     * @param app  应用名称
     * @param ip   IP地址
     * @param port 端口号
     * @param rule 规则
     * @return 规则实体
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
     * 获取创建时间.
     *
     * @return 创建时间
     */
    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 转换为规则对象.
     *
     * @return 规则对象
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
