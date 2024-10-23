/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterAppAssignMap;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * 集群客户端的完全分配请求
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterAppFullAssignRequest {

    /**
     * 集群应用分配映射列表
     */
    private List<ClusterAppAssignMap> clusterMap;

    /**
     * 剩余项集合
     */
    private Set<String> remainingList;

    /**
     * 设置集群应用分配映射列表
     *
     * @param clusterMap 集群应用分配映射列表
     * @return 当前实例，方便链式调用
     */
    public ClusterAppFullAssignRequest setClusterMap(List<ClusterAppAssignMap> clusterMap) {
        this.clusterMap = clusterMap;
        return this;
    }

    /**
     * 设置剩余项集合
     *
     * @param remainingList 剩余项的集合
     * @return 当前实例，方便链式调用
     */
    public ClusterAppFullAssignRequest setRemainingList(Set<String> remainingList) {
        this.remainingList = remainingList;
        return this;
    }

    /**
     * 获取当前实例的字符串表示形式。
     *
     * @return 当前实例的字符串表示形式
     */
    @Override
    public String toString() {
        return "ClusterAppFullAssignRequest{" +
                "clusterMap=" + clusterMap +
                ", remainingList=" + remainingList +
                '}';
    }
}
