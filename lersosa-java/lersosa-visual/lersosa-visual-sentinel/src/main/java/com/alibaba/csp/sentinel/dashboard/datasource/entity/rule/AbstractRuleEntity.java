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

import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import lombok.Data;

import java.util.Date;

/**
 * 规则的抽象实体.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public abstract class AbstractRuleEntity<T extends AbstractRule> implements RuleEntity {

    /**
     * 规则ID，由 Sentinel 控制台自动生成，不重复且递增.
     */
    protected Long id;

    /**
     * 应用名称，即 Sentinel 控制台项目名称.
     */
    protected String app;

    /**
     * 应用实例 IP，即 Sentinel 控制台项目实例 IP.
     */
    protected String ip;

    /**
     * 应用实例端口，即 Sentinel 控制台项目实例端口.
     */
    protected Integer port;

    /**
     * 规则对象，可以是 FlowRule、DegradeRule 等.
     */
    protected T rule;

    /**
     * 规则创建时间，由 Sentinel 控制台自动生成.
     */
    private Date gmtCreate;

    /**
     * 规则修改时间，由 Sentinel 控制台自动生成.
     */
    private Date gmtModified;

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
     * 设置应用名称.
     *
     * @param app 应用名称
     * @return 规则实体
     */
    public AbstractRuleEntity<T> setApp(String app) {
        this.app = app;
        return this;
    }

    /**
     * 获取应用实例 IP.
     *
     * @return 应用实例 IP
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 设置应用实例 IP.
     *
     * @param ip 应用实例 IP
     * @return 规则实体
     */
    public AbstractRuleEntity<T> setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 获取应用实例端口.
     *
     * @return 应用实例端口
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * 设置应用实例端口.
     *
     * @param port 应用实例端口
     * @return 规则实体
     */
    public AbstractRuleEntity<T> setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 设置规则对象.
     *
     * @param rule 规则对象
     * @return 规则实体
     */
    public AbstractRuleEntity<T> setRule(T rule) {
        this.rule = rule;
        return this;
    }

    /**
     * 获取规则创建时间.
     *
     * @return 规则创建时间
     */
    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置规则创建时间.
     *
     * @param gmtCreate 规则创建时间
     * @return 规则实体
     */
    public AbstractRuleEntity<T> setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    /**
     * 设置规则修改时间.
     *
     * @param gmtModified 规则修改时间
     * @return 规则实体
     */
    public AbstractRuleEntity<T> setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }

    /**
     * 将规则实体转换为规则对象.
     *
     * @return 规则对象
     */
    @Override
    public T toRule() {
        return rule;
    }
}
