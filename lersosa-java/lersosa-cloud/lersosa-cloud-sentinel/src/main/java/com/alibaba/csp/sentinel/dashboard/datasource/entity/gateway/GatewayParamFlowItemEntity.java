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

package com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import lombok.Data;

import java.util.Objects;

/**
 * {@link GatewayParamFlowItem} 的实体
 *
 * @author cdfive
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.7.0
 * @since 2024/9/3
 */
@Data
public class GatewayParamFlowItemEntity {

    /**
     * 解析策略
     */
    private Integer parseStrategy;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 匹配模式
     */
    private String pattern;

    /**
     * 匹配策略
     */
    private Integer matchStrategy;

    /**
     * 是否相等
     *
     * @param o 比较对象
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
        GatewayParamFlowItemEntity that = (GatewayParamFlowItemEntity) o;
        return Objects.equals(parseStrategy, that.parseStrategy) &&
               Objects.equals(fieldName, that.fieldName) &&
               Objects.equals(pattern, that.pattern) &&
               Objects.equals(matchStrategy, that.matchStrategy);
    }

    /**
     * 哈希码
     *
     * @return 哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(parseStrategy, fieldName, pattern, matchStrategy);
    }

    /**
     * 字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "GatewayParamFlowItemEntity{" +
               "parseStrategy=" + parseStrategy +
               ", fieldName='" + fieldName + '\'' +
               ", pattern='" + pattern + '\'' +
               ", matchStrategy=" + matchStrategy +
               '}';
    }
}
