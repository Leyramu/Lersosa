/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
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
