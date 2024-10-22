/*
 * Copyright 1999-2023 Alibaba Group Holding Ltd.
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

package com.alibaba.nacos.console.config;

import com.alibaba.nacos.sys.module.ModuleState;
import com.alibaba.nacos.sys.module.ModuleStateBuilder;
import com.alibaba.nacos.sys.utils.ApplicationUtils;

/**
 * 控制台模块状态生成器
 *
 * @author xiweng.yy
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.2.0
 * @since 2024/7/31
 */
public class ConsoleModuleStateBuilder implements ModuleStateBuilder {

    /**
     * 控制台模块名称
     */
    public static final String CONSOLE_MODULE = "console";

    /**
     * 控制台模块状态：控制台UI是否启用
     */
    private static final String CONSOLE_UI_ENABLED = "console_ui_enabled";

    /**
     * 构建控制台模块状态
     *
     * @return 控制台模块状态
     */
    @Override
    public ModuleState build() {
        ModuleState result = new ModuleState(CONSOLE_MODULE);
        try {
            ConsoleConfig consoleConfig = ApplicationUtils.getBean(ConsoleConfig.class);
            result.newState(CONSOLE_UI_ENABLED, consoleConfig.isConsoleUiEnabled());
        } catch (Exception ignored) {
        }
        return result;
    }
}
