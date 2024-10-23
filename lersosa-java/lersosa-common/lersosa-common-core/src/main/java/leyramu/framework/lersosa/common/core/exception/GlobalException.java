/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 全局异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@NoArgsConstructor
public class GlobalException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    @Getter
    private String detailMessage;

    /**
     * 全局异常，只存在 message
     *
     * @param message 错误提示
     */
    public GlobalException(String message) {
        this.message = message;
    }

    /**
     * 添加明细信息
     *
     * @param detailMessage 错误明细信息
     * @return this
     */
    public GlobalException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    /**
     * 获取提示信息
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 设置提示信息
     *
     * @param message 提示信息
     * @return this
     */
    public GlobalException setMessage(String message) {
        this.message = message;
        return this;
    }
}
