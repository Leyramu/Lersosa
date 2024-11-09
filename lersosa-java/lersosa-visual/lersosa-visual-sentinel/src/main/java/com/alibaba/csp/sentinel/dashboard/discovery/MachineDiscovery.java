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

public interface MachineDiscovery {

    String UNKNOWN_APP_NAME = "CLUSTER_NOT_STARTED";

    List<String> getAppNames();

    Set<AppInfo> getBriefApps();

    AppInfo getDetailApp(String app);

    /**
     * Remove the given app from the application registry.
     *
     * @param app application name
     * @since 1.5.0
     */
    void removeApp(String app);

    long addMachine(MachineInfo machineInfo);

    /**
     * Remove the given machine instance from the application registry.
     *
     * @param app  the application name of the machine
     * @param ip   machine IP
     * @param port machine port
     * @return true if removed, otherwise false
     * @since 1.5.0
     */
    boolean removeMachine(String app, String ip, int port);
}
