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

import lombok.Data;

/**
 * 集群服务器状态的包装器.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class AppClusterServerStateWrapVO {

    /**
     * 集群服务器唯一标识.
     */
    private String id;

    /**
     * 集群服务器IP.
     */
    private String ip;

    /**
     * 集群服务器端口.
     */
    private Integer port;

    /**
     * 当前连接数.
     */
    private Integer connectedCount;

    /**
     * 是否属于当前应用.
     */
    private Boolean belongToApp;

    /**
     * 集群服务器状态.
     */
    private ClusterServerStateVO state;

    /**
     * 设置集群服务器唯一标识.
     *
     * @param id 集群服务器唯一标识
     * @return 包装器自身
     */
    public AppClusterServerStateWrapVO setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 设置集群服务器IP.
     *
     * @param ip 集群服务器IP
     * @return 包装器自身
     */
    public AppClusterServerStateWrapVO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 设置集群服务器端口.
     *
     * @param port 集群服务器端口
     * @return 包装器自身
     */
    public AppClusterServerStateWrapVO setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 设置是否属于当前应用.
     *
     * @param belongToApp 是否属于当前应用
     * @return 包装器自身
     */
    public AppClusterServerStateWrapVO setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }

    /**
     * 设置当前连接数.
     *
     * @param connectedCount 当前连接数
     * @return 包装器自身
     */
    public AppClusterServerStateWrapVO setConnectedCount(Integer connectedCount) {
        this.connectedCount = connectedCount;
        return this;
    }

    /**
     * 设置集群服务器状态.
     *
     * @param state 集群服务器状态
     * @return 包装器自身
     */
    public AppClusterServerStateWrapVO setState(ClusterServerStateVO state) {
        this.state = state;
        return this;
    }

    /**
     * 重写toString方法.
     *
     * @return 包装器信息
     */
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
