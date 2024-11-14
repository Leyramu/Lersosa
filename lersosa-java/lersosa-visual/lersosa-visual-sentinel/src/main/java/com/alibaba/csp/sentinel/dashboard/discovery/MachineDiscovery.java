/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.discovery;

import java.util.List;
import java.util.Set;

/**
 * Machine Discovery 界面.
 *
 * @author Sentinel
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
public interface MachineDiscovery {

    /**
     * 未知的应用程序名称.
     */
    @SuppressWarnings("unused")
    String UNKNOWN_APP_NAME = "CLUSTER_NOT_STARTED";

    /**
     * 获取应用程序名称列表.
     *
     * @return 应用程序名称列表
     */
    List<String> getAppNames();

    /**
     * 获取应用程序的简要信息.
     *
     * @return 应用程序简要信息列表
     */
    Set<AppInfo> getBriefApps();

    /**
     * 获取应用程序的详细信息.
     *
     * @param app 应用程序名称
     * @return 应用程序详细信息
     */
    AppInfo getDetailApp(String app);

    /**
     * 从应用程序注册表中删除给定的应用程序.
     *
     * @param app 应用程序名称
     */
    void removeApp(String app);

    /**
     * 将新计算机添加到应用程序注册表.
     *
     * @param machineInfo 机器信息
     * @return 计算机 ID
     */
    long addMachine(MachineInfo machineInfo);

    /**
     * 从应用程序注册表中删除给定的计算机实例.
     *
     * @param app  计算机的应用程序名称
     * @param ip   机器 IP
     * @param port 机器端口
     * @return 如果删除，则为 true，否则为 false
     */
    boolean removeMachine(String app, String ip, int port);
}
