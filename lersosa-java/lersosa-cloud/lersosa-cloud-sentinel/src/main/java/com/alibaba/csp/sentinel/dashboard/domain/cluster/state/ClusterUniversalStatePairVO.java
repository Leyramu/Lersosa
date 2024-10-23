/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 集群通用状态对
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClusterUniversalStatePairVO {

    /**
     * IP 地址
     */
    private String ip;

    /**
     * 命令端口
     */
    private Integer commandPort;

    /**
     * 状态信息
     */
    private ClusterUniversalStateVO state;

    /**
     * 设置IP地址
     *
     * @param ip 要设置的IP地址
     * @return 当前实例，支持链式调用
     */
    public ClusterUniversalStatePairVO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 设置命令端口
     *
     * @param commandPort 要设置的命令端口号
     * @return 当前实例，支持链式调用
     */
    public ClusterUniversalStatePairVO setCommandPort(Integer commandPort) {
        this.commandPort = commandPort;
        return this;
    }

    /**
     * 设置状态信息
     *
     * @param state 要设置的状态信息
     * @return 当前实例，支持链式调用
     */
    public ClusterUniversalStatePairVO setState(ClusterUniversalStateVO state) {
        this.state = state;
        return this;
    }

    /**
     * 重写toString方法，方便打印对象状态
     *
     * @return 对象的状态信息字符串
     */
    @Override
    public String toString() {
        return "ClusterUniversalStatePairVO{"
               + "ip='" + ip + '\''
               + ", commandPort=" + commandPort
               + ", state=" + state
               + '}';
    }
}
