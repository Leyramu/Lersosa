/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.stream.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "demo-exchange";
    public static final String QUEUE_NAME = "demo-queue";
    public static final String ROUTING_KEY = "demo.routing.key";

    /**
     * 创建交换机
     * ExchangeBuilder有四种交换机模式
     * Direct Exchange：直连交换机，根据Routing Key(路由键)进行投递到不同队列。
     * Fanout Exchange：扇形交换机，采用广播模式，根据绑定的交换机，路由到与之对应的所有队列。
     * Topic Exchange：主题交换机，对路由键进行模式匹配后进行投递，符号#表示一个或多个词，*表示一个词。
     * Header Exchange：头交换机，不处理路由键。而是根据发送的消息内容中的headers属性进行匹配。
     * durable 交换器是否持久化（false 不持久化，true 持久化）
     **/
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * 创建队列
     * durable 队列是否持久化 队列调用此方法就是持久化 可查看方法的源码
     * deliveryMode 消息是否持久化（1 不持久化，2 持久化）
     **/
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    /**
     * 绑定交换机和队列
     * bing 方法参数可以是队列和交换机
     * to 方法参数必须是交换机
     * with 方法参数是路由Key 这里是以rabbit.开头
     * noargs 就是不要参数的意思
     * 这个方法的意思是把rabbit开头的消息 和 上面的队列 和 上面的交换机绑定
     **/
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

}
