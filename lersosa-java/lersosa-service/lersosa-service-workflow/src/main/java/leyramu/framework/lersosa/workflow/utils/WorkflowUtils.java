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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.core.utils.StreamUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.mail.utils.MailUtils;
import leyramu.framework.lersosa.common.satoken.utils.LoginHelper;
import leyramu.framework.lersosa.common.tenant.helper.TenantHelper;
import leyramu.framework.lersosa.common.websocket.dto.WebSocketMessageDto;
import leyramu.framework.lersosa.common.websocket.utils.WebSocketUtils;
import leyramu.framework.lersosa.system.api.RemoteUserService;
import leyramu.framework.lersosa.system.api.domain.vo.RemoteUserVo;
import leyramu.framework.lersosa.system.api.model.RoleDTO;
import leyramu.framework.lersosa.workflow.common.constant.FlowConstant;
import leyramu.framework.lersosa.workflow.common.enums.MessageTypeEnum;
import leyramu.framework.lersosa.workflow.common.enums.TaskStatusEnum;
import leyramu.framework.lersosa.workflow.domain.ActHiTaskinst;
import leyramu.framework.lersosa.workflow.domain.vo.MultiInstanceVo;
import leyramu.framework.lersosa.workflow.domain.vo.ParticipantVo;
import leyramu.framework.lersosa.workflow.flowable.cmd.UpdateHiTaskInstCmd;
import leyramu.framework.lersosa.workflow.mapper.ActHiTaskinstMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.*;

/**
 * 工作流工具.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkflowUtils {

    private static final ProcessEngine PROCESS_ENGINE = SpringUtils.getBean(ProcessEngine.class);
    private static final ActHiTaskinstMapper ACT_HI_TASKINST_MAPPER = SpringUtils.getBean(ActHiTaskinstMapper.class);

    /**
     * 创建一个新任务.
     *
     * @param currentTask 参数
     */
    public static TaskEntity createNewTask(Task currentTask) {
        TaskEntity task = null;
        if (ObjectUtil.isNotEmpty(currentTask)) {
            task = (TaskEntity) PROCESS_ENGINE.getTaskService().newTask();
            task.setCategory(currentTask.getCategory());
            task.setDescription(currentTask.getDescription());
            task.setAssignee(currentTask.getAssignee());
            task.setName(currentTask.getName());
            task.setProcessDefinitionId(currentTask.getProcessDefinitionId());
            task.setProcessInstanceId(currentTask.getProcessInstanceId());
            task.setTaskDefinitionKey(currentTask.getTaskDefinitionKey());
            task.setPriority(currentTask.getPriority());
            task.setCreateTime(new Date());
            task.setTenantId(TenantHelper.getTenantId());
            PROCESS_ENGINE.getTaskService().saveTask(task);
        }
        if (ObjectUtil.isNotNull(task)) {
            UpdateHiTaskInstCmd updateHiTaskInstCmd = new UpdateHiTaskInstCmd(Collections.singletonList(task.getId()), task.getProcessDefinitionId(), task.getProcessInstanceId());
            PROCESS_ENGINE.getManagementService().executeCommand(updateHiTaskInstCmd);
        }
        return task;
    }

    /**
     * 抄送任务.
     *
     * @param parentTaskList 父级任务
     * @param userIds        人员id
     */
    public static void createCopyTask(List<Task> parentTaskList, List<Long> userIds) {
        List<Task> list = new ArrayList<>();
        String tenantId = TenantHelper.getTenantId();
        for (Task parentTask : parentTaskList) {
            for (Long userId : userIds) {
                TaskEntity newTask = (TaskEntity) PROCESS_ENGINE.getTaskService().newTask();
                newTask.setParentTaskId(parentTask.getId());
                newTask.setAssignee(userId.toString());
                newTask.setName("【抄送】-" + parentTask.getName());
                newTask.setProcessDefinitionId(parentTask.getProcessDefinitionId());
                newTask.setProcessInstanceId(parentTask.getProcessInstanceId());
                newTask.setTaskDefinitionKey(parentTask.getTaskDefinitionKey());
                newTask.setTenantId(tenantId);
                list.add(newTask);
            }
        }
        PROCESS_ENGINE.getTaskService().bulkSaveTasks(list);
        if (CollUtil.isNotEmpty(list) && CollUtil.isNotEmpty(parentTaskList)) {
            String processInstanceId = parentTaskList.getFirst().getProcessInstanceId();
            String processDefinitionId = parentTaskList.getFirst().getProcessDefinitionId();
            List<String> taskIds = StreamUtils.toList(list, Task::getId);
            ActHiTaskinst actHiTaskinst = new ActHiTaskinst();
            actHiTaskinst.setProcDefId(processDefinitionId);
            actHiTaskinst.setProcInstId(processInstanceId);
            actHiTaskinst.setScopeType(TaskStatusEnum.COPY.getStatus());
            actHiTaskinst.setTenantId(tenantId);
            LambdaUpdateWrapper<ActHiTaskinst> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(ActHiTaskinst::getId, taskIds);
            ACT_HI_TASKINST_MAPPER.update(actHiTaskinst, updateWrapper);
            for (Task task : list) {
                PROCESS_ENGINE.getTaskService().addComment(task.getId(), task.getProcessInstanceId(), TaskStatusEnum.COPY.getStatus(), StrUtil.EMPTY);
            }
        }
    }

    /**
     * 获取当前任务参与者.
     *
     * @param taskId 任务id
     */
    public static ParticipantVo getCurrentTaskParticipant(String taskId, RemoteUserService remoteUserService) {
        ParticipantVo participantVo = new ParticipantVo();
        List<HistoricIdentityLink> linksForTask = PROCESS_ENGINE.getHistoryService().getHistoricIdentityLinksForTask(taskId);
        Task task = QueryUtils.taskQuery().taskId(taskId).singleResult();
        if (task != null && CollUtil.isNotEmpty(linksForTask)) {
            List<HistoricIdentityLink> groupList = StreamUtils.filter(linksForTask, e -> StringUtils.isNotBlank(e.getGroupId()));
            if (CollUtil.isNotEmpty(groupList)) {
                List<Long> groupIds = StreamUtils.toList(groupList, e -> Long.valueOf(e.getGroupId()));

                List<Long> userIds = remoteUserService.selectUserIdsByRoleIds(groupIds);
                if (CollUtil.isNotEmpty(userIds)) {
                    participantVo.setGroupIds(groupIds);
                    List<RemoteUserVo> userList = remoteUserService.selectListByIds(userIds);
                    if (CollUtil.isNotEmpty(userList)) {
                        List<Long> userIdList = StreamUtils.toList(userList, RemoteUserVo::getUserId);
                        List<String> nickNames = StreamUtils.toList(userList, RemoteUserVo::getNickName);
                        participantVo.setCandidate(userIdList);
                        participantVo.setCandidateName(nickNames);
                        participantVo.setClaim(!StringUtils.isBlank(task.getAssignee()));
                    }
                }
            } else {
                List<HistoricIdentityLink> candidateList = StreamUtils.filter(linksForTask, e -> FlowConstant.CANDIDATE.equals(e.getType()));
                List<Long> userIdList = new ArrayList<>();
                for (HistoricIdentityLink historicIdentityLink : linksForTask) {
                    try {
                        userIdList.add(Long.valueOf(historicIdentityLink.getUserId()));
                    } catch (NumberFormatException ignored) {

                    }
                }
                List<RemoteUserVo> userList = remoteUserService.selectListByIds(userIdList);
                if (CollUtil.isNotEmpty(userList)) {
                    List<Long> userIds = StreamUtils.toList(userList, RemoteUserVo::getUserId);
                    List<String> nickNames = StreamUtils.toList(userList, RemoteUserVo::getNickName);
                    participantVo.setCandidate(userIds);
                    participantVo.setCandidateName(nickNames);
                    // 判断当前任务是否具有多个办理人
                    if (CollUtil.isNotEmpty(candidateList) && candidateList.size() > 1) {
                        // 如果 assignee 存在，则设置当前任务已经被认领
                        participantVo.setClaim(StringUtils.isNotBlank(task.getAssignee()));
                    }
                }
            }
        }
        return participantVo;
    }

    /**
     * 判断当前节点是否为会签节点.
     *
     * @param processDefinitionId 流程定义id
     * @param taskDefinitionKey   流程定义id
     */
    public static MultiInstanceVo isMultiInstance(String processDefinitionId, String taskDefinitionKey) {
        BpmnModel bpmnModel = PROCESS_ENGINE.getRepositoryService().getBpmnModel(processDefinitionId);
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(taskDefinitionKey);
        MultiInstanceVo multiInstanceVo = new MultiInstanceVo();
        //判断是否为并行会签节点
        if (flowNode.getBehavior() instanceof ParallelMultiInstanceBehavior behavior && behavior.getCollectionExpression() != null) {
            Expression collectionExpression = behavior.getCollectionExpression();
            String assigneeList = collectionExpression.getExpressionText();
            String assignee = behavior.getCollectionElementVariable();
            multiInstanceVo.setType(behavior);
            multiInstanceVo.setAssignee(assignee);
            multiInstanceVo.setAssigneeList(assigneeList);
            return multiInstanceVo;
            //判断是否为串行会签节点
        } else if (flowNode.getBehavior() instanceof SequentialMultiInstanceBehavior behavior && behavior.getCollectionExpression() != null) {
            Expression collectionExpression = behavior.getCollectionExpression();
            String assigneeList = collectionExpression.getExpressionText();
            String assignee = behavior.getCollectionElementVariable();
            multiInstanceVo.setType(behavior);
            multiInstanceVo.setAssignee(assignee);
            multiInstanceVo.setAssigneeList(assigneeList);
            return multiInstanceVo;
        }
        return null;
    }

    /**
     * 获取当前流程状态.
     *
     * @param taskId 任务id
     */
    public static String getBusinessStatusByTaskId(String taskId) {
        HistoricTaskInstance historicTaskInstance = QueryUtils.hisTaskInstanceQuery().taskId(taskId).singleResult();
        HistoricProcessInstance historicProcessInstance = QueryUtils.hisInstanceQuery(historicTaskInstance.getProcessInstanceId()).singleResult();
        return historicProcessInstance.getBusinessStatus();
    }

    /**
     * 获取当前流程状态.
     *
     * @param businessKey 业务id
     */
    public static String getBusinessStatus(String businessKey) {
        HistoricProcessInstance historicProcessInstance = QueryUtils.hisBusinessKeyQuery(businessKey).singleResult();
        return historicProcessInstance.getBusinessStatus();
    }

    /**
     * 发送消息.
     *
     * @param list        任务
     * @param name        流程名称
     * @param messageType 消息类型
     * @param message     消息内容，为空则发送默认配置的消息内容
     */
    public static void sendMessage(List<Task> list, String name, List<String> messageType, String message, RemoteUserService remoteUserService) {
        Set<Long> userIds = new HashSet<>();
        if (StringUtils.isBlank(message)) {
            message = "有新的【" + name + "】单据已经提交至您的待办，请您及时处理。";
        }
        for (Task t : list) {
            ParticipantVo taskParticipant = WorkflowUtils.getCurrentTaskParticipant(t.getId(), remoteUserService);
            if (CollUtil.isNotEmpty(taskParticipant.getGroupIds())) {
                List<Long> userIdList = remoteUserService.selectUserIdsByRoleIds(taskParticipant.getGroupIds());
                if (CollUtil.isNotEmpty(userIdList)) {
                    userIds.addAll(userIdList);
                }
            }
            List<Long> candidate = taskParticipant.getCandidate();
            if (CollUtil.isNotEmpty(candidate)) {
                userIds.addAll(candidate);
            }
        }
        if (CollUtil.isNotEmpty(userIds)) {
            List<RemoteUserVo> userList = remoteUserService.selectListByIds(new ArrayList<>(userIds));
            for (String code : messageType) {
                MessageTypeEnum messageTypeEnum = MessageTypeEnum.getByCode(code);
                if (ObjectUtil.isNotEmpty(messageTypeEnum)) {
                    switch (messageTypeEnum) {
                        case SYSTEM_MESSAGE:
                            WebSocketMessageDto dto = new WebSocketMessageDto();
                            dto.setSessionKeys(new ArrayList<>(userIds));
                            dto.setMessage(message);
                            WebSocketUtils.publishMessage(dto);
                            break;
                        case EMAIL_MESSAGE:
                            MailUtils.sendText(StreamUtils.join(userList, RemoteUserVo::getEmail), "单据审批提醒", message);
                            break;
                        case SMS_MESSAGE:
                            //todo 短信发送
                            break;
                    }
                }
            }
        }
    }

    /**
     * 根据任务id查询 当前用户的任务，检查 当前人员 是否是该 taskId 的办理人.
     *
     * @param taskId 任务id
     * @return 结果
     */
    public static Task getTaskByCurrentUser(String taskId) {
        TaskQuery taskQuery = QueryUtils.taskQuery();
        taskQuery.taskId(taskId).taskCandidateOrAssigned(String.valueOf(LoginHelper.getUserId()));

        List<RoleDTO> roles = Objects.requireNonNull(LoginHelper.getLoginUser()).getRoles();
        if (CollUtil.isNotEmpty(roles)) {
            List<String> groupIds = StreamUtils.toList(roles, e -> String.valueOf(e.getRoleId()));
            taskQuery.taskCandidateGroupIn(groupIds);
        }
        return taskQuery.singleResult();
    }
}
