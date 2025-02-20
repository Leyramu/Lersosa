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

package leyramu.framework.lersosa.system.service;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import leyramu.framework.lersosa.common.core.constant.CacheNames;
import leyramu.framework.lersosa.common.core.utils.MapstructUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.system.api.ISysClientService;
import leyramu.framework.lersosa.system.domain.SysClient;
import leyramu.framework.lersosa.system.domain.bo.SysClientBo;
import leyramu.framework.lersosa.system.domain.vo.SysClientVo;
import leyramu.framework.lersosa.system.mapper.SysClientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 客户端管理Service业务层处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysClientServiceImpl implements ISysClientService {

    private final SysClientMapper baseMapper;

    /**
     * 查询客户端管理.
     */
    @Override
    public SysClientVo queryById(Long id) {
        SysClientVo vo = baseMapper.selectVoById(id);
        vo.setGrantTypeList(List.of(vo.getGrantType().split(",")));
        return vo;
    }


    /**
     * 查询客户端管理.
     */
    @Cacheable(cacheNames = CacheNames.SYS_CLIENT, key = "#clientId")
    @Override
    public SysClientVo queryByClientId(String clientId) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<SysClient>().eq(SysClient::getClientId, clientId));
    }

    /**
     * 查询客户端管理列表.
     */
    @Override
    public TableDataInfo<SysClientVo> queryPageList(SysClientBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysClient> lqw = buildQueryWrapper(bo);
        Page<SysClientVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(r -> r.setGrantTypeList(List.of(r.getGrantType().split(","))));
        return TableDataInfo.build(result);
    }

    /**
     * 查询客户端管理列表.
     */
    @Override
    public List<SysClientVo> queryList(SysClientBo bo) {
        LambdaQueryWrapper<SysClient> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysClient> buildQueryWrapper(SysClientBo bo) {
        LambdaQueryWrapper<SysClient> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getClientId()), SysClient::getClientId, bo.getClientId());
        lqw.eq(StringUtils.isNotBlank(bo.getClientKey()), SysClient::getClientKey, bo.getClientKey());
        lqw.eq(StringUtils.isNotBlank(bo.getClientSecret()), SysClient::getClientSecret, bo.getClientSecret());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysClient::getStatus, bo.getStatus());
        lqw.orderByAsc(SysClient::getId);
        return lqw;
    }

    /**
     * 新增客户端管理.
     */
    @Override
    public Boolean insertByBo(SysClientBo bo) {
        SysClient add = MapstructUtils.convert(bo, SysClient.class);
        validEntityBeforeSave(add);
        Objects.requireNonNull(add).setGrantType(String.join(",", bo.getGrantTypeList()));
        // 生成clientId
        String clientKey = bo.getClientKey();
        String clientSecret = bo.getClientSecret();
        add.setClientId(SecureUtil.md5(clientKey + clientSecret));
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改客户端管理.
     */
    @CacheEvict(cacheNames = CacheNames.SYS_CLIENT, key = "#bo.clientId")
    @Override
    public Boolean updateByBo(SysClientBo bo) {
        SysClient update = MapstructUtils.convert(bo, SysClient.class);
        validEntityBeforeSave(update);
        Objects.requireNonNull(update).setGrantType(String.join(",", bo.getGrantTypeList()));
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 修改状态.
     */
    @CacheEvict(cacheNames = CacheNames.SYS_CLIENT, key = "#clientId")
    @Override
    public int updateUserStatus(String clientId, String status) {
        return baseMapper.update(null,
            new LambdaUpdateWrapper<SysClient>()
                .set(SysClient::getStatus, status)
                .eq(SysClient::getClientId, clientId));
    }

    /**
     * 保存前的数据校验.
     */
    private void validEntityBeforeSave(SysClient ignoredEntity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除客户端管理.
     */
    @CacheEvict(cacheNames = CacheNames.SYS_CLIENT, allEntries = true)
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
            log.info("未实现");
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
