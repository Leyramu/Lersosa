/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import leyramu.framework.lersosa.common.core.utils.MapstructUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.system.domain.SysSocial;
import leyramu.framework.lersosa.system.domain.bo.SysSocialBo;
import leyramu.framework.lersosa.system.domain.vo.SysSocialVo;
import leyramu.framework.lersosa.system.mapper.SysSocialMapper;
import leyramu.framework.lersosa.system.service.ISysSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 社会化关系Service业务层处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@RequiredArgsConstructor
@Service
public class SysSocialServiceImpl implements ISysSocialService {

    private final SysSocialMapper baseMapper;

    /**
     * 根据ID查询社会化关系.
     *
     * @param id 社会化关系的唯一标识符
     * @return 返回与给定ID对应的SysSocialVo对象，如果未找到则返回null
     */
    @Override
    public SysSocialVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询社会化关系列表.
     *
     * @param bo 用于过滤查询条件的SysSocialBo对象
     * @return 返回符合条件的SysSocialVo对象列表
     */
    @Override
    public List<SysSocialVo> queryList(SysSocialBo bo) {
        LambdaQueryWrapper<SysSocial> lqw = new LambdaQueryWrapper<SysSocial>()
            .eq(ObjectUtil.isNotNull(bo.getUserId()), SysSocial::getUserId, bo.getUserId())
            .eq(StringUtils.isNotBlank(bo.getAuthId()), SysSocial::getAuthId, bo.getAuthId())
            .eq(StringUtils.isNotBlank(bo.getSource()), SysSocial::getSource, bo.getSource());
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 根据用户ID查询社会化关系列表.
     *
     * @param userId 用户的唯一标识符
     * @return 返回与给定用户ID相关联的SysSocialVo对象列表
     */
    @Override
    public List<SysSocialVo> queryListByUserId(Long userId) {
        return baseMapper.selectVoList(new LambdaQueryWrapper<SysSocial>().eq(SysSocial::getUserId, userId));
    }

    /**
     * 新增授权关系.
     *
     * @param bo 包含新增授权关系信息的SysSocialBo对象
     * @return 返回新增操作的结果，成功返回true，失败返回false
     */
    @Override
    public Boolean insertByBo(SysSocialBo bo) {
        SysSocial add = MapstructUtils.convert(bo, SysSocial.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            if (add != null) {
                bo.setId(add.getId());
            } else {
                return false;
            }
        }
        return flag;
    }

    /**
     * 更新社会化关系.
     *
     * @param bo 包含更新信息的SysSocialBo对象
     * @return 返回更新操作的结果，成功返回true，失败返回false
     */
    @Override
    public Boolean updateByBo(SysSocialBo bo) {
        SysSocial update = MapstructUtils.convert(bo, SysSocial.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验.
     */
    private void validEntityBeforeSave(SysSocial ignoredEntity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除社会化关系信息.
     *
     * @param id 要删除的社会化关系的唯一标识符
     * @return 返回删除操作的结果，成功返回true，失败返回false
     */
    @Override
    public Boolean deleteWithValidById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 根据认证ID查询社会化关系和用户信息.
     *
     * @param authId 认证ID
     * @return 返回包含SysSocial和用户信息的SysSocialVo对象列表
     */
    @Override
    public List<SysSocialVo> selectByAuthId(String authId) {
        return baseMapper.selectVoList(new LambdaQueryWrapper<SysSocial>().eq(SysSocial::getAuthId, authId));
    }
}
