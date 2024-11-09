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

import java.util.Optional;

/**
 * Util class for parsing version.
 *
 * @author Eric Zhao
 * @since 0.2.1
 */
public final class VersionUtils {

    private VersionUtils() {
    }

    /**
     * Parse version of Sentinel from raw string.
     *
     * @param verStr version string
     * @return parsed {@link SentinelVersion} if the version is valid; empty if
     * there is something wrong with the format
     */
    public static Optional<SentinelVersion> parseVersion(String verStr) {
        if (StringUtil.isBlank(verStr)) {
            return Optional.empty();
        }
        try {
            String versionFull = verStr;
            SentinelVersion version = new SentinelVersion();

            // postfix
            int index = versionFull.indexOf("-");
            if (index == 0) {
                // Start with "-"
                return Optional.empty();
            }
            if (index == versionFull.length() - 1) {
                // End with "-"
            } else if (index > 0) {
                version.setPostfix(versionFull.substring(index + 1));
            }

            if (index >= 0) {
                versionFull = versionFull.substring(0, index);
            }

            // x.x.x
            int segment = 0;
            int[] ver = new int[3];
            while (segment < ver.length) {
                index = versionFull.indexOf('.');
                if (index < 0) {
                    if (versionFull.length() > 0) {
                        ver[segment] = Integer.valueOf(versionFull);
                    }
                    break;
                }
                ver[segment] = Integer.valueOf(versionFull.substring(0, index));
                versionFull = versionFull.substring(index + 1);
                segment++;
            }

            if (ver[0] < 1) {
                // Wrong format, return empty.
                return Optional.empty();
            } else {
                return Optional.of(version
                    .setMajorVersion(ver[0])
                    .setMinorVersion(ver[1])
                    .setFixVersion(ver[2]));
            }
        } catch (Exception ex) {
            // Parse fail, return empty.
            return Optional.empty();
        }
    }
}
