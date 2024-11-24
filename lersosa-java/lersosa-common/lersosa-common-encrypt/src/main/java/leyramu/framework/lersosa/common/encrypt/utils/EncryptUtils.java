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

package leyramu.framework.lersosa.common.encrypt.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SM2;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 安全相关工具类.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class EncryptUtils {

    /**
     * 公钥.
     */
    public static final String PUBLIC_KEY = "publicKey";
    /**
     * 私钥.
     */
    public static final String PRIVATE_KEY = "privateKey";

    /**
     * Base64加密.
     *
     * @param data 待加密数据
     * @return 加密后字符串
     */
    public static String encryptByBase64(String data) {
        return Base64.encode(data, StandardCharsets.UTF_8);
    }

    /**
     * Base64解密.
     *
     * @param data 待解密数据
     * @return 解密后字符串
     */
    public static String decryptByBase64(String data) {
        return Base64.decodeStr(data, StandardCharsets.UTF_8);
    }

    /**
     * AES加密.
     *
     * @param data     待解密数据
     * @param password 秘钥字符串
     * @return 加密后字符串, 采用Base64编码
     */
    public static String encryptByAes(String data, String password) {
        if (StrUtil.isBlank(password)) {
            throw new IllegalArgumentException("AES需要传入秘钥信息");
        }
        // aes算法的秘钥要求是16位、24位、32位
        int[] array = {16, 24, 32};
        if (!ArrayUtil.contains(array, password.length())) {
            throw new IllegalArgumentException("AES秘钥长度要求为16位、24位、32位");
        }
        return SecureUtil.aes(password.getBytes(StandardCharsets.UTF_8)).encryptBase64(data, StandardCharsets.UTF_8);
    }

    /**
     * AES加密.
     *
     * @param data     待解密数据
     * @param password 秘钥字符串
     * @return 加密后字符串, 采用Hex编码
     */
    public static String encryptByAesHex(String data, String password) {
        if (StrUtil.isBlank(password)) {
            throw new IllegalArgumentException("AES需要传入秘钥信息");
        }
        // aes算法的秘钥要求是16位、24位、32位
        int[] array = {16, 24, 32};
        if (!ArrayUtil.contains(array, password.length())) {
            throw new IllegalArgumentException("AES秘钥长度要求为16位、24位、32位");
        }
        return SecureUtil.aes(password.getBytes(StandardCharsets.UTF_8)).encryptHex(data, StandardCharsets.UTF_8);
    }

    /**
     * AES解密.
     *
     * @param data     待解密数据
     * @param password 秘钥字符串
     * @return 解密后字符串
     */
    public static String decryptByAes(String data, String password) {
        if (StrUtil.isBlank(password)) {
            throw new IllegalArgumentException("AES需要传入秘钥信息");
        }
        // aes算法的秘钥要求是16位、24位、32位
        int[] array = {16, 24, 32};
        if (!ArrayUtil.contains(array, password.length())) {
            throw new IllegalArgumentException("AES秘钥长度要求为16位、24位、32位");
        }
        return SecureUtil.aes(password.getBytes(StandardCharsets.UTF_8)).decryptStr(data, StandardCharsets.UTF_8);
    }

    /**
     * sm4加密.
     *
     * @param data     待加密数据
     * @param password 秘钥字符串
     * @return 加密后字符串, 采用Base64编码
     */
    public static String encryptBySm4(String data, String password) {
        if (StrUtil.isBlank(password)) {
            throw new IllegalArgumentException("SM4需要传入秘钥信息");
        }
        // sm4算法的秘钥要求是16位长度
        int sm4PasswordLength = 16;
        if (sm4PasswordLength != password.length()) {
            throw new IllegalArgumentException("SM4秘钥长度要求为16位");
        }
        return SmUtil.sm4(password.getBytes(StandardCharsets.UTF_8)).encryptBase64(data, StandardCharsets.UTF_8);
    }

    /**
     * sm4加密.
     *
     * @param data     待加密数据
     * @param password 秘钥字符串
     * @return 加密后字符串, 采用Base64编码
     */
    public static String encryptBySm4Hex(String data, String password) {
        if (StrUtil.isBlank(password)) {
            throw new IllegalArgumentException("SM4需要传入秘钥信息");
        }
        // sm4算法的秘钥要求是16位长度
        int sm4PasswordLength = 16;
        if (sm4PasswordLength != password.length()) {
            throw new IllegalArgumentException("SM4秘钥长度要求为16位");
        }
        return SmUtil.sm4(password.getBytes(StandardCharsets.UTF_8)).encryptHex(data, StandardCharsets.UTF_8);
    }

    /**
     * sm4解密.
     *
     * @param data     待解密数据
     * @param password 秘钥字符串
     * @return 解密后字符串
     */
    public static String decryptBySm4(String data, String password) {
        if (StrUtil.isBlank(password)) {
            throw new IllegalArgumentException("SM4需要传入秘钥信息");
        }
        // sm4算法的秘钥要求是16位长度
        int sm4PasswordLength = 16;
        if (sm4PasswordLength != password.length()) {
            throw new IllegalArgumentException("SM4秘钥长度要求为16位");
        }
        return SmUtil.sm4(password.getBytes(StandardCharsets.UTF_8)).decryptStr(data, StandardCharsets.UTF_8);
    }

    /**
     * 产生sm2加解密需要的公钥和私钥.
     *
     * @return 公私钥Map
     */
    @SuppressWarnings("unused")
    public static Map<String, String> generateSm2Key() {
        Map<String, String> keyMap = new HashMap<>(2);
        SM2 sm2 = SmUtil.sm2();
        keyMap.put(PRIVATE_KEY, sm2.getPrivateKeyBase64());
        keyMap.put(PUBLIC_KEY, sm2.getPublicKeyBase64());
        return keyMap;
    }

    /**
     * sm2公钥加密.
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后字符串, 采用Base64编码
     */
    public static String encryptBySm2(String data, String publicKey) {
        if (StrUtil.isBlank(publicKey)) {
            throw new IllegalArgumentException("SM2需要传入公钥进行加密");
        }
        SM2 sm2 = SmUtil.sm2(null, publicKey);
        return sm2.encryptBase64(data, StandardCharsets.UTF_8, KeyType.PublicKey);
    }

    /**
     * sm2公钥加密.
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后字符串, 采用Hex编码
     */
    public static String encryptBySm2Hex(String data, String publicKey) {
        if (StrUtil.isBlank(publicKey)) {
            throw new IllegalArgumentException("SM2需要传入公钥进行加密");
        }
        SM2 sm2 = SmUtil.sm2(null, publicKey);
        return sm2.encryptHex(data, StandardCharsets.UTF_8, KeyType.PublicKey);
    }

    /**
     * sm2私钥解密.
     *
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return 解密后字符串
     */
    public static String decryptBySm2(String data, String privateKey) {
        if (StrUtil.isBlank(privateKey)) {
            throw new IllegalArgumentException("SM2需要传入私钥进行解密");
        }
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        return sm2.decryptStr(data, KeyType.PrivateKey, StandardCharsets.UTF_8);
    }

    /**
     * 产生RSA加解密需要的公钥和私钥.
     *
     * @return 公私钥Map
     */
    @SuppressWarnings("unused")
    public static Map<String, String> generateRsaKey() {
        Map<String, String> keyMap = new HashMap<>(2);
        RSA rsa = SecureUtil.rsa();
        keyMap.put(PRIVATE_KEY, rsa.getPrivateKeyBase64());
        keyMap.put(PUBLIC_KEY, rsa.getPublicKeyBase64());
        return keyMap;
    }

    /**
     * rsa公钥加密.
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后字符串, 采用Base64编码
     */
    public static String encryptByRsa(String data, String publicKey) {
        if (StrUtil.isBlank(publicKey)) {
            throw new IllegalArgumentException("RSA需要传入公钥进行加密");
        }
        RSA rsa = SecureUtil.rsa(null, publicKey);
        return rsa.encryptBase64(data, StandardCharsets.UTF_8, KeyType.PublicKey);
    }

    /**
     * rsa公钥加密.
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后字符串, 采用Hex编码
     */
    public static String encryptByRsaHex(String data, String publicKey) {
        if (StrUtil.isBlank(publicKey)) {
            throw new IllegalArgumentException("RSA需要传入公钥进行加密");
        }
        RSA rsa = SecureUtil.rsa(null, publicKey);
        return rsa.encryptHex(data, StandardCharsets.UTF_8, KeyType.PublicKey);
    }

    /**
     * rsa私钥解密.
     *
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return 解密后字符串
     */
    public static String decryptByRsa(String data, String privateKey) {
        if (StrUtil.isBlank(privateKey)) {
            throw new IllegalArgumentException("RSA需要传入私钥进行解密");
        }
        RSA rsa = SecureUtil.rsa(privateKey, null);
        return rsa.decryptStr(data, KeyType.PrivateKey, StandardCharsets.UTF_8);
    }

    /**
     * md5加密.
     *
     * @param data 待加密数据
     * @return 加密后字符串, 采用Hex编码
     */
    public static String encryptByMd5(String data) {
        return SecureUtil.md5(data);
    }

    /**
     * sha256加密.
     *
     * @param data 待加密数据
     * @return 加密后字符串, 采用Hex编码
     */
    public static String encryptBySha256(String data) {
        return SecureUtil.sha256(data);
    }

    /**
     * sm3加密.
     *
     * @param data 待加密数据
     * @return 加密后字符串, 采用Hex编码
     */
    public static String encryptBySm3(String data) {
        return SmUtil.sm3(data);
    }
}
