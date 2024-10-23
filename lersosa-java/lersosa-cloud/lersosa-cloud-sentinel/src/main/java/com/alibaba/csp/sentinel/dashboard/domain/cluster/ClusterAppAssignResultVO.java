/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster;

import lombok.Getter;

import java.util.Set;

/**
 * 集群应用程序分配结果
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.1
 * @since 2024/9/3
 */
@Getter
public class ClusterAppAssignResultVO {

    /**
     * 失败的服务器集合
     */
    private Set<String> failedServerSet;

    /**
     * 失败的客户端集合
     */
    private Set<String> failedClientSet;

    /**
     * 总数量
     */
    private Integer totalCount;

    /**
     * 设置失败的服务器集合
     *
     * @param failedServerSet 失败的服务器集合
     * @return 当前的 ClusterAppAssignResultVO 实例
     */
    public ClusterAppAssignResultVO setFailedServerSet(Set<String> failedServerSet) {
        this.failedServerSet = failedServerSet;
        return this;
    }

    /**
     * 设置失败的客户端集合
     *
     * @param failedClientSet 失败的客户端集合
     * @return 当前的 ClusterAppAssignResultVO 实例
     */
    public ClusterAppAssignResultVO setFailedClientSet(Set<String> failedClientSet) {
        this.failedClientSet = failedClientSet;
        return this;
    }

    /**
     * 设置总数量
     *
     * @param totalCount 总数量
     * @return 当前的 ClusterAppAssignResultVO 实例
     */
    public ClusterAppAssignResultVO setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    /**
     * 将当前对象转换为字符串表示
     *
     * @return 当前对象的字符串表示
     */
    @Override
    public String toString() {
        return "ClusterAppAssignResultVO{" +
                "failedServerSet=" + failedServerSet +
                ", failedClientSet=" + failedClientSet +
                ", totalCount=" + totalCount +
                '}';
    }
}
