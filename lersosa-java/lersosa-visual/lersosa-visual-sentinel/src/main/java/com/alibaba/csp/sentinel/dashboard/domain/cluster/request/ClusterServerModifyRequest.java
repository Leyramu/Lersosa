/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster.request;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerTransportConfig;
import lombok.Data;

import java.util.Set;

/**
 * 集群服务器请求.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class ClusterServerModifyRequest implements ClusterModifyRequest {

    /**
     * 应用名称.
     */
    private String app;

    /**
     * IP地址.
     */
    private String ip;

    /**
     * 端口号.
     */
    private Integer port;

    /**
     * 运行模式.
     */
    private Integer mode;

    /**
     * 流控配置.
     */
    private ServerFlowConfig flowConfig;

    /**
     * 传输配置.
     */
    private ServerTransportConfig transportConfig;

    /**
     * 命名空间集合.
     */
    private Set<String> namespaceSet;

    /**
     * 获取应用名称.
     *
     * @return 应用名称
     */
    @Override
    public String getApp() {
        return app;
    }

    /**
     * 设置应用名称.
     *
     * @param app 应用名称
     * @return 当前对象
     */
    public ClusterServerModifyRequest setApp(String app) {
        this.app = app;
        return this;
    }

    /**
     * 获取IP地址.
     *
     * @return IP地址
     */
    @Override
    public String getIp() {
        return ip;
    }

    /**
     * 设置IP地址.
     *
     * @param ip IP地址
     * @return 当前对象
     */
    public ClusterServerModifyRequest setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 获取端口号.
     *
     * @return 端口号
     */
    @Override
    public Integer getPort() {
        return port;
    }

    /**
     * 设置端口号.
     *
     * @param port 端口号
     * @return 当前对象
     */
    public ClusterServerModifyRequest setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 获取运行模式.
     *
     * @return 运行模式
     */
    @Override
    public Integer getMode() {
        return mode;
    }

    /**
     * 设置运行模式.
     *
     * @param mode 运行模式
     * @return 当前对象
     */
    public ClusterServerModifyRequest setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 设置流控配置.
     *
     * @param flowConfig 流控配置
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterServerModifyRequest setFlowConfig(
        ServerFlowConfig flowConfig) {
        this.flowConfig = flowConfig;
        return this;
    }

    /**
     * 设置传输配置.
     *
     * @param transportConfig 传输配置
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterServerModifyRequest setTransportConfig(
        ServerTransportConfig transportConfig) {
        this.transportConfig = transportConfig;
        return this;
    }

    /**
     * 设置命名空间集合.
     *
     * @param namespaceSet 命名空间集合
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterServerModifyRequest setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }

    /**
     * 获取当前对象字符串表示形式.
     *
     * @return 当前对象字符串表示形式
     */
    @Override
    public String toString() {
        return "ClusterServerModifyRequest{" +
            "app='" + app + '\'' +
            ", ip='" + ip + '\'' +
            ", port=" + port +
            ", mode=" + mode +
            ", flowConfig=" + flowConfig +
            ", transportConfig=" + transportConfig +
            ", namespaceSet=" + namespaceSet +
            '}';
    }
}
