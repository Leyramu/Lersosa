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
 * 群集状态简单实体
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterStateSimpleEntity {

    /**
     * 集群模式
     */
    private Integer mode;

    /**
     * 上次修改时间戳
     */
    private Long lastModified;

    /**
     * 客户端可用性
     */
    private Boolean clientAvailable;

    /**
     * 服务端可用性
     */
    private Boolean serverAvailable;

    /**
     * 设置集群模式
     *
     * @param mode 集群的模式
     * @return 当前的集群状态实体对象，支持链式调用
     */
    public ClusterStateSimpleEntity setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 设置上次修改时间
     *
     * @param lastModified 上次修改的时间戳
     * @return 当前的集群状态实体对象，支持链式调用
     */
    public ClusterStateSimpleEntity setLastModified(Long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    /**
     * 设置客户端可用性
     *
     * @param clientAvailable 客户端是否可用
     * @return 当前的集群状态实体对象，支持链式调用
     */
    public ClusterStateSimpleEntity setClientAvailable(Boolean clientAvailable) {
        this.clientAvailable = clientAvailable;
        return this;
    }

    /**
     * 设置服务器端可用性
     *
     * @param serverAvailable 服务器是否可用
     * @return 当前的集群状态实体对象，支持链式调用
     */
    public ClusterStateSimpleEntity setServerAvailable(Boolean serverAvailable) {
        this.serverAvailable = serverAvailable;
        return this;
    }

    /**
     * 重写toString方法，方便打印和展示集群状态实体的信息
     *
     * @return 包含集群状态信息的字符串
     */
    @Override
    public String toString() {
        return "ClusterStateSimpleEntity{"
               + "mode=" + mode
               + ", lastModified=" + lastModified
               + ", clientAvailable=" + clientAvailable
               + ", serverAvailable=" + serverAvailable
               + '}';
    }
}
