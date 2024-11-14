/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import leyramu.framework.lersosa.common.encrypt.annotation.EncryptField;
import leyramu.framework.lersosa.common.encrypt.enumd.AlgorithmType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 测试加密.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("test_demo")
public class TestDemoEncrypt extends TestDemo {

    /**
     * key键.
     */
    // @EncryptField(algorithm=AlgorithmType.SM2, privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgZSlOvw8FBiH+aFJWLYZP/VRjg9wjfRarTkGBZd/T3N+gCgYIKoEcz1UBgi2hRANCAAR5DGuQwJqkxnbCsP+iPSDoHWIF4RwcR5EsSvT8QPxO1wRkR2IhCkzvRb32x2CUgJFdvoqVqfApFDPZzShqzBwX", publicKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEeQxrkMCapMZ2wrD/oj0g6B1iBeEcHEeRLEr0/ED8TtcEZEdiIQpM70W99sdglICRXb6KlanwKRQz2c0oaswcFw==")
    @EncryptField(algorithm = AlgorithmType.RSA, privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANBBEeueWlXlkkj2+WY5l+IWe42d8b5K28g+G/CFKC/yYAEHtqGlCsBOrb+YBkG9mPzmuYA/n9k0NFIc8E8yY5vZQaroyFBrTTWEzG9RY2f7Y3svVyybs6jpXSUs4xff8abo7wL1Y/wUaeatTViamxYnyTvdTmLm3d+JjRij68rxAgMBAAECgYAB0TnhXraSopwIVRfmboea1b0upl+BUdTJcmci412UjrKr5aE695ZLPkXbFXijVu7HJlyyv94NVUdaMACV7Ku/S2RuNB70M7YJm8rAjHFC3/i2ZeIM60h1Ziy4QKv0XM3pRATlDCDNhC1WUrtQCQSgU8kcp6eUUppruOqDzcY04QJBAPm9+sBP9CwDRgy3e5+V8aZtJkwDstb0lVVV/KY890cydVxiCwvX3fqVnxKMlb+x0YtH0sb9v+71xvK2lGobaRECQQDVePU6r/cCEfpc+nkWF6osAH1f8Mux3rYv2DoBGvaPzV2BGfsLed4neRfCwWNCKvGPCdW+L0xMJg8+RwaoBUPhAkAT5kViqXxFPYWJYd1h2+rDXhMdH3ZSlm6HvDBDdrwlWinr0Iwcx3iSjPV93uHXwm118aUj4fg3LDJMCKxOwBxhAkByrQXfvwOMYygBprRBf/j0plazoWFrbd6lGR0f1uI5IfNnFRPdeFw1DEINZ2Hw+6zEUF44SqRMC+4IYJNc02dBAkBCgy7RvfyV/A7N6kKXxTHauY0v6XwSSvpeKtRJkbIcRWOdIYvaHO9L7cklj3vIEdwjSUp9K4VTBYYlmAz1xh03", publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQQRHrnlpV5ZJI9vlmOZfiFnuNnfG+StvIPhvwhSgv8mABB7ahpQrATq2/mAZBvZj85rmAP5/ZNDRSHPBPMmOb2UGq6MhQa001hMxvUWNn+2N7L1csm7Oo6V0lLOMX3/Gm6O8C9WP8FGnmrU1YmpsWJ8k73U5i5t3fiY0Yo+vK8QIDAQAB")
    private String testKey;

    /**
     * 值.
     */
    // @EncryptField // 什么也不写走默认yml配置
    // @EncryptField(algorithm = AlgorithmType.SM4, password = "10rfylhtccpuyke5")
    @EncryptField(algorithm = AlgorithmType.AES, password = "10rfylhtccpuyke5")
    private String value;
}
