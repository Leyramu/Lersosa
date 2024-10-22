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

import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import lombok.Data;

import java.util.Date;

/**
 * 规则的抽象实体
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 0.2.1
 * @since 2024/9/3
 */
@Data
public abstract class AbstractRuleEntity<T extends AbstractRule> implements RuleEntity {

    /**
     * 规则 ID
     */
    protected Long id;

    /**
     * 应用名称
     */
    protected String app;

    /**
     * IP 地址
     */
    protected String ip;

    /**
     * 端口号
     */
    protected Integer port;

    /**
     * 规则对象
     */
    protected T rule;

    /**
     * 规则创建时间
     */
    private Date gmtCreate;

    /**
     * 规则修改时间
     */
    private Date gmtModified;

    /**
     * 获取规则 ID
     *
     * @return 规则 ID
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * 设置规则 ID
     *
     * @param id 规则 ID
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
     * 设置应用名称
     *
     * @param app 应用名称
     */
    public AbstractRuleEntity<T> setApp(String app) {
        this.app = app;
        return this;
    }

    /**
     * 获取 IP 地址
     *
     * @return IP 地址
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 设置 IP 地址
     *
     * @param ip IP 地址
     */
    public AbstractRuleEntity<T> setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 获取端口号
     *
     * @return 端口号
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * 设置端口号
     *
     * @param port 端口号
     */
    public AbstractRuleEntity<T> setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 获取规则对象
     *
     * @return 规则对象
     */
    public AbstractRuleEntity<T> setRule(T rule) {
        this.rule = rule;
        return this;
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
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public AbstractRuleEntity<T> setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public AbstractRuleEntity<T> setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }

    /**
     * 转换为规则对象
     *
     * @return 规则对象
     */
    @Override
    public T toRule() {
        return rule;
    }
}
