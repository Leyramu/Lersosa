/*
 * Copyright 1999-2023 Alibaba Group Holding Ltd.
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

package com.alibaba.nacos.console.paramcheck;

import com.alibaba.nacos.common.paramcheck.ParamInfo;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.core.paramcheck.AbstractHttpParamExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制台默认 http 参数提取器
 *
 * @author zhuoguang
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.2.0
 * @since 2024/7/31
 */
public class ConsoleDefaultHttpParamExtractor extends AbstractHttpParamExtractor {

    /**
     * 提取参数
     *
     * @param request 请求
     * @return 参数列表
     */
    @Override
    public List<ParamInfo> extractParam(HttpServletRequest request) {
        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setNamespaceId(getAliasNamespaceId(request));
        paramInfo.setNamespaceShowName(getAliasNamespaceShowName(request));
        ArrayList<ParamInfo> paramInfos = new ArrayList<>();
        paramInfos.add(paramInfo);
        return paramInfos;
    }

    /**
     * 获取命名空间ID
     *
     * @param request 请求
     * @return 命名空间ID
     */
    private String getAliasNamespaceId(HttpServletRequest request) {
        String namespaceId = request.getParameter("namespaceId");
        if (StringUtils.isBlank(namespaceId)) {
            namespaceId = request.getParameter("customNamespaceId");
        }
        return namespaceId;
    }

    /**
     * 获取命名空间名称
     *
     * @param request 请求
     * @return 命名空间名称
     */
    private String getAliasNamespaceShowName(HttpServletRequest request) {
        return request.getParameter("namespaceName");
    }
}
