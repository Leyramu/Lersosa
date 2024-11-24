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

package leyramu.framework.lersosa.workflow.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import jakarta.validation.constraints.NotBlank;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import leyramu.framework.lersosa.common.idempotent.annotation.RepeatSubmit;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.common.satoken.utils.LoginHelper;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.workflow.domain.WfTaskBackNode;
import leyramu.framework.lersosa.workflow.domain.bo.*;
import leyramu.framework.lersosa.workflow.domain.vo.TaskVo;
import leyramu.framework.lersosa.workflow.domain.vo.VariableVo;
import leyramu.framework.lersosa.workflow.service.IActTaskService;
import leyramu.framework.lersosa.workflow.service.IWfTaskBackNodeService;
import leyramu.framework.lersosa.workflow.utils.QueryUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务管理 控制层.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/task")
public class ActTaskController extends BaseController {

    private final IActTaskService actTaskService;

    private final IWfTaskBackNodeService wfTaskBackNodeService;

    private final TaskService taskService;

    /**
     * 启动任务.
     *
     * @param startProcessBo 启动流程参数
     */
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/startWorkFlow")
    public R<Map<String, Object>> startWorkFlow(@Validated(AddGroup.class) @RequestBody StartProcessBo startProcessBo) {
        Map<String, Object> map = actTaskService.startWorkFlow(startProcessBo);
        return R.ok("提交成功", map);
    }

    /**
     * 办理任务.
     *
     * @param completeTaskBo 办理任务参数
     */
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/completeTask")
    public R<Void> completeTask(@Validated(AddGroup.class) @RequestBody CompleteTaskBo completeTaskBo) {
        return toAjax(actTaskService.completeTask(completeTaskBo));
    }

    /**
     * 查询当前用户的待办任务.
     *
     * @param taskBo 参数
     */
    @GetMapping("/getPageByTaskWait")
    public TableDataInfo<TaskVo> getPageByTaskWait(TaskBo taskBo, PageQuery pageQuery) {
        return actTaskService.getPageByTaskWait(taskBo, pageQuery);
    }

    /**
     * 查询当前租户所有待办任务.
     *
     * @param taskBo 参数
     */
    @GetMapping("/getPageByAllTaskWait")
    public TableDataInfo<TaskVo> getPageByAllTaskWait(TaskBo taskBo, PageQuery pageQuery) {
        return actTaskService.getPageByAllTaskWait(taskBo, pageQuery);
    }

    /**
     * 查询当前用户的已办任务.
     *
     * @param taskBo 参数
     */
    @GetMapping("/getPageByTaskFinish")
    public TableDataInfo<TaskVo> getPageByTaskFinish(TaskBo taskBo, PageQuery pageQuery) {
        return actTaskService.getPageByTaskFinish(taskBo, pageQuery);
    }

    /**
     * 查询当前用户的抄送.
     *
     * @param taskBo 参数
     */
    @GetMapping("/getPageByTaskCopy")
    public TableDataInfo<TaskVo> getPageByTaskCopy(TaskBo taskBo, PageQuery pageQuery) {
        return actTaskService.getPageByTaskCopy(taskBo, pageQuery);
    }

    /**
     * 查询当前租户所有已办任务.
     *
     * @param taskBo 参数
     */
    @GetMapping("/getPageByAllTaskFinish")
    public TableDataInfo<TaskVo> getPageByAllTaskFinish(TaskBo taskBo, PageQuery pageQuery) {
        return actTaskService.getPageByAllTaskFinish(taskBo, pageQuery);
    }

    /**
     * 签收（拾取）任务.
     *
     * @param taskId 任务id
     */
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/claim/{taskId}")
    public R<Void> claimTask(@NotBlank(message = "任务id不能为空") @PathVariable String taskId) {
        try {
            taskService.claim(taskId, Convert.toStr(LoginHelper.getUserId()));
            return R.ok();
        } catch (Exception e) {
            log.error("签收任务失败", e);
            return R.fail("签收任务失败：" + e.getMessage());
        }
    }

    /**
     * 归还（拾取的）任务.
     *
     * @param taskId 任务id
     */
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/returnTask/{taskId}")
    public R<Void> returnTask(@NotBlank(message = "任务id不能为空") @PathVariable String taskId) {
        try {
            taskService.setAssignee(taskId, null);
            return R.ok();
        } catch (Exception e) {
            log.error("归还任务失败", e);
            return R.fail("归还任务失败：" + e.getMessage());
        }
    }

    /**
     * 委派任务.
     *
     * @param delegateBo 参数
     */
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/delegateTask")
    public R<Void> delegateTask(@Validated({AddGroup.class}) @RequestBody DelegateBo delegateBo) {
        return toAjax(actTaskService.delegateTask(delegateBo));
    }

    /**
     * 终止任务.
     *
     * @param terminationBo 参数
     */
    @Log(title = "任务管理", businessType = BusinessType.DELETE)
    @RepeatSubmit()
    @PostMapping("/terminationTask")
    public R<Void> terminationTask(@RequestBody TerminationBo terminationBo) {
        return toAjax(actTaskService.terminationTask(terminationBo));
    }

    /**
     * 转办任务.
     *
     * @param transmitBo 参数
     */
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/transferTask")
    public R<Void> transferTask(@Validated({AddGroup.class}) @RequestBody TransmitBo transmitBo) {
        return toAjax(actTaskService.transferTask(transmitBo));
    }

    /**
     * 会签任务加签.
     *
     * @param addMultiBo 参数
     */
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/addMultiInstanceExecution")
    public R<Void> addMultiInstanceExecution(@Validated({AddGroup.class}) @RequestBody AddMultiBo addMultiBo) {
        return toAjax(actTaskService.addMultiInstanceExecution(addMultiBo));
    }

    /**
     * 会签任务减签.
     *
     * @param deleteMultiBo 参数
     */
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/deleteMultiInstanceExecution")
    public R<Void> deleteMultiInstanceExecution(@Validated({AddGroup.class}) @RequestBody DeleteMultiBo deleteMultiBo) {
        return toAjax(actTaskService.deleteMultiInstanceExecution(deleteMultiBo));
    }

    /**
     * 驳回审批.
     *
     * @param backProcessBo 参数
     */
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/backProcess")
    public R<String> backProcess(@Validated({AddGroup.class}) @RequestBody BackProcessBo backProcessBo) {
        return R.ok(actTaskService.backProcess(backProcessBo));
    }

    /**
     * 获取当前任务.
     *
     * @param taskId 任务id
     */
    @GetMapping("/getTaskById/{taskId}")
    public R<TaskVo> getTaskById(@PathVariable String taskId) {
        return R.ok(QueryUtils.getTask(taskId));
    }


    /**
     * 修改任务办理人.
     *
     * @param taskIds 任务id
     * @param userId  办理人id
     */
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/updateAssignee/{taskIds}/{userId}")
    public R<Void> updateAssignee(@PathVariable String[] taskIds, @PathVariable String userId) {
        return toAjax(actTaskService.updateAssignee(taskIds, userId));
    }

    /**
     * 查询流程变量.
     *
     * @param taskId 任务id
     */
    @GetMapping("/getInstanceVariable/{taskId}")
    public R<List<VariableVo>> getProcessInstVariable(@PathVariable String taskId) {
        return R.ok(actTaskService.getInstanceVariable(taskId));
    }

    /**
     * 获取可驳回得任务节点.
     *
     * @param processInstanceId 流程实例id
     */
    @GetMapping("/getTaskNodeList/{processInstanceId}")
    public R<List<WfTaskBackNode>> getNodeList(@PathVariable String processInstanceId) {
        return R.ok(CollUtil.reverse(wfTaskBackNodeService.getListByInstanceId(processInstanceId)));
    }

    /**
     * 查询工作流任务用户选择加签人员.
     *
     * @param taskId 任务id
     */
    @GetMapping("/getTaskUserIdsByAddMultiInstance/{taskId}")
    public R<String> getTaskUserIdsByAddMultiInstance(@PathVariable String taskId) {
        return R.ok(actTaskService.getTaskUserIdsByAddMultiInstance(taskId));
    }

    /**
     * 查询工作流选择减签人员.
     *
     * @param taskId 任务id
     */
    @GetMapping("/getListByDeleteMultiInstance/{taskId}")
    public R<List<TaskVo>> getListByDeleteMultiInstance(@PathVariable String taskId) {
        return R.ok(actTaskService.getListByDeleteMultiInstance(taskId));
    }
}
