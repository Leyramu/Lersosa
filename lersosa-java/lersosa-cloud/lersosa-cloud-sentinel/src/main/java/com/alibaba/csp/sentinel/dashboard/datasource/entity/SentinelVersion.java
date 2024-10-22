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

package com.alibaba.csp.sentinel.dashboard.datasource.entity;

import lombok.Data;

import java.util.Objects;

/**
 * @author Eric Zhao
 * @since 0.2.1
 */
@Data
public class SentinelVersion {

    /**
     * Sentinel 主版本
     */
    private int majorVersion;

    /**
     * Sentinel 次版本
     */
    private int minorVersion;

    /**
     * Sentinel 补丁版本
     */
    private int fixVersion;

    /**
     * Sentinel 补丁版本后缀
     */
    private String postfix;

    /**
     * 默认构造函数
     */
    public SentinelVersion() {
        this(0, 0, 0);
    }

    /**
     * 构造函数
     *
     * @param major 主版本
     * @param minor 次版本
     * @param fix   补丁版本
     */
    public SentinelVersion(int major, int minor, int fix) {
        this(major, minor, fix, null);
    }

    /**
     * 构造函数
     *
     * @param major   主版本
     * @param minor   次版本
     * @param fix     补丁版本
     * @param postfix 补丁版本后缀
     */
    public SentinelVersion(int major, int minor, int fix, String postfix) {
        this.majorVersion = major;
        this.minorVersion = minor;
        this.fixVersion = fix;
        this.postfix = postfix;
    }

    /**
     * 000, 000, 000
     */
    public int getFullVersion() {
        return majorVersion * 1000000 + minorVersion * 1000 + fixVersion;
    }

    /**
     * 设置主版本
     *
     * @param majorVersion 主版本
     * @return SentinelVersion
     */
    public SentinelVersion setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }

    /**
     * 设置次版本
     *
     * @param minorVersion 次版本
     * @return SentinelVersion
     */
    public SentinelVersion setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
        return this;
    }

    /**
     * 设置补丁版本
     *
     * @param fixVersion 补丁版本
     * @return SentinelVersion
     */
    public SentinelVersion setFixVersion(int fixVersion) {
        this.fixVersion = fixVersion;
        return this;
    }

    /**
     * 设置补丁版本后缀
     *
     * @param version 补丁版本
     * @return SentinelVersion
     */
    public boolean greaterThan(SentinelVersion version) {
        if (version == null) {
            return true;
        }
        return getFullVersion() > version.getFullVersion();
    }

    /**
     * 设置补丁版本后缀
     *
     * @param version 补丁版本后缀
     * @return SentinelVersion
     */
    public boolean greaterOrEqual(SentinelVersion version) {
        if (version == null) {
            return true;
        }
        return getFullVersion() >= version.getFullVersion();
    }

    /**
     * 比较两个版本是否相等
     *
     * @param o 对象
     * @return 是否相等
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
     * 获取hashCode
     *
     * @return hashCode
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
     * 获取字符串
     *
     * @return 字符串
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
