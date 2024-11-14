/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.nacos.console.controller.v2;

import com.alibaba.nacos.api.annotation.NacosApi;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.exception.api.NacosApiException;
import com.alibaba.nacos.api.model.v2.ErrorCode;
import com.alibaba.nacos.api.model.v2.Result;
import com.alibaba.nacos.auth.annotation.Secured;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.console.paramcheck.ConsoleDefaultHttpParamExtractor;
import com.alibaba.nacos.core.namespace.model.Namespace;
import com.alibaba.nacos.core.namespace.model.form.NamespaceForm;
import com.alibaba.nacos.core.namespace.repository.NamespacePersistService;
import com.alibaba.nacos.core.paramcheck.ExtractorManager;
import com.alibaba.nacos.core.service.NamespaceOperationService;
import com.alibaba.nacos.plugin.auth.constant.ActionTypes;
import com.alibaba.nacos.plugin.auth.constant.SignType;
import com.alibaba.nacos.plugin.auth.impl.constant.AuthConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 命名空间控制器 V 2.
 *
 * @author dongyafei
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/13
 */
@NacosApi
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/console/namespace")
@ExtractorManager.Extractor(httpExtractor = ConsoleDefaultHttpParamExtractor.class)
public class NamespaceControllerV2 {

    private static final int NAMESPACE_ID_MAX_LENGTH = 128;
    private final NamespaceOperationService namespaceOperationService;
    private final Pattern namespaceIdCheckPattern = Pattern.compile("^[\\w-]+");
    private final Pattern namespaceNameCheckPattern = Pattern.compile("^[^@#$%^&*]+$");
    @SuppressWarnings("all")
    private final NamespacePersistService namespacePersistService;


    /**
     * 获取命名空间列表.
     *
     * @return 命名空间列表
     */
    @GetMapping("/list")
    public Result<List<Namespace>> getNamespaceList() {
        return Result.success(namespaceOperationService.getNamespaceList());
    }

    /**
     * 按命名空间 ID 获取命名空间所有信息.
     *
     * @param namespaceId 命名空间 ID
     * @return 命名空间 所有信息
     */
    @GetMapping()
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX
        + "namespaces", action = ActionTypes.READ, signType = SignType.CONSOLE)
    public Result<Namespace> getNamespace(@RequestParam("namespaceId") String namespaceId) throws NacosException {
        return Result.success(namespaceOperationService.getNamespace(namespaceId));
    }

    /**
     * 创建命名空间.
     *
     * @param namespaceForm 命名空间形式
     * @return 是否创建成功
     */
    @PostMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX
        + "namespaces", action = ActionTypes.WRITE, signType = SignType.CONSOLE)
    public Result<Boolean> createNamespace(NamespaceForm namespaceForm) throws NacosException {

        namespaceForm.validate();

        String namespaceId = namespaceForm.getNamespaceId();
        String namespaceName = namespaceForm.getNamespaceName();
        String namespaceDesc = namespaceForm.getNamespaceDesc();

        if (StringUtils.isBlank(namespaceId)) {
            namespaceId = UUID.randomUUID().toString();
        } else {
            namespaceId = namespaceId.trim();
            if (!namespaceIdCheckPattern.matcher(namespaceId).matches()) {
                throw new NacosApiException(HttpStatus.BAD_REQUEST.value(), ErrorCode.ILLEGAL_NAMESPACE,
                    "namespaceId [" + namespaceId + "] mismatch the pattern");
            }
            if (namespaceId.length() > NAMESPACE_ID_MAX_LENGTH) {
                throw new NacosApiException(HttpStatus.BAD_REQUEST.value(), ErrorCode.ILLEGAL_NAMESPACE,
                    "too long namespaceId, over " + NAMESPACE_ID_MAX_LENGTH);
            }
            if (namespacePersistService.tenantInfoCountByTenantId(namespaceId) > 0) {
                throw new NacosApiException(HttpStatus.BAD_REQUEST.value(), ErrorCode.ILLEGAL_NAMESPACE,
                    "the namespaceId is existed, namespaceId: " + namespaceForm.getNamespaceId());
            }
        }
        if (!namespaceNameCheckPattern.matcher(namespaceName).matches()) {
            throw new NacosApiException(HttpStatus.BAD_REQUEST.value(), ErrorCode.ILLEGAL_NAMESPACE,
                "namespaceName [" + namespaceName + "] contains illegal char");
        }
        return Result.success(namespaceOperationService.createNamespace(namespaceId, namespaceName, namespaceDesc));
    }

    /**
     * 编辑命名空间.
     *
     * @param namespaceForm 命名空间参数
     * @return 是否编辑确定
     */
    @PutMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX
        + "namespaces", action = ActionTypes.WRITE, signType = SignType.CONSOLE)
    public Result<Boolean> editNamespace(NamespaceForm namespaceForm) throws NacosException {
        namespaceForm.validate();
        if (!namespaceNameCheckPattern.matcher(namespaceForm.getNamespaceName()).matches()) {
            throw new NacosApiException(HttpStatus.BAD_REQUEST.value(), ErrorCode.ILLEGAL_NAMESPACE,
                "namespaceName [" + namespaceForm.getNamespaceName() + "] contains illegal char");
        }
        return Result.success(namespaceOperationService
            .editNamespace(namespaceForm.getNamespaceId(), namespaceForm.getNamespaceName(),
                namespaceForm.getNamespaceDesc()));
    }

    /**
     * 按 ID 删除命名空间.
     *
     * @param namespaceId 命名空间 ID
     * @return 是否删除 OK
     */
    @DeleteMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX
        + "namespaces", action = ActionTypes.WRITE, signType = SignType.CONSOLE)
    public Result<Boolean> deleteNamespace(@RequestParam("namespaceId") String namespaceId) {
        return Result.success(namespaceOperationService.removeNamespace(namespaceId));
    }
}
