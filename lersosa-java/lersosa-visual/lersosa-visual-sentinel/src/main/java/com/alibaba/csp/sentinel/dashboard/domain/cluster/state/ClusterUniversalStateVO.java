/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster.state;

import lombok.Data;

/**
 * 群集通用状态视图对象.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class ClusterUniversalStateVO {

    /**
     * 状态概要信息.
     */
    private ClusterStateSimpleEntity stateInfo;

    /**
     * 集群客户端状态.
     */
    private ClusterClientStateVO client;

    /**
     * 集群服务端状态.
     */
    private ClusterServerStateVO server;

    /**
     * 设置客户端状态.
     *
     * @param client 客户端状态
     * @return this
     */
    public ClusterUniversalStateVO setClient(ClusterClientStateVO client) {
        this.client = client;
        return this;
    }

    /**
     * 设置服务端状态.
     *
     * @param server 服务端状态
     * @return this
     */
    public ClusterUniversalStateVO setServer(ClusterServerStateVO server) {
        this.server = server;
        return this;
    }

    /**
     * 设置状态概要信息.
     *
     * @param stateInfo 状态概要信息
     * @return this
     */
    public ClusterUniversalStateVO setStateInfo(
        ClusterStateSimpleEntity stateInfo) {
        this.stateInfo = stateInfo;
        return this;
    }

    /**
     * 获取状态概要信息.
     *
     * @return 状态概要信息
     */
    @Override
    public String toString() {
        return "ClusterUniversalStateVO{" +
            "stateInfo=" + stateInfo +
            ", client=" + client +
            ", server=" + server +
            '}';
    }
}
