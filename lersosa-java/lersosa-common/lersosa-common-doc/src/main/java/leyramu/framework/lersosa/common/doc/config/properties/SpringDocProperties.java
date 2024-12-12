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

package leyramu.framework.lersosa.common.doc.config.properties;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * swagger 配置属性.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ConfigurationProperties(prefix = "springdoc")
public class SpringDocProperties {

    /**
     * 网关
     */
    private String gatewayUrl;

    /**
     * 文档基本信息.
     */
    @NestedConfigurationProperty
    private InfoProperties info = new InfoProperties();

    /**
     * 扩展文档地址.
     */
    @NestedConfigurationProperty
    private ExternalDocumentation externalDocs;

    /**
     * 标签.
     */
    private List<Tag> tags = null;

    /**
     * 路径.
     */
    @NestedConfigurationProperty
    private Paths paths = null;

    /**
     * 组件.
     */
    @NestedConfigurationProperty
    private Components components = null;

    /**
     * 文档的基础属性信息.
     */
    @Data
    public static class InfoProperties {

        /**
         * 标题.
         */
        private String title = null;

        /**
         * 描述.
         */
        private String description = null;

        /**
         * 联系人信息.
         */
        @NestedConfigurationProperty
        private Contact contact = null;

        /**
         * 许可证.
         */
        @NestedConfigurationProperty
        private License license = null;

        /**
         * 版本.
         */
        private String version = null;
    }
}
