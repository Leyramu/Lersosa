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

package com.alibaba.csp.sentinel.dashboard.repository.rule;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.util.AssertUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储规则的抽象类，用于实现规则的持久化存储.
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
public abstract class InMemoryRuleRepositoryAdapter<T extends RuleEntity> implements RuleRepository<T, Long> {

    @SuppressWarnings("unused")
    private static final int MAX_RULES_SIZE = 10000;
    /**
     * {@code <machine, <id, rule>>}.
     */
    private final Map<MachineInfo, Map<Long, T>> machineRules = new ConcurrentHashMap<>(16);
    private final Map<Long, T> allRules = new ConcurrentHashMap<>(16);
    private final Map<String, Map<Long, T>> appRules = new ConcurrentHashMap<>(16);

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(nextId());
        }
        T processedEntity = preProcess(entity);
        if (processedEntity != null) {
            allRules.put(processedEntity.getId(), processedEntity);
            machineRules.computeIfAbsent(MachineInfo.of(processedEntity.getApp(), processedEntity.getIp(),
                    processedEntity.getPort()), e -> new ConcurrentHashMap<>(32))
                .put(processedEntity.getId(), processedEntity);
            appRules.computeIfAbsent(processedEntity.getApp(), v -> new ConcurrentHashMap<>(32))
                .put(processedEntity.getId(), processedEntity);
        }

        return processedEntity;
    }

    @Override
    public List<T> saveAll(List<T> rules) {
        // TODO: check here.
        allRules.clear();
        machineRules.clear();
        appRules.clear();

        if (rules == null) {
            return null;
        }
        List<T> savedRules = new ArrayList<>(rules.size());
        for (T rule : rules) {
            savedRules.add(save(rule));
        }
        return savedRules;
    }

    @Override
    public T delete(Long id) {
        T entity = allRules.remove(id);
        if (entity != null) {
            if (appRules.get(entity.getApp()) != null) {
                appRules.get(entity.getApp()).remove(id);
            }
            machineRules.get(MachineInfo.of(entity.getApp(), entity.getIp(), entity.getPort())).remove(id);
        }
        return entity;
    }

    @Override
    public T findById(Long id) {
        return allRules.get(id);
    }

    @Override
    public List<T> findAllByMachine(MachineInfo machineInfo) {
        Map<Long, T> entities = machineRules.get(machineInfo);
        if (entities == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(entities.values());
    }

    @Override
    public List<T> findAllByApp(String appName) {
        AssertUtil.notEmpty(appName, "appName cannot be empty");
        Map<Long, T> entities = appRules.get(appName);
        if (entities == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(entities.values());
    }

    @SuppressWarnings("unused")
    public void clearAll() {
        allRules.clear();
        machineRules.clear();
        appRules.clear();
    }

    protected T preProcess(T entity) {
        return entity;
    }

    /**
     * 获取下一个未使用的 ID.
     *
     * @return 下一个未使用的 ID
     */
    abstract protected long nextId();
}
