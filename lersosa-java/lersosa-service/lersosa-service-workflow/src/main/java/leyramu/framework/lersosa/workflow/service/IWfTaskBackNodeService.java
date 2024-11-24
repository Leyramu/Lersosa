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

package leyramu.framework.lersosa.workflow.service;


import leyramu.framework.lersosa.workflow.domain.WfTaskBackNode;
import org.flowable.task.api.Task;

import java.util.List;

/**
 * 节点驳回记录Service接口.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface IWfTaskBackNodeService {

    /**
     * 记录审批节点.
     *
     * @param task 任务
     */
    void recordExecuteNode(Task task);

    /**
     * 按流程实例id查询.
     *
     * @param processInstanceId 流程实例id
     * @return 结果
     */
    List<WfTaskBackNode> getListByInstanceId(String processInstanceId);

    /**
     * 按照流程实例id，节点id查询.
     *
     * @param processInstanceId 流程实例id
     * @param nodeId            节点id
     * @return 结果
     */
    WfTaskBackNode getListByInstanceIdAndNodeId(String processInstanceId, String nodeId);

    /**
     * 删除驳回后的节点.
     *
     * @param processInstanceId 流程实例id
     * @param targetActivityId  节点id
     */
    void deleteBackTaskNode(String processInstanceId, String targetActivityId);

    /**
     * 按流程实例id删除.
     *
     * @param processInstanceId 流程实例id
     * @return 结果
     */
    @SuppressWarnings("unused")
    boolean deleteByInstanceId(String processInstanceId);

    /**
     * 按流程实例id删除.
     *
     * @param processInstanceIds 流程实例id
     */
    void deleteByInstanceIds(List<String> processInstanceIds);
}
