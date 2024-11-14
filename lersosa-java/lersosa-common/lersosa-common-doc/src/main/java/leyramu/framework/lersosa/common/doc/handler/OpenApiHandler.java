/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.doc.handler;

import cn.hutool.core.io.IoUtil;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.tags.Tags;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 自定义 openapi 处理器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@SuppressWarnings("all")
public class OpenApiHandler extends OpenAPIService {

    /**
     * Basic 错误控制器.
     */
    private static Class<?> basicErrorController;

    /**
     * 安全解析器.
     */
    private final SecurityService securityParser;

    /**
     * Mappings 地图.
     */
    private final Map<String, Object> mappingsMap = new HashMap<>();

    /**
     * Springdoc 标签.
     */
    private final Map<HandlerMethod, Tag> springdocTags = new HashMap<>();

    /**
     * Open API 构建器定制器.
     */
    private final Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomisers;

    /**
     * 服务器基 URL 定制器.
     */
    private final Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers;

    /**
     * Spring doc 配置属性.
     */
    private final SpringDocConfigProperties springDocConfigProperties;

    /**
     * 缓存的开放 API 映射.
     */
    private final Map<String, OpenAPI> cachedOpenAPI = new HashMap<>();

    /**
     * 属性解析程序 utils.
     */
    private final PropertyResolverUtils propertyResolverUtils;

    /**
     * javadoc 提供程序.
     */
    private final Optional<JavadocProvider> javadocProvider;

    /**
     * 背景.
     */
    private ApplicationContext context;

    /**
     * 开放 API.
     */
    private OpenAPI openApi;

    /**
     * 存在的 Is 服务器.
     */
    private boolean isServersPresent;

    /**
     * Server 基 URL.
     */
    private String serverBaseUrl;

    /**
     * 实例化新的 Open API 构建器.
     *
     * @param openApi                   开放 API
     * @param securityParser            安全解析器
     * @param springDocConfigProperties Spring Doc 配置属性
     * @param propertyResolverUtils     属性 resolver utils
     * @param openApiBuilderCustomizers Open API Builder 定制器
     * @param serverBaseUrlCustomizers  服务器基本 URL 定制器
     * @param javadocProvider           Javadoc 提供程序
     */
    public OpenApiHandler(
        Optional<OpenAPI> openApi,
        SecurityService securityParser,
        SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils,
        Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers,
        Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers,
        Optional<JavadocProvider> javadocProvider) {
        super(openApi, securityParser, springDocConfigProperties, propertyResolverUtils, openApiBuilderCustomizers, serverBaseUrlCustomizers, javadocProvider);
        if (openApi.isPresent()) {
            this.openApi = openApi.get();
            if (this.openApi.getComponents() == null) {
                this.openApi.setComponents(new Components());
            }
            if (this.openApi.getPaths() == null) {
                this.openApi.setPaths(new Paths());
            }
            if (!CollectionUtils.isEmpty(this.openApi.getServers())) {
                this.isServersPresent = true;
            }
        }
        this.propertyResolverUtils = propertyResolverUtils;
        this.securityParser = securityParser;
        this.springDocConfigProperties = springDocConfigProperties;
        this.openApiBuilderCustomisers = openApiBuilderCustomizers;
        this.serverBaseUrlCustomizers = serverBaseUrlCustomizers;
        this.javadocProvider = javadocProvider;
        if (springDocConfigProperties.isUseFqn()) {
            TypeNameResolver.std.setUseFqn(true);
        }
    }

    @Override
    public Operation buildTags(HandlerMethod handlerMethod, Operation operation, OpenAPI openApi, Locale locale) {

        Set<Tag> tags = new HashSet<>();
        Set<String> tagsStr = new HashSet<>();

        buildTagsFromMethod(handlerMethod.getMethod(), tags, tagsStr, locale);
        buildTagsFromClass(handlerMethod.getBeanType(), tags, tagsStr, locale);

        if (!CollectionUtils.isEmpty(tagsStr)) {
            tagsStr = tagsStr.stream()
                .map(str -> propertyResolverUtils.resolve(str, locale))
                .collect(Collectors.toSet());
        }

        if (springdocTags.containsKey(handlerMethod)) {
            Tag tag = springdocTags.get(handlerMethod);
            tagsStr.add(tag.getName());
            if (openApi.getTags() == null || !openApi.getTags().contains(tag)) {
                openApi.addTagsItem(tag);
            }
        }

        if (!CollectionUtils.isEmpty(tagsStr)) {
            if (CollectionUtils.isEmpty(operation.getTags())) {
                operation.setTags(new ArrayList<>(tagsStr));
            } else {
                Set<String> operationTagsSet = new HashSet<>(operation.getTags());
                operationTagsSet.addAll(tagsStr);
                operation.getTags().clear();
                operation.getTags().addAll(operationTagsSet);
            }
        }

        if (isAutoTagClasses(operation)) {


            if (javadocProvider.isPresent()) {
                String description = javadocProvider.get().getClassJavadoc(handlerMethod.getBeanType());
                if (StringUtils.isNotBlank(description)) {
                    Tag tag = new Tag();

                    // 自定义部分 修改使用java注释当tag名
                    List<String> list = IoUtil.readLines(new StringReader(description), new ArrayList<>());
                    // tag.setName(tagAutoName);
                    tag.setName(list.getFirst());
                    operation.addTagsItem(list.getFirst());

                    tag.setDescription(description);
                    if (openApi.getTags() == null || !openApi.getTags().contains(tag)) {
                        openApi.addTagsItem(tag);
                    }
                }
            } else {
                String tagAutoName = splitCamelCase(handlerMethod.getBeanType().getSimpleName());
                operation.addTagsItem(tagAutoName);
            }
        }

        if (!CollectionUtils.isEmpty(tags)) {
            // Existing tags
            List<Tag> openApiTags = openApi.getTags();
            if (!CollectionUtils.isEmpty(openApiTags)) {
                tags.addAll(openApiTags);
            }
            openApi.setTags(new ArrayList<>(tags));
        }

        // Handle SecurityRequirement at operation level
        io.swagger.v3.oas.annotations.security.SecurityRequirement[] securityRequirements = securityParser
            .getSecurityRequirements(handlerMethod);
        if (securityRequirements != null) {
            if (securityRequirements.length == 0) {
                operation.setSecurity(Collections.emptyList());
            } else {
                securityParser.buildSecurityRequirement(securityRequirements, operation);
            }
        }

        return operation;
    }

    private void buildTagsFromMethod(Method method, Set<Tag> tags, Set<String> tagsStr, Locale locale) {
        // method tags
        Set<Tags> tagsSet = AnnotatedElementUtils
            .findAllMergedAnnotations(method, Tags.class);
        Set<io.swagger.v3.oas.annotations.tags.Tag> methodTags = tagsSet.stream()
            .flatMap(x -> Stream.of(x.value())).collect(Collectors.toSet());
        methodTags.addAll(AnnotatedElementUtils.findAllMergedAnnotations(method, io.swagger.v3.oas.annotations.tags.Tag.class));
        if (!CollectionUtils.isEmpty(methodTags)) {
            tagsStr.addAll(methodTags.stream().map(tag -> propertyResolverUtils.resolve(tag.name(), locale)).collect(Collectors.toSet()));
            List<io.swagger.v3.oas.annotations.tags.Tag> allTags = new ArrayList<>(methodTags);
            addTags(allTags, tags, locale);
        }
    }

    private void addTags(List<io.swagger.v3.oas.annotations.tags.Tag> sourceTags, Set<Tag> tags, Locale locale) {
        Optional<Set<Tag>> optionalTagSet = AnnotationsUtils
            .getTags(sourceTags.toArray(new io.swagger.v3.oas.annotations.tags.Tag[0]), true);
        optionalTagSet.ifPresent(tagsSet -> tagsSet.forEach(tag -> {
            tag.name(propertyResolverUtils.resolve(tag.getName(), locale));
            tag.description(propertyResolverUtils.resolve(tag.getDescription(), locale));
            if (tags.stream().noneMatch(t -> t.getName().equals(tag.getName()))) {
                tags.add(tag);
            }
        }));
    }
}
