/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.datasource.entity;

import lombok.Data;

import java.util.Objects;

/**
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class SentinelVersion {

    /**
     * 主要版本号.
     */
    private int majorVersion;

    /**
     * 镜像版本号.
     */
    private int minorVersion;

    /**
     * 修复版本号.
     */
    private int fixVersion;

    /**
     * 版本后缀.
     */
    private String postfix;

    /**
     * 无参构造函数.
     */
    public SentinelVersion() {
        this(0, 0, 0);
    }

    /**
     * 构造函数.
     *
     * @param major 主要版本号
     * @param minor 镜像版本号
     * @param fix   修复版本号
     */
    public SentinelVersion(int major, int minor, int fix) {
        this(major, minor, fix, null);
    }

    /**
     * 构造函数.
     *
     * @param major   主要版本号
     * @param minor   镜像版本号
     * @param fix     修复版本号
     * @param postfix 版本后缀
     */
    public SentinelVersion(int major, int minor, int fix, String postfix) {
        this.majorVersion = major;
        this.minorVersion = minor;
        this.fixVersion = fix;
        this.postfix = postfix;
    }

    /**
     * 获取完整版本号.
     */
    public int getFullVersion() {
        return majorVersion * 1000000 + minorVersion * 1000 + fixVersion;
    }

    /**
     * 设置主要版本号.
     *
     * @param majorVersion 主要版本号
     * @return this
     */
    public SentinelVersion setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }

    /**
     * 设置镜像版本号.
     *
     * @param minorVersion 镜像版本号
     * @return this
     */
    public SentinelVersion setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
        return this;
    }

    /**
     * 设置修复版本号.
     *
     * @param fixVersion 修复版本号
     * @return this
     */
    public SentinelVersion setFixVersion(int fixVersion) {
        this.fixVersion = fixVersion;
        return this;
    }

    /**
     * 比较两个版本号大小，返回true表示当前版本号大于参数版本号，否则返回false.
     *
     * @param version 版本号
     * @return boolean
     */
    @SuppressWarnings("unused")
    public boolean greaterThan(SentinelVersion version) {
        if (version == null) {
            return true;
        }
        return getFullVersion() > version.getFullVersion();
    }

    /**
     * 比较两个版本号大小，返回true表示当前版本号大于等于参数版本号，否则返回false.
     *
     * @param version 版本号
     * @return boolean
     */
    public boolean greaterOrEqual(SentinelVersion version) {
        if (version == null) {
            return true;
        }
        return getFullVersion() >= version.getFullVersion();
    }

    /**
     * 判断两个版本号是否相等.
     *
     * @param o 版本号
     * @return boolean
     */
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

    /**
     * 获取版本号hash值.
     *
     * @return int
     */
    @Override
    public int hashCode() {
        int result = majorVersion;
        result = 31 * result + minorVersion;
        result = 31 * result + fixVersion;
        result = 31 * result + (postfix != null ? postfix.hashCode() : 0);
        return result;
    }

    /**
     * 获取版本号字符串.
     *
     * @return String
     */
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
