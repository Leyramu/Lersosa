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

package leyramu.framework.lersosa.common.ssl.core;

import leyramu.framework.lersosa.common.ssl.annotation.EnableTlsConfig;
import leyramu.framework.lersosa.common.ssl.constant.CustomTlsConstant;
import leyramu.framework.lersosa.common.ssl.utils.ObtainPathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 自定义 SpringApplication 启动程序.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/12/10
 */
@Slf4j
@EnableTlsConfig(enable = false)
public class CustomSpringApplication extends SpringApplication {

    /**
     * 模式类型常量.
     */
    public static final String[] MODE_TYPE = new String[]{"server", "client"};

    /**
     * 构造方法.
     *
     * @param primarySources 主源
     */
    public CustomSpringApplication(Class<?>... primarySources) {
        super(primarySources);
        processTlsConfig(primarySources);
    }

    /**
     * 运行.
     *
     * @param primarySource 主源
     * @param args          参数
     * @return 配置
     */
    public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
        return new CustomSpringApplication(primarySource).run(args);
    }

    /**
     * 运行.
     *
     * @param primarySources 主源
     * @param args           参数
     * @return 配置
     */
    public static ConfigurableApplicationContext run(Class<?>[] primarySources, String... args) {
        return new CustomSpringApplication(primarySources).run(args);
    }

    /**
     * 处理 TLS 配置.
     *
     * @param primarySources 主源
     */
    private static void processTlsConfig(Class<?>... primarySources) {
        EnableTlsConfig annotation = CustomSpringApplication.class.getAnnotation(EnableTlsConfig.class);
        if (annotation.enable()) {
            for (Class<?> primarySource : primarySources) {
                EnableTlsConfig enableTlsConfig = primarySource.getAnnotation(EnableTlsConfig.class);
                if (enableTlsConfig != null) {
                    String type = enableTlsConfig.type();
                    boolean enable = enableTlsConfig.enable();
                    boolean auth = enableTlsConfig.auth();
                    boolean compatibility = enableTlsConfig.compatibility();
                    String certPath = enableTlsConfig.certPath();
                    String clientCertPath = ObtainPathUtil.getRootPath() + ObtainPathUtil.getPath(enableTlsConfig.clientCertPath());
                    String privateKey = enableTlsConfig.privateKey();
                    String privateKeyPassword = enableTlsConfig.privateKeyPassword();
                    String trustCert = enableTlsConfig.trustCert();

                    if (MODE_TYPE[0].equalsIgnoreCase(type)) {
                        System.setProperty(CustomTlsConstant.RPC_SERVER_TLS_ENABLE, Boolean.toString(enable));
                        System.setProperty(CustomTlsConstant.RPC_SERVER_TLS_AUTH, Boolean.toString(auth));
                        System.setProperty(CustomTlsConstant.RPC_SERVER_TLS_COMPATIBILITY, Boolean.toString(compatibility));
                        System.setProperty(CustomTlsConstant.RPC_SERVER_TLS_CERT_PATH, certPath);
                        System.setProperty(CustomTlsConstant.RPC_SERVER_TLS_PRIVATE_KEY, privateKey);
                        System.setProperty(CustomTlsConstant.RPC_SERVER_TLS_PRIVATE_KEY_PASSWORD, privateKeyPassword);
                        System.setProperty(CustomTlsConstant.RPC_SERVER_TLS_TRUST_CERT, trustCert);

                        log.info("服务端 TLS 配置加载完毕");
                    } else if (MODE_TYPE[1].equalsIgnoreCase(type)) {
                        System.setProperty(CustomTlsConstant.TLS_ENABLE, Boolean.toString(enable));
                        System.setProperty(CustomTlsConstant.CLIENT_AUTH, Boolean.toString(auth));
                        System.setProperty(CustomTlsConstant.CLIENT_CERT_PATH, clientCertPath);
                        System.setProperty(CustomTlsConstant.RPC_CLIENT_TLS_ENABLE, Boolean.toString(enable));
                        System.setProperty(CustomTlsConstant.RPC_CLIENT_TLS_AUTH, Boolean.toString(auth));
                        System.setProperty(CustomTlsConstant.RPC_CLIENT_TLS_CERT_PATH, certPath);
                        System.setProperty(CustomTlsConstant.RPC_CLIENT_TLS_PRIVATE_KEY, privateKey);
                        System.setProperty(CustomTlsConstant.RPC_CLIENT_TLS_PRIVATE_KEY_PASSWORD, privateKeyPassword);
                        System.setProperty(CustomTlsConstant.RPC_CLIENT_TLS_TRUST_CERT, trustCert);

                        log.info("客户端 TLS 配置加载完毕");
                    } else {
                        log.error("不支持 TLS 配置的类型");
                        throw new IllegalArgumentException("The type of TLS configuration is not supported");
                    }
                }
            }
        } else {
            log.info("未启用 TLS 配置");
        }
    }
}
