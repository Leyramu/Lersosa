/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.flowable.cmd;

import leyramu.framework.lersosa.common.core.exception.ServiceException;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.HistoricTaskService;
import org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntity;

import java.util.Date;
import java.util.List;


/**
 * 修改流程历史.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class UpdateHiTaskInstCmd implements Command<Boolean> {

    private final List<String> taskIds;

    private final String processDefinitionId;

    private final String processInstanceId;

    public UpdateHiTaskInstCmd(List<String> taskIds, String processDefinitionId, String processInstanceId) {
        this.taskIds = taskIds;
        this.processDefinitionId = processDefinitionId;
        this.processInstanceId = processInstanceId;
    }

    @Override
    public Boolean execute(CommandContext commandContext) {
        try {
            HistoricTaskService historicTaskService = CommandContextUtil.getHistoricTaskService();
            for (String taskId : taskIds) {
                HistoricTaskInstanceEntity historicTask = historicTaskService.getHistoricTask(taskId);
                if (historicTask != null) {
                    historicTask.setProcessDefinitionId(processDefinitionId);
                    historicTask.setProcessInstanceId(processInstanceId);
                    historicTask.setCreateTime(new Date());
                    CommandContextUtil.getHistoricTaskService().updateHistoricTask(historicTask, true);
                }
            }
            return true;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
