/*
 * Copyright (c) 2025 Leyramu Group. All rights reserved.
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

package org.dromara.easyes.spring.config;

import lombok.Setter;
import org.dromara.easyes.common.property.EasyEsDynamicProperties;
import org.dromara.easyes.common.property.EasyEsProperties;
import org.dromara.easyes.common.strategy.AutoProcessIndexStrategy;
import org.dromara.easyes.common.utils.RestHighLevelClientUtils;
import org.dromara.easyes.core.index.AutoProcessIndexNotSmoothlyStrategy;
import org.dromara.easyes.core.index.AutoProcessIndexSmoothlyStrategy;
import org.dromara.easyes.spring.factory.IndexStrategyFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * easy-es 配置类.
 *
 * @author MoJie
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/6
 */
@Setter
@Configuration
@ConditionalOnProperty(value = "easy-es.enable", havingValue = "true")
public class EasyEsConfiguration implements InitializingBean {

    /**
     * easy-es配置类.
     */
    private EasyEsProperties easyEsProperties;

    /**
     * 动态多数据源配置类.
     */
    private EasyEsDynamicProperties easyEsDynamicProperties;

    /**
     * 构造函数
     *
     * @param easyEsProperties          easy-es配置类
     * @param easyEsDynamicProperties   动态多数据源配置类
     */
    @Autowired(required = false)
    public EasyEsConfiguration(EasyEsProperties easyEsProperties, EasyEsDynamicProperties easyEsDynamicProperties) {
        this.easyEsProperties = easyEsProperties;
        this.easyEsDynamicProperties = easyEsDynamicProperties;
    }

    /**
     * 初始化.
     */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.easyEsProperties, "easyEsProperties must is A bean. easy-es配置类必须给配置一个bean");
    }

    /**
     * 索引策略注册.
     *
     * @return {@link IndexStrategyFactory}
     * @author MoJie
     */
    @Bean
    public IndexStrategyFactory indexStrategyFactory() {
        return new IndexStrategyFactory();
    }

    /**
     * restHighLevelClientUtils注册.
     *
     * @return {@link RestHighLevelClientUtils}
     * @author MoJie
     */
    @Bean
    public RestHighLevelClientUtils restHighLevelClientUtils() {
        RestHighLevelClientUtils restHighLevelClientUtils = new RestHighLevelClientUtils();
        if (this.easyEsDynamicProperties == null) {
            this.easyEsDynamicProperties = new EasyEsDynamicProperties();
        }
        Map<String, EasyEsProperties> datasourceMap = this.easyEsDynamicProperties.getDatasource();
        if (datasourceMap.isEmpty()) {
            // 设置默认数据源,兼容不使用多数据源配置场景的老用户使用习惯
            datasourceMap.put(RestHighLevelClientUtils.DEFAULT_DS, this.easyEsProperties);
        }
        for (String key : datasourceMap.keySet()) {
            EasyEsProperties easyEsConfigProperties = datasourceMap.get(key);
            RestHighLevelClientUtils.registerRestHighLevelClient(key, RestHighLevelClientUtils
                    .restHighLevelClient(easyEsConfigProperties));
        }
        return restHighLevelClientUtils;
    }

    /**
     * 索引策略注册.
     *
     * @return {@link AutoProcessIndexStrategy}
     * @author MoJie
     */
    @Bean
    public AutoProcessIndexStrategy autoProcessIndexSmoothlyStrategy() {
        return new AutoProcessIndexSmoothlyStrategy();
    }

    /**
     * 索引策略注册.
     *
     * @return {@link AutoProcessIndexStrategy}
     * @author MoJie
     */
    @Bean
    public AutoProcessIndexStrategy autoProcessIndexNotSmoothlyStrategy() {
        return new AutoProcessIndexNotSmoothlyStrategy();
    }
}
