/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
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

package com.alibaba.csp.sentinel.dashboard.domain.cluster.config;

import lombok.Getter;

/**
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ServerTransportConfig {

    /**
     * 默认端口
     */
    public static final int DEFAULT_PORT = 18730;

    /**
     * 默认空闲时间
     */
    public static final int DEFAULT_IDLE_SECONDS = 600;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 空闲时间
     */
    private Integer idleSeconds;

    /**
     * 默认构造函数
     */
    public ServerTransportConfig() {
        this(DEFAULT_PORT, DEFAULT_IDLE_SECONDS);
    }

    /**
     * 构造函数
     *
     * @param port        端口
     * @param idleSeconds 空闲时间
     */
    public ServerTransportConfig(Integer port, Integer idleSeconds) {
        this.port = port;
        this.idleSeconds = idleSeconds;
    }

    /**
     * 设置端口
     *
     * @param port 端口
     * @return this
     */
    public ServerTransportConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 设置空闲时间
     *
     * @param idleSeconds 空闲时间
     * @return this
     */
    public ServerTransportConfig setIdleSeconds(Integer idleSeconds) {
        this.idleSeconds = idleSeconds;
        return this;
    }

    /**
     * 转换为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "ServerTransportConfig{" +
               "port=" + port +
               ", idleSeconds=" + idleSeconds +
               '}';
    }
}
