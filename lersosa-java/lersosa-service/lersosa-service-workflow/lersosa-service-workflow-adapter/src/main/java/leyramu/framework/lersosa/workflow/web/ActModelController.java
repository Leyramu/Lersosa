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

package leyramu.framework.lersosa.workflow.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import leyramu.framework.lersosa.common.core.validate.EditGroup;
import leyramu.framework.lersosa.common.idempotent.annotation.RepeatSubmit;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.workflow.api.IActModelService;
import leyramu.framework.lersosa.workflow.domain.bo.ModelBo;
import leyramu.framework.lersosa.workflow.domain.vo.ModelVo;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 模型管理 控制层.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/model")
public class ActModelController extends BaseController {

    private final RepositoryService repositoryService;
    private final IActModelService actModelService;

    /**
     * 分页查询模型.
     *
     * @param modelBo 模型参数
     */
    @GetMapping("/list")
    public TableDataInfo<Model> page(ModelBo modelBo, PageQuery pageQuery) {
        return actModelService.page(modelBo, pageQuery);
    }

    /**
     * 新增模型.
     *
     * @param modelBo 模型请求对象
     */
    @Log(title = "模型管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/save")
    public R<Void> saveNewModel(@Validated(AddGroup.class) @RequestBody ModelBo modelBo) {
        return toAjax(actModelService.saveNewModel(modelBo));
    }

    /**
     * 查询模型.
     *
     * @param id 模型id
     */
    @GetMapping("/getInfo/{id}")
    public R<ModelVo> getInfo(@NotBlank(message = "模型id不能为空") @PathVariable String id) {
        return R.ok(actModelService.getInfo(id));
    }

    /**
     * 修改模型信息.
     *
     * @param modelBo 模型数据
     */
    @Log(title = "模型管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping(value = "/update")
    public R<Void> update(@RequestBody ModelBo modelBo) {
        return toAjax(actModelService.update(modelBo));
    }

    /**
     * 编辑XMl模型.
     *
     * @param modelBo 模型数据
     */
    @Log(title = "模型管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping(value = "/editModelXml")
    public R<Void> editModel(@Validated(EditGroup.class) @RequestBody ModelBo modelBo) {
        return toAjax(actModelService.editModelXml(modelBo));
    }

    /**
     * 删除流程模型.
     *
     * @param ids 模型id
     */
    @Log(title = "模型管理", businessType = BusinessType.DELETE)
    @RepeatSubmit()
    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public R<Void> delete(@NotEmpty(message = "主键不能为空") @PathVariable String[] ids) {
        Arrays.stream(ids).parallel().forEachOrdered(repositoryService::deleteModel);
        return R.ok();
    }

    /**
     * 模型部署.
     *
     * @param id 模型id
     */
    @Log(title = "模型管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/modelDeploy/{id}")
    public R<Void> deploy(@NotBlank(message = "模型id不能为空") @PathVariable("id") String id) {
        return toAjax(actModelService.modelDeploy(id));
    }

    /**
     * 导出模型zip压缩包.
     *
     * @param modelIds 模型id
     * @param response 相应
     */
    @GetMapping("/export/zip/{modelIds}")
    public void exportZip(@NotEmpty(message = "模型id不能为空") @PathVariable List<String> modelIds,
                          HttpServletResponse response) {
        actModelService.exportZip(modelIds, response);
    }

    /**
     * 复制模型.
     *
     * @param modelBo 模型数据
     */
    @PostMapping("/copyModel")
    public R<Void> copyModel(@RequestBody ModelBo modelBo) {
        return toAjax(actModelService.copyModel(modelBo));
    }
}
