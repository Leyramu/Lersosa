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

package com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import lombok.Data;

import java.util.Objects;

/**
 * {@link GatewayParamFlowItem} 的实体.
 *
 * @author cdfive
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class GatewayParamFlowItemEntity {

    /**
     * 参数类型，参考{@link GatewayParamFlowItem#getParseStrategy()}.
     */
    private Integer parseStrategy;

    /**
     * 参数名称，参考{@link GatewayParamFlowItem#getFieldName()}.
     */
    private String fieldName;

    /**
     * 正则表达式，参考{@link GatewayParamFlowItem#getPattern()}.
     */
    private String pattern;

    /**
     * 匹配策略，参考{@link GatewayParamFlowItem#getMatchStrategy()}.
     */
    private Integer matchStrategy;

    /**
     * 重写equals方法以自定义对象相等性比较.
     *
     * @param o 要与当前对象进行比较的对象
     * @return 如果两个对象相等则返回true，否则返回false
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
     * 重写hashCode方法以自定义对象哈希值计算.
     *
     * @return 对象哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hash(parseStrategy, fieldName, pattern, matchStrategy);
    }

    /**
     * 重写toString方法以自定义对象字符串表示.
     *
     * @return 对象的字符串表示形式
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
