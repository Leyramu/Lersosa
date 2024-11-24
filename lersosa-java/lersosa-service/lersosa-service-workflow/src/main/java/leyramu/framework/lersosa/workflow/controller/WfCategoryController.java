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

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import leyramu.framework.lersosa.common.core.validate.EditGroup;
import leyramu.framework.lersosa.common.excel.utils.ExcelUtil;
import leyramu.framework.lersosa.common.idempotent.annotation.RepeatSubmit;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.workflow.domain.bo.WfCategoryBo;
import leyramu.framework.lersosa.workflow.domain.vo.WfCategoryVo;
import leyramu.framework.lersosa.workflow.service.IWfCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程分类.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class WfCategoryController extends BaseController {

    private final IWfCategoryService wfCategoryService;

    /**
     * 查询流程分类列表.
     */
    @SaCheckPermission("workflow:category:list")
    @GetMapping("/list")
    public R<List<WfCategoryVo>> list(WfCategoryBo bo) {
        List<WfCategoryVo> list = wfCategoryService.queryList(bo);
        return R.ok(list);

    }

    /**
     * 导出流程分类列表.
     */
    @SaCheckPermission("workflow:category:export")
    @Log(title = "流程分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfCategoryBo bo, HttpServletResponse response) {
        List<WfCategoryVo> list = wfCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "流程分类", WfCategoryVo.class, response);
    }

    /**
     * 获取流程分类详细信息.
     *
     * @param id 主键
     */
    @SaCheckPermission("workflow:category:query")
    @GetMapping("/{id}")
    public R<WfCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                   @PathVariable Long id) {
        return R.ok(wfCategoryService.queryById(id));
    }

    /**
     * 新增流程分类.
     */
    @SaCheckPermission("workflow:category:add")
    @Log(title = "流程分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfCategoryBo bo) {
        return toAjax(wfCategoryService.insertByBo(bo));
    }

    /**
     * 修改流程分类.
     */
    @SaCheckPermission("workflow:category:edit")
    @Log(title = "流程分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfCategoryBo bo) {
        return toAjax(wfCategoryService.updateByBo(bo));
    }

    /**
     * 删除流程分类.
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workflow:category:remove")
    @Log(title = "流程分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wfCategoryService.deleteWithValidByIds(List.of(ids), true));
    }
}
