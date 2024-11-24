/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.resource.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import leyramu.framework.lersosa.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象存储配置对象 sys_oss_config.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oss_config")
public class SysOssConfig extends BaseEntity {

    /**
     * 主键.
     */
    @TableId(value = "oss_config_id")
    private Long ossConfigId;

    /**
     * 配置key.
     */
    private String configKey;

    /**
     * accessKey.
     */
    private String accessKey;

    /**
     * 秘钥.
     */
    private String secretKey;

    /**
     * 桶名称.
     */
    private String bucketName;

    /**
     * 前缀.
     */
    private String prefix;

    /**
     * 访问站点.
     */
    private String endpoint;

    /**
     * 自定义域名.
     */
    private String domain;


    /**
     * 是否https（0否 1是）.
     */
    private String isHttps;

    /**
     * 域.
     */
    private String region;

    /**
     * 是否默认（0=是,1=否）.
     */
    private String status;

    /**
     * 扩展字段.
     */
    private String ext1;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 桶权限类型(0private 1public 2custom).
     */
    private String accessPolicy;
}
