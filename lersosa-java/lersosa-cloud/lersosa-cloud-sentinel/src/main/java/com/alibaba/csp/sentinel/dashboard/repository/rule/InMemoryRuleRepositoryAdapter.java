/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
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
 * 内存存储的规则仓库实现
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 0..2.1
 * @since 2024/9/3
 */
public abstract class InMemoryRuleRepositoryAdapter<T extends RuleEntity> implements RuleRepository<T, Long> {

    /**
     * {@code <machine, <id, rule>>}
     */
    private final Map<MachineInfo, Map<Long, T>> machineRules = new ConcurrentHashMap<>(16);

    /**
     * {@code <id, rule>}
     */
    private final Map<Long, T> allRules = new ConcurrentHashMap<>(16);

    /**
     * {@code <app, <id, rule>>}
     */
    private final Map<String, Map<Long, T>> appRules = new ConcurrentHashMap<>(16);

    /**
     * 最大规则数量
     */
    private static final int MAX_RULES_SIZE = 10000;

    /**
     * 保存规则
     *
     * @param entity 规则实体
     * @return 保存后的规则实体
     */
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

    /**
     * 保存所有规则
     *
     * @param rules 规则列表
     * @return 保存后的规则列表
     */
    @Override
    public List<T> saveAll(List<T> rules) {
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

    /**
     * 删除规则
     *
     * @param id 规则id
     * @return 删除的规则
     */
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

    /**
     * 根据id查找规则
     *
     * @param id 规则id
     * @return 规则实体
     */
    @Override
    public T findById(Long id) {
        return allRules.get(id);
    }

    /**
     * 根据机器信息查找规则
     *
     * @param machineInfo 机器信息
     * @return 规则实体列表
     */
    @Override
    public List<T> findAllByMachine(MachineInfo machineInfo) {
        Map<Long, T> entities = machineRules.get(machineInfo);
        if (entities == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(entities.values());
    }

    /**
     * 根据应用名称查找规则
     *
     * @param appName 应用名称
     * @return 规则实体列表
     */
    @Override
    public List<T> findAllByApp(String appName) {
        AssertUtil.notEmpty(appName, "appName cannot be empty");
        Map<Long, T> entities = appRules.get(appName);
        if (entities == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(entities.values());
    }

    /**
     * 清空所有规则
     */
    public void clearAll() {
        allRules.clear();
        machineRules.clear();
        appRules.clear();
    }

    /**
     * 预处理规则
     *
     * @param entity 规则实体
     * @return 处理后的规则实体
     */
    protected T preProcess(T entity) {
        return entity;
    }

    /**
     * 获取下一个未使用的 ID
     *
     * @return 下一个未使用的 ID
     */
    abstract protected long nextId();
}
