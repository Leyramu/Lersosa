/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.openai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * OpenAi 模块 应用层
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/6/23
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LersosaOpenAiApplication {

    /**
     * 启动 OpenAi 服务
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LersosaOpenAiApplication.class, args);
        log.info("""
                OpenAi 模块 服务启动成功
                       __   _         ________        __         \s
                      |  ] (_)       |_   __  |      |  ]        \s
                  .--.| |  __   .--./) | |_ \\_|  .--.| | __   _  \s
                / /'`\\' | [  | / /'`\\; |  _| _ / /'`\\' |[  | | | \s
                | \\__/  |  | | \\ \\._//_| |__/ || \\__/  | | \\_/ |,\s
                 '.__.;__][___].',__`|________| '.__.;__]'.__.'_/\s
                              ( ( __))     \s
                """);
    }
}
