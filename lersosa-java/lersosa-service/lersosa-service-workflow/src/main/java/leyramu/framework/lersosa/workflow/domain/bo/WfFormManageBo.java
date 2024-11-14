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
import leyramu.framework.lersosa.workflow.domain.WfFormManage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表单管理业务对象 wf_form_manage.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WfFormManage.class, reverseConvertGenerate = false)
public class WfFormManageBo extends BaseEntity {

    /**
     * 主键.
     */
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 表单名称.
     */
    @NotBlank(message = "表单名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String formName;

    /**
     * 表单类型.
     */
    @NotBlank(message = "表单类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String formType;

    /**
     * 路由地址/表单ID.
     */
    @NotBlank(message = "路由地址/表单ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String router;

    /**
     * 备注.
     */
    private String remark;
}
