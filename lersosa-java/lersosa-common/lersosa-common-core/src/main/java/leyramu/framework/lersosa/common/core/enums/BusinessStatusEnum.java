/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.enums;

import cn.hutool.core.util.StrUtil;
import leyramu.framework.lersosa.common.core.exception.ServiceException;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 业务状态枚举
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Getter
@AllArgsConstructor
public enum BusinessStatusEnum {

    /**
     * 已撤销
     */
    CANCEL("cancel", "已撤销"),
    /**
     * 草稿
     */
    DRAFT("draft", "草稿"),
    /**
     * 待审核
     */
    WAITING("waiting", "待审核"),
    /**
     * 已完成
     */
    FINISH("finish", "已完成"),
    /**
     * 已作废
     */
    INVALID("invalid", "已作废"),
    /**
     * 已退回
     */
    BACK("back", "已退回"),
    /**
     * 已终止
     */
    TERMINATION("termination", "已终止");

    /**
     * 状态
     */
    private final String status;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 获取业务状态
     *
     * @param status 状态
     */
    public static String findByStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return StrUtil.EMPTY;
        }
        return Arrays.stream(BusinessStatusEnum.values())
            .filter(statusEnum -> statusEnum.getStatus().equals(status))
            .findFirst()
            .map(BusinessStatusEnum::getDesc)
            .orElse(StrUtil.EMPTY);
    }

    /**
     * 启动流程校验
     *
     * @param status 状态
     */
    public static void checkStartStatus(String status) {
        if (WAITING.getStatus().equals(status)) {
            throw new ServiceException("该单据已提交过申请,正在审批中！");
        } else if (FINISH.getStatus().equals(status)) {
            throw new ServiceException("该单据已完成申请！");
        } else if (INVALID.getStatus().equals(status)) {
            throw new ServiceException("该单据已作废！");
        } else if (TERMINATION.getStatus().equals(status)) {
            throw new ServiceException("该单据已终止！");
        } else if (StringUtils.isBlank(status)) {
            throw new ServiceException("流程状态为空！");
        }
    }

    /**
     * 撤销流程校验
     *
     * @param status 状态
     */
    public static void checkCancelStatus(String status) {
        if (CANCEL.getStatus().equals(status)) {
            throw new ServiceException("该单据已撤销！");
        } else if (FINISH.getStatus().equals(status)) {
            throw new ServiceException("该单据已完成申请！");
        } else if (INVALID.getStatus().equals(status)) {
            throw new ServiceException("该单据已作废！");
        } else if (TERMINATION.getStatus().equals(status)) {
            throw new ServiceException("该单据已终止！");
        } else if (BACK.getStatus().equals(status)) {
            throw new ServiceException("该单据已退回！");
        } else if (StringUtils.isBlank(status)) {
            throw new ServiceException("流程状态为空！");
        }
    }

    /**
     * 驳回流程校验
     *
     * @param status 状态
     */
    public static void checkBackStatus(String status) {
        if (BACK.getStatus().equals(status)) {
            throw new ServiceException("该单据已退回！");
        } else if (FINISH.getStatus().equals(status)) {
            throw new ServiceException("该单据已完成申请！");
        } else if (INVALID.getStatus().equals(status)) {
            throw new ServiceException("该单据已作废！");
        } else if (TERMINATION.getStatus().equals(status)) {
            throw new ServiceException("该单据已终止！");
        } else if (CANCEL.getStatus().equals(status)) {
            throw new ServiceException("该单据已撤销！");
        } else if (StringUtils.isBlank(status)) {
            throw new ServiceException("流程状态为空！");
        }
    }

    /**
     * 作废,终止流程校验
     *
     * @param status 状态
     */
    public static void checkInvalidStatus(String status) {
        if (FINISH.getStatus().equals(status)) {
            throw new ServiceException("该单据已完成申请！");
        } else if (INVALID.getStatus().equals(status)) {
            throw new ServiceException("该单据已作废！");
        } else if (TERMINATION.getStatus().equals(status)) {
            throw new ServiceException("该单据已终止！");
        } else if (StringUtils.isBlank(status)) {
            throw new ServiceException("流程状态为空！");
        }
    }
}

