/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.nacos.console.config;

import com.alibaba.nacos.sys.module.ModuleState;
import com.alibaba.nacos.sys.module.ModuleStateBuilder;
import com.alibaba.nacos.sys.utils.ApplicationUtils;

/**
 * Console module state builder.
 *
 * @author xiweng.yy
 */
public class ConsoleModuleStateBuilder implements ModuleStateBuilder {

    public static final String CONSOLE_MODULE = "console";

    private static final String CONSOLE_UI_ENABLED = "console_ui_enabled";

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
