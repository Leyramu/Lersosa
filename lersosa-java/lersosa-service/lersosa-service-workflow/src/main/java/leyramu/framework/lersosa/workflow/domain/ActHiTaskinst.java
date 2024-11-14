/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 流程历史任务对象 act_hi_taskinst.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@TableName("act_hi_taskinst")
public class ActHiTaskinst implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "ID_")
    private String id;

    /**
     * 版本.
     */
    @TableField(value = "REV_")
    private Long rev;

    /**
     * 流程定义id.
     */
    @TableField(value = "PROC_DEF_ID_")
    private String procDefId;

    /**
     *
     */
    @TableField(value = "TASK_DEF_ID_")
    private String taskDefId;

    /**
     * 任务节点id.
     */
    @TableField(value = "TASK_DEF_KEY_")
    private String taskDefKey;

    /**
     * 流程实例id.
     */
    @TableField(value = "PROC_INST_ID_")
    private String procInstId;

    /**
     * 流程执行id.
     */
    @TableField(value = "EXECUTION_ID")
    private String executionId;

    /**
     *
     */
    @TableField(value = "SCOPE_ID_")
    private String scopeId;

    /**
     *
     */
    @TableField(value = "SUB_SCOPE_ID_")
    private String subScopeId;

    /**
     * 先用当前字段标识抄送类型.
     */
    @TableField(value = "SCOPE_TYPE_")
    private String scopeType;

    /**
     *
     */
    @TableField(value = "SCOPE_DEFINITION_ID_")
    private String scopeDefinitionId;

    /**
     *
     */
    @TableField(value = "PROPAGATED_STAGE_INST_ID_")
    private String propagatedStageInstId;

    /**
     * 任务名称.
     */
    @TableField(value = "NAME_")
    private String name;

    /**
     * 父级id.
     */
    @TableField(value = "PARENT_TASK_ID_")
    private String parentTaskId;

    /**
     * 描述.
     */
    @TableField(value = "DESCRIPTION_")
    private String description;

    /**
     * 办理人.
     */
    @TableField(value = "OWNER_")
    private String owner;

    /**
     * 办理人.
     */
    @TableField(value = "ASSIGNEE_")
    private String assignee;

    /**
     * 开始事件.
     */
    @TableField(value = "START_TIME_")
    private Date startTime;

    /**
     * 认领时间.
     */
    @TableField(value = "CLAIM_TIME_")
    private Date claimTime;

    /**
     * 结束时间.
     */
    @TableField(value = "END_TIME_")
    private Date endTime;

    /**
     * 持续时间.
     */
    @TableField(value = "DURATION_")
    private Long duration;

    /**
     * 删除原因.
     */
    @TableField(value = "DELETE_REASON_")
    private String deleteReason;

    /**
     * 优先级.
     */
    @TableField(value = "PRIORITY_")
    private Long priority;

    /**
     * 到期时间.
     */
    @TableField(value = "DUE_DATE_")
    private Date dueDate;

    /**
     *
     */
    @TableField(value = "FORM_KEY_")
    private String formKey;

    /**
     * 分类.
     */
    @TableField(value = "CATEGORY_")
    private String category;

    /**
     * 最后修改时间.
     */
    @TableField(value = "LAST_UPDATED_TIME_")
    private Date lastUpdatedTime;

    /**
     * 租户id.
     */
    @TableField(value = "TENANT_ID_")
    private String tenantId;
}
