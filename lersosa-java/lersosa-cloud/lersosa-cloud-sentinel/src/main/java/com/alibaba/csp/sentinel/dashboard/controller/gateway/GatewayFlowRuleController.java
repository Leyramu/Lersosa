/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.controller.gateway;


import com.alibaba.csp.sentinel.dashboard.auth.AuthAction;
import com.alibaba.csp.sentinel.dashboard.auth.AuthService;
import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayParamFlowItemEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.domain.vo.gateway.rule.AddFlowRuleReqVo;
import com.alibaba.csp.sentinel.dashboard.domain.vo.gateway.rule.GatewayParamFlowItemVo;
import com.alibaba.csp.sentinel.dashboard.domain.vo.gateway.rule.UpdateFlowRuleReqVo;
import com.alibaba.csp.sentinel.dashboard.repository.gateway.InMemGatewayFlowRuleStore;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants.*;
import static com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity.*;
import static com.alibaba.csp.sentinel.slots.block.RuleConstant.*;

/**
 * 网关流规则 用于管理网关流规则的控制器
 *
 * @author cdfive
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.7.0
 * @since 2024/9/3
 */
@RestController
@RequestMapping(value = "/gateway/flow")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GatewayFlowRuleController {

    /**
     * 日志记录器
     */
    private final Logger logger = LoggerFactory.getLogger(GatewayFlowRuleController.class);

    /**
     * 网关流规则存储
     */
    private final InMemGatewayFlowRuleStore repository;

    /**
     * 网关客户端客户端
     */
    private final SentinelApiClient sentinelApiClient;

    /**
     * 查询网关流规则
     *
     * @param app  应用名称
     * @param ip   IP地址
     * @param port 端口号
     * @return 网关流规则列表
     */
    @GetMapping("/list.json")
    @AuthAction(AuthService.PrivilegeType.READ_RULE)
    public Result<List<GatewayFlowRuleEntity>> queryFlowRules(String app, String ip, Integer port) {

        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }
        if (StringUtil.isEmpty(ip)) {
            return Result.ofFail(-1, "ip can't be null or empty");
        }
        if (port == null) {
            return Result.ofFail(-1, "port can't be null");
        }

        try {
            List<GatewayFlowRuleEntity> rules = sentinelApiClient.fetchGatewayFlowRules(app, ip, port).get();
            repository.saveAll(rules);
            return Result.ofSuccess(rules);
        } catch (Throwable throwable) {
            logger.error("query gateway flow rules error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
    }

    /**
     * 新增网关流规则
     *
     * @param reqVo 网关流规则信息
     * @return 新增网关流规则结果
     */
    @PostMapping("/new.json")
    @AuthAction(AuthService.PrivilegeType.WRITE_RULE)
    public Result<GatewayFlowRuleEntity> addFlowRule(@RequestBody AddFlowRuleReqVo reqVo) {

        String app = reqVo.getApp();
        if (StringUtil.isBlank(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }

        GatewayFlowRuleEntity entity = new GatewayFlowRuleEntity();
        entity.setApp(app.trim());

        String ip = reqVo.getIp();
        if (StringUtil.isBlank(ip)) {
            return Result.ofFail(-1, "ip can't be null or empty");
        }
        entity.setIp(ip.trim());

        Integer port = reqVo.getPort();
        if (port == null) {
            return Result.ofFail(-1, "port can't be null");
        }
        entity.setPort(port);

        Integer resourceMode = reqVo.getResourceMode();
        if (resourceMode == null) {
            return Result.ofFail(-1, "resourceMode can't be null");
        }
        if (!Arrays.asList(RESOURCE_MODE_ROUTE_ID, RESOURCE_MODE_CUSTOM_API_NAME).contains(resourceMode)) {
            return Result.ofFail(-1, "invalid resourceMode: " + resourceMode);
        }
        entity.setResourceMode(resourceMode);

        String resource = reqVo.getResource();
        if (StringUtil.isBlank(resource)) {
            return Result.ofFail(-1, "resource can't be null or empty");
        }
        entity.setResource(resource.trim());

        GatewayParamFlowItemVo paramItem = reqVo.getParamItem();
        if (paramItem != null) {
            GatewayParamFlowItemEntity itemEntity = new GatewayParamFlowItemEntity();
            entity.setParamItem(itemEntity);

            Integer parseStrategy = paramItem.getParseStrategy();
            if (!Arrays.asList(PARAM_PARSE_STRATEGY_CLIENT_IP, PARAM_PARSE_STRATEGY_HOST, PARAM_PARSE_STRATEGY_HEADER
                    , PARAM_PARSE_STRATEGY_URL_PARAM, PARAM_PARSE_STRATEGY_COOKIE).contains(parseStrategy)) {
                return Result.ofFail(-1, "invalid parseStrategy: " + parseStrategy);
            }
            itemEntity.setParseStrategy(paramItem.getParseStrategy());

            if (Arrays.asList(PARAM_PARSE_STRATEGY_HEADER, PARAM_PARSE_STRATEGY_URL_PARAM, PARAM_PARSE_STRATEGY_COOKIE).contains(parseStrategy)) {
                String fieldName = paramItem.getFieldName();
                if (StringUtil.isBlank(fieldName)) {
                    return Result.ofFail(-1, "fieldName can't be null or empty");
                }
                itemEntity.setFieldName(paramItem.getFieldName());
            }

            String pattern = paramItem.getPattern();
            if (StringUtil.isNotEmpty(pattern)) {
                itemEntity.setPattern(pattern);
                Integer matchStrategy = paramItem.getMatchStrategy();
                if (!Arrays.asList(PARAM_MATCH_STRATEGY_EXACT, PARAM_MATCH_STRATEGY_CONTAINS, PARAM_MATCH_STRATEGY_REGEX).contains(matchStrategy)) {
                    return Result.ofFail(-1, "invalid matchStrategy: " + matchStrategy);
                }
                itemEntity.setMatchStrategy(matchStrategy);
            }
        }

        Integer grade = reqVo.getGrade();
        if (grade == null) {
            return Result.ofFail(-1, "grade can't be null");
        }
        if (!Arrays.asList(FLOW_GRADE_THREAD, FLOW_GRADE_QPS).contains(grade)) {
            return Result.ofFail(-1, "invalid grade: " + grade);
        }
        entity.setGrade(grade);

        Double count = reqVo.getCount();
        if (count == null) {
            return Result.ofFail(-1, "count can't be null");
        }
        if (count < 0) {
            return Result.ofFail(-1, "count should be at lease zero");
        }
        entity.setCount(count);

        Long interval = reqVo.getInterval();
        if (interval == null) {
            return Result.ofFail(-1, "interval can't be null");
        }
        if (interval <= 0) {
            return Result.ofFail(-1, "interval should be greater than zero");
        }
        entity.setInterval(interval);

        Integer intervalUnit = reqVo.getIntervalUnit();
        if (intervalUnit == null) {
            return Result.ofFail(-1, "intervalUnit can't be null");
        }
        if (!Arrays.asList(INTERVAL_UNIT_SECOND, INTERVAL_UNIT_MINUTE, INTERVAL_UNIT_HOUR, INTERVAL_UNIT_DAY).contains(intervalUnit)) {
            return Result.ofFail(-1, "Invalid intervalUnit: " + intervalUnit);
        }
        entity.setIntervalUnit(intervalUnit);

        Integer controlBehavior = reqVo.getControlBehavior();
        if (controlBehavior == null) {
            return Result.ofFail(-1, "controlBehavior can't be null");
        }
        if (!Arrays.asList(CONTROL_BEHAVIOR_DEFAULT, CONTROL_BEHAVIOR_RATE_LIMITER).contains(controlBehavior)) {
            return Result.ofFail(-1, "invalid controlBehavior: " + controlBehavior);
        }
        entity.setControlBehavior(controlBehavior);

        if (CONTROL_BEHAVIOR_DEFAULT == controlBehavior) {
            Integer burst = reqVo.getBurst();
            if (burst == null) {
                return Result.ofFail(-1, "burst can't be null");
            }
            if (burst < 0) {
                return Result.ofFail(-1, "invalid burst: " + burst);
            }
            entity.setBurst(burst);
        } else if (CONTROL_BEHAVIOR_RATE_LIMITER == controlBehavior) {
            Integer maxQueueingTimeoutMs = reqVo.getMaxQueueingTimeoutMs();
            if (maxQueueingTimeoutMs == null) {
                return Result.ofFail(-1, "maxQueueingTimeoutMs can't be null");
            }
            if (maxQueueingTimeoutMs < 0) {
                return Result.ofFail(-1, "invalid maxQueueingTimeoutMs: " + maxQueueingTimeoutMs);
            }
            entity.setMaxQueueingTimeoutMs(maxQueueingTimeoutMs);
        }

        Date date = new Date();
        entity.setGmtCreate(date);
        entity.setGmtModified(date);

        try {
            entity = repository.save(entity);
        } catch (Throwable throwable) {
            logger.error("add gateway flow rule error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }

        if (publishRules(app, ip, port)) {
            logger.warn("publish gateway flow rules fail after add");
        }

        return Result.ofSuccess(entity);
    }

    /**
     * 更新限流规则
     *
     * @param reqVo 网关限流规则
     * @return 限流规则
     */
    @PostMapping("/save.json")
    @AuthAction(AuthService.PrivilegeType.WRITE_RULE)
    public Result<GatewayFlowRuleEntity> updateFlowRule(@RequestBody UpdateFlowRuleReqVo reqVo) {

        String app = reqVo.getApp();
        if (StringUtil.isBlank(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }

        Long id = reqVo.getId();
        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }

        GatewayFlowRuleEntity entity = repository.findById(id);
        if (entity == null) {
            return Result.ofFail(-1, "gateway flow rule does not exist, id=" + id);
        }

        GatewayParamFlowItemVo paramItem = reqVo.getParamItem();
        if (paramItem != null) {
            GatewayParamFlowItemEntity itemEntity = new GatewayParamFlowItemEntity();
            entity.setParamItem(itemEntity);

            Integer parseStrategy = paramItem.getParseStrategy();
            if (!Arrays.asList(PARAM_PARSE_STRATEGY_CLIENT_IP, PARAM_PARSE_STRATEGY_HOST, PARAM_PARSE_STRATEGY_HEADER
                    , PARAM_PARSE_STRATEGY_URL_PARAM, PARAM_PARSE_STRATEGY_COOKIE).contains(parseStrategy)) {
                return Result.ofFail(-1, "invalid parseStrategy: " + parseStrategy);
            }
            itemEntity.setParseStrategy(paramItem.getParseStrategy());

            if (Arrays.asList(PARAM_PARSE_STRATEGY_HEADER, PARAM_PARSE_STRATEGY_URL_PARAM, PARAM_PARSE_STRATEGY_COOKIE).contains(parseStrategy)) {
                String fieldName = paramItem.getFieldName();
                if (StringUtil.isBlank(fieldName)) {
                    return Result.ofFail(-1, "fieldName can't be null or empty");
                }
                itemEntity.setFieldName(paramItem.getFieldName());
            }

            String pattern = paramItem.getPattern();
            if (StringUtil.isNotEmpty(pattern)) {
                itemEntity.setPattern(pattern);
                Integer matchStrategy = paramItem.getMatchStrategy();
                if (!Arrays.asList(PARAM_MATCH_STRATEGY_EXACT, PARAM_MATCH_STRATEGY_CONTAINS, PARAM_MATCH_STRATEGY_REGEX).contains(matchStrategy)) {
                    return Result.ofFail(-1, "invalid matchStrategy: " + matchStrategy);
                }
                itemEntity.setMatchStrategy(matchStrategy);
            }
        } else {
            entity.setParamItem(null);
        }

        Integer grade = reqVo.getGrade();
        if (grade == null) {
            return Result.ofFail(-1, "grade can't be null");
        }
        if (!Arrays.asList(FLOW_GRADE_THREAD, FLOW_GRADE_QPS).contains(grade)) {
            return Result.ofFail(-1, "invalid grade: " + grade);
        }
        entity.setGrade(grade);

        Double count = reqVo.getCount();
        if (count == null) {
            return Result.ofFail(-1, "count can't be null");
        }
        if (count < 0) {
            return Result.ofFail(-1, "count should be at lease zero");
        }
        entity.setCount(count);

        Long interval = reqVo.getInterval();
        if (interval == null) {
            return Result.ofFail(-1, "interval can't be null");
        }
        if (interval <= 0) {
            return Result.ofFail(-1, "interval should be greater than zero");
        }
        entity.setInterval(interval);

        Integer intervalUnit = reqVo.getIntervalUnit();
        if (intervalUnit == null) {
            return Result.ofFail(-1, "intervalUnit can't be null");
        }
        if (!Arrays.asList(INTERVAL_UNIT_SECOND, INTERVAL_UNIT_MINUTE, INTERVAL_UNIT_HOUR, INTERVAL_UNIT_DAY).contains(intervalUnit)) {
            return Result.ofFail(-1, "Invalid intervalUnit: " + intervalUnit);
        }
        entity.setIntervalUnit(intervalUnit);

        Integer controlBehavior = reqVo.getControlBehavior();
        if (controlBehavior == null) {
            return Result.ofFail(-1, "controlBehavior can't be null");
        }
        if (!Arrays.asList(CONTROL_BEHAVIOR_DEFAULT, CONTROL_BEHAVIOR_RATE_LIMITER).contains(controlBehavior)) {
            return Result.ofFail(-1, "invalid controlBehavior: " + controlBehavior);
        }
        entity.setControlBehavior(controlBehavior);

        if (CONTROL_BEHAVIOR_DEFAULT == controlBehavior) {
            Integer burst = reqVo.getBurst();
            if (burst == null) {
                return Result.ofFail(-1, "burst can't be null");
            }
            if (burst < 0) {
                return Result.ofFail(-1, "invalid burst: " + burst);
            }
            entity.setBurst(burst);
        } else if (CONTROL_BEHAVIOR_RATE_LIMITER == controlBehavior) {
            Integer maxQueueingTimeoutMs = reqVo.getMaxQueueingTimeoutMs();
            if (maxQueueingTimeoutMs == null) {
                return Result.ofFail(-1, "maxQueueingTimeoutMs can't be null");
            }
            if (maxQueueingTimeoutMs < 0) {
                return Result.ofFail(-1, "invalid maxQueueingTimeoutMs: " + maxQueueingTimeoutMs);
            }
            entity.setMaxQueueingTimeoutMs(maxQueueingTimeoutMs);
        }

        Date date = new Date();
        entity.setGmtModified(date);

        try {
            entity = repository.save(entity);
        } catch (Throwable throwable) {
            logger.error("update gateway flow rule error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }

        if (publishRules(app, entity.getIp(), entity.getPort())) {
            logger.warn("publish gateway flow rules fail after update");
        }

        return Result.ofSuccess(entity);
    }

    /**
     * 删除
     *
     * @param id ID
     * @return Result<Long>
     */
    @PostMapping("/delete.json")
    @AuthAction(AuthService.PrivilegeType.DELETE_RULE)
    public Result<Long> deleteFlowRule(Long id) {

        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }

        GatewayFlowRuleEntity oldEntity = repository.findById(id);
        if (oldEntity == null) {
            return Result.ofSuccess(null);
        }

        try {
            repository.delete(id);
        } catch (Throwable throwable) {
            logger.error("delete gateway flow rule error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }

        if (publishRules(oldEntity.getApp(), oldEntity.getIp(), oldEntity.getPort())) {
            logger.warn("publish gateway flow rules fail after delete");
        }

        return Result.ofSuccess(id);
    }

    /**
     * 发布规则
     *
     * @param app  应用名称
     * @param ip   IP地址
     * @param port 端口号
     * @return boolean
     */
    private boolean publishRules(String app, String ip, Integer port) {
        List<GatewayFlowRuleEntity> rules = repository.findAllByMachine(MachineInfo.of(app, ip, port));
        return !sentinelApiClient.modifyGatewayFlowRules(app, ip, port, rules);
    }
}
