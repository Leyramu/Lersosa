/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import leyramu.framework.lersosa.common.core.utils.MapstructUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.workflow.common.enums.FormTypeEnum;
import leyramu.framework.lersosa.workflow.domain.WfFormManage;
import leyramu.framework.lersosa.workflow.domain.bo.WfFormManageBo;
import leyramu.framework.lersosa.workflow.domain.vo.WfFormManageVo;
import leyramu.framework.lersosa.workflow.mapper.WfFormManageMapper;
import leyramu.framework.lersosa.workflow.service.IWfFormManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 表单管理Service业务层处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@Service
public class WfFormManageServiceImpl implements IWfFormManageService {

    private final WfFormManageMapper baseMapper;

    /**
     * 查询表单管理.
     */
    @Override
    public WfFormManageVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public List<WfFormManageVo> queryByIds(List<Long> ids) {
        return baseMapper.selectVoByIds(ids);
    }

    /**
     * 查询表单管理列表.
     */
    @Override
    public TableDataInfo<WfFormManageVo> queryPageList(WfFormManageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfFormManage> lqw = buildQueryWrapper(bo);
        Page<WfFormManageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<WfFormManageVo> selectList() {
        List<WfFormManageVo> wfFormManageVos = baseMapper.selectVoList(new LambdaQueryWrapper<WfFormManage>().orderByDesc(WfFormManage::getUpdateTime));
        for (WfFormManageVo wfFormManageVo : wfFormManageVos) {
            wfFormManageVo.setFormTypeName(FormTypeEnum.findByType(wfFormManageVo.getFormType()));
        }
        return wfFormManageVos;
    }

    /**
     * 查询表单管理列表.
     */
    @Override
    public List<WfFormManageVo> queryList(WfFormManageBo bo) {
        LambdaQueryWrapper<WfFormManage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WfFormManage> buildQueryWrapper(WfFormManageBo bo) {
        LambdaQueryWrapper<WfFormManage> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFormName()), WfFormManage::getFormName, bo.getFormName());
        lqw.eq(StringUtils.isNotBlank(bo.getFormType()), WfFormManage::getFormType, bo.getFormType());
        return lqw;
    }

    /**
     * 新增表单管理.
     */
    @Override
    public Boolean insertByBo(WfFormManageBo bo) {
        WfFormManage add = MapstructUtils.convert(bo, WfFormManage.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(Objects.requireNonNull(add).getId());
        }
        return flag;
    }

    /**
     * 修改表单管理.
     */
    @Override
    public Boolean updateByBo(WfFormManageBo bo) {
        WfFormManage update = MapstructUtils.convert(bo, WfFormManage.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除表单管理.
     */
    @Override
    public Boolean deleteByIds(Collection<Long> ids) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
