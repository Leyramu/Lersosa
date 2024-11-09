/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.datasource.entity.rule;

import com.alibaba.csp.sentinel.slots.block.AbstractRule;

import java.util.Date;

/**
 * @author Eric Zhao
 * @since 0.2.1
 */
public abstract class AbstractRuleEntity<T extends AbstractRule> implements RuleEntity {

    protected Long id;

    protected String app;
    protected String ip;
    protected Integer port;

    protected T rule;

    private Date gmtCreate;
    private Date gmtModified;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getApp() {
        return app;
    }

    public AbstractRuleEntity<T> setApp(String app) {
        this.app = app;
        return this;
    }

    @Override
    public String getIp() {
        return ip;
    }

    public AbstractRuleEntity<T> setIp(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public AbstractRuleEntity<T> setPort(Integer port) {
        this.port = port;
        return this;
    }

    public T getRule() {
        return rule;
    }

    public AbstractRuleEntity<T> setRule(T rule) {
        this.rule = rule;
        return this;
    }

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    public AbstractRuleEntity<T> setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public AbstractRuleEntity<T> setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }

    @Override
    public T toRule() {
        return rule;
    }
}
