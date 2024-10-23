/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.csp.sentinel.dashboard.domain.vo.gateway.rule;

import lombok.Data;

/**
 * 用于更新网关流规则的值对象
 *
 * @author cdfive
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.7.0
 * @since 2024/9/3
 */
@Data
public class UpdateFlowRuleReqVo {

    /**
     * 规则 ID
     */
    private Long id;

    /**
     * 应用名称
     */
    private String app;

    /**
     * 流控模式
     */
    private Integer grade;

    /**
     * 限流阈值
     */
    private Double count;

    /**
     * 统计时间窗口，单位（秒）
     */
    private Long interval;

    /**
     * 时间窗口单位
     */
    private Integer intervalUnit;

    /**
     * 流控策略
     */
    private Integer controlBehavior;

    /**
     * 令牌桶模式下，每秒放入的令牌数
     */
    private Integer burst;

    /**
     * 队列超时时间，单位（ms）
     */
    private Integer maxQueueingTimeoutMs;

    /**
     * 匹配规则
     */
    private GatewayParamFlowItemVo paramItem;
}
