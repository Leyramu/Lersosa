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

package leyramu.framework.lersosa.system.web.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.common.excel.utils.ExcelUtil;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.common.web.core.BaseController;
import leyramu.framework.lersosa.system.api.ISysDictTypeService;
import leyramu.framework.lersosa.system.domain.bo.SysDictTypeBo;
import leyramu.framework.lersosa.system.domain.vo.SysDictTypeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dict/type")
public class SysDictTypeController extends BaseController {

    private final ISysDictTypeService dictTypeService;

    /**
     * 查询字典类型列表.
     */
    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    public TableDataInfo<SysDictTypeVo> list(SysDictTypeBo dictType, PageQuery pageQuery) {
        return dictTypeService.selectPageDictTypeList(dictType, pageQuery);
    }

    /**
     * 导出字典类型列表.
     */
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:dict:export")
    @PostMapping("/export")
    public void export(SysDictTypeBo dictType, HttpServletResponse response) {
        List<SysDictTypeVo> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil.exportExcel(list, "字典类型", SysDictTypeVo.class, response);
    }

    /**
     * 查询字典类型详细.
     *
     * @param dictId 字典ID
     */
    @SaCheckPermission("system:dict:query")
    @GetMapping(value = "/{dictId}")
    public R<SysDictTypeVo> getInfo(@PathVariable Long dictId) {
        return R.ok(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型.
     */
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysDictTypeBo dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return R.fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.insertDictType(dict);
        return R.ok();
    }

    /**
     * 修改字典类型.
     */
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysDictTypeBo dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return R.fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.updateDictType(dict);
        return R.ok();
    }

    /**
     * 删除字典类型.
     *
     * @param dictIds 字典ID串
     */
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public R<Void> remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return R.ok();
    }

    /**
     * 刷新字典缓存.
     */
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R<Void> refreshCache() {
        dictTypeService.resetDictCache();
        return R.ok();
    }

    /**
     * 获取字典选择框列表.
     */
    @GetMapping("/optionselect")
    public R<List<SysDictTypeVo>> optionselect() {
        List<SysDictTypeVo> dictTypes = dictTypeService.selectDictTypeAll();
        return R.ok(dictTypes);
    }
}
