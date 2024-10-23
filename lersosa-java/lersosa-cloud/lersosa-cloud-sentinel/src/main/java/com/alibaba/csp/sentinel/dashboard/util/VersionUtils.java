/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.util;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.SentinelVersion;
import com.alibaba.csp.sentinel.util.StringUtil;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * 用于解析版本的 Util 类
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 0.2.1
 * @since 2024/9/3
 */
@NoArgsConstructor
public final class VersionUtils {

    /**
     * 从原始字符串解析 Sentinel 的版本
     *
     * @param verStr version string
     * @return 如果版本有效，则解析 {@link SentinelVersion};如果格式有问题，则为空
     */
    public static Optional<SentinelVersion> parseVersion(String verStr) {
        if (StringUtil.isBlank(verStr)) {
            return Optional.empty();
        }
        try {
            String versionFull = verStr;
            SentinelVersion version = new SentinelVersion();

            int index = versionFull.indexOf("-");
            if (index == 0) {
                return Optional.empty();
            }
            if (index == versionFull.length() - 1) {
                return Optional.empty();
            } else if (index > 0) {
                version.setPostfix(versionFull.substring(index + 1));
            }

            if (index >= 0) {
                versionFull = versionFull.substring(0, index);
            }

            int segment = 0;
            int[] ver = new int[3];
            while (segment < ver.length) {
                index = versionFull.indexOf('.');
                if (index < 0) {
                    if (!versionFull.isEmpty()) {
                        ver[segment] = Integer.parseInt(versionFull);
                    }
                    break;
                }
                ver[segment] = Integer.parseInt(versionFull.substring(0, index));
                versionFull = versionFull.substring(index + 1);
                segment++;
            }

            if (ver[0] < 1) {
                return Optional.empty();
            } else {
                return Optional.of(version
                        .setMajorVersion(ver[0])
                        .setMinorVersion(ver[1])
                        .setFixVersion(ver[2]));
            }
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
