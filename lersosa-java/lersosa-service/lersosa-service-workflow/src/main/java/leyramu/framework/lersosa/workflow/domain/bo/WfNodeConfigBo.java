/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import leyramu.framework.lersosa.common.core.validate.EditGroup;
import leyramu.framework.lersosa.common.mybatis.core.domain.BaseEntity;
import leyramu.framework.lersosa.workflow.domain.WfNodeConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 节点配置业务对象 wf_node_config
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WfNodeConfig.class, reverseConvertGenerate = false)
public class WfNodeConfigBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 表单id
     */
    private Long formId;

    /**
     * 表单类型
     */
    private String formType;

    /**
     * 节点名称
     */
    @NotBlank(message = "节点名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String nodeName;

    /**
     * 节点id
     */
    @NotBlank(message = "节点id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String nodeId;

    /**
     * 流程定义id
     */
    @NotBlank(message = "流程定义id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String definitionId;

    /**
     * 是否为申请人节点 （0是 1否）
     */
    @NotBlank(message = "是否为申请人节点不能为空", groups = {AddGroup.class, EditGroup.class})
    private String applyUserTask;

}
