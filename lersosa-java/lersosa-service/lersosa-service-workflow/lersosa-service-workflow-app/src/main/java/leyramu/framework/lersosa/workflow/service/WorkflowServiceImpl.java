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

package leyramu.framework.lersosa.workflow.service;

import cn.hutool.core.util.StrUtil;
import leyramu.framework.lersosa.workflow.api.IActHiProcinstService;
import leyramu.framework.lersosa.workflow.api.IActProcessInstanceService;
import leyramu.framework.lersosa.workflow.api.WorkflowService;
import leyramu.framework.lersosa.workflow.domain.ActHiProcinst;
import leyramu.framework.lersosa.workflow.utils.WorkflowUtils;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 通用 工作流服务实现.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@Service
public class WorkflowServiceImpl implements WorkflowService {

    private final IActProcessInstanceService actProcessInstanceService;
    private final IActHiProcinstService actHiProcinstService;
    private final TaskService taskService;

    /**
     * 运行中的实例 删除程实例，删除历史记录，删除业务与流程关联信息.
     *
     * @param businessKeys 业务id
     */
    @Override
    public void deleteRunAndHisInstance(List<String> businessKeys) {
        actProcessInstanceService.deleteRunAndHisInstance(businessKeys);
    }

    /**
     * 获取当前流程状态.
     *
     * @param taskId 任务id
     */
    @Override
    public String getBusinessStatusByTaskId(String taskId) {
        return WorkflowUtils.getBusinessStatusByTaskId(taskId);
    }

    /**
     * 获取当前流程状态.
     *
     * @param businessKey 业务id
     */
    @Override
    public String getBusinessStatus(String businessKey) {
        return WorkflowUtils.getBusinessStatus(businessKey);
    }

    /**
     * 设置流程变量(全局变量).
     *
     * @param taskId       任务id
     * @param variableName 变量名称
     * @param value        变量值
     */
    @Override
    public void setVariable(String taskId, String variableName, Object value) {
        taskService.setVariable(taskId, variableName, value);
    }

    /**
     * 设置流程变量(全局变量).
     *
     * @param taskId    任务id
     * @param variables 流程变量
     */
    @Override
    public void setVariables(String taskId, Map<String, Object> variables) {
        taskService.setVariables(taskId, variables);
    }

    /**
     * 设置流程变量(本地变量,非全局变量).
     *
     * @param taskId       任务id
     * @param variableName 变量名称
     * @param value        变量值
     */
    @Override
    public void setVariableLocal(String taskId, String variableName, Object value) {
        taskService.setVariableLocal(taskId, variableName, value);
    }

    /**
     * 设置流程变量(本地变量,非全局变量).
     *
     * @param taskId    任务id
     * @param variables 流程变量
     */
    @Override
    public void setVariablesLocal(String taskId, Map<String, Object> variables) {
        taskService.setVariablesLocal(taskId, variables);
    }

    /**
     * 按照业务id查询流程实例id.
     *
     * @param businessKey 业务id
     * @return 结果
     */
    @Override
    public String getInstanceIdByBusinessKey(String businessKey) {
        ActHiProcinst actHiProcinst = actHiProcinstService.selectByBusinessKey(businessKey);
        if (actHiProcinst == null) {
            return StrUtil.EMPTY;
        }
        return actHiProcinst.getId();
    }
}
