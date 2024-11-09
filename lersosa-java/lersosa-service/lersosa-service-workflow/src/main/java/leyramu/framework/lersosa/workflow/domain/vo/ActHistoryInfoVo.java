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
import org.flowable.engine.task.Attachment;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 流程审批记录视图
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class ActHistoryInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 任务id
     */
    private String id;
    /**
     * 节点id
     */
    private String taskDefinitionKey;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 版本
     */
    private Integer version;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 运行时长
     */
    private String runDuration;
    /**
     * 状态
     */
    private String status;
    /**
     * 状态
     */
    private String statusName;
    /**
     * 办理人id
     */
    private String assignee;

    /**
     * 办理人名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NICKNAME, mapper = "assignee")
    private String nickName;

    /**
     * 办理人id
     */
    private String owner;

    /**
     * 审批信息id
     */
    private String commentId;

    /**
     * 审批信息
     */
    private String comment;

    /**
     * 审批附件
     */
    private List<Attachment> attachmentList;
}
