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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 集群通用状态.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClusterUniversalStatePairVO {

    /**
     * 客户端 IP.
     */
    private String ip;

    /**
     * 客户端端口.
     */
    private Integer commandPort;

    /**
     * 集群状态.
     */
    private ClusterUniversalStateVO state;

    /**
     * 设置客户端 IP.
     *
     * @param ip 客户端 IP
     * @return 当前对象
     */
    public ClusterUniversalStatePairVO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 设置客户端端口.
     *
     * @param commandPort 客户端端口
     * @return 当前对象
     */
    @SuppressWarnings("unused")
    public ClusterUniversalStatePairVO setCommandPort(Integer commandPort) {
        this.commandPort = commandPort;
        return this;
    }

    /**
     * 设置集群状态.
     *
     * @param state 集群状态
     * @return 当前对象
     */
    public ClusterUniversalStatePairVO setState(ClusterUniversalStateVO state) {
        this.state = state;
        return this;
    }

    /**
     * 重写 toString 方法.
     *
     * @return 集群通用状态对VO的字符串表示形式
     */
    @Override
    public String toString() {
        return "ClusterUniversalStatePairVO{" +
            "ip='" + ip + '\'' +
            ", commandPort=" + commandPort +
            ", state=" + state +
            '}';
    }
}
