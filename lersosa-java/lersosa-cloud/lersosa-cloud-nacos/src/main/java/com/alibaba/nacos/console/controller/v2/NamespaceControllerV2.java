/*
 * Copyright 1999-2022 Alibaba Group Holding Ltd.
 *
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 命名空间控制器 V2
 *
 * @author dongyafei
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.2.0
 * @since 2024/7/31
 */
@NacosApi
@RestController
@RequestMapping("/v2/console/namespace")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ExtractorManager.Extractor(httpExtractor = ConsoleDefaultHttpParamExtractor.class)
public class NamespaceControllerV2 {

    /**
     * 命名空间 ID 最大长度
     */
    private static final int NAMESPACE_ID_MAX_LENGTH = 128;

    /**
     * 命名空间运维服务
     */
    private final NamespaceOperationService namespaceOperationService;

    /**
     * 命名空间持久化服务
     */
    private final NamespacePersistService namespacePersistService;

    /**
     * 命名空间 ID 正则表达式
     */
    private final Pattern namespaceIdCheckPattern = Pattern.compile("^[\\w-]+");

    /**
     * 命名空间名称正则表达式
     */
    private final Pattern namespaceNameCheckPattern = Pattern.compile("^[^@#$%^&*]+$");

    /**
     * 获取命名空间列表
     *
     * @return 命名空间列表
     * @apiNote 获取命名空间列表
     */
    @GetMapping(value = "/list")
    public Result<List<Namespace>> getNamespaceList() {
        return Result.success(namespaceOperationService.getNamespaceList());
    }

    /**
     * 按命名空间 ID 获取命名空间所有信息
     *
     * @param namespaceId 命名空间 ID
     * @return 命名空间所有信息
     * @throws NacosException 命名空间不存在异常
     * @apiNote 按命名空间 ID 获取命名空间所有信息
     */
    @GetMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.READ,
            signType = SignType.CONSOLE)
    public Result<Namespace> getNamespace(@RequestParam("namespaceId") String namespaceId) throws NacosException {
        return Result.success(namespaceOperationService.getNamespace(namespaceId));
    }

    /**
     * 创建命名空间
     *
     * @param namespaceForm 命名空间表单
     * @return 是否创建确定
     * @throws NacosException 命名空间创建异常
     * @apiNote 创建命名空间
     */
    @PostMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.WRITE,
            signType = SignType.CONSOLE)
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
     * 编辑命名空间
     *
     * @param namespaceForm 命名空间参数
     * @return 是否编辑确定
     * @throws NacosException 命名空间不存在异常
     * @apiNote 编辑命名空间
     */
    @PutMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.WRITE,
            signType = SignType.CONSOLE)
    public Result<Boolean> editNamespace(NamespaceForm namespaceForm) throws NacosException {
        namespaceForm.validate();
        if (!namespaceNameCheckPattern.matcher(namespaceForm.getNamespaceName()).matches()) {
            throw new NacosApiException(HttpStatus.BAD_REQUEST.value(), ErrorCode.ILLEGAL_NAMESPACE,
                    "namespaceName [" + namespaceForm.getNamespaceName() + "] contains illegal char");
        }
        return Result.success(namespaceOperationService.editNamespace(namespaceForm.getNamespaceId(),
                namespaceForm.getNamespaceName(), namespaceForm.getNamespaceDesc()));
    }

    /**
     * 按 ID 删除命名空间
     *
     * @param namespaceId 命名空间 ID
     * @return 是否删除确定
     * @apiNote 按 ID 删除命名空间
     */
    @DeleteMapping
    @Secured(resource = AuthConstants.CONSOLE_RESOURCE_NAME_PREFIX + "namespaces", action = ActionTypes.WRITE,
            signType = SignType.CONSOLE)
    public Result<Boolean> deleteNamespace(@RequestParam("namespaceId") String namespaceId) {
        return Result.success(namespaceOperationService.removeNamespace(namespaceId));
    }
}
