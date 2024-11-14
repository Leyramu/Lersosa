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
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * {@link ApiDefinition} 的实体.
 *
 * @author cdfive
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
@NoArgsConstructor
public class ApiDefinitionEntity implements RuleEntity {

    /**
     * 规则ID.
     */
    private Long id;

    /**
     * 应用名称.
     */
    private String app;

    /**
     * 应用IP.
     */
    private String ip;

    /**
     * 应用端口.
     */
    private Integer port;

    /**
     * 创建时间.
     */
    private Date gmtCreate;

    /**
     * 修改时间.
     */
    private Date gmtModified;

    /**
     * API名称.
     */
    private String apiName;

    /**
     * 匹配规则集合.
     */
    private Set<ApiPredicateItemEntity> predicateItems;

    /**
     * 构造函数.
     *
     * @param apiName        API名称
     * @param predicateItems 匹配规则集合
     */
    @SuppressWarnings("unused")
    public ApiDefinitionEntity(String apiName, Set<ApiPredicateItemEntity> predicateItems) {
        this.apiName = apiName;
        this.predicateItems = predicateItems;
    }

    /**
     * 从{@link ApiDefinition}创建实体.
     *
     * @param app           应用名称
     * @param ip            应用IP
     * @param port          应用端口
     * @param apiDefinition API定义
     * @return 实体
     */
    @SuppressWarnings("unused")
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

    /**
     * 转换为{@link ApiDefinition}.
     *
     * @return {@link ApiDefinition}
     */
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
     * 获取应用IP.
     *
     * @return 应用IP
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 获取应用端口.
     *
     * @return 应用端口
     */
    @Override
    public Integer getPort() {
        return port;
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
    public Rule toRule() {
        return null;
    }

    /**
     * 判断两个实体是否相等.
     *
     * @param o 对象
     * @return 是否相等
     */
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

    /**
     * 获取哈希值.
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, app, ip, port, gmtCreate, gmtModified, apiName, predicateItems);
    }

    /**
     * 获取字符串表示.
     *
     * @return 字符串表示
     */
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
