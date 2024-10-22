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
