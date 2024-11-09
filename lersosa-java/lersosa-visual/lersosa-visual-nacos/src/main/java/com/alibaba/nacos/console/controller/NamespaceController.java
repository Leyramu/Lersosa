/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * namespace service.
 *
 * @author Nacos
 */
@RestController
@RequestMapping("/v1/console/namespaces")
@ExtractorManager.Extractor(httpExtractor = ConsoleDefaultHttpParamExtractor.class)
public class NamespaceController {

    private static final int NAMESPACE_ID_MAX_LENGTH = 128;
    private final Pattern namespaceIdCheckPattern = Pattern.compile("^[\\w-]+");
    private final Pattern namespaceNameCheckPattern = Pattern.compile("^[^@#$%^&*]+$");
    @Autowired
    private NamespacePersistService namespacePersistService;
    @Autowired
    private NamespaceOperationService namespaceOperationService;

    /**
     * Get namespace list.
     *
     * @return namespace list
     */
    @GetMapping
    public RestResult<List<Namespace>> getNamespaces() {
        return RestResultUtils.success(namespaceOperationService.getNamespaceList());
    }

    /**
     * get namespace all info by namespace id.
     *
     * @param namespaceId namespaceId
     * @return namespace all info
     */
    @GetMapping(params = "show=all")
    public Namespace getNamespace(@RequestParam("namespaceId") String namespaceId) throws NacosException {
        return namespaceOperationService.getNamespace(namespaceId);
    }

    /**
     * create namespace.
     *
     * @param namespaceName namespace Name
     * @param namespaceDesc namespace Desc
     * @return whether create ok
     */
    @PostMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.WRITE)
    public Boolean createNamespace(@RequestParam("customNamespaceId") String namespaceId,
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
            // check unique
            if (namespacePersistService.tenantInfoCountByTenantId(namespaceId) > 0) {
                return false;
            }
        }
        // contains illegal chars
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
     * check namespaceId exist.
     *
     * @param namespaceId namespace id
     * @return true if exist, otherwise false
     */
    @GetMapping(params = "checkNamespaceIdExist=true")
    public Boolean checkNamespaceIdExist(@RequestParam("customNamespaceId") String namespaceId) {
        if (StringUtils.isBlank(namespaceId)) {
            return false;
        }
        return (namespacePersistService.tenantInfoCountByTenantId(namespaceId) > 0);
    }

    /**
     * edit namespace.
     *
     * @param namespace         namespace
     * @param namespaceShowName namespace ShowName
     * @param namespaceDesc     namespace Desc
     * @return whether edit ok
     */
    @PutMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.WRITE)
    public Boolean editNamespace(@RequestParam("namespace") String namespace,
                                 @RequestParam("namespaceShowName") String namespaceShowName,
                                 @RequestParam(value = "namespaceDesc", required = false) String namespaceDesc) {
        // contains illegal chars
        if (!namespaceNameCheckPattern.matcher(namespaceShowName).matches()) {
            return false;
        }
        return namespaceOperationService.editNamespace(namespace, namespaceShowName, namespaceDesc);
    }

    /**
     * del namespace by id.
     *
     * @param namespaceId namespace Id
     * @return whether del ok
     */
    @DeleteMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.WRITE)
    public Boolean deleteNamespace(@RequestParam("namespaceId") String namespaceId) {
        return namespaceOperationService.removeNamespace(namespaceId);
    }

}
