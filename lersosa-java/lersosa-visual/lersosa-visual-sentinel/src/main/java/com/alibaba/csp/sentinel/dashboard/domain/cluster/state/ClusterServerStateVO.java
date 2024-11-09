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

import java.util.List;
import java.util.Set;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public class ClusterServerStateVO {

    private String appName;

    private ServerTransportConfig transport;
    private ServerFlowConfig flow;
    private Set<String> namespaceSet;

    private Integer port;

    private List<ConnectionGroupVO> connection;
    private List<ClusterRequestLimitVO> requestLimitData;

    private Boolean embedded;

    public String getAppName() {
        return appName;
    }

    public ClusterServerStateVO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public ServerTransportConfig getTransport() {
        return transport;
    }

    public ClusterServerStateVO setTransport(ServerTransportConfig transport) {
        this.transport = transport;
        return this;
    }

    public ServerFlowConfig getFlow() {
        return flow;
    }

    public ClusterServerStateVO setFlow(ServerFlowConfig flow) {
        this.flow = flow;
        return this;
    }

    public Set<String> getNamespaceSet() {
        return namespaceSet;
    }

    public ClusterServerStateVO setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public ClusterServerStateVO setPort(Integer port) {
        this.port = port;
        return this;
    }

    public List<ConnectionGroupVO> getConnection() {
        return connection;
    }

    public ClusterServerStateVO setConnection(List<ConnectionGroupVO> connection) {
        this.connection = connection;
        return this;
    }

    public List<ClusterRequestLimitVO> getRequestLimitData() {
        return requestLimitData;
    }

    public ClusterServerStateVO setRequestLimitData(List<ClusterRequestLimitVO> requestLimitData) {
        this.requestLimitData = requestLimitData;
        return this;
    }

    public Boolean getEmbedded() {
        return embedded;
    }

    public ClusterServerStateVO setEmbedded(Boolean embedded) {
        this.embedded = embedded;
        return this;
    }

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
