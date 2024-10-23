/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster.config;

import lombok.Getter;

/**
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterClientConfig {

    /**
     * 服务器主机
     */
    private String serverHost;

    /**
     * 服务器端口
     */
    private Integer serverPort;

    /**
     * 请求超时时间
     */
    private Integer requestTimeout;

    /**
     * 连接超时时间
     */
    private Integer connectTimeout;

    /**
     * 设置服务器主机
     *
     * @param serverHost 服务器主机
     * @return this
     */
    public ClusterClientConfig setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }

    /**
     * 设置服务器端口
     *
     * @param serverPort 服务器端口
     * @return this
     */
    public ClusterClientConfig setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    /**
     * 设置请求超时时间
     *
     * @param requestTimeout 请求超时时间
     * @return this
     */
    public ClusterClientConfig setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    /**
     * 设置连接超时时间
     *
     * @param connectTimeout 连接超时时间
     * @return this
     */
    public ClusterClientConfig setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 获取主机和端口的组合字符串
     *
     * @return 主机和端口的组合字符串
     */
    @Override
    public String toString() {
        return "ClusterClientConfig{" +
                "serverHost='" + serverHost + '\'' +
                ", serverPort=" + serverPort +
                ", requestTimeout=" + requestTimeout +
                ", connectTimeout=" + connectTimeout +
                '}';
    }
}
