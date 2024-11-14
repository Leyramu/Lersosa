/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.oss.properties;

import lombok.Data;

/**
 * OSS对象存储 配置属性.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
public class OssProperties {

    /**
     * 租户id.
     */
    private String tenantId;

    /**
     * 访问站点.
     */
    private String endpoint;

    /**
     * 自定义域名.
     */
    private String domain;

    /**
     * 前缀.
     */
    private String prefix;

    /**
     * ACCESS_KEY.
     */
    private String accessKey;

    /**
     * SECRET_KEY.
     */
    private String secretKey;

    /**
     * 存储空间名.
     */
    private String bucketName;

    /**
     * 存储区域.
     */
    private String region;

    /**
     * 是否https（Y=是,N=否）.
     */
    private String isHttps;

    /**
     * 桶权限类型(0private 1public 2custom).
     */
    private String accessPolicy;
}
