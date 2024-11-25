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

package com.alibaba.csp.sentinel.dashboard.controller;

import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设备注册控制器.
 *
 * @author Sentinel
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/12
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/registry", produces = MediaType.APPLICATION_JSON_VALUE)
public class MachineRegistryController {

    /**
     * 应用管理.
     */
    private final AppManagement appManagement;

    /**
     * 接收心跳.
     *
     * @param app      应用名称
     * @param appType  应用类型
     * @param version  版本
     * @param v        版本号
     * @param hostname 主机名
     * @param ip       ip地址
     * @param port     端口
     * @return 请求处理结果
     */
    @ResponseBody
    @RequestMapping("/machine")
    public Result<?> receiveHeartBeat(
        String app,
        @RequestParam(value = "app_type", required = false, defaultValue = "0")
        Integer appType, Long version, String v, String hostname, String ip,
        Integer port) {
        if (StringUtil.isBlank(app) || app.length() > 256) {
            return Result.ofFail(-1, "invalid appName");
        }
        if (StringUtil.isBlank(ip) || ip.length() > 128) {
            return Result.ofFail(-1, "invalid ip: " + ip);
        }
        if (!InetAddressUtils.isIPv4Address(ip) && !InetAddressUtils.isIPv6Address(ip)) {
            return Result.ofFail(-1, "invalid ip: " + ip);
        }
        if (port == null || port < -1) {
            return Result.ofFail(-1, "invalid port");
        }
        if (hostname != null && hostname.length() > 256) {
            return Result.ofFail(-1, "hostname too long");
        }
        if (port == -1) {
            log.warn("Receive heartbeat from {} but port not set yet", ip);
            return Result.ofFail(-1, "your port not set yet");
        }
        String sentinelVersion = StringUtil.isBlank(v) ? "unknown" : v;

        version = version == null ? System.currentTimeMillis() : version;
        try {
            MachineInfo machineInfo = new MachineInfo();
            machineInfo.setApp(app);
            machineInfo.setAppType(appType);
            machineInfo.setHostname(hostname);
            machineInfo.setIp(ip);
            machineInfo.setPort(port);
            machineInfo.setHeartbeatVersion(version);
            machineInfo.setLastHeartbeat(System.currentTimeMillis());
            machineInfo.setVersion(sentinelVersion);
            appManagement.addMachine(machineInfo);
            return Result.ofSuccessMsg("success");
        } catch (Exception e) {
            log.error("Receive heartbeat error", e);
            return Result.ofFail(-1, e.getMessage());
        }
    }
}
