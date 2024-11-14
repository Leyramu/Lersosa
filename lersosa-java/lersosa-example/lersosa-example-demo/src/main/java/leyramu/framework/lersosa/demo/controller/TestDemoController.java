/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.core.utils.ValidatorUtils;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import leyramu.framework.lersosa.common.core.validate.EditGroup;
import leyramu.framework.lersosa.common.core.validate.QueryGroup;
import leyramu.framework.lersosa.common.excel.core.ExcelResult;
import leyramu.framework.lersosa.common.excel.utils.ExcelUtil;
import leyramu.framework.lersosa.common.idempotent.annotation.RepeatSubmit;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.demo.domain.TestDemo;
import leyramu.framework.lersosa.demo.domain.bo.TestDemoBo;
import leyramu.framework.lersosa.demo.domain.bo.TestDemoImportVo;
import leyramu.framework.lersosa.demo.domain.vo.TestDemoVo;
import leyramu.framework.lersosa.demo.service.ITestDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 测试单表Controller.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo")
public class TestDemoController extends BaseController {

    private final ITestDemoService iTestDemoService;

    /**
     * 查询测试单表列表.
     */
    @SaCheckPermission("demo:demo:list")
    @GetMapping("/list")
    public TableDataInfo<TestDemoVo> list(TestDemoBo bo, PageQuery pageQuery) {
        return iTestDemoService.queryPageList(bo, pageQuery);
    }

    /**
     * 自定义分页查询.
     */
    @SaCheckPermission("demo:demo:list")
    @GetMapping("/page")
    public TableDataInfo<TestDemoVo> page(@Validated(QueryGroup.class) TestDemoBo bo, PageQuery pageQuery) {
        return iTestDemoService.customPageList(bo, pageQuery);
    }

    /**
     * 导入测试-校验.
     *
     * @param file 导入文件
     */
    @Log(title = "测试单表", businessType = BusinessType.IMPORT)
    @SaCheckPermission("demo:demo:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file) throws Exception {
        ExcelResult<TestDemoImportVo> excelResult = ExcelUtil.importExcel(file.getInputStream(), TestDemoImportVo.class, true);
        List<TestDemoImportVo> volist = excelResult.getList();
        List<TestDemo> list = BeanUtil.copyToList(volist, TestDemo.class);
        iTestDemoService.saveBatch(list);
        return R.ok(excelResult.getAnalysis());
    }

    /**
     * 导出测试单表列表.
     */
    @SaCheckPermission("demo:demo:export")
    @Log(title = "测试单表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated TestDemoBo bo, HttpServletResponse response) {
        List<TestDemoVo> list = iTestDemoService.queryList(bo);
        // 测试雪花id导出
//        for (TestDemoVo vo : list) {
//            vo.setId(1234567891234567893L);
//        }
        ExcelUtil.exportExcel(list, "测试单表", TestDemoVo.class, response);
    }

    /**
     * 获取测试单表详细信息.
     *
     * @param id 测试ID
     */
    @SaCheckPermission("demo:demo:query")
    @GetMapping("/{id}")
    public R<TestDemoVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        return R.ok(iTestDemoService.queryById(id));
    }

    /**
     * 新增测试单表.
     */
    @SaCheckPermission("demo:demo:add")
    @Log(title = "测试单表", businessType = BusinessType.INSERT)
    @RepeatSubmit(interval = 2, timeUnit = TimeUnit.SECONDS)
    @PostMapping()
    public R<Void> add(@RequestBody TestDemoBo bo) {
        // 使用校验工具对标 @Validated(AddGroup.class) 注解
        // 用于在非 Controller 的地方校验对象
        ValidatorUtils.validate(bo, AddGroup.class);
        return toAjax(iTestDemoService.insertByBo(bo));
    }

    /**
     * 修改测试单表.
     */
    @SaCheckPermission("demo:demo:edit")
    @Log(title = "测试单表", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TestDemoBo bo) {
        return toAjax(iTestDemoService.updateByBo(bo));
    }

    /**
     * 删除测试单表.
     *
     * @param ids 测试ID串
     */
    @SaCheckPermission("demo:demo:remove")
    @Log(title = "测试单表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(iTestDemoService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
