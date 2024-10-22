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

import com.alibaba.csp.sentinel.util.AssertUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author leyou
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/9/3
 */
@Component
public class SimpleMachineDiscovery implements MachineDiscovery {

    /**
     * 应用程序信息
     */
    private final ConcurrentMap<String, AppInfo> apps = new ConcurrentHashMap<>();

    /**
     * 添加机器
     *
     * @param machineInfo 机器信息
     * @return 添加成功返回1，否则返回0
     */
    @Override
    public long addMachine(MachineInfo machineInfo) {
        AssertUtil.notNull(machineInfo, "machineInfo cannot be null");
        AppInfo appInfo = apps.computeIfAbsent(machineInfo.getApp(), o -> new AppInfo(machineInfo.getApp(), machineInfo.getAppType()));
        appInfo.addMachine(machineInfo);
        return 1;
    }

    /**
     * 删除机器
     *
     * @param app  计算机的应用程序名称
     * @param ip   机器 IP
     * @param port 机器端口
     * @return 删除成功返回true，否则返回false
     */
    @Override
    public boolean removeMachine(String app, String ip, int port) {
        AssertUtil.assertNotBlank(app, "app name cannot be blank");
        AppInfo appInfo = apps.get(app);
        if (appInfo != null) {
            return appInfo.removeMachine(ip, port);
        }
        return false;
    }

    /**
     * 获取应用程序名称列表
     *
     * @return 应用程序名称列表
     */
    @Override
    public List<String> getAppNames() {
        return new ArrayList<>(apps.keySet());
    }

    /**
     * 获取应用程序详情
     *
     * @param app 应用程序名称
     * @return 应用程序详情
     */
    @Override
    public AppInfo getDetailApp(String app) {
        AssertUtil.assertNotBlank(app, "app name cannot be blank");
        return apps.get(app);
    }

    /**
     * 获取简要的应用程序详情
     *
     * @return 简要的应用程序详情
     */
    @Override
    public Set<AppInfo> getBriefApps() {
        return new HashSet<>(apps.values());
    }

    /**
     * 删除应用程序
     *
     * @param app 应用程序名称
     */
    @Override
    public void removeApp(String app) {
        AssertUtil.assertNotBlank(app, "app name cannot be blank");
        apps.remove(app);
    }
}
