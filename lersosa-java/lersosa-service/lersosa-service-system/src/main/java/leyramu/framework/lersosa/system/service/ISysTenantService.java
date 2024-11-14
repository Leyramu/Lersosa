/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.service;

import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.system.domain.bo.SysTenantBo;
import leyramu.framework.lersosa.system.domain.vo.SysTenantVo;

import java.util.Collection;
import java.util.List;

/**
 * 租户Service接口.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface ISysTenantService {

    /**
     * 查询租户.
     */
    SysTenantVo queryById(Long id);

    /**
     * 基于租户ID查询租户.
     */
    SysTenantVo queryByTenantId(String tenantId);

    /**
     * 查询租户列表.
     */
    TableDataInfo<SysTenantVo> queryPageList(SysTenantBo bo, PageQuery pageQuery);

    /**
     * 查询租户列表.
     */
    List<SysTenantVo> queryList(SysTenantBo bo);

    /**
     * 新增租户.
     */
    Boolean insertByBo(SysTenantBo bo);

    /**
     * 修改租户.
     */
    Boolean updateByBo(SysTenantBo bo);

    /**
     * 修改租户状态.
     */
    int updateTenantStatus(SysTenantBo bo);

    /**
     * 校验租户是否允许操作.
     */
    void checkTenantAllowed(String tenantId);

    /**
     * 校验并批量删除租户信息.
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 校验企业名称是否唯一.
     */
    boolean checkCompanyNameUnique(SysTenantBo bo);

    /**
     * 校验账号余额.
     */
    boolean checkAccountBalance(String tenantId);

    /**
     * 校验有效期.
     */
    @SuppressWarnings("unused")
    boolean checkExpireTime(String tenantId);

    /**
     * 同步租户套餐.
     */
    Boolean syncTenantPackage(String tenantId, Long packageId);

    /**
     * 同步租户字典.
     */
    void syncTenantDict();
}
