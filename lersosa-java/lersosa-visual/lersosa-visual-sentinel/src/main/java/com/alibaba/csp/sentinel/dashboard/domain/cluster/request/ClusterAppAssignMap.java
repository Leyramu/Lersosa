/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster.request;

import lombok.Getter;

import java.util.Set;

/**
 * 群集应用分配请求实体
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterAppAssignMap {

    /**
     * 集群机器ID
     */
    private String machineId;

    /**
     * 集群机器IP
     */
    private String ip;

    /**
     * 集群机器端口
     */
    private Integer port;

    /**
     * 所属应用
     */
    private Boolean belongToApp;

    /**
     * 客户端集合
     */
    private Set<String> clientSet;

    /**
     * 命名空间集合
     */
    private Set<String> namespaceSet;

    /**
     * 最大允许QPS
     */
    private Double maxAllowedQps;

    /**
     * 设置机器ID
     *
     * @param machineId 机器ID
     * @return 集群应用程序分配映射
     */
    public ClusterAppAssignMap setMachineId(String machineId) {
        this.machineId = machineId;
        return this;
    }

    /**
     * 设置IP
     *
     * @param ip IP
     * @return 集群应用程序分配映射
     */
    public ClusterAppAssignMap setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 设置端口
     *
     * @param port 端口
     * @return 集群应用程序分配映射
     */
    public ClusterAppAssignMap setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 设置客户端集合
     *
     * @param clientSet 客户端集合
     * @return 集群应用程序分配映射
     */
    public ClusterAppAssignMap setClientSet(Set<String> clientSet) {
        this.clientSet = clientSet;
        return this;
    }

    /**
     * 设置命名空间集合
     *
     * @param namespaceSet 命名空间集合
     * @return 集群应用程序分配映射
     */
    public ClusterAppAssignMap setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }

    /**
     * 设置是否属于应用
     *
     * @param belongToApp 是否属于应用
     * @return 集群应用程序分配映射
     */
    public ClusterAppAssignMap setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }

    /**
     * 设置最大允许QPS
     *
     * @param maxAllowedQps 最大允许QPS
     * @return 集群应用程序分配映射
     */
    public ClusterAppAssignMap setMaxAllowedQps(Double maxAllowedQps) {
        this.maxAllowedQps = maxAllowedQps;
        return this;
    }

    /**
     * 重写toString方法
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "ClusterAppAssignMap{" +
               "machineId='" + machineId + '\'' +
               ", ip='" + ip + '\'' +
               ", port=" + port +
               ", belongToApp=" + belongToApp +
               ", clientSet=" + clientSet +
               ", namespaceSet=" + namespaceSet +
               ", maxAllowedQps=" + maxAllowedQps +
               '}';
    }
}
