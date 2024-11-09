/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.common.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 任务状态枚举
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    /**
     * 撤销
     */
    CANCEL("cancel", "撤销"),
    /**
     * 通过
     */
    PASS("pass", "通过"),
    /**
     * 待审核
     */
    WAITING("waiting", "待审核"),
    /**
     * 作废
     */
    INVALID("invalid", "作废"),
    /**
     * 退回
     */
    BACK("back", "退回"),
    /**
     * 终止
     */
    TERMINATION("termination", "终止"),
    /**
     * 转办
     */
    TRANSFER("transfer", "转办"),
    /**
     * 委托
     */
    PENDING("pending", "委托"),
    /**
     * 抄送
     */
    COPY("copy", "抄送"),
    /**
     * 加签
     */
    SIGN("sign", "加签"),
    /**
     * 减签
     */
    SIGN_OFF("sign_off", "减签"),
    /**
     * 超时
     */
    TIMEOUT("timeout", "超时");

    /**
     * 状态
     */
    private final String status;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 任务业务状态
     *
     * @param status 状态
     */
    public static String findByStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return StrUtil.EMPTY;
        }

        return Arrays.stream(TaskStatusEnum.values())
            .filter(statusEnum -> statusEnum.getStatus().equals(status))
            .findFirst()
            .map(TaskStatusEnum::getDesc)
            .orElse(StrUtil.EMPTY);
    }
}

