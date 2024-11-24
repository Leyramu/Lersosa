/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package com.alibaba.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Indexed;

/**
 * Nacos 注册中心 启动类.
 *
 * @author nacos
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.2.0
 * @since 2024/7/31
 */
@Indexed
@EnableScheduling
@SpringBootApplication
@ServletComponentScan
public class LersosaNacosApplication {

    /**
     * 启动 Nacos 注册中心 模块.
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // true 单机模式 false 为集群模式 集群模式需搭配 cluster.conf 使用 使用方法请查看文档
        System.setProperty("nacos.standalone", "true");
        System.setProperty("server.tomcat.accesslog.enabled", "false");
        // 本地集群搭建使用 分别在所有 nacos 目录下创建 conf/cluster.conf 文件用于编写集群ip端口
        // 注意 如果本地启动多个 nacos 此目录不能相同 例如 nacos1 nacos2 nacos3 对应三个nacos服务
        // System.setProperty("nacos.home", "D:/nacos");
        SpringApplication.run(LersosaNacosApplication.class, args);
    }
}

