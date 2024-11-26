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

package leyramu.framework.lersosa.workflow.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 流程实例视图.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class ProcessInstanceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 流程实例id.
     */
    private String id;

    /**
     * 流程定义id.
     */
    private String processDefinitionId;

    /**
     * 流程定义名称.
     */
    private String processDefinitionName;

    /**
     * 流程定义key.
     */
    private String processDefinitionKey;

    /**
     * 流程定义版本.
     */
    private Integer processDefinitionVersion;

    /**
     * 部署id.
     */
    private String deploymentId;

    /**
     * 业务id.
     */
    private String businessKey;

    /**
     * 是否挂起.
     */
    private Boolean isSuspended;

    /**
     * 租户id.
     */
    private String tenantId;

    /**
     * 启动时间.
     */
    private Date startTime;

    /**
     * 结束时间.
     */
    private Date endTime;

    /**
     * 启动人id.
     */
    private String startUserId;

    /**
     * 流程状态.
     */
    private String businessStatus;

    /**
     * 流程状态.
     */
    private String businessStatusName;

    /**
     * 待办任务集合.
     */
    private List<TaskVo> taskVoList;

    /**
     * 节点配置.
     */
    private WfNodeConfigVo wfNodeConfigVo;
}
