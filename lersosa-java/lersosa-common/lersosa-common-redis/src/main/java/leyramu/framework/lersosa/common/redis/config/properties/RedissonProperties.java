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

package leyramu.framework.lersosa.common.redis.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson 配置属性.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {

    /**
     * redis缓存key前缀.
     */
    private String keyPrefix;

    /**
     * 线程池数量,默认值 = 当前处理核数量 * 2.
     */
    private int threads;

    /**
     * Netty线程池数量,默认值 = 当前处理核数量 * 2.
     */
    private int nettyThreads;

    /**
     * 单机服务配置.
     */
    private SingleServerConfig singleServerConfig;

    /**
     * 集群服务配置.
     */
    private ClusterServersConfig clusterServersConfig;

    @Data
    @NoArgsConstructor
    public static class SingleServerConfig {

        /**
         * 客户端名称.
         */
        private String clientName;

        /**
         * 最小空闲连接数.
         */
        private int connectionMinimumIdleSize;

        /**
         * 连接池大小.
         */
        private int connectionPoolSize;

        /**
         * 连接空闲超时，单位：毫秒.
         */
        private int idleConnectionTimeout;

        /**
         * 命令等待超时，单位：毫秒.
         */
        private int timeout;

        /**
         * 发布和订阅连接池大小.
         */
        private int subscriptionConnectionPoolSize;

    }

    @Data
    @NoArgsConstructor
    public static class ClusterServersConfig {

        /**
         * 客户端名称.
         */
        private String clientName;

        /**
         * master最小空闲连接数.
         */
        private int masterConnectionMinimumIdleSize;

        /**
         * master连接池大小.
         */
        private int masterConnectionPoolSize;

        /**
         * slave最小空闲连接数.
         */
        private int slaveConnectionMinimumIdleSize;

        /**
         * slave连接池大小.
         */
        private int slaveConnectionPoolSize;

        /**
         * 连接空闲超时，单位：毫秒.
         */
        private int idleConnectionTimeout;

        /**
         * 命令等待超时，单位：毫秒.
         */
        private int timeout;

        /**
         * 发布和订阅连接池大小.
         */
        private int subscriptionConnectionPoolSize;

        /**
         * 读取模式.
         */
        private ReadMode readMode;

        /**
         * 订阅模式.
         */
        private SubscriptionMode subscriptionMode;
    }
}
