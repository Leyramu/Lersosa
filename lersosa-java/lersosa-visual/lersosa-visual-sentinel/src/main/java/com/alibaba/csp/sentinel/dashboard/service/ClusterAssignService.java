/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package com.alibaba.csp.sentinel.dashboard.service;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterAppAssignResultVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterAppAssignMap;

import java.util.List;
import java.util.Set;

/**
 * @author Eric Zhao
 * @since 1.4.1
 */
public interface ClusterAssignService {

    /**
     * Unbind a specific cluster server and its clients.
     *
     * @param app       app name
     * @param machineId valid machine ID ({@code host@commandPort})
     * @return assign result
     */
    ClusterAppAssignResultVO unbindClusterServer(String app, String machineId);

    /**
     * Unbind a set of cluster servers and its clients.
     *
     * @param app          app name
     * @param machineIdSet set of valid machine ID ({@code host@commandPort})
     * @return assign result
     */
    ClusterAppAssignResultVO unbindClusterServers(String app, Set<String> machineIdSet);

    /**
     * Apply cluster server and client assignment for provided app.
     *
     * @param app          app name
     * @param clusterMap   cluster assign map (server -> clients)
     * @param remainingSet unassigned set of machine ID
     * @return assign result
     */
    ClusterAppAssignResultVO applyAssignToApp(String app, List<ClusterAppAssignMap> clusterMap,
                                              Set<String> remainingSet);
}
