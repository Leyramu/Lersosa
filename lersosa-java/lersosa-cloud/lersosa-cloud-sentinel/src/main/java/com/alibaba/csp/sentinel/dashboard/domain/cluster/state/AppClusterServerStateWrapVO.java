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
 * 集群服务器状态包装器
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class AppClusterServerStateWrapVO {

    /**
     * 服务器的唯一标识符
     */
    private String id;

    /**
     * 服务器 IP 地址
     */
    private String ip;

    /**
     * 服务器端口号
     */
    private Integer port;

    /**
     * 与服务器的连接数
     */
    private Integer connectedCount;

    /**
     * 是否属于应用
     */
    private Boolean belongToApp;

    /**
     * 服务器状态信息
     */
    private ClusterServerStateVO state;

    /**
     * 设置服务器的身份 ID
     *
     * @param id 服务器的身份 ID
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 设置服务器的 IP 地址
     *
     * @param ip 服务器的 IP 地址
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * 设置服务器的端口号
     *
     * @param port 服务器的端口号
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 设置服务器是否属于应用程序
     *
     * @param belongToApp 表示服务器是否属于该 App
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }

    /**
     * 设置与服务器的连接数
     *
     * @param connectedCount 与服务器的连接数
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setConnectedCount(Integer connectedCount) {
        this.connectedCount = connectedCount;
        return this;
    }

    /**
     * 设置服务器的状态信息
     *
     * @param state 服务器的状态信息
     * @return 用于链接调用的 AppClusterServerStateWrapVO 实例
     */
    public AppClusterServerStateWrapVO setState(ClusterServerStateVO state) {
        this.state = state;
        return this;
    }

    /**
     * 重写 toString 方法，以字符串格式显示 AppClusterServerStateWrapVO 对象的信息
     *
     * @return 对象信息的字符串表示形式
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
