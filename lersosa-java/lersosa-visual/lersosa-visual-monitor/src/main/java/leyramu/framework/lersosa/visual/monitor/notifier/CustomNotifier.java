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
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static de.codecentric.boot.admin.server.domain.values.StatusInfo.*;

/**
 * 自定义事件通知处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@Component
public class CustomNotifier extends AbstractEventNotifier {

    /**
     * 构造函数.
     *
     * @param repository 实例存储库
     */
    protected CustomNotifier(InstanceRepository repository) {
        super(repository);
    }

    /**
     * 通知处理.
     *
     * @param event    事件
     * @param instance 实例
     */
    @Override
    @NonNull
    protected Mono<Void> doNotify(@NonNull InstanceEvent event, @NonNull Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                String registName = instance.getRegistration().getName();
                String instanceId = event.getInstance().getValue();
                String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                String serviceUrl = instance.getRegistration().getServiceUrl();
                String statusName = switch (status) {
                    case STATUS_UP -> "服务上线";
                    case STATUS_OFFLINE -> "服务离线";
                    case STATUS_RESTRICTED -> "服务受限";
                    case STATUS_OUT_OF_SERVICE -> "停止服务状态";
                    case STATUS_DOWN -> "服务下线";
                    case STATUS_UNKNOWN -> "服务未知异常";
                    default -> "未知状态";
                };
                log.info("Instance Status Change: 状态名称【{}】, 注册名称【{}】, 实例ID【{}】, 状态【{}】, 服务URL【{}】",
                    statusName, registName, instanceId, status, serviceUrl);
            }
        });
    }
}
