/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.resource.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("all")
public class RemoteMessageServiceStub implements RemoteMessageService {

    private final RemoteMessageService remoteMessageService;

    /**
     * 发送消息.
     *
     * @param sessionKey session主键 一般为用户id
     * @param message    消息文本
     */
    @Override
    public void publishMessage(Long sessionKey, String message) {
        try {
            remoteMessageService.publishMessage(sessionKey, message);
        } catch (Exception e) {
            log.warn("推送功能未开启或服务未找到");
        }
    }

    /**
     * 发布订阅的消息(群发).
     *
     * @param message 消息内容
     */
    @Override
    public void publishAll(String message) {
        try {
            remoteMessageService.publishAll(message);
        } catch (Exception e) {
            log.warn("推送功能未开启或服务未找到");
        }
    }
}
