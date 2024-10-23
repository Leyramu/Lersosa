/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.cluster;

import lombok.Getter;

import java.util.List;

/**
 * 集群组连接信息
 *
 * @author Eric Zhao
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.4.0
 * @since 2024/9/3
 */
@Getter
public class ConnectionGroupVO {

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 连接描述符集合
     */
    private List<ConnectionDescriptorVO> connectionSet;

    /**
     * 已连接数量
     */
    private Integer connectedCount;

    /**
     * 设置命名空间方法
     *
     * @param namespace 命名空间字符串
     * @return 当前对象实例，支持链式调用
     */
    public ConnectionGroupVO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * 设置连接集合方法
     *
     * @param connectionSet 连接描述符列表
     * @return 当前对象实例，支持链式调用
     */
    public ConnectionGroupVO setConnectionSet(List<ConnectionDescriptorVO> connectionSet) {
        this.connectionSet = connectionSet;
        return this;
    }

    /**
     * 设置已连接数量方法
     *
     * @param connectedCount 已连接数量
     * @return 当前对象实例，支持链式调用
     */
    public ConnectionGroupVO setConnectedCount(Integer connectedCount) {
        this.connectedCount = connectedCount;
        return this;
    }

    /**
     * 重写toString方法，用于打印对象信息
     *
     * @return 对象信息的字符串表示
     */
    @Override
    public String toString() {
        return "ConnectionGroupVO{" +
                "namespace='" + namespace + '\'' +
                ", connectionSet=" + connectionSet +
                ", connectedCount=" + connectedCount +
                '}';
    }
}
