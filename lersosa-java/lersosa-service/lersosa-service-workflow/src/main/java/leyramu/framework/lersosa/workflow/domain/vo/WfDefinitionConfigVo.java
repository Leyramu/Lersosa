/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import leyramu.framework.lersosa.workflow.domain.WfDefinitionConfig;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 流程定义配置视图对象 wf_definition_config.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WfDefinitionConfig.class)
public class WfDefinitionConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 表名.
     */
    @ExcelProperty(value = "表名")
    private String tableName;

    /**
     * 流程定义ID.
     */
    @ExcelProperty(value = "流程定义ID")
    private String definitionId;

    /**
     * 流程KEY.
     */
    @ExcelProperty(value = "流程KEY")
    private String processKey;


    /**
     * 流程版本.
     */
    @ExcelProperty(value = "流程版本")
    private Integer version;

    /**
     * 备注.
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 表单管理.
     */
    private WfFormManageVo wfFormManageVo;
}
