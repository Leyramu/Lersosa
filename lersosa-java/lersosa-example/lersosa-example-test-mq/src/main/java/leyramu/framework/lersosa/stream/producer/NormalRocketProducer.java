/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.stream.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 普通消息生产者
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 **/
@Slf4j
@Component
public class NormalRocketProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage() {
        // 发送普通消息
        // rocketMQTemplate.convertAndSend("test-topic", "test");

        // 发送带tag的消息
        Message<String> message = MessageBuilder.withPayload("test").setHeader(RocketMQHeaders.TAGS, "test-tag").build();
        rocketMQTemplate.send("test-topic", message);

        // 延迟消息
        // RocketMQ预定义了一些延迟等级，每个等级对应不同的延迟时间范围。这些等级从1到18，分别对应1s、5s、10s、30s、1m、2m、3m、4m、5m、6m、7m、8m、9m、10m、20m、30m、1h、2h的延迟时间。
        org.apache.rocketmq.common.message.Message msg = new org.apache.rocketmq.common.message.Message();
        msg.setDelayTimeLevel(3);
        try {
            rocketMQTemplate.getProducer().send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
