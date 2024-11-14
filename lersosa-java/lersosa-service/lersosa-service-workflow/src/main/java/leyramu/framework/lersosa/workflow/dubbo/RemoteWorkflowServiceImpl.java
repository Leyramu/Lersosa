/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.dubbo;

import leyramu.framework.lersosa.workflow.api.domain.RemoteWorkflowService;
import leyramu.framework.lersosa.workflow.service.IActHiProcinstService;
import leyramu.framework.lersosa.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;
import java.util.Map;

/**
 * 远程调用工作流服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@DubboService
@RequiredArgsConstructor
public class RemoteWorkflowServiceImpl implements RemoteWorkflowService {

    private final WorkflowService workflowService;

    @SuppressWarnings("unused")
    private final IActHiProcinstService actHiProcinstService;

    @Override
    public void deleteRunAndHisInstance(List<String> businessKeys) {
        workflowService.deleteRunAndHisInstance(businessKeys);
    }

    @Override
    public String getBusinessStatusByTaskId(String taskId) {
        return workflowService.getBusinessStatusByTaskId(taskId);
    }

    @Override
    public String getBusinessStatus(String businessKey) {
        return workflowService.getBusinessStatus(businessKey);
    }

    @Override
    public void setVariable(String taskId, String variableName, Object value) {
        workflowService.setVariable(taskId, variableName, value);
    }

    @Override
    public void setVariables(String taskId, Map<String, Object> variables) {
        workflowService.setVariables(taskId, variables);
    }

    @Override
    public void setVariableLocal(String taskId, String variableName, Object value) {
        workflowService.setVariableLocal(taskId, variableName, value);
    }

    @Override
    public void setVariablesLocal(String taskId, Map<String, Object> variables) {
        workflowService.setVariablesLocal(taskId, variables);
    }

    /**
     * 按照业务id查询流程实例id.
     *
     * @param businessKey 业务id
     * @return 结果
     */
    @Override
    public String getInstanceIdByBusinessKey(String businessKey) {
        return workflowService.getInstanceIdByBusinessKey(businessKey);
    }
}
