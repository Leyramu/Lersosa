/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.domain.cluster.state;

/**
 * @author Eric Zhao
 * @since 1.4.1
 */
public class AppClusterServerStateWrapVO {

    /**
     * {ip}@{transport_command_port}.
     */
    private String id;

    private String ip;
    private Integer port;

    private Integer connectedCount;

    private Boolean belongToApp;

    private ClusterServerStateVO state;

    public String getId() {
        return id;
    }

    public AppClusterServerStateWrapVO setId(String id) {
        this.id = id;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public AppClusterServerStateWrapVO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public AppClusterServerStateWrapVO setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Boolean getBelongToApp() {
        return belongToApp;
    }

    public AppClusterServerStateWrapVO setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }

    public Integer getConnectedCount() {
        return connectedCount;
    }

    public AppClusterServerStateWrapVO setConnectedCount(Integer connectedCount) {
        this.connectedCount = connectedCount;
        return this;
    }

    public ClusterServerStateVO getState() {
        return state;
    }

    public AppClusterServerStateWrapVO setState(ClusterServerStateVO state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        return "AppClusterServerStateWrapVO{" +
            "id='" + id + '\'' +
            ", ip='" + ip + '\'' +
            ", port='" + port + '\'' +
            ", belongToApp=" + belongToApp +
            ", state=" + state +
            '}';
    }
}
