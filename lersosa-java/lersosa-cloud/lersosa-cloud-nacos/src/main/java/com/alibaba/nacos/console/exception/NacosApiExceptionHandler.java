/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.nacos.console.exception;

import com.alibaba.nacos.api.annotation.NacosApi;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.exception.api.NacosApiException;
import com.alibaba.nacos.api.exception.runtime.NacosRuntimeException;
import com.alibaba.nacos.api.model.v2.ErrorCode;
import com.alibaba.nacos.api.model.v2.Result;
import com.alibaba.nacos.common.utils.ExceptionUtil;
import com.alibaba.nacos.plugin.auth.exception.AccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Nacos API 的异常处理程序
 *
 * @author dongyafei
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.2.0
 * @since 2024/7/31
 */
@Order(-1)
@ResponseBody
@ControllerAdvice(annotations = {NacosApi.class})
public class NacosApiExceptionHandler {

    /**
     * Nacos API 异常处理程序
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosApiExceptionHandler.class);

    /**
     * Nacos API 异常处理程序
     *
     * @param e NacosApiException
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(NacosApiException.class)
    public ResponseEntity<Result<String>> handleNacosApiException(NacosApiException e) {
        LOGGER.error("got exception. {} {}", e.getErrAbstract(), e.getErrMsg());
        return ResponseEntity.status(e.getErrCode())
                .body(new Result<>(e.getDetailErrCode(), e.getErrAbstract(), e.getErrMsg()));
    }

    /**
     * Nacos 异常处理程序
     *
     * @param e {@link NacosException}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(NacosException.class)
    public ResponseEntity<Result<String>> handleNacosException(NacosException e) {
        LOGGER.error("got exception. {}", e.getErrMsg());
        return ResponseEntity.status(e.getErrCode()).body(Result.failure(ErrorCode.SERVER_ERROR, e.getErrMsg()));
    }

    /**
     * Nacos 异常处理程序
     *
     * @param e {@link NacosRuntimeException}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(NacosRuntimeException.class)
    public ResponseEntity<Result<String>> handleNacosRuntimeException(NacosRuntimeException e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return ResponseEntity.status(e.getErrCode()).body(Result.failure(ErrorCode.SERVER_ERROR, e.getMessage()));
    }

    /**
     * Nacos 异常处理程序
     *
     * @param e {@link DataAccessException}
     * @return {@link ResponseEntity}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return Result.failure(ErrorCode.PARAMETER_MISSING, e.getMessage());
    }

    /**
     * Nacos 异常处理程序
     *
     * @param e {@link HttpMessageConversionException}
     * @return {@link ResponseEntity}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public Result<String> handleHttpMessageConversionException(HttpMessageConversionException e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return Result.failure(ErrorCode.PARAMETER_VALIDATE_ERROR, e.getMessage());
    }

    /**
     * 处理数字格式异常
     *
     * @param e {@link NumberFormatException}
     * @return {@link ResponseEntity}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public Result<String> handleNumberFormatException(NumberFormatException e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return Result.failure(ErrorCode.PARAMETER_VALIDATE_ERROR, e.getMessage());
    }

    /**
     * 处理非法参数异常
     *
     * @param e {@link IllegalArgumentException}
     * @return {@link ResponseEntity}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return Result.failure(ErrorCode.PARAMETER_VALIDATE_ERROR, e.getMessage());
    }

    /**
     * 处理缺少的 servlet 请求参数异常
     *
     * @param e {@link MissingServletRequestParameterException}
     * @return {@link ResponseEntity}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return Result.failure(ErrorCode.PARAMETER_MISSING, e.getMessage());
    }

    /**
     * 处理 HTTP 媒体类型异常
     *
     * @param e {@link HttpMediaTypeException}
     * @return {@link ResponseEntity}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeException.class)
    public Result<String> handleHttpMediaTypeException(HttpMediaTypeException e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return Result.failure(ErrorCode.MEDIA_TYPE_ERROR, e.getMessage());
    }

    /**
     * 处理访问异常
     *
     * @param e {@link AccessException}
     * @return {@link ResponseEntity}
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessException.class)
    public Result<String> handleAccessException(AccessException e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return Result.failure(ErrorCode.ACCESS_DENIED, e.getErrMsg());
    }

    /**
     * 处理数据访问异常
     *
     * @param e {@link DataAccessException}
     * @return {@link ResponseEntity}
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {DataAccessException.class, ServletException.class, IOException.class})
    public Result<String> handleDataAccessException(Exception e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return Result.failure(ErrorCode.DATA_ACCESS_ERROR, e.getMessage());
    }

    /**
     * 处理其他异常
     *
     * @param e {@link Exception}
     * @return {@link ResponseEntity}
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<String> handleOtherException(Exception e) {
        logErrorMessage(e.getMessage(), ExceptionUtil.getAllExceptionMsg(e));
        return Result.failure(e.getMessage());
    }

    /**
     * 处理错误信息
     *
     * @param message         错误信息
     * @param allExceptionMsg 所有异常消息
     */
    private void logErrorMessage(String message, String allExceptionMsg) {
        LOGGER.error("异常：{} {}", message, allExceptionMsg);
    }
}
