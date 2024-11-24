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

package com.alibaba.nacos.console.controller;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.auth.annotation.Secured;
import com.alibaba.nacos.common.model.RestResult;
import com.alibaba.nacos.common.model.RestResultUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.console.paramcheck.ConsoleDefaultHttpParamExtractor;
import com.alibaba.nacos.core.namespace.model.Namespace;
import com.alibaba.nacos.core.namespace.repository.NamespacePersistService;
import com.alibaba.nacos.core.paramcheck.ExtractorManager;
import com.alibaba.nacos.core.service.NamespaceOperationService;
import com.alibaba.nacos.plugin.auth.constant.ActionTypes;
import com.alibaba.nacos.plugin.auth.impl.constant.AuthConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 命名空间服务.
 *
 * @author Nacos
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/13
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/console/namespaces")
@ExtractorManager.Extractor(httpExtractor = ConsoleDefaultHttpParamExtractor.class)
public class NamespaceController {

    private static final int NAMESPACE_ID_MAX_LENGTH = 128;
    private final Pattern namespaceIdCheckPattern = Pattern.compile("^[\\w-]+");
    private final Pattern namespaceNameCheckPattern = Pattern.compile("^[^@#$%^&*]+$");

    @SuppressWarnings("all")
    private final NamespacePersistService namespacePersistService;

    private final NamespaceOperationService namespaceOperationService;

    /**
     * 获取命名空间列表.
     *
     * @return 命名空间列表
     */
    @GetMapping
    public RestResult<List<Namespace>> getNamespaces() {
        return RestResultUtils.success(namespaceOperationService.getNamespaceList());
    }

    /**
     * 按命名空间 ID 获取命名空间所有信息.
     *
     * @param namespaceId 命名空间 ID
     * @return 命名空间 所有信息
     */
    @GetMapping(params = "show=all")
    public Namespace getNamespace(@RequestParam("namespaceId") String namespaceId) throws NacosException {
        return namespaceOperationService.getNamespace(namespaceId);
    }

    /**
     * 创建命名空间.
     *
     * @param namespaceName 命名空间名称
     * @param namespaceDesc 命名空间描述
     * @return 是否创建成功
     */
    @PostMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.WRITE)
    public Boolean createNamespace(
        @RequestParam("customNamespaceId") String namespaceId,
        @RequestParam("namespaceName") String namespaceName,
        @RequestParam(value = "namespaceDesc", required = false) String namespaceDesc) {
        if (StringUtils.isBlank(namespaceId)) {
            namespaceId = UUID.randomUUID().toString();
        } else {
            namespaceId = namespaceId.trim();
            if (!namespaceIdCheckPattern.matcher(namespaceId).matches()) {
                return false;
            }
            if (namespaceId.length() > NAMESPACE_ID_MAX_LENGTH) {
                return false;
            }
            if (namespacePersistService.tenantInfoCountByTenantId(namespaceId) > 0) {
                return false;
            }
        }
        if (!namespaceNameCheckPattern.matcher(namespaceName).matches()) {
            return false;
        }
        try {
            return namespaceOperationService.createNamespace(namespaceId, namespaceName, namespaceDesc);
        } catch (NacosException e) {
            return false;
        }
    }

    /**
     * 检查 namespaceId 是否存在.
     *
     * @param namespaceId 命名空间 ID
     * @return 如果存在，则为 true，否则为 false
     */
    @GetMapping(params = "checkNamespaceIdExist=true")
    public Boolean checkNamespaceIdExist(@RequestParam("customNamespaceId") String namespaceId) {
        if (StringUtils.isBlank(namespaceId)) {
            return false;
        }
        return (namespacePersistService.tenantInfoCountByTenantId(namespaceId) > 0);
    }

    /**
     * 编辑命名空间.
     *
     * @param namespace         命名空间
     * @param namespaceShowName 命名空间 ShowName
     * @param namespaceDesc     命名空间描述
     * @return 是否编辑成功
     */
    @PutMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.WRITE)
    public Boolean editNamespace(
        @RequestParam("namespace") String namespace,
        @RequestParam("namespaceShowName") String namespaceShowName,
        @RequestParam(value = "namespaceDesc", required = false) String namespaceDesc) {
        if (!namespaceNameCheckPattern.matcher(namespaceShowName).matches()) {
            return false;
        }
        return namespaceOperationService.editNamespace(namespace, namespaceShowName, namespaceDesc);
    }

    /**
     * 按 ID 划分的 del 命名空间.
     *
     * @param namespaceId 命名空间 ID
     * @return 是否删除成功
     */
    @DeleteMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.WRITE)
    public Boolean deleteNamespace(@RequestParam("namespaceId") String namespaceId) {
        return namespaceOperationService.removeNamespace(namespaceId);
    }
}
