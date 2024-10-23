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
public class ServerFlowConfig {

    /**
     * 默认超出阈值
     */
    public static final double DEFAULT_EXCEED_COUNT = 1.0d;

    /**
     * 默认最大占用比例
     */
    public static final double DEFAULT_MAX_OCCUPY_RATIO = 1.0d;

    /**
     * 默认间隔时间
     */
    public static final int DEFAULT_INTERVAL_MS = 1000;

    /**
     * 默认采样数量
     */
    public static final int DEFAULT_SAMPLE_COUNT = 10;

    /**
     * 默认最大允许QPS
     */
    public static final double DEFAULT_MAX_ALLOWED_QPS = 30000;

    /**
     * 命名空间
     */
    private final String namespace;

    /**
     * 超出阈值
     */
    private Double exceedCount = DEFAULT_EXCEED_COUNT;

    /**
     * 最大占用比例
     */
    private Double maxOccupyRatio = DEFAULT_MAX_OCCUPY_RATIO;

    /**
     * 间隔时间
     */
    private Integer intervalMs = DEFAULT_INTERVAL_MS;

    /**
     * 采样数量
     */
    private Integer sampleCount = DEFAULT_SAMPLE_COUNT;

    /**
     * 最大允许QPS
     */
    private Double maxAllowedQps = DEFAULT_MAX_ALLOWED_QPS;

    /**
     * 默认构造函数
     */
    public ServerFlowConfig() {
        this("default");
    }

    /**
     * 构造函数
     *
     * @param namespace 命名空间
     */
    public ServerFlowConfig(String namespace) {
        this.namespace = namespace;
    }

    /**
     * 设置超出阈值
     *
     * @param exceedCount 超出阈值
     * @return this
     */
    public ServerFlowConfig setExceedCount(Double exceedCount) {
        this.exceedCount = exceedCount;
        return this;
    }

    /**
     * 设置最大占用比例
     *
     * @param maxOccupyRatio 最大占用比例
     * @return this
     */
    public ServerFlowConfig setMaxOccupyRatio(Double maxOccupyRatio) {
        this.maxOccupyRatio = maxOccupyRatio;
        return this;
    }

    /**
     * 设置间隔时间
     *
     * @param intervalMs 间隔时间
     * @return this
     */
    public ServerFlowConfig setIntervalMs(Integer intervalMs) {
        this.intervalMs = intervalMs;
        return this;
    }

    /**
     * 设置采样数量
     *
     * @param sampleCount 采样数量
     * @return this
     */
    public ServerFlowConfig setSampleCount(Integer sampleCount) {
        this.sampleCount = sampleCount;
        return this;
    }

    /**
     * 设置最大允许QPS
     *
     * @param maxAllowedQps 最大允许QPS
     * @return this
     */
    public ServerFlowConfig setMaxAllowedQps(Double maxAllowedQps) {
        this.maxAllowedQps = maxAllowedQps;
        return this;
    }

    /**
     * 转换为字符串
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
