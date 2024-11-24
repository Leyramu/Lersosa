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
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.repository.rule.RuleRepository;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 关于降级规则的 API 的控制者.
 *
 * @author Carpenter Lee
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/degrade")
public class DegradeController {

    /**
     * 规则存储库，用于存储和检索规则实体.
     */
    private final RuleRepository<DegradeRuleEntity, Long> repository;

    /**
     * 客户端API客户端，用于与 Sentinel 服务器进行通信以获取规则等数据.
     */
    private final SentinelApiClient sentinelApiClient;

    /**
     * 应用管理器，用于管理应用和机器信息.
     */
    private final AppManagement appManagement;

    /**
     * 处理查询机器降级规则的API请求.
     *
     * @param app  应用名称，用于标识一个具体的应用
     * @param ip   机器IP地址，用于标识应用所在的机器
     * @param port 机器端口号，用于进一步精确标识应用所在的机器
     * @return 返回一个Result对象，其中包含降级规则列表
     */
    @GetMapping("/rules.json")
    @AuthAction(PrivilegeType.READ_RULE)
    public Result<List<DegradeRuleEntity>> apiQueryMachineRules(String app, String ip, Integer port) {
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
        try {
            List<DegradeRuleEntity> rules = sentinelApiClient.fetchDegradeRuleOfMachine(app, ip, port);
            rules = repository.saveAll(rules);
            return Result.ofSuccess(rules);
        } catch (Throwable throwable) {
            log.error("queryApps error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
    }

    /**
     * 处理添加降级规则的API请求.
     *
     * @param entity 降级规则实体，包含降级规则的所有必要信息
     * @return 返回一个Result对象，其中包含添加后的降级规则实体
     */
    @PostMapping("/rule")
    @AuthAction(PrivilegeType.WRITE_RULE)
    public Result<DegradeRuleEntity> apiAddRule(@RequestBody DegradeRuleEntity entity) {
        Result<DegradeRuleEntity> checkResult = checkEntityInternal(entity);
        if (checkResult != null) {
            return checkResult;
        }
        Date date = new Date();
        entity.setGmtCreate(date);
        entity.setGmtModified(date);
        try {
            entity = repository.save(entity);
        } catch (Throwable t) {
            log.error("Failed to add new degrade rule, app={}, ip={}", entity.getApp(), entity.getIp(), t);
            return Result.ofThrowable(-1, t);
        }
        if (publishRules(entity.getApp(), entity.getIp(), entity.getPort())) {
            log.warn("Publish degrade rules failed, app={}", entity.getApp());
        }
        return Result.ofSuccess(entity);
    }

    /**
     * 更新降级规则接口.
     *
     * @param id     规则标识符
     * @param entity 更新后的降级规则实体
     * @return 更新结果，包含可能的错误信息
     */
    @PutMapping("/rule/{id}")
    @AuthAction(PrivilegeType.WRITE_RULE)
    public Result<DegradeRuleEntity> apiUpdateRule(
        @PathVariable("id") Long id,
        @RequestBody DegradeRuleEntity entity) {
        if (id == null || id <= 0) {
            return Result.ofFail(-1, "id can't be null or negative");
        }
        DegradeRuleEntity oldEntity = repository.findById(id);
        if (oldEntity == null) {
            return Result.ofFail(-1, "Degrade rule does not exist, id=" + id);
        }
        entity.setApp(oldEntity.getApp());
        entity.setIp(oldEntity.getIp());
        entity.setPort(oldEntity.getPort());
        entity.setId(oldEntity.getId());
        Result<DegradeRuleEntity> checkResult = checkEntityInternal(entity);
        if (checkResult != null) {
            return checkResult;
        }

        entity.setGmtCreate(oldEntity.getGmtCreate());
        entity.setGmtModified(new Date());
        try {
            entity = repository.save(entity);
        } catch (Throwable t) {
            log.error("Failed to save degrade rule, id={}, rule={}", id, entity, t);
            return Result.ofThrowable(-1, t);
        }
        if (publishRules(entity.getApp(), entity.getIp(), entity.getPort())) {
            log.warn("Publish degrade rules failed, app={}", entity.getApp());
        }
        return Result.ofSuccess(entity);
    }

    /**
     * 删除降级规则接口.
     *
     * @param id 规则标识符
     * @return 删除结果，包含可能的错误信息
     */
    @DeleteMapping("/rule/{id}")
    @AuthAction(PrivilegeType.DELETE_RULE)
    public Result<Long> delete(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }

        DegradeRuleEntity oldEntity = repository.findById(id);
        if (oldEntity == null) {
            return Result.ofSuccess(null);
        }

        try {
            repository.delete(id);
        } catch (Throwable throwable) {
            log.error("Failed to delete degrade rule, id={}", id, throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (publishRules(oldEntity.getApp(), oldEntity.getIp(), oldEntity.getPort())) {
            log.warn("Publish degrade rules failed, app={}", oldEntity.getApp());
        }
        return Result.ofSuccess(id);
    }

    /**
     * 发布降级规则到指定应用和机器.
     *
     * @param app  应用名称
     * @param ip   机器IP
     * @param port 机器端口
     * @return 发布是否失败
     */
    private boolean publishRules(String app, String ip, Integer port) {
        List<DegradeRuleEntity> rules = repository.findAllByMachine(MachineInfo.of(app, ip, port));
        return !sentinelApiClient.setDegradeRuleOfMachine(app, ip, port, rules);
    }

    /**
     * 内部校验降级规则实体.
     *
     * @param entity 待校验的降级规则实体
     * @return 校验结果，如果校验失败则包含错误信息，否则为null
     */
    private <R> Result<R> checkEntityInternal(DegradeRuleEntity entity) {
        if (StringUtil.isBlank(entity.getApp())) {
            return Result.ofFail(-1, "app can't be blank");
        }
        if (StringUtil.isBlank(entity.getIp())) {
            return Result.ofFail(-1, "ip can't be null or empty");
        }
        if (appManagement.isValidMachineOfApp(entity.getApp(), entity.getIp())) {
            return Result.ofFail(-1, "given ip does not belong to given app");
        }
        if (entity.getPort() == null || entity.getPort() <= 0) {
            return Result.ofFail(-1, "invalid port: " + entity.getPort());
        }
        if (StringUtil.isBlank(entity.getLimitApp())) {
            return Result.ofFail(-1, "limitApp can't be null or empty");
        }
        if (StringUtil.isBlank(entity.getResource())) {
            return Result.ofFail(-1, "resource can't be null or empty");
        }
        Double threshold = entity.getCount();
        if (threshold == null || threshold < 0) {
            return Result.ofFail(-1, "invalid threshold: " + threshold);
        }
        Integer recoveryTimeoutSec = entity.getTimeWindow();
        if (recoveryTimeoutSec == null || recoveryTimeoutSec <= 0) {
            return Result.ofFail(-1, "recoveryTimeout should be positive");
        }
        Integer strategy = entity.getGrade();
        if (strategy == null) {
            return Result.ofFail(-1, "circuit breaker strategy cannot be null");
        }
        if (strategy < CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType()
            || strategy > RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT) {
            return Result.ofFail(-1, "Invalid circuit breaker strategy: " + strategy);
        }
        if (entity.getMinRequestAmount() == null || entity.getMinRequestAmount() <= 0) {
            return Result.ofFail(-1, "Invalid minRequestAmount");
        }
        if (entity.getStatIntervalMs() == null || entity.getStatIntervalMs() <= 0) {
            return Result.ofFail(-1, "Invalid statInterval");
        }
        if (strategy == RuleConstant.DEGRADE_GRADE_RT) {
            Double slowRatio = entity.getSlowRatioThreshold();
            if (slowRatio == null) {
                return Result.ofFail(-1, "SlowRatioThreshold is required for slow request ratio strategy");
            } else if (slowRatio < 0 || slowRatio > 1) {
                return Result.ofFail(-1, "SlowRatioThreshold should be in range: [0.0, 1.0]");
            }
        } else if (strategy == RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO) {
            if (threshold > 1) {
                return Result.ofFail(-1, "Ratio threshold should be in range: [0.0, 1.0]");
            }
        }
        return null;
    }
}
