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

package com.alibaba.csp.sentinel.dashboard.domain.cluster.request;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ClusterClientConfig;
import lombok.Data;

/**
 * 集群客户端修改请求.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class ClusterClientModifyRequest implements ClusterModifyRequest {

    /**
     * 客户端应用程序名称.
     */
    private String app;

    /**
     * 客户端IP.
     */
    private String ip;

    /**
     * 客户端端口.
     */
    private Integer port;

    /**
     * 集群模式.
     */
    private Integer mode;

    /**
     * 客户端配置.
     */
    private ClusterClientConfig clientConfig;

    /**
     * 获取客户端应用程序名称.
     *
     * @return 客户端应用程序名称
     */
    @Override
    public String getApp() {
        return app;
    }

    /**
     * 设置客户端应用程序名称.
     *
     * @param app 客户端应用程序名称
     * @return 当前对象
     */
    public ClusterClientModifyRequest setApp(String app) {
        this.app = app;
        return this;
    }

    /**
     * 获取客户端IP.
     *
     * @return 客户端IP
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 设置客户端IP.
     *
     * @param ip 客户端IP
     * @return 当前对象
     */
    public ClusterClientModifyRequest setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 获取客户端端口.
     *
     * @return 客户端端口
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * 设置客户端端口.
     *
     * @param port 客户端端口
     * @return 当前对象
     */
    public ClusterClientModifyRequest setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 获取集群模式.
     *
     * @return 集群模式
     */
    @Override
    public Integer getMode() {
        return mode;
    }

    /**
     * 设置集群模式.
     *
     * @param mode 集群模式
     * @return 当前对象
     */
    public ClusterClientModifyRequest setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 设置客户端配置.
     *
     * @param clientConfig 客户端配置
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterClientModifyRequest setClientConfig(
        ClusterClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        return this;
    }
}
