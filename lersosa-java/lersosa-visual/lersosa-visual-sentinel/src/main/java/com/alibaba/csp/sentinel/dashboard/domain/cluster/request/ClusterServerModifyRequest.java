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

import java.util.Set;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public class ClusterServerModifyRequest implements ClusterModifyRequest {

    private String app;
    private String ip;
    private Integer port;

    private Integer mode;
    private ServerFlowConfig flowConfig;
    private ServerTransportConfig transportConfig;
    private Set<String> namespaceSet;

    @Override
    public String getApp() {
        return app;
    }

    public ClusterServerModifyRequest setApp(String app) {
        this.app = app;
        return this;
    }

    @Override
    public String getIp() {
        return ip;
    }

    public ClusterServerModifyRequest setIp(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public ClusterServerModifyRequest setPort(Integer port) {
        this.port = port;
        return this;
    }

    @Override
    public Integer getMode() {
        return mode;
    }

    public ClusterServerModifyRequest setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    public ServerFlowConfig getFlowConfig() {
        return flowConfig;
    }

    public ClusterServerModifyRequest setFlowConfig(
        ServerFlowConfig flowConfig) {
        this.flowConfig = flowConfig;
        return this;
    }

    public ServerTransportConfig getTransportConfig() {
        return transportConfig;
    }

    public ClusterServerModifyRequest setTransportConfig(
        ServerTransportConfig transportConfig) {
        this.transportConfig = transportConfig;
        return this;
    }

    public Set<String> getNamespaceSet() {
        return namespaceSet;
    }

    public ClusterServerModifyRequest setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }

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
