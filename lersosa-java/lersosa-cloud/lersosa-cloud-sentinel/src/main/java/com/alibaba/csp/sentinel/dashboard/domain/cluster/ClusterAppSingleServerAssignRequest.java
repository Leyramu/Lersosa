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

import java.util.Set;

/**
 * 集群应用单服务器分配请求
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterAppSingleServerAssignRequest {

    /**
     * 集群应用分配映射，表示分配给单个服务器的应用信息
     */
    private ClusterAppAssignMap clusterMap;

    /**
     * 剩余服务器列表，表示未分配的应用服务器
     */
    private Set<String> remainingList;

    /**
     * 设置集群应用分配映射的方法
     *
     * @param clusterMap 集群应用分配映射
     * @return 当前的ClusterAppSingleServerAssignRequest实例，以便进行链式调用
     */
    public ClusterAppSingleServerAssignRequest setClusterMap(ClusterAppAssignMap clusterMap) {
        this.clusterMap = clusterMap;
        return this;
    }

    /**
     * 设置剩余服务器列表的方法
     *
     * @param remainingList 服务器的集合，表示剩余可用的服务器
     * @return 当前的ClusterAppSingleServerAssignRequest实例，以便进行链式调用
     */
    public ClusterAppSingleServerAssignRequest setRemainingList(Set<String> remainingList) {
        this.remainingList = remainingList;
        return this;
    }

    /**
     * 重写toString方法，用于打印ClusterAppSingleServerAssignRequest对象的信息
     *
     * @return 包含clusterMap和remainingList信息的字符串
     */
    @Override
    public String toString() {
        return "ClusterAppSingleServerAssignRequest{" +
                "clusterMap=" + clusterMap +
                ", remainingList=" + remainingList +
                '}';
    }
}
