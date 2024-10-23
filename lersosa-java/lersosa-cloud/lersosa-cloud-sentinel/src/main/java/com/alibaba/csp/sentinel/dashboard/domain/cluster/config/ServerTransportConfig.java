/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
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
