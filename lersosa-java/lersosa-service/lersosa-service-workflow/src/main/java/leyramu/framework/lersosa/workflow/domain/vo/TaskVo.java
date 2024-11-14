/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.domain.vo;

import leyramu.framework.lersosa.common.translation.annotation.Translation;
import leyramu.framework.lersosa.common.translation.constant.TransConstant;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 任务视图.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class TaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务id.
     */
    private String id;

    /**
     * 任务名称.
     */
    private String name;

    /**
     * 描述.
     */
    private String description;

    /**
     * 优先级.
     */
    private Integer priority;

    /**
     * 负责此任务的人员的用户id.
     */
    private String owner;

    /**
     * 办理人id.
     */
    private Long assignee;

    /**
     * 办理人.
     */
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME, mapper = "assignee")
    private String assigneeName;


    /**
     * 流程实例id.
     */
    private String processInstanceId;

    /**
     * 执行id.
     */
    private String executionId;

    /**
     * 无用.
     */
    private String taskDefinitionId;

    /**
     * 流程定义id.
     */
    private String processDefinitionId;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 已办任务-创建时间.
     */
    private Date startTime;

    /**
     * 结束时间.
     */
    private Date endTime;

    /**
     * 节点id.
     */
    private String taskDefinitionKey;

    /**
     * 任务截止日期.
     */
    private Date dueDate;

    /**
     * 流程类别.
     */
    private String category;

    /**
     * 父级任务id.
     */
    private String parentTaskId;

    /**
     * 租户id.
     */
    private String tenantId;

    /**
     * 认领时间.
     */
    private Date claimTime;

    /**
     * 流程状态.
     */
    private String businessStatus;

    /**
     * 流程状态.
     */
    private String businessStatusName;

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
     * 参与者.
     */
    private ParticipantVo participantVo;

    /**
     * 是否会签.
     */
    private Boolean multiInstance;

    /**
     * 业务id.
     */
    private String businessKey;

    /**
     * 流程定义配置.
     */
    private WfDefinitionConfigVo wfDefinitionConfigVo;

    /**
     * 节点配置.
     */
    private WfNodeConfigVo wfNodeConfigVo;
}
