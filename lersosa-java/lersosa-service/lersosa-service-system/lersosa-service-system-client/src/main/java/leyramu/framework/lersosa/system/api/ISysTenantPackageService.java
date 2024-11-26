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

package leyramu.framework.lersosa.system.api;

import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.system.domain.bo.SysTenantPackageBo;
import leyramu.framework.lersosa.system.domain.vo.SysTenantPackageVo;

import java.util.Collection;
import java.util.List;

/**
 * 租户套餐Service接口.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface ISysTenantPackageService {

    /**
     * 查询租户套餐.
     */
    SysTenantPackageVo queryById(Long packageId);

    /**
     * 查询租户套餐列表.
     */
    TableDataInfo<SysTenantPackageVo> queryPageList(SysTenantPackageBo bo, PageQuery pageQuery);

    /**
     * 查询租户套餐已启用列表.
     */
    List<SysTenantPackageVo> selectList();

    /**
     * 查询租户套餐列表.
     */
    List<SysTenantPackageVo> queryList(SysTenantPackageBo bo);

    /**
     * 新增租户套餐.
     */
    Boolean insertByBo(SysTenantPackageBo bo);

    /**
     * 修改租户套餐.
     */
    Boolean updateByBo(SysTenantPackageBo bo);

    /**
     * 校验套餐名称是否唯一.
     */
    boolean checkPackageNameUnique(SysTenantPackageBo bo);

    /**
     * 修改套餐状态.
     */
    int updatePackageStatus(SysTenantPackageBo bo);

    /**
     * 校验并批量删除租户套餐信息.
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
