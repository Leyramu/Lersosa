/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.common.constant;


/**
 * 工作流常量.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface FlowConstant {

    String MESSAGE_CURRENT_TASK_IS_NULL = "当前任务不存在或你不是任务办理人！";

    String MESSAGE_SUSPENDED = "当前任务已挂起不可审批！";

    /**
     * 连线.
     */
    String SEQUENCE_FLOW = "sequenceFlow";

    /**
     * 并行网关.
     */
    String PARALLEL_GATEWAY = "parallelGateway";

    /**
     * 排它网关.
     */
    String EXCLUSIVE_GATEWAY = "exclusiveGateway";

    /**
     * 包含网关.
     */
    String INCLUSIVE_GATEWAY = "inclusiveGateway";

    /**
     * 结束节点.
     */
    String END_EVENT = "endEvent";


    /**
     * 流程委派标识.
     */
    String PENDING = "PENDING";

    /**
     * 候选人标识.
     */
    String CANDIDATE = "candidate";

    /**
     * 会签任务总数.
     */
    String NUMBER_OF_INSTANCES = "nrOfInstances";

    /**
     * 正在执行的会签总数.
     */
    @SuppressWarnings("unused")
    String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";

    /**
     * 已完成的会签任务总数.
     */
    @SuppressWarnings("unused")
    String NUMBER_OF_COMPLETED_INSTANCES = "nrOfCompletedInstances";

    /**
     * 循环的索引值，可以使用elementIndexVariable属性修改loopCounter的变量名.
     */
    String LOOP_COUNTER = "loopCounter";

    String ZIP = "ZIP";

    /**
     * 业务与流程实例关联对象.
     */
    @SuppressWarnings("unused")
    String BUSINESS_INSTANCE_DTO = "businessInstanceDTO";

    /**
     * 流程定义配置.
     */
    @SuppressWarnings("unused")
    String WF_DEFINITION_CONFIG_VO = "wfDefinitionConfigVo";

    /**
     * 节点配置.
     */
    @SuppressWarnings("unused")
    String WF_NODE_CONFIG_VO = "wfNodeConfigVo";

    /**
     * 流程发起人.
     */
    String INITIATOR = "initiator";

    /**
     * 流程实例id.
     */
    String PROCESS_INSTANCE_ID = "processInstanceId";

    /**
     * 业务id.
     */
    String BUSINESS_KEY = "businessKey";

    /**
     * 流程定义id.
     */
    @SuppressWarnings("unused")
    String PROCESS_DEFINITION_ID = "processDefinitionId";

    /**
     * 开启跳过表达式变量.
     */
    String FLOWABLE_SKIP_EXPRESSION_ENABLED = "_FLOWABLE_SKIP_EXPRESSION_ENABLED";

    /**
     * 用户任务.
     */
    String USER_TASK = "userTask";

    /**
     * 会签.
     */
    String MULTI_INSTANCE = "multiInstance";

    /**
     * 是.
     */
    String TRUE = "0";

    /**
     * 否.
     */
    String FALSE = "1";
}
