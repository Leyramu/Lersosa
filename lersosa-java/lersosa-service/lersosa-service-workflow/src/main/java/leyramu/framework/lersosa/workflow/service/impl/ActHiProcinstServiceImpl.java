/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import leyramu.framework.lersosa.common.tenant.helper.TenantHelper;
import leyramu.framework.lersosa.workflow.domain.ActHiProcinst;
import leyramu.framework.lersosa.workflow.mapper.ActHiProcinstMapper;
import leyramu.framework.lersosa.workflow.service.IActHiProcinstService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 流程实例Service业务层处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@Service
public class ActHiProcinstServiceImpl implements IActHiProcinstService {

    private final ActHiProcinstMapper baseMapper;

    /**
     * 按照业务id查询.
     *
     * @param businessKeys 业务id
     */
    @Override
    public List<ActHiProcinst> selectByBusinessKeyIn(List<String> businessKeys) {
        return baseMapper.selectList(new LambdaQueryWrapper<ActHiProcinst>()
            .in(ActHiProcinst::getBusinessKey, businessKeys)
            .eq(TenantHelper.isEnable(), ActHiProcinst::getTenantId, TenantHelper.getTenantId()));
    }

    /**
     * 按照业务id查询.
     *
     * @param businessKey 业务id
     */
    @Override
    public ActHiProcinst selectByBusinessKey(String businessKey) {
        return baseMapper.selectOne(new LambdaQueryWrapper<ActHiProcinst>()
            .eq(ActHiProcinst::getBusinessKey, businessKey)
            .eq(TenantHelper.isEnable(), ActHiProcinst::getTenantId, TenantHelper.getTenantId()));
    }
}
