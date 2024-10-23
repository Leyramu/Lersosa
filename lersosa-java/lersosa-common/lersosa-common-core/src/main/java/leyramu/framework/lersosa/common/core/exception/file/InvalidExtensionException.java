/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.exception.file;

import lombok.Getter;

import java.io.Serial;
import java.util.Arrays;

/**
 * 文件上传 误异常类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@Getter
public class InvalidExtensionException extends FileUploadException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 允许的扩展名
     */
    private final String[] allowedExtension;

    /**
     * 实际的扩展名
     */
    private final String extension;

    /**
     * 文件名
     */
    private final String filename;

    /**
     * 构造方法
     *
     * @param allowedExtension 允许的扩展名
     * @param extension        实际的扩展名
     * @param filename         文件名
     */
    public InvalidExtensionException(String[] allowedExtension, String extension, String filename) {
        super("filename : [" + filename + "], extension : [" + extension + "], allowed extension : [" + Arrays.toString(allowedExtension) + "]");
        this.allowedExtension = allowedExtension;
        this.extension = extension;
        this.filename = filename;
    }

    /**
     * 图片异常
     */
    public static class InvalidImageExtensionException extends InvalidExtensionException {

        /**
         * 序列化版本号
         */
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 构造方法
         *
         * @param allowedExtension 允许的扩展名
         * @param extension        实际的扩展名
         * @param filename         文件名
         */
        public InvalidImageExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    /**
     * 音频异常
     */
    public static class InvalidFlashExtensionException extends InvalidExtensionException {

        /**
         * 序列化版本号
         */
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 构造方法
         *
         * @param allowedExtension 允许的扩展名
         * @param extension        实际的扩展名
         * @param filename         文件名
         */
        public InvalidFlashExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    /**
     * 视频异常
     */
    public static class InvalidMediaExtensionException extends InvalidExtensionException {

        /**
         * 序列化版本号
         */
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 构造方法
         *
         * @param allowedExtension 允许的扩展名
         * @param extension        实际的扩展名
         * @param filename         文件名
         */
        public InvalidMediaExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    /**
     * 视频异常
     */
    public static class InvalidVideoExtensionException extends InvalidExtensionException {

        /**
         * 序列化版本号
         */
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 构造方法
         *
         * @param allowedExtension 允许的扩展名
         * @param extension        实际的扩展名
         * @param filename         文件名
         */
        public InvalidVideoExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }
}
