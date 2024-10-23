/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.controller;

import jakarta.servlet.http.HttpServletResponse;
import leyramu.framework.lersosa.common.core.constant.CacheConstants;
import leyramu.framework.lersosa.common.core.utils.poi.ExcelUtil;
import leyramu.framework.lersosa.common.core.web.controller.BaseController;
import leyramu.framework.lersosa.common.core.web.domain.AjaxResult;
import leyramu.framework.lersosa.common.core.web.page.TableDataInfo;
import leyramu.framework.lersosa.common.log.annotation.Log;
import leyramu.framework.lersosa.common.log.enums.BusinessType;
import leyramu.framework.lersosa.common.redis.service.RedisService;
import leyramu.framework.lersosa.common.security.annotation.InnerAuth;
import leyramu.framework.lersosa.common.security.annotation.RequiresPermissions;
import leyramu.framework.lersosa.system.api.domain.SysLogininfor;
import leyramu.framework.lersosa.system.service.ISysLogininforService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/logininfor")
public class SysLogininforController extends BaseController {

    /**
     * 登录日志服务
     */
    private final ISysLogininforService logininforService;

    /**
     * Redis 缓存服务
     */
    private final RedisService redisService;

    /**
     * 获取登录日志列表
     *
     * @param logininfor 访问日志对象
     * @return 登录日志集合
     * @apiNote 获取登录日志列表
     */
    @RequiresPermissions("system:logininfor:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogininfor logininfor) {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    /**
     * 导出登录日志列表
     *
     * @param response   响应
     * @param logininfor 访问日志对象
     * @apiNote 导出登录日志列表
     */
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:logininfor:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogininfor logininfor) {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<>(SysLogininfor.class);
        util.exportExcel(response, list, "登录日志");
    }

    /**
     * 批量删除登录日志
     *
     * @param infoIds 登录日志 ID
     * @return 删除结果
     * @apiNote 批量删除登录日志
     */
    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    /**
     * 清空登录日志
     *
     * @return 清空结果
     * @apiNote 清空登录日志
     */
    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        logininforService.cleanLogininfor();
        return success();
    }

    /**
     * 解锁用户账户
     *
     * @param userName 用户名
     * @return 结果
     * @apiNote 解锁用户账户
     */
    @RequiresPermissions("system:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public AjaxResult unlock(@PathVariable("userName") String userName) {
        redisService.deleteObject(CacheConstants.PWD_ERR_CNT_KEY + userName);
        return success();
    }

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     * @return 结果
     * @apiNote 新增系统登录日志
     */
    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLogininfor logininfor) {
        return toAjax(logininforService.insertLogininfor(logininfor));
    }
}
