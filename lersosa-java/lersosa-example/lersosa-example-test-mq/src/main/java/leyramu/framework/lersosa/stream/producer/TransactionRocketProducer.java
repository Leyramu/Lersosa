/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.stream.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 交易消息生产者
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 **/
@Slf4j
@Component
public class TransactionRocketProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendTransactionMessage() {
        List<String> tags = Arrays.asList("TAG-1", "TAG-2", "TAG-3");
        for (int i = 0; i < 3; i++) {
            Message<String> message = MessageBuilder.withPayload("===>事务消息-" + i).build();
            // destination formats: `topicName:tags` message – message Message arg – ext arg
            TransactionSendResult res = rocketMQTemplate.sendMessageInTransaction("transaction-topic:" + tags.get(i), message, i + 1);
            if (res.getLocalTransactionState().equals(LocalTransactionState.COMMIT_MESSAGE) && res.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("【生产者】事物消息发送成功；成功结果：{}", res);
            } else {
                log.info("【生产者】事务发送失败：失败原因：{}", res);
            }
        }
    }

}
