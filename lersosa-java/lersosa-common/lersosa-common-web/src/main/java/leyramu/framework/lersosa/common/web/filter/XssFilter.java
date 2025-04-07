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

package leyramu.framework.lersosa.common.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.web.config.properties.XssProperties;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;

/**
 * 防止XSS攻击的过滤器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/6
 */
public class XssFilter implements Filter {

    /**
     * 初始化.
     *
     * @param filterConfig 过滤器配置
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * 过滤.
     *
     * @param request  请求
     * @param response 响应
     * @param chain    过滤器链
     * @throws IOException      IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeUrl(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    /**
     * 处理排除URL.
     *
     * @param request  请求
     * @param ignoredResponse 响应
     * @return 是否排除
     */
    private boolean handleExcludeUrl(HttpServletRequest request, HttpServletResponse ignoredResponse) {
        String url = request.getServletPath();
        String method = request.getMethod();
        // GET DELETE 不过滤
        if (method == null || HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method)) {
            return true;
        }
        // 每次都获取处理 支持nacos热更配置
        XssProperties properties = SpringUtils.getBean(XssProperties.class);
        String prefix = StringUtils.blankToDefault(request.getHeader("X-Forwarded-Prefix"), "");
        // 从请求头获取gateway转发的服务前缀
        List<String> excludeUrls = properties.getExcludeUrls().stream()
            .filter(x -> StringUtils.startsWith(x, prefix))
            .map(x -> x.replaceFirst(prefix, StringUtils.EMPTY))
            .toList();
        return StringUtils.matches(url, excludeUrls);
    }

    /**
     * 销毁.
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
