/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
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
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.entity;

import lombok.Data;

import java.util.Objects;

/**
 * Sentinel 版本实体类。
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
@SuppressWarnings("unused")
public class SentinelVersion {
    private int majorVersion;
    private int minorVersion;
    private int fixVersion;
    private String postfix;

    public SentinelVersion() {
        this(0, 0, 0);
    }

    public SentinelVersion(int major, int minor, int fix) {
        this(major, minor, fix, null);
    }

    public SentinelVersion(int major, int minor, int fix, String postfix) {
        this.majorVersion = major;
        this.minorVersion = minor;
        this.fixVersion = fix;
        this.postfix = postfix;
    }

    public int getFullVersion() {
        return majorVersion * 1000000 + minorVersion * 1000 + fixVersion;
    }

    public SentinelVersion setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }

    public SentinelVersion setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
        return this;
    }

    public SentinelVersion setFixVersion(int fixVersion) {
        this.fixVersion = fixVersion;
        return this;
    }

    public SentinelVersion setPostfix(String postfix) {
        this.postfix = postfix;
        return this;
    }

    public boolean greaterThan(SentinelVersion version) {
        if (version == null) {
            return true;
        }
        return getFullVersion() > version.getFullVersion();
    }

    public boolean greaterOrEqual(SentinelVersion version) {
        if (version == null) {
            return true;
        }
        return getFullVersion() >= version.getFullVersion();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SentinelVersion that = (SentinelVersion) o;

        if (getFullVersion() != that.getFullVersion()) {
            return false;
        }
        return Objects.equals(postfix, that.postfix);
    }

    @Override
    public int hashCode() {
        int result = majorVersion;
        result = 31 * result + minorVersion;
        result = 31 * result + fixVersion;
        result = 31 * result + (postfix != null ? postfix.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SentinelVersion{" +
            "majorVersion=" + majorVersion +
            ", minorVersion=" + minorVersion +
            ", fixVersion=" + fixVersion +
            ", postfix='" + postfix + '\'' +
            '}';
    }
}
