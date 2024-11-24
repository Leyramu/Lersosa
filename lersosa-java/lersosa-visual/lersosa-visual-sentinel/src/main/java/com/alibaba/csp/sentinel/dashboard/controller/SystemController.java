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

package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.dashboard.auth.AuthAction;
import com.alibaba.csp.sentinel.dashboard.auth.AuthService.PrivilegeType;
import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.repository.rule.RuleRepository;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 系统规则.
 *
 * @author leyou(lihao)
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/system")
public class SystemController {

    /**
     * 规则存储.
     */
    private final RuleRepository<SystemRuleEntity, Long> repository;

    /**
     * Sentinel API 客户端.
     */
    private final SentinelApiClient sentinelApiClient;

    /**
     * 应用管理.
     */
    private final AppManagement appManagement;

    /**
     * 检查基本参数的通用方法.
     *
     * @param app  应用名称
     * @param ip   服务器IP
     * @param port 服务器端口
     * @return 如果参数无效，返回错误结果；否则返回null
     */
    private <R> Result<R> checkBasicParams(String app, String ip, Integer port) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }
        if (StringUtil.isEmpty(ip)) {
            return Result.ofFail(-1, "ip can't be null or empty");
        }
        if (port == null) {
            return Result.ofFail(-1, "port can't be null");
        }
        if (appManagement.isValidMachineOfApp(app, ip)) {
            return Result.ofFail(-1, "given ip does not belong to given app");
        }
        if (port <= 0 || port > 65535) {
            return Result.ofFail(-1, "port should be in (0, 65535)");
        }
        return null;
    }

    /**
     * 查询机器的系统规则接口.
     *
     * @param app  应用名称
     * @param ip   服务器IP
     * @param port 服务器端口
     * @return 系统规则列表的结果对象
     */
    @GetMapping("/rules.json")
    @AuthAction(PrivilegeType.READ_RULE)
    public Result<List<SystemRuleEntity>> apiQueryMachineRules(String app, String ip, Integer port) {
        Result<List<SystemRuleEntity>> checkResult = checkBasicParams(app, ip, port);
        if (checkResult != null) {
            return checkResult;
        }
        try {
            List<SystemRuleEntity> rules = sentinelApiClient.fetchSystemRuleOfMachine(app, ip, port);
            rules = repository.saveAll(rules);
            return Result.ofSuccess(rules);
        } catch (Throwable throwable) {
            log.error("Query machine system rules error", throwable);
            return Result.ofThrowable(-1, throwable);
        }
    }

    /**
     * 计算非空且非负数的参数数量.
     *
     * @param values 参数数组
     * @return 非空且非负数的参数数量
     */
    private int countNotNullAndNotNegative(Number... values) {
        int notNullCount = 0;
        for (Number value : values) {
            if (value != null && value.doubleValue() >= 0) {
                notNullCount++;
            }
        }
        return notNullCount;
    }

    /**
     * 添加系统规则的接口.
     *
     * @param app               应用名称
     * @param ip                服务器IP
     * @param port              服务器端口
     * @param highestSystemLoad 最高系统负载
     * @param highestCpuUsage   最高CPU使用率
     * @param avgRt             平均响应时间
     * @param maxThread         最大线程数
     * @param qps               最大QPS
     * @return 新增系统规则的结果对象
     */
    @RequestMapping("/new.json")
    @AuthAction(PrivilegeType.WRITE_RULE)
    public Result<SystemRuleEntity> apiAdd(
        String app, String ip, Integer port, Double highestSystemLoad,
        Double highestCpuUsage, Long avgRt, Long maxThread, Double qps) {

        Result<SystemRuleEntity> checkResult = checkBasicParams(app, ip, port);
        if (checkResult != null) {
            return checkResult;
        }

        int notNullCount = countNotNullAndNotNegative(highestSystemLoad, avgRt, maxThread, qps, highestCpuUsage);
        if (notNullCount != 1) {
            return Result.ofFail(-1, "only one of [highestSystemLoad, avgRt, maxThread, qps,highestCpuUsage] "
                + "value must be set > 0, but " + notNullCount + " values get");
        }
        if (null != highestCpuUsage && highestCpuUsage > 1) {
            return Result.ofFail(-1, "highestCpuUsage must between [0.0, 1.0]");
        }
        SystemRuleEntity entity = new SystemRuleEntity();
        entity.setApp(app.trim());
        entity.setIp(ip.trim());
        entity.setPort(port);
        entity.setHighestSystemLoad(Objects.requireNonNullElse(highestSystemLoad, -1D));

        entity.setHighestCpuUsage(Objects.requireNonNullElse(highestCpuUsage, -1D));

        entity.setAvgRt(Objects.requireNonNullElse(avgRt, -1L));
        entity.setMaxThread(Objects.requireNonNullElse(maxThread, -1L));
        entity.setQps(Objects.requireNonNullElse(qps, -1D));
        Date date = new Date();
        entity.setGmtCreate(date);
        entity.setGmtModified(date);
        try {
            entity = repository.save(entity);
        } catch (Throwable throwable) {
            log.error("Add SystemRule error", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (publishRules(app, ip, port)) {
            log.warn("Publish system rules fail after rule add");
        }
        return Result.ofSuccess(entity);
    }

    /**
     * 修改系统规则的接口.
     *
     * @param id                规则ID
     * @param app               应用名称
     * @param highestSystemLoad 最高系统负载
     * @param highestCpuUsage   最高CPU使用率
     * @param avgRt             平均响应时间
     * @param maxThread         最大线程数
     * @param qps               最大QPS
     * @return 修改后的系统规则的结果对象
     */
    @GetMapping("/save.json")
    @AuthAction(PrivilegeType.WRITE_RULE)
    public Result<SystemRuleEntity> apiUpdateIfNotNull(
        Long id, String app, Double highestSystemLoad,
        Double highestCpuUsage, Long avgRt, Long maxThread, Double qps) {
        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }
        SystemRuleEntity entity = repository.findById(id);
        if (entity == null) {
            return Result.ofFail(-1, "id " + id + " dose not exist");
        }

        if (StringUtil.isNotBlank(app)) {
            entity.setApp(app.trim());
        }
        if (highestSystemLoad != null) {
            if (highestSystemLoad < 0) {
                return Result.ofFail(-1, "highestSystemLoad must >= 0");
            }
            entity.setHighestSystemLoad(highestSystemLoad);
        }
        if (highestCpuUsage != null) {
            if (highestCpuUsage < 0) {
                return Result.ofFail(-1, "highestCpuUsage must >= 0");
            }
            if (highestCpuUsage > 1) {
                return Result.ofFail(-1, "highestCpuUsage must <= 1");
            }
            entity.setHighestCpuUsage(highestCpuUsage);
        }
        if (avgRt != null) {
            if (avgRt < 0) {
                return Result.ofFail(-1, "avgRt must >= 0");
            }
            entity.setAvgRt(avgRt);
        }
        if (maxThread != null) {
            if (maxThread < 0) {
                return Result.ofFail(-1, "maxThread must >= 0");
            }
            entity.setMaxThread(maxThread);
        }
        if (qps != null) {
            if (qps < 0) {
                return Result.ofFail(-1, "qps must >= 0");
            }
            entity.setQps(qps);
        }
        Date date = new Date();
        entity.setGmtModified(date);
        try {
            entity = repository.save(entity);
        } catch (Throwable throwable) {
            log.error("save error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (publishRules(entity.getApp(), entity.getIp(), entity.getPort())) {
            log.info("publish system rules fail after rule update");
        }
        return Result.ofSuccess(entity);
    }

    /**
     * 删除系统规则的接口.
     *
     * @param id 规则ID
     * @return 删除结果对象
     */
    @RequestMapping("/delete.json")
    @AuthAction(PrivilegeType.DELETE_RULE)
    public Result<?> delete(Long id) {
        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }
        SystemRuleEntity oldEntity = repository.findById(id);
        if (oldEntity == null) {
            return Result.ofSuccess(null);
        }
        try {
            repository.delete(id);
        } catch (Throwable throwable) {
            log.error("delete error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (publishRules(oldEntity.getApp(), oldEntity.getIp(), oldEntity.getPort())) {
            log.info("publish system rules fail after rule delete");
        }
        return Result.ofSuccess(id);
    }

    /**
     * 发布规则到sentinel-dashboard.
     *
     * @param app  应用名称
     * @param ip   应用IP地址
     * @param port 应用端口
     * @return 发布结果
     */
    private boolean publishRules(String app, String ip, Integer port) {
        List<SystemRuleEntity> rules = repository.findAllByMachine(MachineInfo.of(app, ip, port));
        return !sentinelApiClient.setSystemRuleOfMachine(app, ip, port, rules);
    }
}
