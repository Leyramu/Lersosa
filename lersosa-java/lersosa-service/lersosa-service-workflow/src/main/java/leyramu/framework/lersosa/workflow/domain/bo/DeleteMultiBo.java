/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 减签参数请求
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class DeleteMultiBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @NotBlank(message = "任务ID不能为空", groups = AddGroup.class)
    private String taskId;

    /**
     * 减签人员
     */
    @NotEmpty(message = "减签人员不能为空", groups = AddGroup.class)
    private List<String> taskIds;

    /**
     * 执行id
     */
    @NotEmpty(message = "执行id不能为空", groups = AddGroup.class)
    private List<String> executionIds;

    /**
     * 人员id
     */
    @NotEmpty(message = "减签人员id不能为空", groups = AddGroup.class)
    private List<Long> assigneeIds;

    /**
     * 人员名称
     */
    @NotEmpty(message = "减签人员不能为空", groups = AddGroup.class)
    private List<String> assigneeNames;
}
