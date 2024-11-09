/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.auth;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>The Servlet filter for authentication.</p>
 *
 * <p>Note: some urls are excluded as they needn't auth, such as:</p>
 * <ul>
 * <li>index url: {@code /}</li>
 * <li>authentication request url: {@code /login}, {@code /logout}</li>
 * <li>machine registry: {@code /registry/machine}</li>
 * <li>static resources</li>
 * </ul>
 * <p>
 * The excluded urls and urlSuffixes could be configured in {@code application.properties} file.
 *
 * @author cdfive
 * @since 1.6.0
 */
public class DefaultLoginAuthenticationFilter implements LoginAuthenticationFilter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final String URL_SUFFIX_DOT = ".";
    /**
     * Authentication using AuthService interface.
     */
    private final AuthService<HttpServletRequest> authService;
    /**
     * Some urls which needn't auth, such as /auth/login, /registry/machine and so on.
     */
    @Value("#{'${auth.filter.exclude-urls}'.split(',')}")
    private List<String> authFilterExcludeUrls;
    /**
     * Some urls with suffixes which needn't auth, such as htm, html, js and so on.
     */
    @Value("#{'${auth.filter.exclude-url-suffixes}'.split(',')}")
    private List<String> authFilterExcludeUrlSuffixes;

    public DefaultLoginAuthenticationFilter(AuthService<HttpServletRequest> authService) {
        this.authService = authService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String servletPath = httpRequest.getServletPath();

        // Exclude the urls which needn't auth
        boolean authFilterExcludeMatch = authFilterExcludeUrls.stream()
            .anyMatch(authFilterExcludeUrl -> PATH_MATCHER.match(authFilterExcludeUrl, servletPath));
        if (authFilterExcludeMatch) {
            chain.doFilter(request, response);
            return;
        }

        // Exclude the urls with suffixes which needn't auth
        for (String authFilterExcludeUrlSuffix : authFilterExcludeUrlSuffixes) {
            if (StringUtils.isBlank(authFilterExcludeUrlSuffix)) {
                continue;
            }

            // Add . for url suffix so that we needn't add . in property file
            if (!authFilterExcludeUrlSuffix.startsWith(URL_SUFFIX_DOT)) {
                authFilterExcludeUrlSuffix = URL_SUFFIX_DOT + authFilterExcludeUrlSuffix;
            }

            if (servletPath.endsWith(authFilterExcludeUrlSuffix)) {
                chain.doFilter(request, response);
                return;
            }
        }

        AuthService.AuthUser authUser = authService.getAuthUser(httpRequest);

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (authUser == null) {
            // If auth fail, set response status code to 401
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
