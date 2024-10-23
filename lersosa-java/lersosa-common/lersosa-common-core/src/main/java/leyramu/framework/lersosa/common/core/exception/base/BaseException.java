/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.exception.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;

/**
 * 基础异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private final String module;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误码对应的参数
     */
    private final Object[] args;

    /**
     * 错误消息
     */
    private final String defaultMessage;

    /**
     * 构造
     *
     * @param module 模块
     * @param code   错误码
     * @param args   错误码对应的参数
     */
    public BaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    /**
     * 构造
     *
     * @param module         模块
     * @param defaultMessage 默认信息
     */
    public BaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    /**
     * 构造
     *
     * @param code 错误码
     * @param args 错误码对应的参数
     */
    public BaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    /**
     * 构造
     *
     * @param defaultMessage 默认信息
     */
    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }
}
