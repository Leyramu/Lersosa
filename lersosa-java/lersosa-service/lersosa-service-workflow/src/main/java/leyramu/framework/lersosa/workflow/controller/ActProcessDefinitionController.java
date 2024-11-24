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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.idempotent.annotation.RepeatSubmit;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.workflow.domain.bo.ProcessDefinitionBo;
import leyramu.framework.lersosa.workflow.domain.vo.ProcessDefinitionVo;
import leyramu.framework.lersosa.workflow.service.IActProcessDefinitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 流程定义管理 控制层.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/processDefinition")
public class ActProcessDefinitionController extends BaseController {

    private final IActProcessDefinitionService actProcessDefinitionService;

    /**
     * 分页查询.
     *
     * @param bo 参数
     */
    @GetMapping("/list")
    public TableDataInfo<ProcessDefinitionVo> page(ProcessDefinitionBo bo, PageQuery pageQuery) {
        return actProcessDefinitionService.page(bo, pageQuery);
    }

    /**
     * 查询历史流程定义列表.
     *
     * @param key 流程定义key
     */
    @GetMapping("/getListByKey/{key}")
    public R<List<ProcessDefinitionVo>> getListByKey(@NotEmpty(message = "流程定义key不能为空") @PathVariable String key) {
        return R.ok("操作成功", actProcessDefinitionService.getListByKey(key));
    }

    /**
     * 查看流程定义图片.
     *
     * @param processDefinitionId 流程定义id
     */
    @GetMapping("/definitionImage/{processDefinitionId}")
    public R<String> definitionImage(@PathVariable String processDefinitionId) {
        return R.ok("操作成功", actProcessDefinitionService.definitionImage(processDefinitionId));
    }

    /**
     * 查看流程定义xml文件.
     *
     * @param processDefinitionId 流程定义id
     */
    @GetMapping("/definitionXml/{processDefinitionId}")
    public R<Map<String, Object>> definitionXml(@NotBlank(message = "流程定义id不能为空") @PathVariable String processDefinitionId) {
        Map<String, Object> map = new HashMap<>();
        String xmlStr = actProcessDefinitionService.definitionXml(processDefinitionId);
        map.put("xml", Arrays.asList(xmlStr.split("\n")));
        map.put("xmlStr", xmlStr);
        return R.ok(map);
    }

    /**
     * 删除流程定义.
     *
     * @param deploymentIds        部署id
     * @param processDefinitionIds 流程定义id
     */
    @Log(title = "流程定义管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deploymentIds}/{processDefinitionIds}")
    public R<Void> deleteDeployment(@NotNull(message = "流程部署id不能为空") @PathVariable List<String> deploymentIds,
                                    @NotNull(message = "流程定义id不能为空") @PathVariable List<String> processDefinitionIds) {
        return toAjax(actProcessDefinitionService.deleteDeployment(deploymentIds, processDefinitionIds));
    }

    /**
     * 激活或者挂起流程定义.
     *
     * @param processDefinitionId 流程定义id
     */
    @Log(title = "流程定义管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/updateDefinitionState/{processDefinitionId}")
    public R<Void> updateDefinitionState(@NotBlank(message = "流程定义id不能为空") @PathVariable String processDefinitionId) {
        return toAjax(actProcessDefinitionService.updateDefinitionState(processDefinitionId));
    }

    /**
     * 迁移流程定义.
     *
     * @param currentProcessDefinitionId 当前流程定义id
     * @param fromProcessDefinitionId    需要迁移到的流程定义id
     */
    @Log(title = "流程定义管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/migrationDefinition/{currentProcessDefinitionId}/{fromProcessDefinitionId}")
    public R<Void> migrationDefinition(@NotBlank(message = "当前流程定义id") @PathVariable String currentProcessDefinitionId,
                                       @NotBlank(message = "需要迁移到的流程定义id") @PathVariable String fromProcessDefinitionId) {
        return toAjax(actProcessDefinitionService.migrationDefinition(currentProcessDefinitionId, fromProcessDefinitionId));
    }

    /**
     * 流程定义转换为模型.
     *
     * @param processDefinitionId 流程定义id
     */
    @Log(title = "流程定义管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/convertToModel/{processDefinitionId}")
    public R<Void> convertToModel(@NotEmpty(message = "流程定义id不能为空") @PathVariable String processDefinitionId) {
        return toAjax(actProcessDefinitionService.convertToModel(processDefinitionId));
    }

    /**
     * 通过zip或xml部署流程定义.
     *
     * @param file         文件
     * @param categoryCode 分类
     */
    @Log(title = "流程定义管理", businessType = BusinessType.INSERT)
    @PostMapping("/deployByFile")
    public void deployByFile(@RequestParam("file") MultipartFile file, @RequestParam("categoryCode") String categoryCode) {
        actProcessDefinitionService.deployByFile(file, categoryCode);
    }
}
