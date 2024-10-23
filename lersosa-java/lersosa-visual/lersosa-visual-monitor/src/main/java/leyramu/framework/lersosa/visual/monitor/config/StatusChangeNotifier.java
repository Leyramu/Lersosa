/*
 * Copyright (c) 2022-2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.visual.monitor.config;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 通知配置
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/8/2
 */
@Slf4j
@Component
@NonNullApi
public class StatusChangeNotifier extends AbstractStatusChangeNotifier {

    /**
     * 构造方法
     *
     * @param instanceRepository 实例仓库
     */
    public StatusChangeNotifier(InstanceRepository instanceRepository) {
        super(instanceRepository);
    }

    /**
     * 通知消息.
     *
     * @param event    事件
     * @param instance 实例
     * @return 消息
     */
    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent eventStatus) {
                String instanceId = eventStatus.getInstance().getValue();
                String status = eventStatus.getStatusInfo().getStatus();
                switch (status) {
                    case "DOWN" -> log.info("业务实例 {} 未通过健康检查", instanceId);
                    case "OFFLINE" -> log.info("业务实例 {} 离线", instanceId);
                    case "UP" -> log.info("业务实例 {} 上线", instanceId);
                    case "UNKNOWN" -> log.error("业务实例 {} 未知异常", instanceId);
                    default -> log.error("业务实例 {} 缺省信息", instanceId);
                }
            }
        });
    }
}
