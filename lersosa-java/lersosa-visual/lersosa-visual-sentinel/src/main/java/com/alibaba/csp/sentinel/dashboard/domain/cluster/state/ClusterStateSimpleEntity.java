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
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class ClusterStateSimpleEntity {

    /**
     * 集群模式.
     */
    private Integer mode;

    /**
     * 最近一次修改时间.
     */
    private Long lastModified;

    /**
     * 客户端是否可用.
     */
    private Boolean clientAvailable;

    /**
     * 服务端是否可用.
     */
    private Boolean serverAvailable;

    /**
     * 设置模式.
     *
     * @param mode 模式
     * @return this
     */
    public ClusterStateSimpleEntity setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 设置最近一次修改时间.
     *
     * @param lastModified 最近一次修改时间
     * @return this
     */
    @SuppressWarnings("unused")
    public ClusterStateSimpleEntity setLastModified(Long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    /**
     * 设置客户端是否可用.
     *
     * @param clientAvailable 客户端是否可用
     * @return this
     */
    @SuppressWarnings("unused")
    public ClusterStateSimpleEntity setClientAvailable(Boolean clientAvailable) {
        this.clientAvailable = clientAvailable;
        return this;
    }

    /**
     * 设置服务端是否可用.
     *
     * @param serverAvailable 服务端是否可用
     * @return this
     */
    @SuppressWarnings("unused")
    public ClusterStateSimpleEntity setServerAvailable(Boolean serverAvailable) {
        this.serverAvailable = serverAvailable;
        return this;
    }

    /**
     * 输出字符串.
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "ClusterStateSimpleEntity{" +
            "mode=" + mode +
            ", lastModified=" + lastModified +
            ", clientAvailable=" + clientAvailable +
            ", serverAvailable=" + serverAvailable +
            '}';
    }
}
