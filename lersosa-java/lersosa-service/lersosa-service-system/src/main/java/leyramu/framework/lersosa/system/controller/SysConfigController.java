/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.controller;

import jakarta.servlet.http.HttpServletResponse;
import leyramu.framework.lersosa.common.core.utils.poi.ExcelUtil;
import leyramu.framework.lersosa.common.core.web.controller.BaseController;
import leyramu.framework.lersosa.common.core.web.domain.AjaxResult;
import leyramu.framework.lersosa.common.core.web.page.TableDataInfo;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.security.annotation.RequiresPermissions;
import leyramu.framework.lersosa.common.security.utils.SecurityUtils;
import leyramu.framework.lersosa.system.domain.SysConfig;
import leyramu.framework.lersosa.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/config")
public class SysConfigController extends BaseController {

    /**
     * 参数配置 服务对象
     */
    private final ISysConfigService configService;

    /**
     * 获取参数配置列表
     *
     * @param config 参数配置信息
     * @return 列表信息
     * @apiNote 获取参数配置列表
     */
    @RequiresPermissions("system:config:list")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config) {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    /**
     * 导出参数配置列表
     *
     * @param response 响应对象
     * @param config   参数配置信息
     * @apiNote 导出参数配置列表
     */
    @PostMapping("/export")
    @RequiresPermissions("system:config:export")
    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysConfig config) {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }

    /**
     * 根据参数编号获取详细信息
     *
     * @param configId 参数 ID
     * @return 参数信息
     * @apiNote 根据参数编号获取详细信息
     */
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable Long configId) {
        return success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey 参数键名
     * @return 参数键值
     * @apiNote 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey) {
        return success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     *
     * @param config 参数信息
     * @return 结果
     * @apiNote 新增参数配置
     */
    @PostMapping
    @RequiresPermissions("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysConfig config) {
        if (configService.checkConfigKeyUnique(config)) {
            return error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUsername());
        return toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     *
     * @param config 参数信息
     * @return 结果
     * @apiNote 修改参数配置
     */
    @PutMapping
    @RequiresPermissions("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysConfig config) {
        if (configService.checkConfigKeyUnique(config)) {
            return error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     *
     * @param configIds 参数 ID
     * @return 结果
     * @apiNote 删除参数配置
     */
    @DeleteMapping("/{configIds}")
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 刷新参数缓存
     *
     * @return 结果
     * @apiNote 刷新参数缓存
     */
    @DeleteMapping("/refreshCache")
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    public AjaxResult refreshCache() {
        configService.resetConfigCache();
        return success();
    }
}
