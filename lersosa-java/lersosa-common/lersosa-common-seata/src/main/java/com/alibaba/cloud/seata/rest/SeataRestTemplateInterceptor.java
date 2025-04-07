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

package com.alibaba.cloud.seata.rest;

import lombok.NonNull;
import org.apache.seata.core.context.RootContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * seata REST 模板拦截器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
public class SeataRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    /**
     * 拦截给定的请求，并在发送之前对其进行修改.
     *
     * @param httpRequest                用于传出请求的请求
     * @param bytes                      用于传出请求的请求正文
     * @param clientHttpRequestExecution 请求执行
     * @return the response
     * @throws IOException IO 异常
     */
    @Override
    @NonNull
    public ClientHttpResponse intercept(
        @NonNull HttpRequest httpRequest,
        byte @NonNull [] bytes,
        @NonNull ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(httpRequest);

        String xid = RootContext.getXID();

        if (StringUtils.hasLength(xid)) {
            requestWrapper.getHeaders().add(RootContext.KEY_XID, xid);
        }
        return clientHttpRequestExecution.execute(requestWrapper, bytes);
    }
}
