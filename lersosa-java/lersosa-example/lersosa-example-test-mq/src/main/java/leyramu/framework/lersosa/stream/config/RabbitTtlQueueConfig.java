/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.stream.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


/**
 * RabbitTTL队列
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Configuration
public class RabbitTtlQueueConfig {

    // 延迟队列名称
    public static final String DELAY_QUEUE_NAME = "delay-queue";
    // 延迟交换机名称
    public static final String DELAY_EXCHANGE_NAME = "delay-exchange";
    // 延迟路由键名称
    public static final String DELAY_ROUTING_KEY = "delay.routing.key";

    // 死信交换机名称
    public static final String DEAD_LETTER_EXCHANGE = "dlx-exchange";
    // 死信队列名称
    public static final String DEAD_LETTER_QUEUE = "dlx-queue";
    // 死信路由键名称
    public static final String DEAD_LETTER_ROUTING_KEY = "dlx.routing.key";

    /**
     * 声明延迟队列
     */
    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(DELAY_QUEUE_NAME)
            .deadLetterExchange(DEAD_LETTER_EXCHANGE)
            .deadLetterRoutingKey(DEAD_LETTER_ROUTING_KEY)
            .build();
    }

    /**
     * 声明延迟交换机
     */
    @Bean
    public CustomExchange delayExchange() {
        return new CustomExchange(DELAY_EXCHANGE_NAME, "x-delayed-message",
            true, false, Map.of("x-delayed-type", "direct"));
    }

    /**
     * 将延迟队列绑定到延迟交换机
     */
    @Bean
    public Binding delayBinding(Queue delayQueue, CustomExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_ROUTING_KEY).noargs();
    }

    /**
     * 声明死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    /**
     * 声明死信交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    /**
     * 将死信队列绑定到死信交换机
     */
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DEAD_LETTER_ROUTING_KEY);
    }

}

