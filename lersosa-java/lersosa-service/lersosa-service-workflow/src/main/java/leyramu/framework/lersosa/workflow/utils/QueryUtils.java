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

package leyramu.framework.lersosa.workflow.utils;

import cn.hutool.core.bean.BeanUtil;
import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.tenant.helper.TenantHelper;
import leyramu.framework.lersosa.workflow.domain.vo.TaskVo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.DeploymentQuery;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 查询工具.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryUtils {

    private static final ProcessEngine PROCESS_ENGINE = SpringUtils.getBean(ProcessEngine.class);

    public static ModelQuery modelQuery() {
        ModelQuery query = PROCESS_ENGINE.getRepositoryService().createModelQuery();
        if (TenantHelper.isEnable()) {
            query.modelTenantId(TenantHelper.getTenantId());
        }
        return query;
    }

    public static ProcessDefinitionQuery definitionQuery() {
        ProcessDefinitionQuery query = PROCESS_ENGINE.getRepositoryService().createProcessDefinitionQuery();
        if (TenantHelper.isEnable()) {
            query.processDefinitionTenantId(TenantHelper.getTenantId());
        }
        return query;
    }

    public static DeploymentQuery deploymentQuery() {
        DeploymentQuery query = PROCESS_ENGINE.getRepositoryService().createDeploymentQuery();
        if (TenantHelper.isEnable()) {
            query.deploymentTenantId(TenantHelper.getTenantId());
        }
        return query;
    }

    @SuppressWarnings("unused")
    public static DeploymentQuery deploymentQuery(String deploymentId) {
        return deploymentQuery().deploymentId(deploymentId);
    }

    public static DeploymentQuery deploymentQuery(List<String> deploymentIds) {
        return deploymentQuery().deploymentIds(deploymentIds);
    }

    public static HistoricTaskInstanceQuery hisTaskInstanceQuery() {
        HistoricTaskInstanceQuery query = PROCESS_ENGINE.getHistoryService().createHistoricTaskInstanceQuery();
        if (TenantHelper.isEnable()) {
            query.taskTenantId(TenantHelper.getTenantId());
        }
        return query;
    }

    public static HistoricTaskInstanceQuery hisTaskInstanceQuery(String processInstanceId) {
        return hisTaskInstanceQuery().processInstanceId(processInstanceId);
    }

    public static HistoricTaskInstanceQuery hisTaskBusinessKeyQuery(String businessKey) {
        return hisTaskInstanceQuery().processInstanceBusinessKey(businessKey);
    }

    public static ProcessInstanceQuery instanceQuery() {
        ProcessInstanceQuery query = PROCESS_ENGINE.getRuntimeService().createProcessInstanceQuery();
        if (TenantHelper.isEnable()) {
            query.processInstanceTenantId(TenantHelper.getTenantId());
        }
        return query;
    }

    public static ProcessInstanceQuery instanceQuery(String processInstanceId) {
        return instanceQuery().processInstanceId(processInstanceId);
    }

    public static ProcessInstanceQuery businessKeyQuery(String businessKey) {
        return instanceQuery().processInstanceBusinessKey(businessKey);
    }

    public static ProcessInstanceQuery instanceQuery(Set<String> processInstanceIds) {
        return instanceQuery().processInstanceIds(processInstanceIds);
    }

    public static HistoricProcessInstanceQuery hisInstanceQuery() {
        HistoricProcessInstanceQuery query = PROCESS_ENGINE.getHistoryService().createHistoricProcessInstanceQuery();
        if (TenantHelper.isEnable()) {
            query.processInstanceTenantId(TenantHelper.getTenantId());
        }
        return query;
    }

    public static HistoricProcessInstanceQuery hisInstanceQuery(String processInstanceId) {
        return hisInstanceQuery().processInstanceId(processInstanceId);
    }

    public static HistoricProcessInstanceQuery hisBusinessKeyQuery(String businessKey) {
        return hisInstanceQuery().processInstanceBusinessKey(businessKey);
    }

    public static HistoricProcessInstanceQuery hisInstanceQuery(Set<String> processInstanceIds) {
        return hisInstanceQuery().processInstanceIds(processInstanceIds);
    }

    public static HistoricActivityInstanceQuery hisActivityInstanceQuery() {
        HistoricActivityInstanceQuery query = PROCESS_ENGINE.getHistoryService().createHistoricActivityInstanceQuery();
        if (TenantHelper.isEnable()) {
            query.activityTenantId(TenantHelper.getTenantId());
        }
        return query;
    }

    public static HistoricActivityInstanceQuery hisActivityInstanceQuery(String processInstanceId) {
        return hisActivityInstanceQuery().processInstanceId(processInstanceId);
    }

    public static TaskQuery taskQuery() {
        TaskQuery query = PROCESS_ENGINE.getTaskService().createTaskQuery();
        if (TenantHelper.isEnable()) {
            query.taskTenantId(TenantHelper.getTenantId());
        }
        return query;
    }

    public static TaskQuery taskQuery(String processInstanceId) {
        return taskQuery().processInstanceId(processInstanceId);
    }

    public static TaskQuery taskQuery(Collection<String> processInstanceIds) {
        return taskQuery().processInstanceIdIn(processInstanceIds);
    }

    /**
     * 按照任务id查询当前任务.
     *
     * @param taskId 任务id
     */
    public static TaskVo getTask(String taskId) {
        Task task = PROCESS_ENGINE.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return null;
        }
        ProcessInstance processInstance = QueryUtils.instanceQuery(task.getProcessInstanceId()).singleResult();
        TaskVo taskVo = BeanUtil.toBean(task, TaskVo.class);
        taskVo.setBusinessKey(processInstance.getBusinessKey());
        taskVo.setMultiInstance(WorkflowUtils.isMultiInstance(task.getProcessDefinitionId(), task.getTaskDefinitionKey()) != null);
        String businessStatus = WorkflowUtils.getBusinessStatus(taskVo.getBusinessKey());
        taskVo.setBusinessStatus(businessStatus);
        return taskVo;
    }
}
