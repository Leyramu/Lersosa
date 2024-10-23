/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.domain;

import leyramu.framework.lersosa.common.core.constant.Constants;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@Data
public class R<T> implements Serializable {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = Constants.SUCCESS;

    /**
     * 失败
     */
    public static final int FAIL = Constants.FAIL;

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应成功
     *
     * @param <T> 泛型
     * @return 统一返回值
     */
    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, null);
    }

    /**
     * 响应成功
     *
     * @param data 响应数据
     * @param <T>  泛型
     * @return 统一返回值
     */
    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, null);
    }

    /**
     * 响应成功
     *
     * @param data 响应数据
     * @param msg  响应信息
     * @param <T>  泛型
     * @return 统一返回值
     */
    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    /**
     * 响应失败
     *
     * @param <T> 泛型
     * @return 统一返回值
     */
    public static <T> R<T> fail() {
        return restResult(null, FAIL, null);
    }

    /**
     * 响应失败
     *
     * @param msg 响应信息
     * @param <T> 泛型
     * @return 统一返回值
     */
    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    /**
     * 响应失败
     *
     * @param data 响应数据
     * @param <T>  泛型
     * @return 统一返回值
     */
    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, null);
    }

    /**
     * 响应失败
     *
     * @param data 响应数据
     * @param msg  响应信息
     * @param <T>  泛型
     * @return 统一返回值
     */
    public static <T> R<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    /**
     * 响应失败
     *
     * @param code 响应码
     * @param msg  响应信息
     * @param <T>  泛型
     * @return 统一返回值
     */
    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    /**
     * 响应结果
     *
     * @param data 响应数据
     * @param code 响应码
     * @param msg  响应信息
     * @param <T>  泛型
     * @return 统一返回值
     */
    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    /**
     * 判断是否响应失败
     *
     * @param ret 统一返回值
     * @return boolean
     */
    public static <T> Boolean isError(R<T> ret) {
        return !isSuccess(ret);
    }

    /**
     * 判断是否响应成功
     *
     * @param ret 统一返回值
     * @return boolean
     */
    public static <T> Boolean isSuccess(R<T> ret) {
        return R.SUCCESS == ret.getCode();
    }
}
