/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.constant;

/**
 * 返回状态码.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@SuppressWarnings("unused")
public interface HttpStatus {

    /**
     * 操作成功.
     */
    int SUCCESS = 200;

    /**
     * 对象创建成功.
     */
    int CREATED = 201;

    /**
     * 请求已经被接受.
     */
    int ACCEPTED = 202;

    /**
     * 操作已经执行成功，但是没有返回数据.
     */
    int NO_CONTENT = 204;

    /**
     * 资源已被移除.
     */
    int MOVED_PERM = 301;

    /**
     * 重定向.
     */
    int SEE_OTHER = 303;

    /**
     * 资源没有被修改.
     */
    int NOT_MODIFIED = 304;

    /**
     * 参数列表错误（缺少，格式不匹配）.
     */
    int BAD_REQUEST = 400;

    /**
     * 未授权.
     */
    int UNAUTHORIZED = 401;

    /**
     * 访问受限，授权过期.
     */
    int FORBIDDEN = 403;

    /**
     * 资源，服务未找到.
     */
    int NOT_FOUND = 404;

    /**
     * 不允许的http方法.
     */
    int BAD_METHOD = 405;

    /**
     * 资源冲突，或者资源被锁.
     */
    int CONFLICT = 409;

    /**
     * 不支持的数据，媒体类型.
     */
    int UNSUPPORTED_TYPE = 415;

    /**
     * 系统内部错误.
     */
    int ERROR = 500;

    /**
     * 接口未实现.
     */
    int NOT_IMPLEMENTED = 501;

    /**
     * 系统警告消息.
     */
    int WARN = 601;
}
