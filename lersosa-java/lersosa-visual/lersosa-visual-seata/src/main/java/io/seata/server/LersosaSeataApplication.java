/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */
package io.seata.server;

import io.seata.common.aot.NativeUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Seata 分布式事务服务
 *
 * @author spilledyear@outlook.com
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@SpringBootApplication(scanBasePackages = {"io.seata"})
public class LersosaSeataApplication {

    /**
     * 启动 Seata 分布式事务服务 模块
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) throws Throwable {
        try {
            SpringApplication.run(LersosaSeataApplication.class, args);
        } catch (Throwable t) {
            if ("org.springframework.boot.SpringApplication$AbandonedRunException".equals(t.getClass().getName())) {
                throw t;
            }
            if (NativeUtils.inNativeImage()) {
                t.printStackTrace();
                Thread.sleep(20000);
            }
            throw t;
        }
    }
}