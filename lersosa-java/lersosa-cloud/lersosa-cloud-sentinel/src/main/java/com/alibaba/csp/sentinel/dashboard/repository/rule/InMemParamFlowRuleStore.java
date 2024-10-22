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

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowClusterConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 内存存储的{@link ParamFlowRuleEntity}仓库实现
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 0..2.1
 * @since 2024/9/3
 */
@Component
public class InMemParamFlowRuleStore extends InMemoryRuleRepositoryAdapter<ParamFlowRuleEntity> {

    /**
     * 唯一ID生成器
     */
    private static final AtomicLong IDS = new AtomicLong(0);

    /**
     * 获取下一个ID
     *
     * @return 下一个ID
     */
    @Override
    protected long nextId() {
        return IDS.incrementAndGet();
    }

    /**
     * 规则实体预处理
     *
     * @param entity 规则实体
     * @return 处理后的实体
     */
    @Override
    protected ParamFlowRuleEntity preProcess(ParamFlowRuleEntity entity) {
        if (entity != null && entity.isClusterMode()) {
            ParamFlowClusterConfig config = entity.getClusterConfig();
            if (config == null) {
                config = new ParamFlowClusterConfig();
            }
            config.setFlowId(entity.getId());
        }
        return entity;
    }
}
