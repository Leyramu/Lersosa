/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.api.domain;

import java.util.List;
import java.util.Map;

/**
 * 通用 工作流服务
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface RemoteWorkflowService {

    /**
     * 运行中的实例 删除程实例，删除历史记录，删除业务与流程关联信息
     *
     * @param businessKeys 业务id
     * @return 结果
     */
    boolean deleteRunAndHisInstance(List<String> businessKeys);

    /**
     * 获取当前流程状态
     *
     * @param taskId 任务id
     */
    String getBusinessStatusByTaskId(String taskId);

    /**
     * 获取当前流程状态
     *
     * @param businessKey 业务id
     */
    String getBusinessStatus(String businessKey);

    /**
     * 设置流程变量(全局变量)
     *
     * @param taskId       任务id
     * @param variableName 变量名称
     * @param value        变量值
     */
    void setVariable(String taskId, String variableName, Object value);

    /**
     * 设置流程变量(全局变量)
     *
     * @param taskId    任务id
     * @param variables 流程变量
     */
    void setVariables(String taskId, Map<String, Object> variables);

    /**
     * 设置流程变量(本地变量,非全局变量)
     *
     * @param taskId       任务id
     * @param variableName 变量名称
     * @param value        变量值
     */
    void setVariableLocal(String taskId, String variableName, Object value);

    /**
     * 设置流程变量(本地变量,非全局变量)
     *
     * @param taskId    任务id
     * @param variables 流程变量
     */
    void setVariablesLocal(String taskId, Map<String, Object> variables);

    /**
     * 按照业务id查询流程实例id
     *
     * @param businessKey 业务id
     * @return 结果
     */
    String getInstanceIdByBusinessKey(String businessKey);

}
