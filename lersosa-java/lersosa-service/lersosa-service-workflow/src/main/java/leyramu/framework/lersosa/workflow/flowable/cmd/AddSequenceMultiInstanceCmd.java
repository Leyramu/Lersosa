/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.flowable.cmd;

import cn.hutool.core.collection.CollUtil;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static leyramu.framework.lersosa.workflow.common.constant.FlowConstant.NUMBER_OF_INSTANCES;

/**
 * 串行加签.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class AddSequenceMultiInstanceCmd implements Command<Void> {

    /**
     * 执行id.
     */
    private final String executionId;

    /**
     * 会签人员集合KEY.
     */
    private final String assigneeList;

    /**
     * 加签人员.
     */
    private final List<Long> assignees;

    public AddSequenceMultiInstanceCmd(String executionId, String assigneeList, List<Long> assignees) {
        this.executionId = executionId;
        this.assigneeList = assigneeList;
        this.assignees = assignees;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        ExecutionEntity entity = executionEntityManager.findById(executionId);
        // 多实例任务总数加 assignees.size()
        if (entity.getVariable(NUMBER_OF_INSTANCES) instanceof Integer nrOfInstances) {
            entity.setVariable(NUMBER_OF_INSTANCES, nrOfInstances + assignees.size());
        }
        // 设置流程变量
        if (entity.getVariable(assigneeList) instanceof List<?> userIds) {
            CollUtil.addAll(userIds, assignees);
            Map<String, Object> variables = new HashMap<>(16);
            variables.put(assigneeList, userIds);
            entity.setVariables(variables);
        }
        return null;
    }
}
