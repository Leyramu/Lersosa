/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster;

import lombok.Getter;

/**
 * 集群客户端信息视图对象
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterClientInfoVO {

    /**
     * 服务器主机地址
     */
    private String serverHost;

    /**
     * 服务器端口
     */
    private Integer serverPort;

    /**
     * 客户端状态
     */
    private Integer clientState;

    /**
     * 请求超时时间
     */
    private Integer requestTimeout;

    /**
     * 设置服务器主机地址
     *
     * @param serverHost 服务器主机地址
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterClientInfoVO setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }

    /**
     * 设置服务器端口
     *
     * @param serverPort 服务器端口
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterClientInfoVO setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    /**
     * 设置客户端状态
     *
     * @param clientState 客户端状态
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterClientInfoVO setClientState(Integer clientState) {
        this.clientState = clientState;
        return this;
    }

    /**
     * 设置请求超时时间
     *
     * @param requestTimeout 请求超时时间
     * @return 当前对象实例，以便进行链式调用
     */
    public ClusterClientInfoVO setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    /**
     * 重写toString方法，用于打印对象信息
     *
     * @return 对象信息的字符串表示
     */
    @Override
    public String toString() {
        return "ClusterClientInfoVO{" +
               "serverHost='" + serverHost + '\'' +
               ", serverPort=" + serverPort +
               ", clientState=" + clientState +
               ", requestTimeout=" + requestTimeout +
               '}';
    }
}
