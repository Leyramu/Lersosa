/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.discovery;

import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Sentinel
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Component
@RequiredArgsConstructor
public class AppManagement implements MachineDiscovery {

    /**
     * Spring 上下文
     */
    private final ApplicationContext context;

    /**
     * 机器发现
     */
    private MachineDiscovery machineDiscovery;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        machineDiscovery = context.getBean(SimpleMachineDiscovery.class);
    }

    /**
     * 获取应用信息
     *
     * @return 应用信息
     */
    @Override
    public Set<AppInfo> getBriefApps() {
        return machineDiscovery.getBriefApps();
    }

    /**
     * 添加机器
     *
     * @param machineInfo 机器信息
     * @return 添加结果
     */
    @Override
    public long addMachine(MachineInfo machineInfo) {
        return machineDiscovery.addMachine(machineInfo);
    }

    /**
     * 删除机器
     *
     * @param app  计算机的应用程序名称
     * @param ip   机器 IP
     * @param port 机器端口
     * @return 删除结果
     */
    @Override
    public boolean removeMachine(String app, String ip, int port) {
        return machineDiscovery.removeMachine(app, ip, port);
    }

    /**
     * 获取应用名称列表
     *
     * @return 应用名称列表
     */
    @Override
    public List<String> getAppNames() {
        return machineDiscovery.getAppNames();
    }

    /**
     * 获取应用详情
     *
     * @param app 应用名称
     * @return 应用详情
     */
    @Override
    public AppInfo getDetailApp(String app) {
        return machineDiscovery.getDetailApp(app);
    }

    /**
     * 删除应用
     *
     * @param app 应用名称
     */
    @Override
    public void removeApp(String app) {
        machineDiscovery.removeApp(app);
    }

    /**
     * 是否为有效的机器
     *
     * @param app 应用名称
     * @param ip  机器 IP
     * @return 是否为有效的机器
     */
    public boolean isValidMachineOfApp(String app, String ip) {
        if (StringUtil.isEmpty(app)) {
            return true;
        }
        return Optional.ofNullable(getDetailApp(app))
                .flatMap(a -> a.getMachine(ip))
                .isEmpty();
    }
}
