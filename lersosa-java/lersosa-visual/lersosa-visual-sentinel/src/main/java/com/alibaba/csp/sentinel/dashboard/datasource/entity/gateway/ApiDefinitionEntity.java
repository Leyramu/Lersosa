/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.slots.block.Rule;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entity for {@link ApiDefinition}.
 *
 * @author cdfive
 * @since 1.7.0
 */
public class ApiDefinitionEntity implements RuleEntity {

    private Long id;
    private String app;
    private String ip;
    private Integer port;

    private Date gmtCreate;
    private Date gmtModified;

    private String apiName;
    private Set<ApiPredicateItemEntity> predicateItems;

    public ApiDefinitionEntity() {

    }

    public ApiDefinitionEntity(String apiName, Set<ApiPredicateItemEntity> predicateItems) {
        this.apiName = apiName;
        this.predicateItems = predicateItems;
    }

    public static ApiDefinitionEntity fromApiDefinition(String app, String ip, Integer port, ApiDefinition apiDefinition) {
        ApiDefinitionEntity entity = new ApiDefinitionEntity();
        entity.setApp(app);
        entity.setIp(ip);
        entity.setPort(port);
        entity.setApiName(apiDefinition.getApiName());

        Set<ApiPredicateItemEntity> predicateItems = new LinkedHashSet<>();
        entity.setPredicateItems(predicateItems);

        Set<ApiPredicateItem> apiPredicateItems = apiDefinition.getPredicateItems();
        if (apiPredicateItems != null) {
            for (ApiPredicateItem apiPredicateItem : apiPredicateItems) {
                ApiPredicateItemEntity itemEntity = new ApiPredicateItemEntity();
                predicateItems.add(itemEntity);
                ApiPathPredicateItem pathPredicateItem = (ApiPathPredicateItem) apiPredicateItem;
                itemEntity.setPattern(pathPredicateItem.getPattern());
                itemEntity.setMatchStrategy(pathPredicateItem.getMatchStrategy());
            }
        }

        return entity;
    }

    public ApiDefinition toApiDefinition() {
        ApiDefinition apiDefinition = new ApiDefinition();
        apiDefinition.setApiName(apiName);

        Set<ApiPredicateItem> apiPredicateItems = new LinkedHashSet<>();
        apiDefinition.setPredicateItems(apiPredicateItems);

        if (predicateItems != null) {
            for (ApiPredicateItemEntity predicateItem : predicateItems) {
                ApiPathPredicateItem apiPredicateItem = new ApiPathPredicateItem();
                apiPredicateItems.add(apiPredicateItem);
                apiPredicateItem.setMatchStrategy(predicateItem.getMatchStrategy());
                apiPredicateItem.setPattern(predicateItem.getPattern());
            }
        }

        return apiDefinition;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Set<ApiPredicateItemEntity> getPredicateItems() {
        return predicateItems;
    }

    public void setPredicateItems(Set<ApiPredicateItemEntity> predicateItems) {
        this.predicateItems = predicateItems;
    }

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

    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public Rule toRule() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiDefinitionEntity entity = (ApiDefinitionEntity) o;
        return Objects.equals(id, entity.id) &&
            Objects.equals(app, entity.app) &&
            Objects.equals(ip, entity.ip) &&
            Objects.equals(port, entity.port) &&
            Objects.equals(gmtCreate, entity.gmtCreate) &&
            Objects.equals(gmtModified, entity.gmtModified) &&
            Objects.equals(apiName, entity.apiName) &&
            Objects.equals(predicateItems, entity.predicateItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, ip, port, gmtCreate, gmtModified, apiName, predicateItems);
    }

    @Override
    public String toString() {
        return "ApiDefinitionEntity{" +
            "id=" + id +
            ", app='" + app + '\'' +
            ", ip='" + ip + '\'' +
            ", port=" + port +
            ", gmtCreate=" + gmtCreate +
            ", gmtModified=" + gmtModified +
            ", apiName='" + apiName + '\'' +
            ", predicateItems=" + predicateItems +
            '}';
    }
}
