/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息类型枚举
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {
    /**
     * 站内信
     */
    SYSTEM_MESSAGE("1", "站内信"),
    /**
     * 邮箱
     */
    EMAIL_MESSAGE("2", "邮箱"),
    /**
     * 短信
     */
    SMS_MESSAGE("3", "短信");

    private final static Map<String, MessageTypeEnum> MESSAGE_TYPE_ENUM_MAP = new ConcurrentHashMap<>(MessageTypeEnum.values().length);

    static {
        for (MessageTypeEnum messageType : MessageTypeEnum.values()) {
            MESSAGE_TYPE_ENUM_MAP.put(messageType.code, messageType);
        }
    }

    private final String code;
    private final String desc;

    /**
     * 根据消息类型 code 获取 MessageTypeEnum
     *
     * @param code 消息类型code
     * @return MessageTypeEnum
     */
    public static MessageTypeEnum getByCode(String code) {
        return MESSAGE_TYPE_ENUM_MAP.get(code);
    }
}

