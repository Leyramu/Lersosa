/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.datasource.entity;

import lombok.Data;

import java.util.Date;

/**
 * 度量实体.
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class MetricEntity {

    /**
     * 监控数据ID.
     */
    private Long id;

    /**
     * 创建时间.
     */
    private Date gmtCreate;

    /**
     * 修改时间.
     */
    private Date gmtModified;

    /**
     * 应用名称.
     */
    private String app;

    /**
     * 监控信息的时间戳.
     */
    private Date timestamp;

    /**
     * 资源名称.
     */
    private String resource;

    /**
     * 通过的QPS.
     */
    private Long passQps;

    /**
     * 成功的QPS.
     */
    private Long successQps;

    /**
     * 阻塞的QPS.
     */
    private Long blockQps;

    /**
     * 异常的QPS.
     */
    private Long exceptionQps;

    /**
     * 所有成功退出 QPS 的 RT 总结.
     */
    private double rt;

    /**
     * 本次聚合的总条数.
     */
    private int count;

    /**
     * 资源的哈希值.
     */
    private int resourceCode;

    /**
     * 复制一个 MetricEntity.
     *
     * @param oldEntity 旧的 MetricEntity
     * @return 新的 MetricEntity
     */
    public static MetricEntity copyOf(MetricEntity oldEntity) {
        MetricEntity entity = new MetricEntity();
        entity.setId(oldEntity.getId());
        entity.setGmtCreate(oldEntity.getGmtCreate());
        entity.setGmtModified(oldEntity.getGmtModified());
        entity.setApp(oldEntity.getApp());
        entity.setTimestamp(oldEntity.getTimestamp());
        entity.setResource(oldEntity.getResource());
        entity.setPassQps(oldEntity.getPassQps());
        entity.setBlockQps(oldEntity.getBlockQps());
        entity.setSuccessQps(oldEntity.getSuccessQps());
        entity.setExceptionQps(oldEntity.getExceptionQps());
        entity.setRt(oldEntity.getRt());
        entity.setCount(oldEntity.getCount());
        return entity;
    }

    /**
     * 增加通过QPS.
     *
     * @param passQps 增加的通过QPS
     */
    public synchronized void addPassQps(Long passQps) {
        this.passQps += passQps;
    }

    /**
     * 增加阻塞QPS.
     *
     * @param blockQps 增加的阻塞QPS
     */
    public synchronized void addBlockQps(Long blockQps) {
        this.blockQps += blockQps;
    }

    /**
     * 增加异常QPS.
     *
     * @param exceptionQps 增加的异常QPS
     */
    public synchronized void addExceptionQps(Long exceptionQps) {
        this.exceptionQps += exceptionQps;
    }

    /**
     * 增加统计总数.
     *
     * @param count 增加的统计总数
     */
    public synchronized void addCount(int count) {
        this.count += count;
    }

    /**
     * 增加平均响应时间和成功QPS.
     *
     * @param avgRt      平均响应时间
     * @param successQps 成功QPS
     */
    public synchronized void addRtAndSuccessQps(double avgRt, Long successQps) {
        this.rt += avgRt * successQps;
        this.successQps += successQps;
    }

    /**
     * 设置平均响应时间和成功QPS.
     *
     * @param avgRt      平均响应时间
     * @param successQps 成功QPS
     */
    public synchronized void setRtAndSuccessQps(double avgRt, Long successQps) {
        this.rt = avgRt * successQps;
        this.successQps = successQps;
    }

    /**
     * 设置资源名称.
     *
     * @param resource 资源名称
     */
    public void setResource(String resource) {
        this.resource = resource;
        this.resourceCode = resource.hashCode();
    }

    /**
     * 重写toString方法.
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "MetricEntity{" +
            "id=" + id +
            ", gmtCreate=" + gmtCreate +
            ", gmtModified=" + gmtModified +
            ", app='" + app + '\'' +
            ", timestamp=" + timestamp +
            ", resource='" + resource + '\'' +
            ", passQps=" + passQps +
            ", blockQps=" + blockQps +
            ", successQps=" + successQps +
            ", exceptionQps=" + exceptionQps +
            ", rt=" + rt +
            ", count=" + count +
            ", resourceCode=" + resourceCode +
            '}';
    }
}
