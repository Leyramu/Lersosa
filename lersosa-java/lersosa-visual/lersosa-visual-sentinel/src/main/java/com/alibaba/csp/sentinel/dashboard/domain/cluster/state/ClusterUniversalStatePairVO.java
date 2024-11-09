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
public class ClusterUniversalStatePairVO {

    private String ip;
    private Integer commandPort;

    private ClusterUniversalStateVO state;

    public ClusterUniversalStatePairVO() {
    }

    public ClusterUniversalStatePairVO(String ip, Integer commandPort, ClusterUniversalStateVO state) {
        this.ip = ip;
        this.commandPort = commandPort;
        this.state = state;
    }

    public String getIp() {
        return ip;
    }

    public ClusterUniversalStatePairVO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getCommandPort() {
        return commandPort;
    }

    public ClusterUniversalStatePairVO setCommandPort(Integer commandPort) {
        this.commandPort = commandPort;
        return this;
    }

    public ClusterUniversalStateVO getState() {
        return state;
    }

    public ClusterUniversalStatePairVO setState(ClusterUniversalStateVO state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        return "ClusterUniversalStatePairVO{" +
            "ip='" + ip + '\'' +
            ", commandPort=" + commandPort +
            ", state=" + state +
            '}';
    }
}
