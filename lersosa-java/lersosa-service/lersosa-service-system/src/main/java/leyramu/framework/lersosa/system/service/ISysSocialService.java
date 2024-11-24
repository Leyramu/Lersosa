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

import leyramu.framework.lersosa.system.domain.bo.SysSocialBo;
import leyramu.framework.lersosa.system.domain.vo.SysSocialVo;

import java.util.List;

/**
 * 社会化关系Service接口.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface ISysSocialService {

    /**
     * 根据ID查询社会化关系.
     *
     * @param id 社会化关系的唯一标识符
     * @return 返回与给定ID对应的SysSocialVo对象，如果未找到则返回null
     */
    SysSocialVo queryById(String id);

    /**
     * 查询社会化关系列表.
     *
     * @param bo 用于过滤查询条件的SysSocialBo对象
     * @return 返回符合条件的SysSocialVo对象列表
     */
    List<SysSocialVo> queryList(SysSocialBo bo);

    /**
     * 根据用户ID查询社会化关系列表.
     *
     * @param userId 用户的唯一标识符
     * @return 返回与给定用户ID相关联的SysSocialVo对象列表
     */
    List<SysSocialVo> queryListByUserId(Long userId);

    /**
     * 新增授权关系.
     *
     * @param bo 包含新增授权关系信息的SysSocialBo对象
     * @return 返回新增操作的结果，成功返回true，失败返回false
     */
    Boolean insertByBo(SysSocialBo bo);

    /**
     * 更新社会化关系.
     *
     * @param bo 包含更新信息的SysSocialBo对象
     * @return 返回更新操作的结果，成功返回true，失败返回false
     */
    Boolean updateByBo(SysSocialBo bo);

    /**
     * 删除社会化关系信息.
     *
     * @param id 要删除的社会化关系的唯一标识符
     * @return 返回删除操作的结果，成功返回true，失败返回false
     */
    Boolean deleteWithValidById(Long id);

    /**
     * 根据认证ID查询社会化关系和用户信息.
     *
     * @param authId 认证ID
     * @return 返回包含SysSocial和用户信息的SysSocialVo对象列表
     */
    List<SysSocialVo> selectByAuthId(String authId);
}
