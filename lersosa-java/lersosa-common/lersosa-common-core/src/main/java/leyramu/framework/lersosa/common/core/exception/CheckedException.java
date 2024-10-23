/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.exception;

import java.io.Serial;

/**
 * 检查异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class CheckedException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造一个新创建的 CheckedException
     *
     * @param message 异常信息
     */
    public CheckedException(String message) {
        super(message);
    }

    /**
     * 构造一个新创建的 CheckedException
     *
     * @param cause 异常信息
     */
    public CheckedException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个新创建的 CheckedException
     *
     * @param message 异常信息
     * @param cause   异常信息
     */
    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个新创建的 CheckedException
     *
     * @param message            异常信息
     * @param cause              异常信息
     * @param enableSuppression  是否启用 suppression
     * @param writableStackTrace 是否启用 writableStackTrace
     */
    public CheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
