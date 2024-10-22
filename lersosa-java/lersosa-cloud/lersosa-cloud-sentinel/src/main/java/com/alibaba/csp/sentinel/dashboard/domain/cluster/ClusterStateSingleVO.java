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
 * 集群状态单一视图
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterStateSingleVO {

    /**
     * 地址信息
     */
    private String address;

    /**
     * 模式信息
     */
    private Integer mode;

    /**
     * 目标信息
     */
    private String target;

    /**
     * 设置地址信息
     *
     * @param address 地址字符串
     * @return 当前ClusterStateSingleVO实例，以便进行链式调用
     */
    public ClusterStateSingleVO setAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * 设置模式信息
     *
     * @param mode 模式整数
     * @return 当前ClusterStateSingleVO实例，以便进行链式调用
     */
    public ClusterStateSingleVO setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 设置目标信息
     *
     * @param target 目标字符串
     * @return 当前ClusterStateSingleVO实例，以便进行链式调用
     */
    public ClusterStateSingleVO setTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * 重写toString方法，方便打印和查看集群状态的详细信息
     *
     * @return 包含地址、模式和目标信息的字符串表示
     */
    @Override
    public String toString() {
        return "ClusterStateSingleVO{" +
               "address='" + address + '\'' +
               ", mode=" + mode +
               ", target='" + target + '\'' +
               '}';
    }
}
