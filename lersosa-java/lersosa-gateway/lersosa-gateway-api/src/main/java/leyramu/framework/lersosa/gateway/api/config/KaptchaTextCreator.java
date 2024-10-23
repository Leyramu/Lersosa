/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.gateway.api.config;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

/**
 * 验证码文本生成器
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/18
 */
public class KaptchaTextCreator extends DefaultTextCreator {

    /**
     * 数字0~9的顺序
     */
    private static final String[] C_NUMBERS = "0,1,2,3,4,5,6,7,8,9,10".split(",");

    /**
     * 获取运算表达式
     */
    @Override
    public String getText() {
        int result;
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        StringBuilder suChinese = new StringBuilder();
        int randomOperands = random.nextInt(3);
        if (randomOperands == 0) {
            result = x * y;
            suChinese.append(C_NUMBERS[x]);
            suChinese.append("*");
            suChinese.append(C_NUMBERS[y]);
        } else if (randomOperands == 1) {
            if ((x != 0) && y % x == 0) {
                result = y / x;
                suChinese.append(C_NUMBERS[y]);
                suChinese.append("/");
                suChinese.append(C_NUMBERS[x]);
            } else {
                result = x + y;
                suChinese.append(C_NUMBERS[x]);
                suChinese.append("+");
                suChinese.append(C_NUMBERS[y]);
            }
        } else {
            if (x >= y) {
                result = x - y;
                suChinese.append(C_NUMBERS[x]);
                suChinese.append("-");
                suChinese.append(C_NUMBERS[y]);
            } else {
                result = y - x;
                suChinese.append(C_NUMBERS[y]);
                suChinese.append("-");
                suChinese.append(C_NUMBERS[x]);
            }
        }
        suChinese.append("=?@").append(result);
        return suChinese.toString();
    }
}
