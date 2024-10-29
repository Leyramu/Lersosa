/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
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
