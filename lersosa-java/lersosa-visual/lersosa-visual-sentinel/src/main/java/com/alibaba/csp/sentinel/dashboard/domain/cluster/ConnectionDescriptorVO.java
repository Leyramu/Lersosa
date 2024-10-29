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
