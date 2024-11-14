/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import leyramu.framework.lersosa.common.core.validate.EditGroup;
import leyramu.framework.lersosa.workflow.common.constant.FlowConstant;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 模型请求对象.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class ModelBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型id.
     */
    @NotBlank(message = "模型ID不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 模型名称.
     */
    @NotBlank(message = "模型名称不能为空", groups = {AddGroup.class})
    private String name;

    /**
     * 模型标识key.
     */
    @NotBlank(message = "模型标识key不能为空", groups = {AddGroup.class})
    @Pattern(regexp = FlowConstant.MODEL_KEY_PATTERN, message = "模型标识key只能字符或者下划线开头", groups = {AddGroup.class})
    private String key;

    /**
     * 模型分类.
     */
    @NotBlank(message = "模型分类不能为空", groups = {AddGroup.class})
    private String categoryCode;

    /**
     * 模型XML.
     */
    @NotBlank(message = "模型XML不能为空", groups = {AddGroup.class})
    private String xml;

    /**
     * 模型SVG图片.
     */
    @NotBlank(message = "模型SVG不能为空", groups = {EditGroup.class})
    private String svg;

    /**
     * 备注.
     */
    private String description;
}
