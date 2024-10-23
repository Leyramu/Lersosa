/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
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
