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

package leyramu.framework.lersosa.auth.gatewayimpl;

import leyramu.framework.lersosa.auth.convert.SocialConvertor;
import leyramu.framework.lersosa.auth.gateway.SocialGateway;
import leyramu.framework.lersosa.auth.model.SocialV;
import leyramu.framework.lersosa.system.api.RemoteSocialService;
import leyramu.framework.lersosa.system.api.domain.bo.RemoteSocialBo;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 社交网关实现.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/7
 */
@Component
@RequiredArgsConstructor
public class SocialGatewayImpl implements SocialGateway {

    /**
     * 远程社交服务.
     */
    @DubboReference
    private RemoteSocialService remoteSocialService;

    /**
     * 根据 authId 查询用户授权信息.
     *
     * @param authId 认证id
     * @return 授权信息集合
     */
    @Override
    public List<SocialV> selectByAuthId(String authId) {
        return remoteSocialService.selectByAuthId(authId).parallelStream().map(SocialConvertor::toDomainObject).toList();
    }

    /**
     * 根据 authId 查询用户授权信息.
     *
     * @param params 查询参数
     * @return 授权信息集合
     */
    @Override
    public List<SocialV> queryList(RemoteSocialBo params) {
        return remoteSocialService.queryList(params).parallelStream().map(SocialConvertor::toDomainObject).toList();
    }

    /**
     * 根据 authId 查询用户授权信息.
     *
     * @param bo 授权信息
     */
    @Override
    public void insertByBo(RemoteSocialBo bo) {
        remoteSocialService.insertByBo(bo);
    }

    /**
     * 更新用户授权信息.
     *
     * @param bo 授权信息
     */
    @Override
    public void updateByBo(RemoteSocialBo bo) {
        remoteSocialService.updateByBo(bo);
    }

    /**
     * 删除用户授权信息.
     *
     * @param socialId 授权信息ID
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidById(Long socialId) {
        return remoteSocialService.deleteWithValidById(socialId);
    }
}
