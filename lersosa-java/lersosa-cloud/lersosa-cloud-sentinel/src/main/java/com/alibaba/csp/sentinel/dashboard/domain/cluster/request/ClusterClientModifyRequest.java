/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster.request;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ClusterClientConfig;
import lombok.Getter;

/**
 * 集群配置修改请求
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterClientModifyRequest implements ClusterModifyRequest {

    /**
     * 应用名称
     */
    private String app;

    /**
     * 应用IP
     */
    private String ip;

    /**
     * 应用端口
     */
    private Integer port;

    /**
     * 集群模式
     */
    private Integer mode;

    /**
     * 客户端配置
     */
    private ClusterClientConfig clientConfig;

    /**
     * 获取应用名称
     *
     * @return 应用名称
     */
    @Override
    public String getApp() {
        return app;
    }

    /**
     * 设置应用名称
     *
     * @param app 应用名称
     * @return 当前对象
     */
    public ClusterClientModifyRequest setApp(String app) {
        this.app = app;
        return this;
    }

    /**
     * 获取应用IP
     *
     * @return 应用IP
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 设置应用IP
     *
     * @param ip 应用IP
     * @return 当前对象
     */
    public ClusterClientModifyRequest setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 获取应用端口
     *
     * @return 应用端口
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * 设置应用端口
     *
     * @param port 应用端口
     * @return 当前对象
     */
    public ClusterClientModifyRequest setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 获取集群模式
     *
     * @return 集群模式
     */
    @Override
    public Integer getMode() {
        return mode;
    }

    /**
     * 设置集群模式
     *
     * @param mode 集群模式
     * @return 当前对象
     */
    public ClusterClientModifyRequest setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 设置客户端配置
     *
     * @return 客户端配置
     */
    public ClusterClientModifyRequest setClientConfig(
            ClusterClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        return this;
    }
}
