/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.util;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * 用于机器信息的 Utils 类.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@NoArgsConstructor
public final class MachineUtils {

    public static Optional<Integer> parseCommandPort(String machineIp) {
        try {
            if (!machineIp.contains("@")) {
                return Optional.empty();
            }
            String[] str = machineIp.split("@");
            if (str.length <= 1) {
                return Optional.empty();
            }
            return Optional.of(Integer.parseInt(str[1]));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public static Optional<Tuple2<String, Integer>> parseCommandIpAndPort(String machineIp) {
        try {
            if (StringUtil.isEmpty(machineIp) || !machineIp.contains("@")) {
                return Optional.empty();
            }
            String[] str = machineIp.split("@");
            if (str.length <= 1) {
                return Optional.empty();
            }
            return Optional.of(Tuple2.of(str[0], Integer.parseInt(str[1])));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
