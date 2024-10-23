/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.job.task;

import leyramu.framework.lersosa.common.core.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/23
 */
@Component("lersosaTask")
public class LersosaTask {

    /**
     * 执行多参方法
     *
     * @param s 字符串
     * @param b 布尔
     * @param l 长整型
     * @param d 浮点型
     * @param i 整型
     */
    public void lersosaMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    /**
     * 执行有参方法
     *
     * @param params 参数
     */
    public void lersosaParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    /**
     * 执行无参方法
     */
    public void lersosaNoParams() {
        System.out.println("执行无参方法");
    }
}
