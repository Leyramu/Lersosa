/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.stream.consumer;

import leyramu.framework.lersosa.stream.config.RabbitConfig;
import leyramu.framework.lersosa.stream.config.RabbitTtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@Component
public class RabbitConsumer {

    /**
     * 普通消息.
     */
    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void listenQueue(Message message) {
        log.info("【消费者】Start consuming data：{}", new String(message.getBody()));
    }

    /**
     * 处理延迟队列消息.
     */
    @RabbitListener(queues = RabbitTtlQueueConfig.DELAY_QUEUE_NAME)
    public void receiveDelayMessage(String message) {
        log.info("【消费者】Received delayed message：{}", message);
    }

    /**
     * 处理死信队列消息.
     */
    @RabbitListener(queues = RabbitTtlQueueConfig.DEAD_LETTER_QUEUE)
    public void receiveDeadMessage(String message) {
        log.info("【消费者】Received dead message：{}", message);
    }
}
