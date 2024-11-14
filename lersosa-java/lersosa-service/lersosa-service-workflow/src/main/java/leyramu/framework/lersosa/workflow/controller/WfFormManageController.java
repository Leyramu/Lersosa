/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
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
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.workflow.domain.bo.WfFormManageBo;
import leyramu.framework.lersosa.workflow.domain.vo.WfFormManageVo;
import leyramu.framework.lersosa.workflow.service.IWfFormManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表单管理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/formManage")
public class WfFormManageController extends BaseController {

    private final IWfFormManageService wfFormManageService;

    /**
     * 查询表单管理列表.
     */
    @SaCheckPermission("workflow:formManage:list")
    @GetMapping("/list")
    public TableDataInfo<WfFormManageVo> list(WfFormManageBo bo, PageQuery pageQuery) {
        return wfFormManageService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询表单管理列表.
     */
    @SaCheckPermission("workflow:formManage:list")
    @GetMapping("/list/selectList")
    public R<List<WfFormManageVo>> selectList() {
        return R.ok(wfFormManageService.selectList());
    }

    /**
     * 导出表单管理列表.
     */
    @SaCheckPermission("workflow:formManage:export")
    @Log(title = "表单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfFormManageBo bo, HttpServletResponse response) {
        List<WfFormManageVo> list = wfFormManageService.queryList(bo);
        ExcelUtil.exportExcel(list, "表单管理", WfFormManageVo.class, response);
    }

    /**
     * 获取表单管理详细信息.
     *
     * @param id 主键
     */
    @SaCheckPermission("workflow:formManage:query")
    @GetMapping("/{id}")
    public R<WfFormManageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(wfFormManageService.queryById(id));
    }

    /**
     * 新增表单管理.
     */
    @SaCheckPermission("workflow:formManage:add")
    @Log(title = "表单管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfFormManageBo bo) {
        return toAjax(wfFormManageService.insertByBo(bo));
    }

    /**
     * 修改表单管理.
     */
    @SaCheckPermission("workflow:formManage:edit")
    @Log(title = "表单管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfFormManageBo bo) {
        return toAjax(wfFormManageService.updateByBo(bo));
    }

    /**
     * 删除表单管理.
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workflow:formManage:remove")
    @Log(title = "表单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wfFormManageService.deleteByIds(List.of(ids)));
    }
}
