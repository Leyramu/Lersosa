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

package com.alibaba.csp.sentinel.dashboard.domain.cluster.config;

import lombok.Data;

/**
 * 服务器流控配置.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class ServerFlowConfig {

    /**
     * 默认流控阈值.
     */
    public static final double DEFAULT_EXCEED_COUNT = 1.0d;

    /**
     * 默认最大占用比例.
     */
    public static final double DEFAULT_MAX_OCCUPY_RATIO = 1.0d;

    /**
     * 默认统计时间间隔.
     */
    public static final int DEFAULT_INTERVAL_MS = 1000;

    /**
     * 默认采样数.
     */
    public static final int DEFAULT_SAMPLE_COUNT = 10;

    /**
     * 默认最大QPS.
     */
    public static final double DEFAULT_MAX_ALLOWED_QPS = 30000;

    /**
     * 命名空间.
     */
    private final String namespace;

    /**
     * 阈值.
     */
    private Double exceedCount = DEFAULT_EXCEED_COUNT;


    /**
     * 最大占用比例.
     */
    private Double maxOccupyRatio = DEFAULT_MAX_OCCUPY_RATIO;

    /**
     * 统计时间间隔.
     */
    private Integer intervalMs = DEFAULT_INTERVAL_MS;

    /**
     * 采样数.
     */
    private Integer sampleCount = DEFAULT_SAMPLE_COUNT;

    /**
     * 最大QPS.
     */
    private Double maxAllowedQps = DEFAULT_MAX_ALLOWED_QPS;

    /**
     * 无参构造.
     */
    public ServerFlowConfig() {
        this("default");
    }

    /**
     * 构造函数.
     *
     * @param namespace 命名空间
     */
    public ServerFlowConfig(String namespace) {
        this.namespace = namespace;
    }

    /**
     * 设置最大QPS.
     *
     * @param maxAllowedQps 最大QPS
     * @return this
     */
    public ServerFlowConfig setMaxAllowedQps(Double maxAllowedQps) {
        this.maxAllowedQps = maxAllowedQps;
        return this;
    }

    /**
     * 获取配置信息.
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "ServerFlowConfig{" +
            "namespace='" + namespace + '\'' +
            ", exceedCount=" + exceedCount +
            ", maxOccupyRatio=" + maxOccupyRatio +
            ", intervalMs=" + intervalMs +
            ", sampleCount=" + sampleCount +
            ", maxAllowedQps=" + maxAllowedQps +
            '}';
    }
}
