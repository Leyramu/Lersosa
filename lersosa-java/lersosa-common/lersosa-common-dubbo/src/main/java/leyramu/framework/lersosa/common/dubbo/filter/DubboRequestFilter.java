/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.dubbo.filter;

import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.dubbo.enumd.RequestLogEnum;
import leyramu.framework.lersosa.common.dubbo.properties.DubboCustomProperties;
import leyramu.framework.lersosa.common.json.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * Dubbo 日志过滤器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = Integer.MAX_VALUE)
public class DubboRequestFilter implements Filter {

    /**
     * Dubbo Filter 接口实现方法，处理服务调用逻辑并记录日志.
     *
     * @param invoker    Dubbo 服务调用者实例
     * @param invocation 调用的具体方法信息
     * @return 调用结果
     * @throws RpcException 如果调用过程中发生异常
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        DubboCustomProperties properties = SpringUtils.getBean(DubboCustomProperties.class);
        // 如果未开启请求日志记录，则直接执行服务调用并返回结果
        if (!properties.getRequestLog()) {
            return invoker.invoke(invocation);
        }

        // 判断是 Provider 还是 Consumer
        String client = CommonConstants.PROVIDER;
        if (RpcContext.getServiceContext().isConsumerSide()) {
            client = CommonConstants.CONSUMER;
        }

        // 构建基础日志信息
        String baselog = "Client[" + client + "],InterfaceName=[" + invocation.getInvoker().getInterface().getSimpleName() + "],MethodName=[" + invocation.getMethodName() + "]";
        // 根据日志级别输出不同详细程度的日志信息
        if (properties.getLogLevel() == RequestLogEnum.INFO) {
            log.info("DUBBO - 服务调用: {}", baselog);
        } else {
            log.info("DUBBO - 服务调用: {},Parameter={}", baselog, invocation.getArguments());
        }

        // 记录调用开始时间
        long startTime = System.currentTimeMillis();
        // 执行接口调用逻辑
        Result result = invoker.invoke(invocation);
        // 计算调用耗时
        long elapsed = System.currentTimeMillis() - startTime;
        // 如果发生异常且调用的不是泛化服务，则记录异常日志
        if (result.hasException() && !invoker.getInterface().equals(GenericService.class)) {
            log.error("DUBBO - 服务异常: {},Exception=", baselog, result.getException());
        } else {
            // 根据日志级别输出服务响应信息
            if (properties.getLogLevel() == RequestLogEnum.INFO) {
                log.info("DUBBO - 服务响应: {},SpendTime=[{}ms]", baselog, elapsed);
            } else if (properties.getLogLevel() == RequestLogEnum.FULL) {
                log.info("DUBBO - 服务响应: {},SpendTime=[{}ms],Response={}", baselog, elapsed, JsonUtils.toJsonString(new Object[]{result.getValue()}));
            }
        }
        return result;
    }
}
