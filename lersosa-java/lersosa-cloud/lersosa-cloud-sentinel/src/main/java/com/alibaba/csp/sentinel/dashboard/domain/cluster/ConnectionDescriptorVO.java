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

package com.alibaba.csp.sentinel.dashboard.domain.cluster;

import lombok.Getter;

/**
 * 连接描述符VO类，用于封装连接描述符信息
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ConnectionDescriptorVO {

    /**
     * 连接地址
     */
    private String address;

    /**
     * 主机信息
     */
    private String host;

    /**
     * 设置连接地址的方法
     *
     * @param address 连接的地址
     * @return 返回ConnectionDescriptorVO实例，支持链式调用
     */
    public ConnectionDescriptorVO setAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * 设置主机信息的方法
     *
     * @param host 主机的信息
     * @return 返回ConnectionDescriptorVO实例，支持链式调用
     */
    public ConnectionDescriptorVO setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * 将连接描述符转换为字符串的方法
     *
     * @return 返回连接描述符的字符串表示形式
     */
    @Override
    public String toString() {
        return "ConnectionDescriptorVO{" +
               "address='" + address + '\'' +
               ", host='" + host + '\'' +
               '}';
    }
}
