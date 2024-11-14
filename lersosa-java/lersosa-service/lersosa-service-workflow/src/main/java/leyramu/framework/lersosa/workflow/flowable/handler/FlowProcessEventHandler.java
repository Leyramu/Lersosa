/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.flowable.handler;

import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.workflow.api.domain.event.ProcessEvent;
import leyramu.framework.lersosa.workflow.api.domain.event.ProcessTaskEvent;
import org.springframework.stereotype.Component;

/**
 * 流程监听服务.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Component
public class FlowProcessEventHandler {

    /**
     * 总体流程监听(例如: 提交 退回 撤销 终止 作废等).
     *
     * @param key         流程key
     * @param businessKey 业务id
     * @param status      状态
     * @param submit      当为true时为申请人节点办理
     */
    public void processHandler(String key, String businessKey, String status, boolean submit) {
        ProcessEvent processEvent = new ProcessEvent();
        processEvent.setKey(key);
        processEvent.setBusinessKey(businessKey);
        processEvent.setStatus(status);
        processEvent.setSubmit(submit);
        SpringUtils.context().publishEvent(processEvent);
    }

    /**
     * 执行办理任务监听.
     *
     * @param key               流程key
     * @param taskDefinitionKey 审批节点key
     * @param taskId            任务id
     * @param businessKey       业务id
     */
    public void processTaskHandler(String key, String taskDefinitionKey, String taskId, String businessKey) {
        ProcessTaskEvent processTaskEvent = new ProcessTaskEvent();
        processTaskEvent.setKey(key);
        processTaskEvent.setTaskDefinitionKey(taskDefinitionKey);
        processTaskEvent.setTaskId(taskId);
        processTaskEvent.setBusinessKey(businessKey);
        SpringUtils.context().publishEvent(processTaskEvent);
    }
}
