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

package leyramu.framework.lersosa.common.purge.config;

import leyramu.framework.lersosa.common.core.factory.YmlPropertySourceFactory;
import leyramu.framework.lersosa.common.purge.aspect.NgxCacheAspect;
import leyramu.framework.lersosa.common.purge.handler.NgxCacheHandler;
import leyramu.framework.lersosa.common.purge.properties.NginxCacheProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * Nginx 缓存配置.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/17
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(NginxCacheProperties.class)
@PropertySource(value = "classpath:common-purge.yml", factory = YmlPropertySourceFactory.class)
@ConditionalOnProperty(name = "nginx.cache.enable", havingValue = "true", matchIfMissing = true)
public class NginxCacheConfig {

    /**
     * Nginx缓存配置.
     */
    private final NginxCacheProperties nginxCacheProperties;

    /**
     * 创建 Nginx 缓存处理器.
     *
     * @return Nginx 缓存处理器
     */
    @Bean
    public NgxCacheHandler ngxCacheHandler() {
        log.info("Nginx 缓存功能已启用");
        return new NgxCacheHandler(nginxCacheProperties);
    }

    /**
     * 创建 Nginx 缓存代理.
     *
     * @return Nginx 缓存代理
     */
    @Bean
    public NgxCacheAspect ngxCacheAspect(NgxCacheHandler handler) {
        log.info("Nginx 缓存处理器已激活 | 服务端点：{}", nginxCacheProperties.getPurgeAddr());
        return new NgxCacheAspect(handler);
    }
}
