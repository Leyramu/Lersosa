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
 * 流程实例对象 act_hi_procinst
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@TableName("act_hi_procinst")
public class ActHiProcinst implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "ID_")
    private String id;

    /**
     *
     */
    @TableField(value = "REV_")
    private Long rev;

    /**
     *
     */
    @TableField(value = "PROC_INST_ID_")
    private String procInstId;

    /**
     *
     */
    @TableField(value = "BUSINESS_KEY_")
    private String businessKey;

    /**
     *
     */
    @TableField(value = "PROC_DEF_ID_")
    private String procDefId;

    /**
     *
     */
    @TableField(value = "START_TIME_")
    private Date startTime;

    /**
     *
     */
    @TableField(value = "END_TIME_")
    private Date endTime;

    /**
     *
     */
    @TableField(value = "DURATION_")
    private Long duration;

    /**
     *
     */
    @TableField(value = "START_USER_ID_")
    private String startUserId;

    /**
     *
     */
    @TableField(value = "START_ACT_ID_")
    private String startActId;

    /**
     *
     */
    @TableField(value = "END_ACT_ID_")
    private String endActId;

    /**
     *
     */
    @TableField(value = "SUPER_PROCESS_INSTANCE_ID_")
    private String superProcessInstanceId;

    /**
     *
     */
    @TableField(value = "DELETE_REASON_")
    private String deleteReason;

    /**
     *
     */
    @TableField(value = "TENANT_ID_")
    private String tenantId;

    /**
     *
     */
    @TableField(value = "NAME_")
    private String name;

    /**
     *
     */
    @TableField(value = "CALLBACK_ID_")
    private String callbackId;

    /**
     *
     */
    @TableField(value = "CALLBACK_TYPE_")
    private String callbackType;

    /**
     *
     */
    @TableField(value = "REFERENCE_ID_")
    private String referenceId;

    /**
     *
     */
    @TableField(value = "REFERENCE_TYPE_")
    private String referenceType;

    /**
     *
     */
    @TableField(value = "PROPAGATED_STAGE_INST_ID_")
    private String propagatedStageInstId;

    /**
     *
     */
    @TableField(value = "BUSINESS_STATUS_")
    private String businessStatus;


}