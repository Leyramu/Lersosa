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

import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;

import java.util.List;

/**
 * 用于存储和查找规则的接口
 *
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
public interface RuleRepository<T, ID> {

    /**
     * 保存一个
     *
     * @param entity 要保存的实体
     * @return 保存的实体
     */
    T save(T entity);

    /**
     * 全部保存
     *
     * @param rules 规则
     * @return 保存的规则
     */
    List<T> saveAll(List<T> rules);

    /**
     * 按 ID 删除
     *
     * @param id ID
     * @return 实体已删除
     */
    T delete(ID id);

    /**
     * 按 ID 查找
     *
     * @param id ID
     * @return 实体
     */
    T findById(ID id);

    /**
     * 按机器查找全部
     *
     * @param machineInfo 机器信息
     * @return 实体
     */
    List<T> findAllByMachine(MachineInfo machineInfo);

    /**
     * 按应用查找全部
     *
     * @param appName 有效的 App 名称
     * @return 应用程序的所有规则
     */
    List<T> findAllByApp(String appName);
}
