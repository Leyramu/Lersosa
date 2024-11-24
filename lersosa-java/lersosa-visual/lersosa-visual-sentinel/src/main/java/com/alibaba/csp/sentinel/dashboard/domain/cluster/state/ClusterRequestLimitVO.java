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

package com.alibaba.csp.sentinel.dashboard.domain.cluster.state;

import lombok.Data;

/**
 * 集群请求限制视图对象.
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 2.0.0
 * @since 2024/11/13
 */
@Data
public class ClusterRequestLimitVO {

    /**
     * 集群命名空间.
     */
    private String namespace;

    /**
     * 当前QPS.
     */
    private Double currentQps;

    /**
     * 最大允许QPS.
     */
    private Double maxAllowedQps;

    /**
     * 设置命名空间.
     *
     * @param namespace 命名空间
     * @return this
     */
    @SuppressWarnings("unused")
    public ClusterRequestLimitVO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * 设置当前QPS.
     *
     * @param currentQps 当前QPS
     * @return this
     */
    @SuppressWarnings("unused")
    public ClusterRequestLimitVO setCurrentQps(Double currentQps) {
        this.currentQps = currentQps;
        return this;
    }

    /**
     * 设置最大允许QPS.
     *
     * @param maxAllowedQps 最大允许QPS
     * @return this
     */
    @SuppressWarnings("unused")
    public ClusterRequestLimitVO setMaxAllowedQps(Double maxAllowedQps) {
        this.maxAllowedQps = maxAllowedQps;
        return this;
    }

    /**
     * 输出字符串.
     *
     * @return 输出字符串
     */
    @Override
    public String toString() {
        return "ClusterRequestLimitVO{" +
            "namespace='" + namespace + '\'' +
            ", currentQps=" + currentQps +
            ", maxAllowedQps=" + maxAllowedQps +
            '}';
    }
}
