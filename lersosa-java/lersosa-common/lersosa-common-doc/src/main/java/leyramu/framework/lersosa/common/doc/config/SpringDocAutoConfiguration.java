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

package leyramu.framework.lersosa.common.doc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.doc.config.properties.SpringDocProperties;
import leyramu.framework.lersosa.common.doc.handler.OpenApiHandler;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Swagger 文档配置.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@AutoConfiguration(before = SpringDocConfiguration.class)
@EnableConfigurationProperties({SpringDocProperties.class, SwaggerUiConfigProperties.class})
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", havingValue = "true", matchIfMissing = true)
public class SpringDocAutoConfiguration {

    private final ServerProperties serverProperties;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI openApi(SpringDocProperties properties) {
        OpenAPI openApi = new OpenAPI();
        // 文档基本信息
        SpringDocProperties.InfoProperties infoProperties = properties.getInfo();
        Info info = convertInfo(infoProperties);
        openApi.info(info);

        // 扩展文档信息
        openApi.externalDocs(properties.getExternalDocs());
        openApi.tags(properties.getTags());
        openApi.paths(properties.getPaths());
        openApi.components(properties.getComponents());

        // 安全设置
        Components components = openApi.getComponents();
        if (components == null) {
            components = new Components();
            openApi.components(components);
        }
        components.addSecuritySchemes("apikey", securityScheme());

        List<SecurityRequirement> securityRequirements = new ArrayList<>();
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("apikey");
        securityRequirements.add(securityRequirement);
        openApi.security(securityRequirements);

        // 服务器信息
        openApi.servers(servers(properties.getGatewayUrl()));

        return openApi;
    }

    /**
     * 安全方案
     */
    public SecurityScheme securityScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.APIKEY)
            .name("Authorization")
            .in(SecurityScheme.In.HEADER)
            .scheme("Bearer");
    }

    private Info convertInfo(SpringDocProperties.InfoProperties infoProperties) {
        Info info = new Info();
        info.setTitle(infoProperties.getTitle());
        info.setDescription(infoProperties.getDescription());
        info.setContact(infoProperties.getContact());
        info.setLicense(infoProperties.getLicense());
        info.setVersion(infoProperties.getVersion());
        return info;
    }

    /**
     * 自定义 openapi 处理器.
     */
    @Bean
    public OpenAPIService openApiBuilder(
        Optional<OpenAPI> openApi,
        SecurityService securityParser,
        SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils,
        Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomisers,
        Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomisers, Optional<JavadocProvider> javadocProvider) {
        return new OpenApiHandler(openApi, securityParser, springDocConfigProperties, propertyResolverUtils, openApiBuilderCustomisers, serverBaseUrlCustomisers, javadocProvider);
    }

    /**
     * 对已经生成好的 OpenApi 进行自定义操作.
     */
    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        // 拼接服务路径
        String appPath = "/" + StringUtils.substring(appName, appName.indexOf("-") + 1);
        String contextPath = serverProperties.getServlet().getContextPath();
        String finalContextPath;
        if (StringUtils.isBlank(contextPath) || "/".equals(contextPath)) {
            finalContextPath = appPath;
        } else {
            finalContextPath = appPath + contextPath;
        }
        // 对所有路径增加前置上下文路径
        return openApi -> {
            Paths oldPaths = openApi.getPaths();
            if (oldPaths instanceof PlusPaths) {
                return;
            }
            PlusPaths newPaths = new PlusPaths();
            oldPaths.forEach((k, v) -> newPaths.addPathItem(finalContextPath + k, v));
            openApi.setPaths(newPaths);
        };
    }

    /**
     * 网关地址
     *
     * @param gatewayUrl 网关地址
     * @return List
     */
    public List<Server> servers(String gatewayUrl) {
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server().url(gatewayUrl));
        return serverList;
    }
}
