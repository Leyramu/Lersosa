/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.dashboard.auth.AuthAction;
import com.alibaba.csp.sentinel.dashboard.auth.AuthService.PrivilegeType;
import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.repository.rule.RuleRepository;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 权限规则控制器
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 0.2.1
 * @since 2024/9/3
 */
@RestController
@RequestMapping(value = "/authority")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorityRuleController {

    /**
     * 日志记录器
     */
    private final Logger logger = LoggerFactory.getLogger(AuthorityRuleController.class);

    /**
     * Sentinel API 客户端
     */
    private final SentinelApiClient sentinelApiClient;

    /**
     * 规则存储仓库
     */
    private final RuleRepository<AuthorityRuleEntity, Long> repository;

    /**
     * 应用管理
     */
    private final AppManagement appManagement;

    /**
     * 查询应用下的所有规则
     *
     * @param app  应用名称
     * @param ip   应用机器IP
     * @param port 应用机器端口
     * @return 规则列表
     */
    @GetMapping("/rules")
    @AuthAction(PrivilegeType.READ_RULE)
    public Result<List<AuthorityRuleEntity>> apiQueryAllRulesForMachine(@RequestParam String app,
                                                                        @RequestParam String ip,
                                                                        @RequestParam Integer port) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app cannot be null or empty");
        }
        if (StringUtil.isEmpty(ip)) {
            return Result.ofFail(-1, "ip cannot be null or empty");
        }
        if (port == null || port <= 0) {
            return Result.ofFail(-1, "Invalid parameter: port");
        }
        if (appManagement.isValidMachineOfApp(app, ip)) {
            return Result.ofFail(-1, "given ip does not belong to given app");
        }
        try {
            List<AuthorityRuleEntity> rules = sentinelApiClient.fetchAuthorityRulesOfMachine(app, ip, port);
            rules = repository.saveAll(rules);
            return Result.ofSuccess(rules);
        } catch (Throwable throwable) {
            logger.error("Error when querying authority rules", throwable);
            return Result.ofFail(-1, throwable.getMessage());
        }
    }

    /**
     * 校验实体
     *
     * @param entity 规则实体
     * @return 校验结果
     */
    private <R> Result<R> checkEntityInternal(AuthorityRuleEntity entity) {
        if (entity == null) {
            return Result.ofFail(-1, "bad rule body");
        }
        if (StringUtil.isBlank(entity.getApp())) {
            return Result.ofFail(-1, "app can't be null or empty");
        }
        if (StringUtil.isBlank(entity.getIp())) {
            return Result.ofFail(-1, "ip can't be null or empty");
        }
        if (entity.getPort() == null || entity.getPort() <= 0) {
            return Result.ofFail(-1, "port can't be null");
        }
        if (entity.getRule() == null) {
            return Result.ofFail(-1, "rule can't be null");
        }
        if (StringUtil.isBlank(entity.getResource())) {
            return Result.ofFail(-1, "resource name cannot be null or empty");
        }
        if (StringUtil.isBlank(entity.getLimitApp())) {
            return Result.ofFail(-1, "limitApp should be valid");
        }
        if (entity.getStrategy() != RuleConstant.AUTHORITY_WHITE
            && entity.getStrategy() != RuleConstant.AUTHORITY_BLACK) {
            return Result.ofFail(-1, "Unknown strategy (must be blacklist or whitelist)");
        }
        return null;
    }

    /**
     * 新增权限规则
     *
     * @param entity 权限规则实体
     * @return 新增结果
     */
    @PostMapping("/rule")
    @AuthAction(PrivilegeType.WRITE_RULE)
    public Result<AuthorityRuleEntity> apiAddAuthorityRule(@RequestBody AuthorityRuleEntity entity) {
        Result<AuthorityRuleEntity> checkResult = checkEntityInternal(entity);
        if (checkResult != null) {
            return checkResult;
        }
        entity.setId(null);
        Date date = new Date();
        entity.setGmtCreate(date);
        entity.setGmtModified(date);
        try {
            entity = repository.save(entity);
        } catch (Throwable throwable) {
            logger.error("Failed to add authority rule", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (publishRules(entity.getApp(), entity.getIp(), entity.getPort())) {
            logger.info("Publish authority rules failed after rule add");
        }
        return Result.ofSuccess(entity);
    }

    /**
     * 更新权限规则
     *
     * @param id     规则ID
     * @param entity 规则实体
     * @return 更新结果
     */
    @PutMapping("/rule/{id}")
    @AuthAction(PrivilegeType.WRITE_RULE)
    public Result<AuthorityRuleEntity> apiUpdateParamFlowRule(
            @PathVariable("id") Long id,
            @RequestBody AuthorityRuleEntity entity) {
        if (id == null || id <= 0) {
            return Result.ofFail(-1, "Invalid id");
        }
        Result<AuthorityRuleEntity> checkResult = checkEntityInternal(entity);
        if (checkResult != null) {
            return checkResult;
        }
        entity.setId(id);
        Date date = new Date();
        entity.setGmtCreate(null);
        entity.setGmtModified(date);
        try {
            entity = repository.save(entity);
            if (entity == null) {
                return Result.ofFail(-1, "Failed to save authority rule");
            }
        } catch (Throwable throwable) {
            logger.error("Failed to save authority rule", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (publishRules(entity.getApp(), entity.getIp(), entity.getPort())) {
            logger.info("Publish authority rules failed after rule update");
        }
        return Result.ofSuccess(entity);
    }

    /**
     * 删除权限规则
     *
     * @param id 规则ID
     * @return 删除结果
     */
    @DeleteMapping("/rule/{id}")
    @AuthAction(PrivilegeType.DELETE_RULE)
    public Result<Long> apiDeleteRule(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.ofFail(-1, "id cannot be null");
        }
        AuthorityRuleEntity oldEntity = repository.findById(id);
        if (oldEntity == null) {
            return Result.ofSuccess(null);
        }
        try {
            repository.delete(id);
        } catch (Exception e) {
            return Result.ofFail(-1, e.getMessage());
        }
        if (publishRules(oldEntity.getApp(), oldEntity.getIp(), oldEntity.getPort())) {
            logger.error("Publish authority rules failed after rule delete");
        }
        return Result.ofSuccess(id);
    }

    /**
     * 发布规则
     *
     * @param app  应用名称
     * @param ip   IP地址
     * @param port 端口号
     * @return 发布结果
     */
    private boolean publishRules(String app, String ip, Integer port) {
        List<AuthorityRuleEntity> rules = repository.findAllByMachine(MachineInfo.of(app, ip, port));
        return !sentinelApiClient.setAuthorityRuleOfMachine(app, ip, port, rules);
    }
}
