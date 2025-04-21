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

package leyramu.framework.lersosa.common.purge.handler;

import leyramu.framework.lersosa.common.purge.properties.NginxCacheProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

/**
 * Nginx 缓存服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NgxCacheHandler {

    /**
     * Nginx缓存配置.
     */
    private final NginxCacheProperties nginxCacheProperties;

    /**
     * 清除Nginx缓存.
     *
     * @param path 要清除的缓存路径
     */
    public void clearCache(String path) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 PURGE 请求
            HttpUriRequestBase purgeRequest = new HttpUriRequestBase("PURGE", URI.create(nginxCacheProperties.getPurgeAddr() + path));
            purgeRequest.addHeader("X-API-Key", nginxCacheProperties.getApiKey());

            // 执行请求
            httpClient.execute(purgeRequest, new AbstractHttpClientResponseHandler<Void>() {

                /**
                 * 处理响应实体.
                 * @param entity 响应实体
                 * @return void
                 * @throws IOException IO异常
                 */
                @Override
                public Void handleEntity(HttpEntity entity) throws IOException {
                    String responseBody;
                    try {
                        responseBody = EntityUtils.toString(entity);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    log.info("Nginx 缓存清除成功 | 响应内容: {}", responseBody.trim());
                    return null;
                }

                /**
                 * 处理响应.
                 * @param response 响应
                 * @return void
                 * @throws IOException IO异常
                 */
                @Override
                public Void handleResponse(ClassicHttpResponse response) throws IOException {
                    return super.handleResponse(response);
                }
            });
        } catch (Exception e) {
            log.error("Nginx 缓存清除异常: {}", e.getMessage());
        }
    }
}
