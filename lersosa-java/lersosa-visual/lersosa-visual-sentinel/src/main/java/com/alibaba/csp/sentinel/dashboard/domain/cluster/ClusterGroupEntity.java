/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.domain.cluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eric Zhao
 * @since 1.4.1
 */
public class ClusterGroupEntity {

    private String machineId;

    private String ip;
    private Integer port;

    private Set<String> clientSet = new HashSet<>();

    private Boolean belongToApp;

    public String getMachineId() {
        return machineId;
    }

    public ClusterGroupEntity setMachineId(String machineId) {
        this.machineId = machineId;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public ClusterGroupEntity setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public ClusterGroupEntity setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Set<String> getClientSet() {
        return clientSet;
    }

    public ClusterGroupEntity setClientSet(Set<String> clientSet) {
        this.clientSet = clientSet;
        return this;
    }

    public Boolean getBelongToApp() {
        return belongToApp;
    }

    public ClusterGroupEntity setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }

    @Override
    public String toString() {
        return "ClusterGroupEntity{" +
            "machineId='" + machineId + '\'' +
            ", ip='" + ip + '\'' +
            ", port=" + port +
            ", clientSet=" + clientSet +
            ", belongToApp=" + belongToApp +
            '}';
    }
}
