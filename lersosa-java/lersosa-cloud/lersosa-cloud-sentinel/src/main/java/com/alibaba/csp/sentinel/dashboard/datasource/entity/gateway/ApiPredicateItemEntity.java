/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
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
