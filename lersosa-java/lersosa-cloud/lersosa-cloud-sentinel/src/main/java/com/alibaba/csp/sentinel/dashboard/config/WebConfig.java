/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.dashboard.auth.AuthorizationInterceptor;
import com.alibaba.csp.sentinel.dashboard.auth.LoginAuthenticationFilter;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Spring MVC 配置
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.5.0
 * @since 2024/9/3
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    /**
     * 日志记录器
     */
    private final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    /**
     * 登录过滤器
     */
    private final LoginAuthenticationFilter loginAuthenticationFilter;

    /**
     * 授权拦截器
     */
    private final AuthorizationInterceptor authorizationInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**");
    }

    /**
     * 添加静态资源映射
     *
     * @param registry 静态资源注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
    }

    /**
     * 添加视图控制器
     *
     * @param registry 视图控制器注册表
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.htm");
    }

    /**
     * 将 {@link CommonFilter} 添加到服务器，这是使用 Sentinel for Web 应用程序的最简单方法
     *
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<Filter> sentinelFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CommonFilter());
        registration.addUrlPatterns("/*");
        registration.setName("sentinelFilter");
        registration.setOrder(1);
        registration.addInitParameter(CommonFilter.WEB_CONTEXT_UNIFY, "true");

        logger.info("Sentinel servlet CommonFilter registered");

        return registration;
    }

    /**
     * 初始化 URL 清理器
     */
    @PostConstruct
    public void doInit() {
        Set<String> suffixSet = new HashSet<>(Arrays.asList(".js", ".css", ".html", ".ico", ".txt",
                ".woff", ".woff2"));
        WebCallbackManager.setUrlCleaner(url -> {
            if (StringUtil.isEmpty(url)) {
                return url;
            }
            if (suffixSet.stream().anyMatch(url::endsWith)) {
                return null;
            }
            return url;
        });
    }

    /**
     * 登录过滤器注册
     *
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<Filter> authenticationFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(loginAuthenticationFilter);
        registration.addUrlPatterns("/*");
        registration.setName("authenticationFilter");
        registration.setOrder(0);
        return registration;
    }
}
