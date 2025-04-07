/*
 * Copyright (c) 2025 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.cloud.seata.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.common.util.StringUtils;
import org.apache.seata.core.context.RootContext;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Seata 处理程序拦截器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
@Slf4j
public class SeataHandlerInterceptor implements HandlerInterceptor {

    /**
     * 在处理程序处理之前调用.
     *
     * @param request  当前请求
     * @param response 当前响应
     * @param handler  当前处理程序
     * @return true 表示继续处理，false 表示拦截请求
     */
    @Override
    public boolean preHandle(
        HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler) {
        String xid = RootContext.getXID();
        String rpcXid = request.getHeader(RootContext.KEY_XID);
        if (log.isDebugEnabled()) {
            log.debug("xid in RootContext {} xid in RpcContext {}", xid, rpcXid);
        }

        if (StringUtils.isBlank(xid) && rpcXid != null) {
            RootContext.bind(rpcXid);
            if (log.isDebugEnabled()) {
                log.debug("bind {} to RootContext", rpcXid);
            }
        }

        return true;
    }

    /**
     * 在处理程序处理后调用.
     *
     * @param request  当前请求
     * @param response 当前响应
     * @param handler  当前处理程序
     * @param e        异常
     */
    @Override
    public void afterCompletion(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler,
        Exception e) {
        if (StringUtils.isNotBlank(RootContext.getXID())) {
            String rpcXid = request.getHeader(RootContext.KEY_XID);

            if (StringUtils.isEmpty(rpcXid)) {
                return;
            }

            String unbindXid = RootContext.unbind();
            if (log.isDebugEnabled()) {
                log.debug("unbind {} from RootContext", unbindXid);
            }
            if (!rpcXid.equalsIgnoreCase(unbindXid)) {
                log.warn("xid in change during RPC from {} to {}", rpcXid, unbindXid);
                if (unbindXid != null) {
                    RootContext.bind(unbindXid);
                    log.warn("bind {} back to RootContext", unbindXid);
                }
            }
        }
    }
}
