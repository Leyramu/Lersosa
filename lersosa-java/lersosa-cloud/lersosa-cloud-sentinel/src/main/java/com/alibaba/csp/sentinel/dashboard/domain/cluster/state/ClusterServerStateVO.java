/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster.state;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.ConnectionGroupVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ServerTransportConfig;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * 集群服务器状态信息
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterServerStateVO {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 服务器传输配置
     */
    private ServerTransportConfig transport;

    /**
     * 服务器流量控制配置
     */
    private ServerFlowConfig flow;

    /**
     * 命名空间集合
     */
    private Set<String> namespaceSet;

    /**
     * 服务器端口
     */
    private Integer port;

    /**
     * 连接分组列表
     */
    private List<ConnectionGroupVO> connection;

    /**
     * 请求限制数据列表
     */
    private List<ClusterRequestLimitVO> requestLimitData;

    /**
     * 是否为嵌入式部署
     */
    private Boolean embedded;

    /**
     * 设置应用名称
     *
     * @param appName 应用名称
     * @return 当前对象，以便进行链式调用
     */
    public ClusterServerStateVO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    /**
     * 设置服务器传输配置
     *
     * @param transport 传输配置对象
     * @return 当前对象，以便进行链式调用
     */
    public ClusterServerStateVO setTransport(ServerTransportConfig transport) {
        this.transport = transport;
        return this;
    }

    /**
     * 设置服务器流量控制配置
     *
     * @param flow 流量控制配置对象
     * @return 当前对象，以便进行链式调用
     */
    public ClusterServerStateVO setFlow(ServerFlowConfig flow) {
        this.flow = flow;
        return this;
    }

    /**
     * 设置命名空间集合
     *
     * @param namespaceSet 命名空间字符串集合
     * @return 当前对象，以便进行链式调用
     */
    public ClusterServerStateVO setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }

    /**
     * 设置服务器端口
     *
     * @param port 端口号
     * @return 当前对象，以便进行链式调用
     */
    public ClusterServerStateVO setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 设置连接分组列表
     *
     * @param connection 连接分组列表
     * @return 当前对象，以便进行链式调用
     */
    public ClusterServerStateVO setConnection(List<ConnectionGroupVO> connection) {
        this.connection = connection;
        return this;
    }

    /**
     * 设置集群请求限制数据列表
     *
     * @param requestLimitData 请求限制数据列表
     * @return 当前对象，以便进行链式调用
     */
    public ClusterServerStateVO setRequestLimitData(List<ClusterRequestLimitVO> requestLimitData) {
        this.requestLimitData = requestLimitData;
        return this;
    }

    /**
     * 设置是否嵌入式部署
     *
     * @param embedded 是否嵌入式部署的标志
     * @return 当前对象，以便进行链式调用
     */
    public ClusterServerStateVO setEmbedded(Boolean embedded) {
        this.embedded = embedded;
        return this;
    }

    /**
     * 重写toString方法，用于打印对象的字符串表示形式，方便日志记录和调试
     *
     * @return 对象的字符串表示形式
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
