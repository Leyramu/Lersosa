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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import leyramu.framework.lersosa.common.core.enums.BusinessStatusEnum;
import leyramu.framework.lersosa.common.core.exception.ServiceException;
import leyramu.framework.lersosa.common.core.utils.StreamUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.common.satoken.utils.LoginHelper;
import leyramu.framework.lersosa.common.tenant.helper.TenantHelper;
import leyramu.framework.lersosa.resource.api.RemoteFileService;
import leyramu.framework.lersosa.system.api.RemoteUserService;
import leyramu.framework.lersosa.system.api.domain.vo.RemoteUserVo;
import leyramu.framework.lersosa.system.api.model.RoleDTO;
import leyramu.framework.lersosa.workflow.api.IActTaskService;
import leyramu.framework.lersosa.workflow.api.IWfDefinitionConfigService;
import leyramu.framework.lersosa.workflow.api.IWfNodeConfigService;
import leyramu.framework.lersosa.workflow.api.IWfTaskBackNodeService;
import leyramu.framework.lersosa.workflow.common.constant.FlowConstant;
import leyramu.framework.lersosa.workflow.common.enums.TaskStatusEnum;
import leyramu.framework.lersosa.workflow.domain.ActHiTaskinst;
import leyramu.framework.lersosa.workflow.domain.WfTaskBackNode;
import leyramu.framework.lersosa.workflow.domain.bo.*;
import leyramu.framework.lersosa.workflow.domain.vo.*;
import leyramu.framework.lersosa.workflow.flowable.cmd.*;
import leyramu.framework.lersosa.workflow.flowable.handler.FlowProcessEventHandler;
import leyramu.framework.lersosa.workflow.mapper.ActHiTaskinstMapper;
import leyramu.framework.lersosa.workflow.mapper.ActTaskMapper;
import leyramu.framework.lersosa.workflow.utils.ModelUtils;
import leyramu.framework.lersosa.workflow.utils.QueryUtils;
import leyramu.framework.lersosa.workflow.utils.WorkflowUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务 服务层实现.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ActTaskServiceImpl implements IActTaskService {

    private final ActTaskMapper actTaskMapper;
    private final IWfTaskBackNodeService wfTaskBackNodeService;
    private final ActHiTaskinstMapper actHiTaskinstMapper;
    private final IWfNodeConfigService wfNodeConfigService;
    private final IWfDefinitionConfigService wfDefinitionConfigService;
    private final FlowProcessEventHandler flowProcessEventHandler;

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final HistoryService historyService;

    private final IdentityService identityService;

    private final ManagementService managementService;
    @DubboReference
    private RemoteUserService remoteUserService;
    @DubboReference
    private RemoteFileService remoteFileService;

    /**
     * 启动任务.
     *
     * @param startProcessBo 启动流程参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> startWorkFlow(StartProcessBo startProcessBo) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(startProcessBo.getBusinessKey())) {
            throw new ServiceException("启动工作流时必须包含业务ID");
        }
        // 判断当前业务是否启动过流程
        HistoricProcessInstanceQuery query = QueryUtils.hisInstanceQuery();
        HistoricProcessInstance historicProcessInstance = query.processInstanceBusinessKey(startProcessBo.getBusinessKey()).singleResult();
        if (ObjectUtil.isNotEmpty(historicProcessInstance)) {
            BusinessStatusEnum.checkStartStatus(historicProcessInstance.getBusinessStatus());
        }
        List<Task> taskResult = QueryUtils.taskQuery().processInstanceBusinessKey(startProcessBo.getBusinessKey()).list();
        if (CollUtil.isNotEmpty(taskResult)) {
            if (CollUtil.isNotEmpty(startProcessBo.getVariables())) {
                taskService.setVariables(taskResult.getFirst().getId(), startProcessBo.getVariables());
            }
            map.put(FlowConstant.PROCESS_INSTANCE_ID, taskResult.getFirst().getProcessInstanceId());
            map.put("taskId", taskResult.getFirst().getId());
            return map;
        }
        WfDefinitionConfigVo wfDefinitionConfigVo = wfDefinitionConfigService.getByTableNameLastVersion(startProcessBo.getTableName());
        if (wfDefinitionConfigVo == null) {
            throw new ServiceException("请到流程定义绑定业务表名与流程KEY！");
        }
        // 设置启动人
        identityService.setAuthenticatedUserId(String.valueOf(LoginHelper.getUserId()));
        Authentication.setAuthenticatedUserId(String.valueOf(LoginHelper.getUserId()));
        // 启动流程实例（提交申请）
        Map<String, Object> variables = startProcessBo.getVariables();
        // 启动跳过表达式
        variables.put(FlowConstant.FLOWABLE_SKIP_EXPRESSION_ENABLED, true);
        // 流程发起人
        variables.put(FlowConstant.INITIATOR, (String.valueOf(LoginHelper.getUserId())));
        ProcessInstance pi;
        try {
            if (TenantHelper.isEnable()) {
                pi = runtimeService.startProcessInstanceByKeyAndTenantId(wfDefinitionConfigVo.getProcessKey(), startProcessBo.getBusinessKey(), variables, TenantHelper.getTenantId());
            } else {
                pi = runtimeService.startProcessInstanceByKey(wfDefinitionConfigVo.getProcessKey(), startProcessBo.getBusinessKey(), variables);
            }
        } catch (FlowableObjectNotFoundException e) {
            throw new ServiceException("找不到当前【" + wfDefinitionConfigVo.getProcessKey() + "】流程定义！");
        }
        // 将流程定义名称 作为 流程实例名称
        runtimeService.setProcessInstanceName(pi.getProcessInstanceId(), pi.getProcessDefinitionName());
        // 申请人执行流程
        List<Task> taskList = QueryUtils.taskQuery(pi.getId()).list();
        if (taskList.size() > 1) {
            throw new ServiceException("请检查流程第一个环节是否为申请人！");
        }

        runtimeService.updateBusinessStatus(pi.getProcessInstanceId(), BusinessStatusEnum.DRAFT.getStatus());
        taskService.setAssignee(taskList.getFirst().getId(), LoginHelper.getUserId().toString());
        taskService.setVariable(taskList.getFirst().getId(), FlowConstant.PROCESS_INSTANCE_ID, pi.getProcessInstanceId());
        taskService.setVariable(taskList.getFirst().getId(), FlowConstant.BUSINESS_KEY, pi.getBusinessKey());
        map.put("processInstanceId", pi.getProcessInstanceId());
        map.put("taskId", taskList.getFirst().getId());
        return map;
    }

    /**
     * 办理任务.
     *
     * @param completeTaskBo 办理任务参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(CompleteTaskBo completeTaskBo) {
        try {
            String userId = String.valueOf(LoginHelper.getUserId());
            Task task = WorkflowUtils.getTaskByCurrentUser(completeTaskBo.getTaskId());
            if (task == null) {
                throw new ServiceException(FlowConstant.MESSAGE_CURRENT_TASK_IS_NULL);
            }
            if (task.isSuspended()) {
                throw new ServiceException(FlowConstant.MESSAGE_SUSPENDED);
            }
            ProcessInstance processInstance = QueryUtils.instanceQuery(task.getProcessInstanceId()).singleResult();
            //办理委托任务
            if (ObjectUtil.isNotEmpty(task.getDelegationState()) && FlowConstant.PENDING.equals(task.getDelegationState().name())) {
                taskService.resolveTask(completeTaskBo.getTaskId());
                TaskEntity newTask = WorkflowUtils.createNewTask(task);
                taskService.addComment(newTask.getId(), task.getProcessInstanceId(), TaskStatusEnum.PASS.getStatus(), StringUtils.isNotBlank(completeTaskBo.getMessage()) ? completeTaskBo.getMessage() : StrUtil.EMPTY);
                taskService.complete(newTask.getId());
                return true;
            }
            //附件上传
            AttachmentCmd attachmentCmd = new AttachmentCmd(completeTaskBo.getFileId(), task.getId(), task.getProcessInstanceId(), remoteFileService);
            managementService.executeCommand(attachmentCmd);
            String businessStatus = WorkflowUtils.getBusinessStatus(processInstance.getBusinessKey());
            //流程提交监听
            if (BusinessStatusEnum.DRAFT.getStatus().equals(businessStatus) || BusinessStatusEnum.BACK.getStatus().equals(businessStatus) || BusinessStatusEnum.CANCEL.getStatus().equals(businessStatus)) {
                flowProcessEventHandler.processHandler(processInstance.getProcessDefinitionKey(), processInstance.getBusinessKey(), businessStatus, true);
            }
            runtimeService.updateBusinessStatus(task.getProcessInstanceId(), BusinessStatusEnum.WAITING.getStatus());
            //办理监听
            flowProcessEventHandler.processTaskHandler(processInstance.getProcessDefinitionKey(), task.getTaskDefinitionKey(),
                task.getId(), processInstance.getBusinessKey());
            //办理意见
            taskService.addComment(completeTaskBo.getTaskId(), task.getProcessInstanceId(), TaskStatusEnum.PASS.getStatus(), StringUtils.isBlank(completeTaskBo.getMessage()) ? "同意" : completeTaskBo.getMessage());
            //办理任务
            taskService.setAssignee(task.getId(), userId);
            if (CollUtil.isNotEmpty(completeTaskBo.getVariables())) {
                taskService.complete(completeTaskBo.getTaskId(), completeTaskBo.getVariables());
            } else {
                taskService.complete(completeTaskBo.getTaskId());
            }
            //记录执行过的流程任务节点
            wfTaskBackNodeService.recordExecuteNode(task);
            ProcessInstance pi = QueryUtils.instanceQuery(task.getProcessInstanceId()).singleResult();
            if (pi == null) {
                UpdateBusinessStatusCmd updateBusinessStatusCmd = new UpdateBusinessStatusCmd(task.getProcessInstanceId(), BusinessStatusEnum.FINISH.getStatus());
                managementService.executeCommand(updateBusinessStatusCmd);
                flowProcessEventHandler.processHandler(processInstance.getProcessDefinitionKey(), processInstance.getBusinessKey(),
                    BusinessStatusEnum.FINISH.getStatus(), false);
            } else {
                List<Task> list = QueryUtils.taskQuery(task.getProcessInstanceId()).list();
                for (Task t : list) {
                    if (ModelUtils.isUserTask(t.getProcessDefinitionId(), t.getTaskDefinitionKey())) {
                        List<HistoricIdentityLink> links = historyService.getHistoricIdentityLinksForTask(t.getId());
                        if (CollUtil.isEmpty(links) && StringUtils.isBlank(t.getAssignee())) {
                            throw new ServiceException("下一节点【" + t.getName() + "】没有办理人!");
                        }
                    }
                }

                if (CollUtil.isNotEmpty(list) && CollUtil.isNotEmpty(completeTaskBo.getWfCopyList())) {
                    TaskEntity newTask = WorkflowUtils.createNewTask(task);
                    taskService.addComment(newTask.getId(), task.getProcessInstanceId(), TaskStatusEnum.COPY.getStatus(), Objects.requireNonNull(LoginHelper.getLoginUser()).getNickname() + "【抄送】给" + String.join(",", StreamUtils.toList(completeTaskBo.getWfCopyList(), WfCopy::getUserName)));
                    taskService.complete(newTask.getId());
                    List<Task> taskList = QueryUtils.taskQuery(task.getProcessInstanceId()).list();
                    WorkflowUtils.createCopyTask(taskList, StreamUtils.toList(completeTaskBo.getWfCopyList(), WfCopy::getUserId));
                }
                sendMessage(list, processInstance.getName(), completeTaskBo.getMessageType(), null);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 发送消息.
     *
     * @param list        任务
     * @param name        流程名称
     * @param messageType 消息类型
     * @param message     消息内容，为空则发送默认配置的消息内容
     */
    @Async
    public void sendMessage(List<Task> list, String name, List<String> messageType, String message) {
        WorkflowUtils.sendMessage(list, name, messageType, message, remoteUserService);
    }

    /**
     * 查询当前用户的待办任务.
     *
     * @param taskBo 参数
     */
    @Override
    public TableDataInfo<TaskVo> getPageByTaskWait(TaskBo taskBo, PageQuery pageQuery) {
        QueryWrapper<TaskVo> queryWrapper = new QueryWrapper<>();
        List<RoleDTO> roles = Objects.requireNonNull(LoginHelper.getLoginUser()).getRoles();
        List<String> roleIds = StreamUtils.toList(roles, e -> String.valueOf(e.getRoleId()));
        String userId = String.valueOf(LoginHelper.getUserId());
        queryWrapper.eq("t.business_status_", BusinessStatusEnum.WAITING.getStatus());
        queryWrapper.eq(TenantHelper.isEnable(), "t.tenant_id_", TenantHelper.getTenantId());
        String ids = StreamUtils.join(roleIds, x -> "'" + x + "'");
        queryWrapper.and(w1 -> w1.eq("t.assignee_", userId).or(w2 -> w2.isNull("t.assignee_").apply("exists ( select LINK.ID_ from ACT_RU_IDENTITYLINK LINK where LINK.TASK_ID_ = t.ID_ and LINK.TYPE_ = 'candidate' and (LINK.USER_ID_ = {0} or ( LINK.GROUP_ID_ IN (" + ids + ") ) ))", userId)));
        if (StringUtils.isNotBlank(taskBo.getName())) {
            queryWrapper.like("t.name_", taskBo.getName());
        }
        if (StringUtils.isNotBlank(taskBo.getProcessDefinitionName())) {
            queryWrapper.like("t.processDefinitionName", taskBo.getProcessDefinitionName());
        }
        if (StringUtils.isNotBlank(taskBo.getProcessDefinitionKey())) {
            queryWrapper.eq("t.processDefinitionKey", taskBo.getProcessDefinitionKey());
        }
        queryWrapper.orderByDesc("t.CREATE_TIME_");
        Page<TaskVo> page = actTaskMapper.getTaskWaitByPage(pageQuery.build(), queryWrapper);

        List<TaskVo> taskList = page.getRecords();
        if (CollUtil.isNotEmpty(taskList)) {
            List<String> processDefinitionIds = StreamUtils.toList(taskList, TaskVo::getProcessDefinitionId);
            List<WfNodeConfigVo> wfNodeConfigVoList = wfNodeConfigService.selectByDefIds(processDefinitionIds);
            for (TaskVo task : taskList) {
                task.setBusinessStatusName(BusinessStatusEnum.findByStatus(task.getBusinessStatus()));
                task.setParticipantVo(WorkflowUtils.getCurrentTaskParticipant(task.getId(), remoteUserService));
                task.setMultiInstance(WorkflowUtils.isMultiInstance(task.getProcessDefinitionId(), task.getTaskDefinitionKey()) != null);
                if (CollUtil.isNotEmpty(wfNodeConfigVoList)) {
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && FlowConstant.TRUE.equals(e.getApplyUserTask())).findFirst().ifPresent(task::setWfNodeConfigVo);
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && e.getNodeId().equals(task.getTaskDefinitionKey()) && FlowConstant.FALSE.equals(e.getApplyUserTask())).findFirst().ifPresent(task::setWfNodeConfigVo);
                }
            }
        }
        return TableDataInfo.build(page);
    }

    /**
     * 查询当前租户所有待办任务.
     *
     * @param taskBo 参数
     */
    @Override
    public TableDataInfo<TaskVo> getPageByAllTaskWait(TaskBo taskBo, PageQuery pageQuery) {
        TaskQuery query = QueryUtils.taskQuery();
        if (StringUtils.isNotBlank(taskBo.getName())) {
            query.taskNameLike("%" + taskBo.getName() + "%");
        }
        if (StringUtils.isNotBlank(taskBo.getProcessDefinitionName())) {
            query.processDefinitionNameLike("%" + taskBo.getProcessDefinitionName() + "%");
        }
        if (StringUtils.isNotBlank(taskBo.getProcessDefinitionKey())) {
            query.processDefinitionKey(taskBo.getProcessDefinitionKey());
        }
        query.orderByTaskCreateTime().desc();
        List<Task> taskList = query.listPage(pageQuery.getFirstNum(), pageQuery.getPageSize());
        List<ProcessInstance> processInstanceList = null;
        if (CollUtil.isNotEmpty(taskList)) {
            Set<String> processInstanceIds = StreamUtils.toSet(taskList, Task::getProcessInstanceId);
            processInstanceList = QueryUtils.instanceQuery(processInstanceIds).list();
        }
        List<TaskVo> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(taskList)) {
            List<String> processDefinitionIds = StreamUtils.toList(taskList, Task::getProcessDefinitionId);
            List<WfNodeConfigVo> wfNodeConfigVoList = wfNodeConfigService.selectByDefIds(processDefinitionIds);
            for (Task task : taskList) {
                TaskVo taskVo = BeanUtil.toBean(task, TaskVo.class);
                if (CollUtil.isNotEmpty(processInstanceList)) {
                    processInstanceList.stream().filter(e -> e.getId().equals(task.getProcessInstanceId())).findFirst().ifPresent(e -> {
                        taskVo.setBusinessStatus(e.getBusinessStatus());
                        taskVo.setBusinessStatusName(BusinessStatusEnum.findByStatus(taskVo.getBusinessStatus()));
                        taskVo.setProcessDefinitionKey(e.getProcessDefinitionKey());
                        taskVo.setProcessDefinitionName(e.getProcessDefinitionName());
                        taskVo.setProcessDefinitionVersion(e.getProcessDefinitionVersion());
                        taskVo.setBusinessKey(e.getBusinessKey());
                    });
                }
                taskVo.setAssignee(StringUtils.isNotBlank(task.getAssignee()) ? Long.valueOf(task.getAssignee()) : null);
                taskVo.setParticipantVo(WorkflowUtils.getCurrentTaskParticipant(task.getId(), remoteUserService));
                taskVo.setMultiInstance(WorkflowUtils.isMultiInstance(task.getProcessDefinitionId(), task.getTaskDefinitionKey()) != null);
                if (CollUtil.isNotEmpty(wfNodeConfigVoList)) {
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && FlowConstant.TRUE.equals(e.getApplyUserTask())).findFirst().ifPresent(taskVo::setWfNodeConfigVo);
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && e.getNodeId().equals(task.getTaskDefinitionKey()) && FlowConstant.FALSE.equals(e.getApplyUserTask())).findFirst().ifPresent(taskVo::setWfNodeConfigVo);
                }
                list.add(taskVo);
            }
        }
        long count = query.count();
        TableDataInfo<TaskVo> build = TableDataInfo.build();
        build.setRows(list);
        build.setTotal(count);
        return build;
    }

    /**
     * 查询当前用户的已办任务.
     *
     * @param taskBo 参数
     */
    @Override
    public TableDataInfo<TaskVo> getPageByTaskFinish(TaskBo taskBo, PageQuery pageQuery) {
        String userId = String.valueOf(LoginHelper.getUserId());
        QueryWrapper<TaskVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(taskBo.getName()), "t.name_", taskBo.getName());
        queryWrapper.like(StringUtils.isNotBlank(taskBo.getProcessDefinitionName()), "t.processDefinitionName", taskBo.getProcessDefinitionName());
        queryWrapper.eq(StringUtils.isNotBlank(taskBo.getProcessDefinitionKey()), "t.processDefinitionKey", taskBo.getProcessDefinitionKey());
        queryWrapper.eq("t.assignee_", userId);
        Page<TaskVo> page = actTaskMapper.getTaskFinishByPage(pageQuery.build(), queryWrapper);

        List<TaskVo> taskList = page.getRecords();
        if (CollUtil.isNotEmpty(taskList)) {
            List<String> processDefinitionIds = StreamUtils.toList(taskList, TaskVo::getProcessDefinitionId);
            List<WfNodeConfigVo> wfNodeConfigVoList = wfNodeConfigService.selectByDefIds(processDefinitionIds);
            for (TaskVo task : taskList) {
                task.setBusinessStatusName(BusinessStatusEnum.findByStatus(task.getBusinessStatus()));
                if (CollUtil.isNotEmpty(wfNodeConfigVoList)) {
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && FlowConstant.TRUE.equals(e.getApplyUserTask())).findFirst().ifPresent(task::setWfNodeConfigVo);
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && e.getNodeId().equals(task.getTaskDefinitionKey()) && FlowConstant.FALSE.equals(e.getApplyUserTask())).findFirst().ifPresent(task::setWfNodeConfigVo);
                }
            }
        }
        return TableDataInfo.build(page);
    }

    /**
     * 查询当前用户的抄送.
     *
     * @param taskBo 参数
     */
    @Override
    public TableDataInfo<TaskVo> getPageByTaskCopy(TaskBo taskBo, PageQuery pageQuery) {
        QueryWrapper<TaskVo> queryWrapper = new QueryWrapper<>();
        String userId = String.valueOf(LoginHelper.getUserId());
        if (StringUtils.isNotBlank(taskBo.getName())) {
            queryWrapper.like("t.name_", taskBo.getName());
        }
        if (StringUtils.isNotBlank(taskBo.getProcessDefinitionName())) {
            queryWrapper.like("t.processDefinitionName", taskBo.getProcessDefinitionName());
        }
        if (StringUtils.isNotBlank(taskBo.getProcessDefinitionKey())) {
            queryWrapper.eq("t.processDefinitionKey", taskBo.getProcessDefinitionKey());
        }
        queryWrapper.eq("t.assignee_", userId);
        Page<TaskVo> page = actTaskMapper.getTaskCopyByPage(pageQuery.build(), queryWrapper);

        List<TaskVo> taskList = page.getRecords();
        if (CollUtil.isNotEmpty(taskList)) {
            List<String> processDefinitionIds = StreamUtils.toList(taskList, TaskVo::getProcessDefinitionId);
            List<WfNodeConfigVo> wfNodeConfigVoList = wfNodeConfigService.selectByDefIds(processDefinitionIds);
            for (TaskVo task : taskList) {
                task.setBusinessStatusName(BusinessStatusEnum.findByStatus(task.getBusinessStatus()));
                if (CollUtil.isNotEmpty(wfNodeConfigVoList)) {
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && FlowConstant.TRUE.equals(e.getApplyUserTask())).findFirst().ifPresent(task::setWfNodeConfigVo);
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && e.getNodeId().equals(task.getTaskDefinitionKey()) && FlowConstant.FALSE.equals(e.getApplyUserTask())).findFirst().ifPresent(task::setWfNodeConfigVo);
                }
            }
        }
        return TableDataInfo.build(page);
    }

    /**
     * 查询当前租户所有已办任务.
     *
     * @param taskBo 参数
     */
    @Override
    public TableDataInfo<TaskVo> getPageByAllTaskFinish(TaskBo taskBo, PageQuery pageQuery) {
        QueryWrapper<TaskVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(taskBo.getName()), "t.name_", taskBo.getName());
        queryWrapper.like(StringUtils.isNotBlank(taskBo.getProcessDefinitionName()), "t.processDefinitionName", taskBo.getProcessDefinitionName());
        queryWrapper.eq(StringUtils.isNotBlank(taskBo.getProcessDefinitionKey()), "t.processDefinitionKey", taskBo.getProcessDefinitionKey());
        Page<TaskVo> page = actTaskMapper.getTaskFinishByPage(pageQuery.build(), queryWrapper);

        List<TaskVo> taskList = page.getRecords();
        if (CollUtil.isNotEmpty(taskList)) {
            List<String> processDefinitionIds = StreamUtils.toList(taskList, TaskVo::getProcessDefinitionId);
            List<WfNodeConfigVo> wfNodeConfigVoList = wfNodeConfigService.selectByDefIds(processDefinitionIds);
            for (TaskVo task : taskList) {
                task.setBusinessStatusName(BusinessStatusEnum.findByStatus(task.getBusinessStatus()));
                if (CollUtil.isNotEmpty(wfNodeConfigVoList)) {
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && FlowConstant.TRUE.equals(e.getApplyUserTask())).findFirst().ifPresent(task::setWfNodeConfigVo);
                    wfNodeConfigVoList.stream().filter(e -> e.getDefinitionId().equals(task.getProcessDefinitionId()) && e.getNodeId().equals(task.getTaskDefinitionKey()) && FlowConstant.FALSE.equals(e.getApplyUserTask())).findFirst().ifPresent(task::setWfNodeConfigVo);
                }
            }
        }
        return TableDataInfo.build(page);
    }

    /**
     * 委派任务.
     *
     * @param delegateBo 参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delegateTask(DelegateBo delegateBo) {
        Task task = WorkflowUtils.getTaskByCurrentUser(delegateBo.getTaskId());

        if (ObjectUtil.isEmpty(task)) {
            throw new ServiceException(FlowConstant.MESSAGE_CURRENT_TASK_IS_NULL);
        }
        if (task.isSuspended()) {
            throw new ServiceException(FlowConstant.MESSAGE_SUSPENDED);
        }
        try {
            TaskEntity newTask = WorkflowUtils.createNewTask(task);
            taskService.addComment(newTask.getId(), task.getProcessInstanceId(), TaskStatusEnum.PENDING.getStatus(), "【" + Objects.requireNonNull(LoginHelper.getLoginUser()).getNickname() + "】委派给【" + delegateBo.getNickName() + "】");
            //委托任务
            taskService.delegateTask(delegateBo.getTaskId(), delegateBo.getUserId());
            //办理生成的任务记录
            taskService.complete(newTask.getId());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 终止任务.
     *
     * @param terminationBo 参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean terminationTask(TerminationBo terminationBo) {
        TaskQuery query = QueryUtils.taskQuery();
        Task task = query.taskId(terminationBo.getTaskId()).singleResult();

        if (ObjectUtil.isEmpty(task)) {
            throw new ServiceException(FlowConstant.MESSAGE_CURRENT_TASK_IS_NULL);
        }
        if (task.isSuspended()) {
            throw new ServiceException(FlowConstant.MESSAGE_SUSPENDED);
        }
        HistoricProcessInstance historicProcessInstance = QueryUtils.hisInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        BusinessStatusEnum.checkInvalidStatus(historicProcessInstance.getBusinessStatus());
        try {
            if (StringUtils.isBlank(terminationBo.getComment())) {
                terminationBo.setComment(Objects.requireNonNull(LoginHelper.getLoginUser()).getNickname() + "终止了申请");
            } else {
                terminationBo.setComment(Objects.requireNonNull(LoginHelper.getLoginUser()).getNickname() + "终止了申请：" + terminationBo.getComment());
            }
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TaskStatusEnum.TERMINATION.getStatus(), terminationBo.getComment());
            List<Task> list = QueryUtils.taskQuery(task.getProcessInstanceId()).list();
            if (CollUtil.isNotEmpty(list)) {
                List<Task> subTasks = StreamUtils.filter(list, e -> StringUtils.isNotBlank(e.getParentTaskId()));
                if (CollUtil.isNotEmpty(subTasks)) {
                    subTasks.forEach(e -> taskService.deleteTask(e.getId()));
                }
                runtimeService.updateBusinessStatus(task.getProcessInstanceId(), BusinessStatusEnum.TERMINATION.getStatus());
                runtimeService.deleteProcessInstance(task.getProcessInstanceId(), StrUtil.EMPTY);
            }
            //流程终止监听
            flowProcessEventHandler.processHandler(historicProcessInstance.getProcessDefinitionKey(),
                historicProcessInstance.getBusinessKey(), BusinessStatusEnum.TERMINATION.getStatus(), false);
            return true;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 转办任务.
     *
     * @param transmitBo 参数
     */
    @Override
    public boolean transferTask(TransmitBo transmitBo) {
        Task task = WorkflowUtils.getTaskByCurrentUser(transmitBo.getTaskId());
        if (ObjectUtil.isEmpty(task)) {
            throw new ServiceException(FlowConstant.MESSAGE_CURRENT_TASK_IS_NULL);
        }
        if (task.isSuspended()) {
            throw new ServiceException(FlowConstant.MESSAGE_SUSPENDED);
        }
        try {
            TaskEntity newTask = WorkflowUtils.createNewTask(task);
            taskService.addComment(newTask.getId(), task.getProcessInstanceId(), TaskStatusEnum.TRANSFER.getStatus(), StringUtils.isNotBlank(transmitBo.getComment()) ? transmitBo.getComment() : LoginHelper.getUsername() + "转办了任务");
            taskService.complete(newTask.getId());
            taskService.setAssignee(task.getId(), transmitBo.getUserId());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 会签任务加签.
     *
     * @param addMultiBo 参数
     */
    @Override
    public boolean addMultiInstanceExecution(AddMultiBo addMultiBo) {
        TaskQuery taskQuery = QueryUtils.taskQuery();
        taskQuery.taskId(addMultiBo.getTaskId());
        if (!LoginHelper.isSuperAdmin() && !LoginHelper.isTenantAdmin()) {
            taskQuery.taskCandidateOrAssigned(String.valueOf(LoginHelper.getUserId()));
        }
        Task task = taskQuery.singleResult();
        if (ObjectUtil.isEmpty(task)) {
            throw new ServiceException(FlowConstant.MESSAGE_CURRENT_TASK_IS_NULL);
        }
        if (task.isSuspended()) {
            throw new ServiceException(FlowConstant.MESSAGE_SUSPENDED);
        }
        String taskDefinitionKey = task.getTaskDefinitionKey();
        String processInstanceId = task.getProcessInstanceId();
        String processDefinitionId = task.getProcessDefinitionId();

        try {
            MultiInstanceVo multiInstanceVo = WorkflowUtils.isMultiInstance(processDefinitionId, taskDefinitionKey);
            if (multiInstanceVo == null) {
                throw new ServiceException("当前环节不是会签节点");
            }
            if (multiInstanceVo.getType() instanceof ParallelMultiInstanceBehavior) {
                for (Long assignee : addMultiBo.getAssignees()) {
                    runtimeService.addMultiInstanceExecution(taskDefinitionKey, processInstanceId, Collections.singletonMap(multiInstanceVo.getAssignee(), assignee));
                }
            } else if (multiInstanceVo.getType() instanceof SequentialMultiInstanceBehavior) {
                AddSequenceMultiInstanceCmd addSequenceMultiInstanceCmd = new AddSequenceMultiInstanceCmd(task.getExecutionId(), multiInstanceVo.getAssigneeList(), addMultiBo.getAssignees());
                managementService.executeCommand(addSequenceMultiInstanceCmd);
            }
            List<String> assigneeNames = addMultiBo.getAssigneeNames();
            String username = LoginHelper.getUsername();
            TaskEntity newTask = WorkflowUtils.createNewTask(task);
            taskService.addComment(newTask.getId(), processInstanceId, TaskStatusEnum.SIGN.getStatus(), username + "加签【" + String.join(StringUtils.SEPARATOR, assigneeNames) + "】");
            taskService.complete(newTask.getId());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 会签任务减签.
     *
     * @param deleteMultiBo 参数
     */
    @Override
    public boolean deleteMultiInstanceExecution(DeleteMultiBo deleteMultiBo) {
        TaskQuery taskQuery = QueryUtils.taskQuery();
        taskQuery.taskId(deleteMultiBo.getTaskId());
        if (!LoginHelper.isSuperAdmin() && !LoginHelper.isTenantAdmin()) {
            taskQuery.taskCandidateOrAssigned(String.valueOf(LoginHelper.getUserId()));
        }
        Task task = taskQuery.singleResult();
        if (ObjectUtil.isEmpty(task)) {
            throw new ServiceException(FlowConstant.MESSAGE_CURRENT_TASK_IS_NULL);
        }
        if (task.isSuspended()) {
            throw new ServiceException(FlowConstant.MESSAGE_SUSPENDED);
        }
        String taskDefinitionKey = task.getTaskDefinitionKey();
        String processInstanceId = task.getProcessInstanceId();
        String processDefinitionId = task.getProcessDefinitionId();
        try {
            MultiInstanceVo multiInstanceVo = WorkflowUtils.isMultiInstance(processDefinitionId, taskDefinitionKey);
            if (multiInstanceVo == null) {
                throw new ServiceException("当前环节不是会签节点");
            }
            if (multiInstanceVo.getType() instanceof ParallelMultiInstanceBehavior) {
                for (String executionId : deleteMultiBo.getExecutionIds()) {
                    runtimeService.deleteMultiInstanceExecution(executionId, false);
                }
                for (String taskId : deleteMultiBo.getTaskIds()) {
                    historyService.deleteHistoricTaskInstance(taskId);
                }
            } else if (multiInstanceVo.getType() instanceof SequentialMultiInstanceBehavior) {
                DeleteSequenceMultiInstanceCmd deleteSequenceMultiInstanceCmd = new DeleteSequenceMultiInstanceCmd(task.getAssignee(), task.getExecutionId(), multiInstanceVo.getAssigneeList(), deleteMultiBo.getAssigneeIds());
                managementService.executeCommand(deleteSequenceMultiInstanceCmd);
            }
            List<String> assigneeNames = deleteMultiBo.getAssigneeNames();
            String username = LoginHelper.getUsername();
            TaskEntity newTask = WorkflowUtils.createNewTask(task);
            taskService.addComment(newTask.getId(), processInstanceId, TaskStatusEnum.SIGN_OFF.getStatus(), username + "减签【" + String.join(StringUtils.SEPARATOR, assigneeNames) + "】");
            taskService.complete(newTask.getId());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 驳回审批.
     *
     * @param backProcessBo 参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String backProcess(BackProcessBo backProcessBo) {
        String userId = String.valueOf(LoginHelper.getUserId());
        Task task = WorkflowUtils.getTaskByCurrentUser(backProcessBo.getTaskId());

        if (ObjectUtil.isEmpty(task)) {
            throw new ServiceException(FlowConstant.MESSAGE_CURRENT_TASK_IS_NULL);
        }
        if (task.isSuspended()) {
            throw new ServiceException(FlowConstant.MESSAGE_SUSPENDED);
        }
        try {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = QueryUtils.instanceQuery(task.getProcessInstanceId()).singleResult();
            //获取并行网关执行后保留的执行实例数据
            ExecutionChildByExecutionIdCmd childByExecutionIdCmd = new ExecutionChildByExecutionIdCmd(task.getExecutionId());
            List<ExecutionEntity> executionEntities = managementService.executeCommand(childByExecutionIdCmd);
            //校验单据
            BusinessStatusEnum.checkBackStatus(processInstance.getBusinessStatus());
            //判断是否有多个任务
            List<Task> taskList = QueryUtils.taskQuery(processInstanceId).list();
            String backTaskDefinitionKey = backProcessBo.getTargetActivityId();
            taskService.addComment(task.getId(), processInstanceId, TaskStatusEnum.BACK.getStatus(), StringUtils.isNotBlank(backProcessBo.getMessage()) ? backProcessBo.getMessage() : "退回");
            if (taskList.size() > 1) {
                //当前多个任务驳回到单个节点
                runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId).moveActivityIdsToSingleActivityId(taskList.stream().map(Task::getTaskDefinitionKey).distinct().collect(Collectors.toList()), backTaskDefinitionKey).changeState();
                ActHiTaskinst actHiTaskinst = new ActHiTaskinst();
                actHiTaskinst.setAssignee(userId);
                actHiTaskinst.setId(task.getId());
                actHiTaskinstMapper.updateById(actHiTaskinst);
            } else {
                //当前单个节点驳回单个节点
                runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId).moveActivityIdTo(task.getTaskDefinitionKey(), backTaskDefinitionKey).changeState();
            }
            //删除并行环节未办理记录
            MultiInstanceVo multiInstance = WorkflowUtils.isMultiInstance(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
            if (multiInstance == null && taskList.size() > 1) {
                List<Task> tasks = StreamUtils.filter(taskList, e -> !e.getTaskDefinitionKey().equals(task.getTaskDefinitionKey()));
                if (CollUtil.isNotEmpty(tasks)) {
                    actHiTaskinstMapper.deleteByIds(StreamUtils.toList(tasks, Task::getId));
                }
            }


            List<HistoricTaskInstance> instanceList = QueryUtils.hisTaskInstanceQuery(processInstanceId).finished().orderByHistoricTaskInstanceEndTime().desc().list();
            List<Task> list = QueryUtils.taskQuery(processInstanceId).list();
            for (Task t : list) {
                instanceList.stream().filter(e -> e.getTaskDefinitionKey().equals(t.getTaskDefinitionKey())).findFirst().ifPresent(e -> taskService.setAssignee(t.getId(), e.getAssignee()));
            }
            //发送消息
            String message = "您的【" + processInstance.getName() + "】单据已经被驳回，请您注意查收。";
            sendMessage(list, processInstance.getName(), backProcessBo.getMessageType(), message);
            //删除流程实例垃圾数据
            for (ExecutionEntity executionEntity : executionEntities) {
                DeleteExecutionCmd deleteExecutionCmd = new DeleteExecutionCmd(executionEntity.getId());
                managementService.executeCommand(deleteExecutionCmd);
            }

            WfTaskBackNode wfTaskBackNode = wfTaskBackNodeService.getListByInstanceIdAndNodeId(task.getProcessInstanceId(), backProcessBo.getTargetActivityId());
            if (ObjectUtil.isNotNull(wfTaskBackNode) && wfTaskBackNode.getOrderNo() == 0) {
                runtimeService.updateBusinessStatus(processInstanceId, BusinessStatusEnum.BACK.getStatus());
                flowProcessEventHandler.processHandler(processInstance.getProcessDefinitionKey(),
                    processInstance.getBusinessKey(), BusinessStatusEnum.BACK.getStatus(), false);
            }
            //删除驳回后的流程节点
            wfTaskBackNodeService.deleteBackTaskNode(processInstanceId, backProcessBo.getTargetActivityId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
        return task.getProcessInstanceId();
    }

    /**
     * 修改任务办理人.
     *
     * @param taskIds 任务id
     * @param userId  办理人id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAssignee(String[] taskIds, String userId) {
        try {
            List<Task> list = QueryUtils.taskQuery().taskIds(Arrays.asList(taskIds)).list();
            for (Task task : list) {
                taskService.setAssignee(task.getId(), userId);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("修改失败：" + e.getMessage());
        }
        return true;
    }

    /**
     * 查询流程变量.
     *
     * @param taskId 任务id
     */
    @Override
    public List<VariableVo> getInstanceVariable(String taskId) {
        List<VariableVo> variableVoList = new ArrayList<>();
        Map<String, VariableInstance> variableInstances = taskService.getVariableInstances(taskId);
        if (CollUtil.isNotEmpty(variableInstances)) {
            for (Map.Entry<String, VariableInstance> entry : variableInstances.entrySet()) {
                VariableVo variableVo = new VariableVo();
                variableVo.setKey(entry.getKey());
                variableVo.setValue(entry.getValue().getValue().toString());
                variableVoList.add(variableVo);
            }
        }
        return variableVoList;
    }

    /**
     * 查询工作流任务用户选择加签人员.
     *
     * @param taskId 任务id
     */
    @Override
    @SuppressWarnings("unchecked")
    public String getTaskUserIdsByAddMultiInstance(String taskId) {
        Task task = QueryUtils.taskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        MultiInstanceVo multiInstance = WorkflowUtils.isMultiInstance(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
        if (multiInstance == null) {
            return "";
        }
        List<Long> userIds;
        if (multiInstance.getType() instanceof SequentialMultiInstanceBehavior) {
            userIds = (List<Long>) runtimeService.getVariable(task.getExecutionId(), multiInstance.getAssigneeList());
        } else {
            List<Task> list = QueryUtils.taskQuery(task.getProcessInstanceId()).list();
            userIds = StreamUtils.toList(list, e -> Long.valueOf(e.getAssignee()));
        }
        return StringUtils.join(userIds, StringUtils.SEPARATOR);
    }

    /**
     * 查询工作流选择减签人员.
     *
     * @param taskId 任务id 任务id
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<TaskVo> getListByDeleteMultiInstance(String taskId) {
        Task task = QueryUtils.taskQuery().taskId(taskId).singleResult();
        List<Task> taskList = QueryUtils.taskQuery(task.getProcessInstanceId()).list();
        MultiInstanceVo multiInstance = WorkflowUtils.isMultiInstance(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
        List<TaskVo> taskListVo = new ArrayList<>();
        if (multiInstance == null) {
            return List.of();
        }
        List<Long> assigneeList = new ArrayList<>();
        if (multiInstance.getType() instanceof SequentialMultiInstanceBehavior) {
            List<Object> variable = (List<Object>) runtimeService.getVariable(task.getExecutionId(), multiInstance.getAssigneeList());
            for (Object o : variable) {
                assigneeList.add(Long.valueOf(o.toString()));
            }
        }

        if (multiInstance.getType() instanceof SequentialMultiInstanceBehavior) {
            List<Long> userIds = StreamUtils.filter(assigneeList, e -> !String.valueOf(e).equals(task.getAssignee()));
            List<RemoteUserVo> userList = remoteUserService.selectListByIds(userIds);
            for (Long userId : userIds) {
                TaskVo taskVo = new TaskVo();
                taskVo.setId("串行会签");
                taskVo.setExecutionId("串行会签");
                taskVo.setProcessInstanceId(task.getProcessInstanceId());
                taskVo.setName(task.getName());
                taskVo.setAssignee(userId);
                if (CollUtil.isNotEmpty(userList)) {
                    userList.stream().filter(u -> u.getUserId().toString().equals(userId.toString())).findFirst().ifPresent(u -> taskVo.setAssigneeName(u.getNickName()));
                }
                taskListVo.add(taskVo);
            }
            return taskListVo;
        } else if (multiInstance.getType() instanceof ParallelMultiInstanceBehavior) {
            List<Task> tasks = StreamUtils.filter(taskList, e -> StringUtils.isBlank(e.getParentTaskId()) && !e.getExecutionId().equals(task.getExecutionId()) && e.getTaskDefinitionKey().equals(task.getTaskDefinitionKey()));
            if (CollUtil.isNotEmpty(tasks)) {
                List<Long> userIds = StreamUtils.toList(tasks, e -> Long.valueOf(e.getAssignee()));
                List<RemoteUserVo> userList = remoteUserService.selectListByIds(userIds);
                for (Task t : tasks) {
                    TaskVo taskVo = new TaskVo();
                    taskVo.setId(t.getId());
                    taskVo.setExecutionId(t.getExecutionId());
                    taskVo.setProcessInstanceId(t.getProcessInstanceId());
                    taskVo.setName(t.getName());
                    taskVo.setAssignee(Long.valueOf(t.getAssignee()));
                    if (CollUtil.isNotEmpty(userList)) {
                        userList.stream().filter(u -> u.getUserId().toString().equals(t.getAssignee())).findFirst().ifPresent(e -> taskVo.setAssigneeName(e.getNickName()));
                    }
                    taskListVo.add(taskVo);
                }
                return taskListVo;
            }
        }
        return List.of();
    }
}
