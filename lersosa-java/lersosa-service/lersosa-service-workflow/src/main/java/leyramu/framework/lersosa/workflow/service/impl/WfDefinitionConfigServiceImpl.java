/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import leyramu.framework.lersosa.common.core.utils.MapstructUtils;
import leyramu.framework.lersosa.workflow.domain.WfDefinitionConfig;
import leyramu.framework.lersosa.workflow.domain.bo.WfDefinitionConfigBo;
import leyramu.framework.lersosa.workflow.domain.vo.WfDefinitionConfigVo;
import leyramu.framework.lersosa.workflow.mapper.WfDefinitionConfigMapper;
import leyramu.framework.lersosa.workflow.service.IWfDefinitionConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 流程定义配置Service业务层处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@Service
public class WfDefinitionConfigServiceImpl implements IWfDefinitionConfigService {

    private final WfDefinitionConfigMapper baseMapper;

    /**
     * 查询流程定义配置.
     */
    @Override
    public WfDefinitionConfigVo getByDefId(String definitionId) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<WfDefinitionConfig>().eq(WfDefinitionConfig::getDefinitionId, definitionId));
    }

    /**
     * 查询流程定义配置.
     *
     * @param tableName 表名
     * @return 结果
     */
    @Override
    public WfDefinitionConfigVo getByTableNameLastVersion(String tableName) {
        List<WfDefinitionConfigVo> wfDefinitionConfigVos = baseMapper.selectVoList(
            new LambdaQueryWrapper<WfDefinitionConfig>().eq(WfDefinitionConfig::getTableName, tableName).orderByDesc(WfDefinitionConfig::getVersion));
        if (CollUtil.isNotEmpty(wfDefinitionConfigVos)) {
            return wfDefinitionConfigVos.getFirst();
        }
        return null;
    }

    /**
     * 查询流程定义配置.
     *
     * @param definitionId 流程定义id
     * @param tableName    表名
     * @return 结果
     */
    @Override
    public WfDefinitionConfigVo getByDefIdAndTableName(String definitionId, String tableName) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<WfDefinitionConfig>()
            .eq(WfDefinitionConfig::getDefinitionId, definitionId)
            .eq(WfDefinitionConfig::getTableName, tableName));
    }

    /**
     * 查询流程定义配置排除当前查询的流程定义.
     *
     * @param tableName    表名
     * @param definitionId 流程定义id
     */
    @Override
    public List<WfDefinitionConfigVo> getByTableNameNotDefId(String tableName, String definitionId) {
        return baseMapper.selectVoList(new LambdaQueryWrapper<WfDefinitionConfig>()
            .eq(WfDefinitionConfig::getTableName, tableName)
            .ne(WfDefinitionConfig::getDefinitionId, definitionId));
    }

    /**
     * 查询流程定义配置列表.
     */
    @Override
    public List<WfDefinitionConfigVo> queryList(List<String> definitionIds) {
        return baseMapper.selectVoList(new LambdaQueryWrapper<WfDefinitionConfig>().in(WfDefinitionConfig::getDefinitionId, definitionIds));
    }

    /**
     * 新增流程定义配置.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(WfDefinitionConfigBo bo) {
        WfDefinitionConfig add = MapstructUtils.convert(bo, WfDefinitionConfig.class);
        baseMapper.delete(new LambdaQueryWrapper<WfDefinitionConfig>().eq(WfDefinitionConfig::getTableName, bo.getTableName()));
        Objects.requireNonNull(add).setTableName(add.getTableName().toLowerCase());
        boolean flag = baseMapper.insertOrUpdate(add);
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 批量删除流程定义配置.
     */
    @Override
    public Boolean deleteByIds(Collection<Long> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public void deleteByDefIds(Collection<String> ids) {
        baseMapper.delete(new LambdaQueryWrapper<WfDefinitionConfig>().in(WfDefinitionConfig::getDefinitionId, ids));
    }
}
