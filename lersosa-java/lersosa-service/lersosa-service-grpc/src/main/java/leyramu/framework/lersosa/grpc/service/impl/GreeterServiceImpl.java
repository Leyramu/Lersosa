/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.grpc.service.impl;

import io.grpc.stub.StreamObserver;
import leyramu.framework.lersosa.common.grpc.lib.GreeterGrpc;
import leyramu.framework.lersosa.common.grpc.lib.GreeterOuterClass;
import leyramu.framework.lersosa.grpc.service.GreeterService;
import leyramu.framework.lersosa.system.api.RemoteUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * RemoteGreeterServiceImpl 类.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/11
 */
@Slf4j
@GrpcService
@DubboService
@RequiredArgsConstructor
public class GreeterServiceImpl extends GreeterGrpc.GreeterImplBase implements GreeterService {

    /**
     * RemoteUserService 远程用户服务.
     */
    @DubboReference
    private final RemoteUserService remoteUserService;

    /**
     * sayHello 远程调用.
     *
     * @param request          请求
     * @param responseObserver 响应观察者
     */
    @Override
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        String message = "Hello World" + remoteUserService.hello(request.getName());
        final GreeterOuterClass.HelloReply.Builder replyBuilder = GreeterOuterClass.HelloReply.newBuilder();
        responseObserver.onNext(replyBuilder.setMessage(message).build());
        responseObserver.onCompleted();
        log.info("Returning：{}", message);
    }
}
