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

package leyramu.framework.lersosa.gen.web;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import jakarta.servlet.http.HttpServletResponse;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.common.mybatis.helper.DataBaseHelper;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.gen.api.IGenTableService;
import leyramu.framework.lersosa.gen.domain.GenTable;
import leyramu.framework.lersosa.gen.domain.GenTableColumn;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成 操作处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/gen")
public class GenController extends BaseController {

    private final IGenTableService genTableService;

    /**
     * 查询代码生成列表.
     */
    @SaCheckPermission("tool:gen:list")
    @GetMapping("/list")
    public TableDataInfo<GenTable> genList(GenTable genTable, PageQuery pageQuery) {
        return genTableService.selectPageGenTableList(genTable, pageQuery);
    }

    /**
     * 修改代码生成业务.
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:query")
    @GetMapping(value = "/{tableId}")
    public R<Map<String, Object>> getInfo(@PathVariable Long tableId) {
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);
        Map<String, Object> map = new HashMap<>(3);
        map.put("info", table);
        map.put("rows", list);
        map.put("tables", tables);
        return R.ok(map);
    }

    /**
     * 查询数据库列表.
     */
    @SaCheckPermission("tool:gen:list")
    @GetMapping("/db/list")
    public TableDataInfo<GenTable> dataList(GenTable genTable, PageQuery pageQuery) {
        return genTableService.selectPageDbTableList(genTable, pageQuery);
    }

    /**
     * 查询数据表字段列表.
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:list")
    @GetMapping(value = "/column/{tableId}")
    public TableDataInfo<GenTableColumn> columnList(@PathVariable("tableId") Long tableId) {
        TableDataInfo<GenTableColumn> dataInfo = new TableDataInfo<>();
        List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    /**
     * 导入表结构（保存）.
     *
     * @param tables 表名串
     */
    @SaCheckPermission("tool:gen:import")
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    public R<Void> importTableSave(String tables, String dataName) {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames, dataName);
        genTableService.importGenTable(tableList, dataName);
        return R.ok();
    }

    /**
     * 修改保存代码生成业务.
     */
    @SaCheckPermission("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> editSave(@Validated @RequestBody GenTable genTable) {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return R.ok();
    }

    /**
     * 删除代码生成.
     *
     * @param tableIds 表ID串
     */
    @SaCheckPermission("tool:gen:remove")
    @Log(title = "代码生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public R<Void> remove(@PathVariable Long[] tableIds) {
        genTableService.deleteGenTableByIds(tableIds);
        return R.ok();
    }

    /**
     * 预览代码.
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:preview")
    @GetMapping("/preview/{tableId}")
    public R<Map<String, String>> preview(@PathVariable("tableId") Long tableId) {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return R.ok(dataMap);
    }

    /**
     * 生成代码（下载方式）.
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/download/{tableId}")
    public void download(HttpServletResponse response, @PathVariable("tableId") Long tableId) throws IOException {
        byte[] data = genTableService.downloadCode(tableId);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）.
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/genCode/{tableId}")
    public R<Void> genCode(@PathVariable("tableId") Long tableId) {
        genTableService.generatorCode(tableId);
        return R.ok();
    }

    /**
     * 同步数据库.
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @GetMapping("/synchDb/{tableId}")
    public R<Void> synchDb(@PathVariable("tableId") Long tableId) {
        genTableService.synchDb(tableId);
        return R.ok();
    }

    /**
     * 批量生成代码.
     *
     * @param tableIdStr 表ID串
     */
    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tableIdStr) throws IOException {
        String[] tableIds = Convert.toStrArray(tableIdStr);
        byte[] data = genTableService.downloadCode(tableIds);
        genCode(response, data);
    }

    /**
     * 生成zip文件.
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"lersosa.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, data);
    }

    /**
     * 查询数据源名称列表.
     */
    @SaCheckPermission("tool:gen:list")
    @GetMapping(value = "/getDataNames")
    public R<Object> getCurrentDataSourceNameList() {
        return R.ok(DataBaseHelper.getDataSourceNameList());
    }
}
