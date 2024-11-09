/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.flowable.handler;

import leyramu.framework.lersosa.workflow.common.enums.TaskStatusEnum;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.jobexecutor.TimerEventHandler;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.job.service.JobHandler;
import org.flowable.job.service.impl.persistence.entity.JobEntity;
import org.flowable.task.api.Task;
import org.flowable.variable.api.delegate.VariableScope;

/**
 * 办理超时(过期)任务
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class TaskTimeoutJobHandler extends TimerEventHandler implements JobHandler {

    public static final String TYPE = "taskTimeout";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void execute(JobEntity job, String configuration, VariableScope variableScope, CommandContext commandContext) {
        TaskService taskService = CommandContextUtil.getProcessEngineConfiguration(commandContext)
            .getTaskService();
        Task task = taskService.createTaskQuery().taskId(configuration).singleResult();
        if (task != null) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TaskStatusEnum.TIMEOUT.getStatus(), "超时自动审批!");
            taskService.complete(configuration);
        }
    }
}
