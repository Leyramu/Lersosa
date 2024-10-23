/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.exception.job;

import lombok.Getter;

import java.io.Serial;

/**
 * 计划策略异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@Getter
public class TaskException extends Exception {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误类型
     */
    private final Code code;

    /**
     * 错误类型枚举
     *
     * @param msg  错误信息
     * @param code 错误类型枚举
     */
    public TaskException(String msg, Code code) {
        this(msg, code, null);
    }

    /**
     * 错误类型枚举
     *
     * @param msg      错误信息
     * @param code     错误类型枚举
     * @param nestedEx 异常
     */
    public TaskException(String msg, Code code, Exception nestedEx) {
        super(msg, nestedEx);
        this.code = code;
    }

    /**
     * 错误类型枚举
     */
    public enum Code {

        /**
         * 任务已存在
         */
        TASK_EXISTS,

        /**
         * 任务不存在
         */
        NO_TASK_EXISTS,

        /**
         * 任务已启动
         */
        TASK_ALREADY_STARTED,

        /**
         * 未知错误
         */
        UNKNOWN,

        /**
         * 任务配置错误
         */
        CONFIG_ERROR,

        /**
         * 计划策略错误
         */
        TASK_NODE_NOT_AVAILABLE
    }
}
