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

package com.alibaba.csp.sentinel.dashboard.domain.cluster.state;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterClientInfoVO;
import lombok.Getter;

/**
 * 群集客户端状态信息
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ClusterClientStateVO {

    /**
     * 群集令牌客户端状态
     */
    private ClusterClientInfoVO clientConfig;

    /**
     * 设置客户端配置信息
     *
     * @param clientConfig 客户端配置信息
     * @return this
     */
    public ClusterClientStateVO setClientConfig(ClusterClientInfoVO clientConfig) {
        this.clientConfig = clientConfig;
        return this;
    }

    /**
     * 重写 toString 方法
     *
     * @return String
     */
    @Override
    public String toString() {
        return "ClusterClientStateVO{" +
               "clientConfig=" + clientConfig +
               '}';
    }
}
