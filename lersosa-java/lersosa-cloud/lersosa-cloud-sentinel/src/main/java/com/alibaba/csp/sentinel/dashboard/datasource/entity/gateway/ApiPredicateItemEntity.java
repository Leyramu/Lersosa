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

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * {@link ApiPredicateItem} 的实体
 *
 * @author cdfive
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.7.0
 * @since 2024/9/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiPredicateItemEntity {

    /**
     * 匹配路径
     */
    private String pattern;

    /**
     * 匹配策略
     */
    private Integer matchStrategy;

    /**
     * 是否为默认的匹配策略
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiPredicateItemEntity that = (ApiPredicateItemEntity) o;
        return Objects.equals(pattern, that.pattern) &&
               Objects.equals(matchStrategy, that.matchStrategy);
    }

    /**
     * 默认的匹配策略
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(pattern, matchStrategy);
    }

    /**
     * 默认的匹配策略
     *
     * @return 默认的匹配策略
     */
    @Override
    public String toString() {
        return "ApiPredicateItemEntity{" +
               "pattern='" + pattern + '\'' +
               ", matchStrategy=" + matchStrategy +
               '}';
    }
}
