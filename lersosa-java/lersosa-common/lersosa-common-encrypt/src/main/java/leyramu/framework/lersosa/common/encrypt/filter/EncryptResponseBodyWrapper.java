/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
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

package leyramu.framework.lersosa.common.encrypt.filter;

import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import leyramu.framework.lersosa.common.encrypt.utils.EncryptUtils;
import lombok.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 加密响应参数包装类.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class EncryptResponseBodyWrapper extends HttpServletResponseWrapper {

    /**
     * 字节数组输出流.
     */
    private final ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Servlet 输出流.
     */
    private final ServletOutputStream servletOutputStream;

    /**
     * 打印流.
     */
    private final PrintWriter printWriter;

    /**
     * 构造方法.
     *
     * @param response response
     */
    public EncryptResponseBodyWrapper(HttpServletResponse response) {
        super(response);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.servletOutputStream = this.getOutputStream();
        this.printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));
    }

    /**
     * 获取打印流.
     *
     * @return 打印流
     */
    @Override
    public PrintWriter getWriter() {
        return printWriter;
    }

    /**
     * 刷新缓冲区.
     *
     * @throws IOException IO异常
     */
    @Override
    public void flushBuffer() throws IOException {
        if (servletOutputStream != null) {
            servletOutputStream.flush();
        }
        if (printWriter != null) {
            printWriter.flush();
        }
    }

    /**
     * 重置缓冲区.
     */
    @Override
    public void reset() {
        byteArrayOutputStream.reset();
    }

    /**
     * 获取响应数据.
     *
     * @return 响应数据
     * @throws IOException IO异常
     */
    @SuppressWarnings("unused")
    public byte[] getResponseData() throws IOException {
        flushBuffer();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 获取响应内容.
     *
     * @return 响应内容
     * @throws IOException IO异常
     */
    public String getContent() throws IOException {
        flushBuffer();
        return byteArrayOutputStream.toString();
    }

    /**
     * 获取加密内容.
     *
     * @param servletResponse response
     * @param publicKey       RSA公钥 (用于加密 AES 秘钥)
     * @param headerFlag      请求头标志
     * @return 加密内容
     * @throws IOException IO异常
     */
    public String getEncryptContent(HttpServletResponse servletResponse, String publicKey, String headerFlag) throws IOException {
        // 生成秘钥
        String aesPassword = RandomUtil.randomString(32);
        // 秘钥使用 Base64 编码
        String encryptAes = EncryptUtils.encryptByBase64(aesPassword);
        // Rsa 公钥加密 Base64 编码
        String encryptPassword = EncryptUtils.encryptByRsa(encryptAes, publicKey);

        // 设置响应头
        servletResponse.addHeader("Access-Control-Expose-Headers", headerFlag);
        servletResponse.setHeader(headerFlag, encryptPassword);
        servletResponse.setHeader("Access-Control-Allow-Origin", "*");
        servletResponse.setHeader("Access-Control-Allow-Methods", "*");
        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        // 获取原始内容
        String originalBody = this.getContent();
        // 对内容进行加密
        return EncryptUtils.encryptByAes(originalBody, aesPassword);
    }

    /**
     * 获取 Servlet 输出流.
     *
     * @return Servlet 输出流
     */
    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStream() {

            /**
             * 判断是否读取就绪.
             *
             * @return 是否读取就绪
             */
            @Override
            public boolean isReady() {
                return false;
            }

            /**
             * 设置读取监听器.
             *
             * @param writeListener 读取监听器
             */
            @Override
            public void setWriteListener(WriteListener writeListener) {
            }

            /**
             * 写入.
             *
             * @param b 字节
             */
            @Override
            public void write(int b) {
                byteArrayOutputStream.write(b);
            }

            /**
             * 写入.
             *
             * @param b 字节数组
             * @throws IOException IO异常
             */
            @Override
            public void write(byte @NonNull [] b) throws IOException {
                byteArrayOutputStream.write(b);
            }

            /**
             * 写入.
             *
             * @param b  字节数组
             * @param off 偏移量
             * @param len 长度
             */
            @Override
            public void write(byte @NonNull [] b, int off, int len) {
                byteArrayOutputStream.write(b, off, len);
            }
        };
    }
}
