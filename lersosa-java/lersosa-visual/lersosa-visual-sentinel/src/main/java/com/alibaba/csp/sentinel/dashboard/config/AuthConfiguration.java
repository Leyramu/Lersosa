/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.dashboard.auth.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthConfiguration {

    private final AuthProperties authProperties;

    public AuthConfiguration(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthService<HttpServletRequest> httpServletRequestAuthService() {
        if (this.authProperties.isEnabled()) {
            return new SimpleWebAuthServiceImpl();
        }
        return new FakeAuthServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginAuthenticationFilter loginAuthenticationFilter(AuthService<HttpServletRequest> httpServletRequestAuthService) {
        return new DefaultLoginAuthenticationFilter(httpServletRequestAuthService);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationInterceptor authorizationInterceptor(AuthService<HttpServletRequest> httpServletRequestAuthService) {
        return new DefaultAuthorizationInterceptor(httpServletRequestAuthService);
    }

}
