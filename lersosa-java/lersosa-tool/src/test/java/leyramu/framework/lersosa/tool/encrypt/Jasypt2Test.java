/*
 * Copyright (c) 2022-2024 Leyramu Group. All rights reserved.
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

package leyramu.framework.lersosa.tool.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * Jasypt 加解密工具类（只针对 spring-boot 2.x. x）.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/25
 */
@Slf4j
class Jasypt2Test {

    @Test
    void testJasypt2() {
        String factor = "Zcx@223852//";
        String plainText = "nacos";
        String encryptWithMD5AndDESStr = encryptWithMD5AndDES(plainText, factor);
        String decryptWithMD5AndDESStr = decryptWithMD5AndDES(encryptWithMD5AndDESStr, factor);
        log.info("采用PBEWithMD5AndDES加密前原文密文：{}", encryptWithMD5AndDESStr);
        log.info("采用PBEWithMD5AndDES解密后密文原文：{}", decryptWithMD5AndDESStr);
        Assertions.assertArrayEquals(decryptWithMD5AndDESStr.getBytes(StandardCharsets.UTF_8),
            plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Jasyp2.x 加密（PBEWithMD5AndDES）.
     *
     * @param plainText 待加密的原文
     * @param factor    加密秘钥
     * @return java.lang.String
     */
    private String encryptWithMD5AndDES(String plainText, String factor) {
        // 3. 加密
        return encryptor(factor).encrypt(plainText);
    }

    /**
     * Jaspy2.x 解密（PBEWithMD5AndDES）.
     *
     * @param encryptedText 待解密密文
     * @param factor        解密秘钥
     * @return java.lang.String
     */
    private String decryptWithMD5AndDES(String encryptedText, String factor) {
        // 3. 解密
        return encryptor(factor).decrypt(encryptedText);
    }

    private StandardPBEStringEncryptor encryptor(String factor) {
        // 1. 创建加解密工具实例
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        // 2. 加解密配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword(factor);
        encryptor.setConfig(config);
        return encryptor;
    }

}
