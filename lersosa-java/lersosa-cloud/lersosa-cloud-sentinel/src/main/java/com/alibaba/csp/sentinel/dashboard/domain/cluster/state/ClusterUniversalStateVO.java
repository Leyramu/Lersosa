/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster.state;

import lombok.Getter;

/**
 * 群集通用状态视图对象
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterUniversalStateVO {

    /**
     * 集群状态信息
     */
    private ClusterStateSimpleEntity stateInfo;

    /**
     * Client 端状态信息
     */
    private ClusterClientStateVO client;

    /**
     * Server 端状态信息
     */
    private ClusterServerStateVO server;

    /**
     * 设置Client端状态信息
     *
     * @param client Client端的状态信息
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterUniversalStateVO setClient(ClusterClientStateVO client) {
        this.client = client;
        return this;
    }

    /**
     * 设置Server端状态信息
     *
     * @param server Server端的状态信息
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterUniversalStateVO setServer(ClusterServerStateVO server) {
        this.server = server;
        return this;
    }

    /**
     * 设置集群状态信息
     *
     * @param stateInfo 集群的基本状态信息
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterUniversalStateVO setStateInfo(
            ClusterStateSimpleEntity stateInfo) {
        this.stateInfo = stateInfo;
        return this;
    }

    /**
     * 将当前对象转换为字符串表示形式
     *
     * @return 当前对象的字符串表示形式，包括状态信息、Client端状态信息、Server端状态信息
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
