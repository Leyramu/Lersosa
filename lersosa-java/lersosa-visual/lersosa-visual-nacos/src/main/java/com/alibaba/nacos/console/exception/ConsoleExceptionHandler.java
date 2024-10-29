/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.nacos.console.exception;

import com.alibaba.nacos.api.exception.runtime.NacosRuntimeException;
import com.alibaba.nacos.common.model.RestResultUtils;
import com.alibaba.nacos.common.utils.ExceptionUtil;
import com.alibaba.nacos.core.utils.Commons;
import com.alibaba.nacos.plugin.auth.exception.AccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制台模块的异常处理程序
 *
 * @author nkorange
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.2.0
 * @since 2024/7/31
 */
@ControllerAdvice
public class ConsoleExceptionHandler {

    /**
     * 控制台模块的异常处理程序
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleExceptionHandler.class);

    /**
     * 处理权限异常
     *
     * @param e 异常
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(AccessException.class)
    private ResponseEntity<String> handleAccessException(AccessException e) {
        logErrorMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getErrMsg());
    }

    /**
     * 处理参数异常
     *
     * @param e 异常
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionUtil.getAllExceptionMsg(e));
    }

    /**
     * 处理运行时异常
     *
     * @param e 异常
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(NacosRuntimeException.class)
    private ResponseEntity<String> handleNacosRuntimeException(NacosRuntimeException e) {
        logErrorMessage(e.getMessage());
        return ResponseEntity.status(e.getErrCode()).body(ExceptionUtil.getAllExceptionMsg(e));
    }

    /**
     * 处理其他异常
     *
     * @param request 请求
     * @param e       异常
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleException(HttpServletRequest request, Exception e) {
        String uri = request.getRequestURI();
        LOGGER.error("CONSOLE {}", uri, e);
        if (uri.contains(Commons.NACOS_SERVER_VERSION_V2)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(RestResultUtils.failed(HtmlUtils.htmlEscape(ExceptionUtil.getAllExceptionMsg(e), "utf-8")));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(HtmlUtils.htmlEscape(ExceptionUtil.getAllExceptionMsg(e), "utf-8"));
    }

    /**
     * 处理错误信息
     *
     * @param errMsg 错误信息
     */
    private void logErrorMessage(String errMsg) {
        LOGGER.error("异常：{}", errMsg);
    }
}
