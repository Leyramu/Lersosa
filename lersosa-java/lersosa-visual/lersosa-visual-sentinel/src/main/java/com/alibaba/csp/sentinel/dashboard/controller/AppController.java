/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.dashboard.discovery.AppInfo;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.domain.vo.MachineInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 应用程序控制器.
 *
 * @author Carpenter Lee
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class AppController {

    /**
     * 应用管理服务接口.
     */
    private final AppManagement appManagement;

    /**
     * 获取所有应用的简要信息列表.
     *
     * @param ignoredRequest HttpServletRequest对象，本方法中未使用
     * @return 包含应用简要信息列表的Result对象
     */
    @GetMapping("/names.json")
    public Result<List<String>> queryApps(HttpServletRequest ignoredRequest) {
        return Result.ofSuccess(appManagement.getAppNames());
    }

    /**
     * 获取所有应用的简要信息列表.
     *
     * @param ignoredRequest HttpServletRequest对象，本方法中未使用
     * @return 包含应用简要信息列表的Result对象
     */
    @GetMapping("/briefinfos.json")
    public Result<List<AppInfo>> queryAppInfos(HttpServletRequest ignoredRequest) {
        List<AppInfo> list = new ArrayList<>(appManagement.getBriefApps());
        list.sort(Comparator.comparing(AppInfo::getApp));
        return Result.ofSuccess(list);
    }

    /**
     * 根据应用名称获取机器信息列表.
     *
     * @param app 应用名称
     * @return 包含机器信息列表的Result对象
     */
    @GetMapping("/{app}/machines.json")
    public Result<List<MachineInfoVo>> getMachinesByApp(@PathVariable("app") String app) {
        AppInfo appInfo = appManagement.getDetailApp(app);
        if (appInfo == null) {
            return Result.ofSuccess(null);
        }
        List<MachineInfo> list = new ArrayList<>(appInfo.getMachines());
        list.sort(Comparator.comparing(MachineInfo::getApp).thenComparing(MachineInfo::getIp).thenComparingInt(MachineInfo::getPort));
        return Result.ofSuccess(MachineInfoVo.fromMachineInfoList(list));
    }

    /**
     * 移除指定应用的机器信息.
     *
     * @param app  应用名称
     * @param ip   机器的IP地址
     * @param port 机器的端口号
     * @return 表示移除操作结果的Result对象
     */
    @RequestMapping("/{app}/machine/remove.json")
    public Result<String> removeMachineById(
        @PathVariable("app") String app,
        @RequestParam(name = "ip") String ip,
        @RequestParam(name = "port") int port) {
        AppInfo appInfo = appManagement.getDetailApp(app);
        if (appInfo == null) {
            return Result.ofSuccess(null);
        }
        if (appManagement.removeMachine(app, ip, port)) {
            return Result.ofSuccessMsg("success");
        } else {
            return Result.ofFail(1, "remove failed");
        }
    }
}
