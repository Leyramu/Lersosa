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

package com.alibaba.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.dashboard.auth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证配置.
 *
 * @author sentinel
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthConfiguration {

    /**
     * 注入配置属性，用于控制认证服务的行为.
     */
    private final AuthProperties authProperties;

    /**
     * 根据配置提供适当的认证服务实现.
     *
     * @return AuthService<HttpServletRequest>的实现，具体取决于认证功能是否已启用
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthService<HttpServletRequest> httpServletRequestAuthService() {
        if (this.authProperties.isEnabled()) {
            return new SimpleWebAuthServiceImpl();
        }
        return new FakeAuthServiceImpl();
    }

    /**
     * 创建并配置登录认证过滤器.
     *
     * @param httpServletRequestAuthService 认证服务，用于处理登录请求
     * @return 初始化并配置好的登录认证过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public LoginAuthenticationFilter loginAuthenticationFilter(AuthService<HttpServletRequest> httpServletRequestAuthService) {
        return new DefaultLoginAuthenticationFilter(httpServletRequestAuthService);
    }

    /**
     * 创建并配置授权拦截器.
     *
     * @param httpServletRequestAuthService 认证服务，用于请求的授权检查
     * @return 初始化并配置好的授权拦截器
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationInterceptor authorizationInterceptor(AuthService<HttpServletRequest> httpServletRequestAuthService) {
        return new DefaultAuthorizationInterceptor(httpServletRequestAuthService);
    }
}
