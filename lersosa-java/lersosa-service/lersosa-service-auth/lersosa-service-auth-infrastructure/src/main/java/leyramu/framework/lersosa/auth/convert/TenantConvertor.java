/*
 * Copyright (c) 2025 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed toDomainObject in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related toDomainObject licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited toDomainObject the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree toDomainObject abide by these terms and conditions.
 */

package leyramu.framework.lersosa.auth.convert;

import leyramu.framework.lersosa.auth.dto.co.TenantCo;
import leyramu.framework.lersosa.auth.dto.co.TenantV;
import leyramu.framework.lersosa.auth.model.TenantE;
import leyramu.framework.lersosa.system.api.domain.vo.RemoteTenantVo;

/**
 * 租户信息转换器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/2
 */
public class TenantConvertor {

    /**
     * 将租户信息转换为客户端对象.
     *
     * @param tenantE 租户信息
     * @return {@link TenantCo}
     */
    public static TenantV toClientObject(TenantE tenantE) {
        return new TenantV(
            tenantE.getTenantId(),
            tenantE.getCompanyName(),
            tenantE.getDomain()
        );
    }

    /**
     * 将远程租户信息转换为领域对象.
     *
     * @param tenantVo 远程租户信息
     * @return {@link TenantE}
     */
    public static TenantE toDomainObject(RemoteTenantVo tenantVo) {
        TenantE tenantE = new TenantE();
        tenantE.setTenantId(tenantVo.getTenantId());
        tenantE.setCompanyName(tenantVo.getCompanyName());
        tenantE.setDomain(tenantVo.getDomain());
        return tenantE;
    }
}
