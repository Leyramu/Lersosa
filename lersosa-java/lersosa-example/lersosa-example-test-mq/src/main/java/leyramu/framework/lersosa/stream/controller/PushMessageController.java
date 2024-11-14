/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.stream.controller;

import leyramu.framework.lersosa.stream.producer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推送消息.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@RestController
@RequestMapping
public class PushMessageController {

    @Autowired
    private NormalRabbitProducer normalRabbitProducer;
    @Autowired
    private DelayRabbitProducer delayRabbitProducer;
    @Autowired
    private NormalRocketProducer normalRocketProducer;
    @Autowired
    private TransactionRocketProducer transactionRocketProducer;
    @Autowired
    private KafkaNormalProducer normalKafkaProducer;

    /**
     * rabbitmq 普通消息.
     */
    @GetMapping("/rabbit/send")
    public void rabbitSend() {
        normalRabbitProducer.send("hello normal RabbitMsg");
    }

    /**
     * rabbitmq 延迟队列消息.
     */
    @GetMapping("/rabbit/sendDelay")
    public void rabbitSendDelay(long delay) {
        delayRabbitProducer.sendDelayMessage("Hello ttl RabbitMsg", delay);
    }

    /**
     * rocketmq 发送消息.
     */
    @GetMapping("/rocket/send")
    public void rocketSend() {
        normalRocketProducer.sendMessage();
    }

    /**
     * rocketmq 事务消息.
     */
    @GetMapping("/rocket/transaction")
    public void rocketTransaction() {
        transactionRocketProducer.sendTransactionMessage();
    }

    /**
     * kafka 发送消息.
     */
    @GetMapping("/kafka/send")
    public void kafkaSend() {
        normalKafkaProducer.sendKafkaMsg();
    }
}
