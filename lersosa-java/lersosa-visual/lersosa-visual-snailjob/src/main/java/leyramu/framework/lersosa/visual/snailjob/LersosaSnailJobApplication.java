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

package leyramu.framework.lersosa.visual.snailjob;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import leyramu.framework.lersosa.common.ssl.annotation.EnableTlsConfig;
import leyramu.framework.lersosa.common.ssl.core.CustomSpringApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Indexed;

/**
 * SnailJob 任务调度模块.
 *
 * @author opensnail
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@Indexed
@EnableTlsConfig(
    certPath = "nacos-client-cert.pem",
    privateKey = "nacos-client-key.pem",
    privateKeyPassword = "Zcx@223852//",
    trustCert = "nacos-ca-cert.pem",
    clientCertPath = "nacos.crt"
)
@SpringBootApplication
@EnableEncryptableProperties
public class LersosaSnailJobApplication {

    /**
     * 启动 SnailJob 模块.
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        CustomSpringApplication.run(com.aizuda.snailjob.server.SnailJobServerApplication.class, args);
        log.info("""
            SnailJob 模块 服务启动成功
             ___       _______   ________  ________  ________  ________  ________    \s
            |\\  \\     |\\  ___ \\ |\\   __  \\|\\   ____\\|\\   __  \\|\\   ____\\|\\   __  \\   \s
            \\ \\  \\    \\ \\   __/|\\ \\  \\|\\  \\ \\  \\___|\\ \\  \\|\\  \\ \\  \\___|\\ \\  \\|\\  \\  \s
             \\ \\  \\    \\ \\  \\_|/_\\ \\   _  _\\ \\_____  \\ \\  \\\\\\  \\ \\_____  \\ \\   __  \\ \s
              \\ \\  \\____\\ \\  \\_|\\ \\ \\  \\\\  \\\\|____|\\  \\ \\  \\\\\\  \\|____|\\  \\ \\  \\ \\  \\\s
               \\ \\_______\\ \\_______\\ \\__\\\\ _\\ ____\\_\\  \\ \\_______\\____\\_\\  \\ \\__\\ \\__\\
                \\|_______|\\|_______|\\|__|\\|__|\\_________\\|_______|\\_________\\|__|\\|__|
                                             \\|_________|        \\|_________|        \s
            """);
    }
}
