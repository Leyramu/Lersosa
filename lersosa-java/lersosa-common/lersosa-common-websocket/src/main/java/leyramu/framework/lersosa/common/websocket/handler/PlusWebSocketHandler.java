/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.websocket.handler;

import cn.hutool.core.util.ObjectUtil;
import leyramu.framework.lersosa.common.websocket.dto.WebSocketMessageDto;
import leyramu.framework.lersosa.common.websocket.holder.WebSocketSessionHolder;
import leyramu.framework.lersosa.common.websocket.utils.WebSocketUtils;
import leyramu.framework.lersosa.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.List;

import static leyramu.framework.lersosa.common.websocket.constant.WebSocketConstants.LOGIN_USER_KEY;

/**
 * WebSocketHandler 实现类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
public class PlusWebSocketHandler extends AbstractWebSocketHandler {

    /**
     * 连接成功后
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        LoginUser loginUser = (LoginUser) session.getAttributes().get(LOGIN_USER_KEY);
        if (ObjectUtil.isNull(loginUser)) {
            session.close(CloseStatus.BAD_DATA);
            log.info("[connect] invalid token received. sessionId: {}", session.getId());
            return;
        }
        WebSocketSessionHolder.addSession(loginUser.getUserId(), session);
        log.info("[connect] sessionId: {},userId:{},userType:{}", session.getId(), loginUser.getUserId(), loginUser.getUserType());
    }

    /**
     * 处理接收到的文本消息
     *
     * @param session WebSocket会话
     * @param message 接收到的文本消息
     * @throws Exception 处理消息过程中可能抛出的异常
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 从WebSocket会话中获取登录用户信息
        LoginUser loginUser = (LoginUser) session.getAttributes().get(LOGIN_USER_KEY);

        // 创建WebSocket消息DTO对象
        WebSocketMessageDto webSocketMessageDto = new WebSocketMessageDto();
        webSocketMessageDto.setSessionKeys(List.of(loginUser.getUserId()));
        webSocketMessageDto.setMessage(message.getPayload());
        WebSocketUtils.publishMessage(webSocketMessageDto);
    }

    /**
     * 处理接收到的二进制消息
     *
     * @param session WebSocket会话
     * @param message 接收到的二进制消息
     * @throws Exception 处理消息过程中可能抛出的异常
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
    }

    /**
     * 处理接收到的Pong消息（心跳监测）
     *
     * @param session WebSocket会话
     * @param message 接收到的Pong消息
     * @throws Exception 处理消息过程中可能抛出的异常
     */
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        WebSocketUtils.sendPongMessage(session);
    }

    /**
     * 处理WebSocket传输错误
     *
     * @param session   WebSocket会话
     * @param exception 发生的异常
     * @throws Exception 处理过程中可能抛出的异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("[transport error] sessionId: {} , exception:{}", session.getId(), exception.getMessage());
    }

    /**
     * 在WebSocket连接关闭后执行清理操作
     *
     * @param session WebSocket会话
     * @param status  关闭状态信息
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        LoginUser loginUser = (LoginUser) session.getAttributes().get(LOGIN_USER_KEY);
        if (ObjectUtil.isNull(loginUser)) {
            log.info("[disconnect] invalid token received. sessionId: {}", session.getId());
            return;
        }
        WebSocketSessionHolder.removeSession(loginUser.getUserId());
        log.info("[disconnect] sessionId: {},userId:{},userType:{}", session.getId(), loginUser.getUserId(), loginUser.getUserType());
    }

    /**
     * 指示处理程序是否支持接收部分消息
     *
     * @return 如果支持接收部分消息，则返回true；否则返回false
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
