/*
 * Copyright (c) 2025 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed toDomainObject in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related toDomainObject licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited toDomainObject the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree toDomainObject abide by these terms and conditions.
 */

package leyramu.framework.lersosa.auth.command.query;

import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.collection.CollUtil;
import jakarta.servlet.http.HttpServletRequest;
import leyramu.framework.lersosa.auth.ability.AuthDomainService;
import leyramu.framework.lersosa.auth.convert.TenantConvertor;
import leyramu.framework.lersosa.auth.dto.co.TenantCo;
import leyramu.framework.lersosa.auth.dto.co.TenantV;
import leyramu.framework.lersosa.common.core.domain.Result;
import leyramu.framework.lersosa.common.core.utils.StreamUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.satoken.utils.LoginHelper;
import leyramu.framework.lersosa.common.tenant.helper.TenantHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

/**
 * 租户查询执行器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/2
 */
@Component
@RequiredArgsConstructor
public class TenantGetQryExe {

    /**
     * 认证领域服务.
     */
    private final AuthDomainService authDomainService;

    /**
     * 执行查询.
     *
     * @param request 请求对象
     * @return {@link Result}<{@link TenantCo}>
     * @throws Exception 异常
     */
    public Result<TenantCo> execute(HttpServletRequest request) throws Exception {
        // 返回对象
        TenantCo result = new TenantCo();
        boolean enable = TenantHelper.isEnable();
        result.setTenantEnabled(enable);
        // 如果未开启租户这直接返回
        if (!enable) {
            return Result.ok(result);
        }

        List<TenantV> voList = authDomainService.queryList().parallelStream().map(TenantConvertor::toClientObject).toList();
        try {
            // 如果只超管返回所有租户
            if (LoginHelper.isSuperAdmin()) {
                result.setVoList(voList);
                return Result.ok(result);
            }
        } catch (NotLoginException ignored) {
        }

        // 获取域名
        String host;
        String referer = request.getHeader("referer");
        if (StringUtils.isNotBlank(referer)) {
            // 这里从referer中取值是为了本地使用hosts添加虚拟域名，方便本地环境调试
            host = referer.split("//")[1].split("/")[0];
        } else {
            host = new URI(request.getRequestURL().toString()).getHost();
        }
        // 根据域名进行筛选
        List<TenantV> list = StreamUtils.filter(voList, vo ->
            StringUtils.equals(vo.domain(), host));
        result.setVoList(CollUtil.isNotEmpty(list) ? list : voList);
        return Result.ok(result);
    }
}
