/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.flowable.cmd;

import leyramu.framework.lersosa.common.core.utils.StreamUtils;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 获取并行网关执行后保留的执行实例数据.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class ExecutionChildByExecutionIdCmd implements Command<List<ExecutionEntity>>, Serializable {

    /**
     * 当前任务执行实例id.
     */
    private final String executionId;

    public ExecutionChildByExecutionIdCmd(String executionId) {
        this.executionId = executionId;
    }

    @Override
    public List<ExecutionEntity> execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        // 获取当前执行数据
        ExecutionEntity executionEntity = executionEntityManager.findById(executionId);
        // 通过当前执行数据的父执行，查询所有子执行数据
        List<ExecutionEntity> allChildrenExecution =
            executionEntityManager.collectChildren(executionEntity.getParent());
        return StreamUtils.filter(allChildrenExecution, e -> !e.isActive());
    }
}
