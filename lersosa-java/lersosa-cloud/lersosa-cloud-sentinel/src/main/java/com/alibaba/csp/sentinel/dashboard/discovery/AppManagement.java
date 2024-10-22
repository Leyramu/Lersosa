/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
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
