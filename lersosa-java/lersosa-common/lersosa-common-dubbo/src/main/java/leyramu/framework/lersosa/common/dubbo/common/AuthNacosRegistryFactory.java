///*
// * Copyright (c) 2024 Leyramu Group. All rights reserved.
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
// *
// * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
// *
// * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
// *
// * By using this project, users acknowledge and agree to abide by these terms and conditions.
// */
//
//package leyramu.framework.lersosa.common.dubbo.common;
//
//import com.alibaba.nacos.api.NacosFactory;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.alibaba.nacos.api.naming.NamingService;
//import org.apache.dubbo.common.URL;
//import org.apache.dubbo.registry.Registry;
//import org.apache.dubbo.registry.nacos.NacosRegistry;
//import org.apache.dubbo.registry.support.AbstractRegistryFactory;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Properties;
//
//import static com.alibaba.nacos.api.PropertyKeyConst.*;
//import static com.alibaba.nacos.client.naming.utils.UtilAndComs.NACOS_NAMING_LOG_NAME;
//import static org.apache.dubbo.common.constants.RemotingConstants.BACKUP_KEY;
//
///**
// * AuthNacosRegistryFactory 类
// *
// * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
// * @version 1.0.0
// * @since 2024/12/11
// */
//public class AuthNacosRegistryFactory extends AbstractRegistryFactory {
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Override
//    protected Registry createRegistry(URL url) {
//        return new NacosRegistry(url, buildNamingService(url));
//    }
//
//    private AuthNacosNamingServiceWrapper buildNamingService(URL url) {
//        Properties nacosProperties = buildNacosProperties(url);
//        NamingService namingService;
//        try {
//            namingService = NacosFactory.createNamingService(nacosProperties);
//        } catch (NacosException e) {
//            if (logger.isErrorEnabled()) {
//                logger.error(e.getErrMsg(), e);
//            }
//            throw new IllegalStateException(e);
//        }
//        return new AuthNacosNamingServiceWrapper(new AuthNacosConnectionManager(namingService));
//    }
//
//    @NotNull
//    private Properties buildNacosProperties(URL url) {
//        Properties properties = new Properties();
//        setServerAddr(url, properties);
//        setProperties(url, properties);
//        return properties;
//    }
//
//    private void setServerAddr(URL url, Properties properties) {
//        StringBuilder serverAddrBuilder =
//            new StringBuilder(url.getHost())
//                .append(":")
//                .append(url.getPort());
//
//        // Append backup parameter as other servers
//        String backup = url.getParameter(BACKUP_KEY);
//        if (backup != null) {
//            serverAddrBuilder.append(",").append(backup);
//        }
//
//        String serverAddr = serverAddrBuilder.toString();
//        properties.put(SERVER_ADDR, serverAddr);
//    }
//
//    private void setProperties(URL url, Properties properties) {
//        putPropertyIfAbsent(url, properties, NAMESPACE);
//        putPropertyIfAbsent(url, properties, NACOS_NAMING_LOG_NAME);
//        putPropertyIfAbsent(url, properties, ENDPOINT);
//        putPropertyIfAbsent(url, properties, ACCESS_KEY);
//        putPropertyIfAbsent(url, properties, SECRET_KEY);
//        putPropertyIfAbsent(url, properties, CLUSTER_NAME);
//        putPropertyIfAbsent(url, properties, USERNAME);
//        putPropertyIfAbsent(url, properties, PASSWORD);
//        logger.info("[FY]=>auth-nacos工厂注册设置，支持nacos开启认证，下面为nacos开启认证后dubbo地址配置方式");
//        logger.info("[FY]=>dubbo.registry.address=auth-nacos://${nacos.ip}?username=${nacos.user}&password=${nacos.pwd}");
//    }
//
//    private void putPropertyIfAbsent(URL url, Properties properties, String propertyName) {
//        String propertyValue = url.getParameter(propertyName);
//        if (propertyValue != null && !propertyValue.trim().isEmpty()) {
//            properties.setProperty(propertyName, propertyValue);
//        }
//    }
//}
