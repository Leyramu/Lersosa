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

package com.alibaba.csp.sentinel.dashboard.domain.cluster.state;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.ConnectionGroupVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerTransportConfig;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 集群服务器状态视图对象.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class ClusterServerStateVO {

    /**
     * 应用程序名称.
     */
    private String appName;

    /**
     * 传输配置.
     */
    private ServerTransportConfig transport;

    /**
     * 流控配置.
     */
    private ServerFlowConfig flow;

    /**
     * 命名空间集合.
     */
    private Set<String> namespaceSet;

    /**
     * 端口号.
     */
    private Integer port;

    /**
     * 连接组.
     */
    private List<ConnectionGroupVO> connection;

    /**
     * 请求限制数据.
     */
    private List<ClusterRequestLimitVO> requestLimitData;

    /**
     * 是否为内嵌模式.
     */
    private Boolean embedded;

    /**
     * 设置应用程序名称.
     *
     * @param appName 应用程序名称
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterServerStateVO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    /**
     * 设置传输配置.
     *
     * @param transport 传输配置
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterServerStateVO setTransport(ServerTransportConfig transport) {
        this.transport = transport;
        return this;
    }

    /**
     * 设置流控配置.
     *
     * @param flow 流控配置
     * @return 当前对象
     */
    public ClusterServerStateVO setFlow(ServerFlowConfig flow) {
        this.flow = flow;
        return this;
    }

    /**
     * 设置命名空间集合.
     *
     * @param namespaceSet 命名空间集合
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterServerStateVO setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }

    /**
     * 设置端口号.
     *
     * @param port 端口号
     * @return 当前对象
     */
    public ClusterServerStateVO setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 设置连接组.
     *
     * @param connection 连接组
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterServerStateVO setConnection(List<ConnectionGroupVO> connection) {
        this.connection = connection;
        return this;
    }

    /**
     * 设置请求限制数据.
     *
     * @param requestLimitData 请求限制数据
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterServerStateVO setRequestLimitData(List<ClusterRequestLimitVO> requestLimitData) {
        this.requestLimitData = requestLimitData;
        return this;
    }

    /**
     * 设置是否为内嵌模式.
     *
     * @param embedded 是否为内嵌模式
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterServerStateVO setEmbedded(Boolean embedded) {
        this.embedded = embedded;
        return this;
    }

    /**
     * 重写toString方法.
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "ClusterServerStateVO{" +
            "appName='" + appName + '\'' +
            ", transport=" + transport +
            ", flow=" + flow +
            ", namespaceSet=" + namespaceSet +
            ", port=" + port +
            ", connection=" + connection +
            ", requestLimitData=" + requestLimitData +
            ", embedded=" + embedded +
            '}';
    }
}
