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

package leyramu.framework.lersosa.workflow.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import leyramu.framework.lersosa.common.core.utils.StreamUtils;
import leyramu.framework.lersosa.workflow.api.IWfFormManageService;
import leyramu.framework.lersosa.workflow.api.IWfNodeConfigService;
import leyramu.framework.lersosa.workflow.domain.WfNodeConfig;
import leyramu.framework.lersosa.workflow.domain.vo.WfFormManageVo;
import leyramu.framework.lersosa.workflow.domain.vo.WfNodeConfigVo;
import leyramu.framework.lersosa.workflow.mapper.WfNodeConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 节点配置Service业务层处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@Service
public class WfNodeConfigServiceImpl implements IWfNodeConfigService {

    private final WfNodeConfigMapper baseMapper;
    private final IWfFormManageService wfFormManageService;

    /**
     * 查询节点配置.
     */
    @Override
    public WfNodeConfigVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 保存节点配置.
     */
    @Override
    public void saveOrUpdate(List<WfNodeConfig> list) {
        baseMapper.insertOrUpdateBatch(list);
    }

    /**
     * 批量删除节点配置.
     */
    @Override
    public Boolean deleteByIds(Collection<Long> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }


    @Override
    public void deleteByDefIds(Collection<String> ids) {
        baseMapper.delete(new LambdaQueryWrapper<WfNodeConfig>().in(WfNodeConfig::getDefinitionId, ids));
    }

    @Override
    public List<WfNodeConfigVo> selectByDefIds(Collection<String> ids) {
        List<WfNodeConfigVo> wfNodeConfigVos = baseMapper.selectVoList(new LambdaQueryWrapper<WfNodeConfig>().in(WfNodeConfig::getDefinitionId, ids));
        if (CollUtil.isNotEmpty(wfNodeConfigVos)) {
            List<Long> formIds = StreamUtils.toList(wfNodeConfigVos, WfNodeConfigVo::getFormId);
            List<WfFormManageVo> wfFormManageVos = wfFormManageService.queryByIds(formIds);
            for (WfNodeConfigVo wfNodeConfigVo : wfNodeConfigVos) {
                wfFormManageVos.stream().filter(e -> ObjectUtil.equals(e.getId(), wfNodeConfigVo.getFormId())).findFirst().ifPresent(wfNodeConfigVo::setWfFormManageVo);
            }
        }
        return wfNodeConfigVos;
    }
}
