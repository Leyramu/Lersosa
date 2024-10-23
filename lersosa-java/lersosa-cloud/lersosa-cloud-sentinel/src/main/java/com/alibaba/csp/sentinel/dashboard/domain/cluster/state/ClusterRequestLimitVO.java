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
 * 集群请求限制信息
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterRequestLimitVO {

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 当前QPS
     */
    private Double currentQps;

    /**
     * 最大允许QPS
     */
    private Double maxAllowedQps;

    /**
     * 设置命名空间
     *
     * @param namespace 命名空间字符串，用于区分不同的服务或分组
     * @return 当前实例，方便链式调用
     */
    public ClusterRequestLimitVO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * 设置当前QPS
     *
     * @param currentQps 当前QPS值，表示正在处理的请求数量
     * @return 当前实例，方便链式调用
     */
    public ClusterRequestLimitVO setCurrentQps(Double currentQps) {
        this.currentQps = currentQps;
        return this;
    }

    /**
     * 设置允许的最大QPS
     *
     * @param maxAllowedQps 最大QPS值，用于限制请求速率
     * @return 当前实例，方便链式调用
     */
    public ClusterRequestLimitVO setMaxAllowedQps(Double maxAllowedQps) {
        this.maxAllowedQps = maxAllowedQps;
        return this;
    }

    /**
     * 重写toString方法，方便打印和查看集群请求限制的详细信息
     *
     * @return 包含命名空间、当前QPS和允许的最大QPS信息的字符串表示
     */
    @Override
    public String toString() {
        return "ClusterRequestLimitVO{" +
                "namespace='" + namespace + '\'' +
                ", currentQps=" + currentQps +
                ", maxAllowedQps=" + maxAllowedQps +
                '}';
    }
}
