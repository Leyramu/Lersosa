/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import leyramu.framework.lersosa.common.core.constant.UserConstants;
import leyramu.framework.lersosa.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据表 sys_dict_data.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
public class SysDictData extends TenantEntity {

    /**
     * 字典编码.
     */
    @TableId(value = "dict_code")
    private Long dictCode;

    /**
     * 字典排序.
     */
    private Integer dictSort;

    /**
     * 字典标签.
     */
    private String dictLabel;

    /**
     * 字典键值.
     */
    private String dictValue;

    /**
     * 字典类型.
     */
    private String dictType;

    /**
     * 样式属性（其他样式扩展）.
     */
    private String cssClass;

    /**
     * 表格字典样式.
     */
    private String listClass;

    /**
     * 是否默认（Y是 N否）.
     */
    private String isDefault;

    /**
     * 备注.
     */
    private String remark;

    @SuppressWarnings("unused")
    public boolean getDefault() {
        return UserConstants.YES.equals(this.isDefault);
    }
}
