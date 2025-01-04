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

package leyramu.framework.lersosa.gateway.api.config;

import com.alibaba.nacos.common.notify.NotifyCenter;
import leyramu.framework.lersosa.gateway.api.config.properties.CustomSwaggerProperties;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc配置类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/18
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnProperty(value = "springdoc.api-docs.enabled", matchIfMissing = true)
public class SpringDocConfig implements InitializingBean {

    /**
     * Swagger UI配置属性
     */
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    /**
     * 自定义Swagger配置属性
     */
    private final CustomSwaggerProperties customSwaggerProperties;

    /**
     * 服务发现客户端
     */
    private final DiscoveryClient discoveryClient;

    /**
     * 在初始化后调用的方法
     */
    @Override
    public void afterPropertiesSet() {
        NotifyCenter.registerSubscriber(new SwaggerDocRegister(swaggerUiConfigProperties, customSwaggerProperties, discoveryClient));
    }

    /**
     * 创建并注册 SwaggerDocRegister Bean
     */
    @Bean
    public SwaggerDocRegister swaggerDocRegister() {
        return new SwaggerDocRegister(swaggerUiConfigProperties, customSwaggerProperties, discoveryClient);
    }
}
