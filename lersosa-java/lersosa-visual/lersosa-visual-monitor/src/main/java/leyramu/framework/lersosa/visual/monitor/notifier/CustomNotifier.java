/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.visual.monitor.notifier;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static de.codecentric.boot.admin.server.domain.values.StatusInfo.*;

/**
 * 自定义事件通知处理
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@Component
public class CustomNotifier extends AbstractEventNotifier {

    protected CustomNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("all")
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            // 实例状态改变事件
            if (event instanceof InstanceStatusChangedEvent) {
                // 获取实例注册名称
                String registName = instance.getRegistration().getName();
                // 获取实例ID
                String instanceId = event.getInstance().getValue();
                // 获取实例状态
                String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                // 获取服务URL
                String serviceUrl = instance.getRegistration().getServiceUrl();
                String statusName = switch (status) {
                    case STATUS_UP -> "服务上线"; // 实例成功启动并可以正常处理请求
                    case STATUS_OFFLINE -> "服务离线"; //实例被手动或自动地从服务中移除
                    case STATUS_RESTRICTED -> "服务受限"; //表示实例在某些方面受限，可能无法完全提供所有服务
                    case STATUS_OUT_OF_SERVICE -> "停止服务状态"; //表示实例已被标记为停止提供服务，可能是计划内维护或测试
                    case STATUS_DOWN -> "服务下线"; //实例因崩溃、错误或其他原因停止运行
                    case STATUS_UNKNOWN -> "服务未知异常"; //监控系统无法确定实例的当前状态
                    default -> "未知状态"; //没有匹配的状态
                };
                log.info("Instance Status Change: 状态名称【{}】, 注册名称【{}】, 实例ID【{}】, 状态【{}】, 服务URL【{}】",
                    statusName, registName, instanceId, status, serviceUrl);
            }

        });
    }

}
