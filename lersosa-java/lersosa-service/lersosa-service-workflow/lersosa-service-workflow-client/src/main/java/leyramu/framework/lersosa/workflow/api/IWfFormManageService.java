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

package leyramu.framework.lersosa.workflow.api;

import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.workflow.domain.bo.WfFormManageBo;
import leyramu.framework.lersosa.workflow.domain.vo.WfFormManageVo;

import java.util.Collection;
import java.util.List;

/**
 * 表单管理Service接口.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface IWfFormManageService {

    /**
     * 查询表单管理.
     *
     * @param id 主键
     * @return 结果
     */
    WfFormManageVo queryById(Long id);

    /**
     * 查询表单管理.
     *
     * @param ids 主键
     * @return 结果
     */
    List<WfFormManageVo> queryByIds(List<Long> ids);

    /**
     * 查询表单管理列表.
     *
     * @param bo        参数
     * @param pageQuery 分页
     * @return 结果
     */
    TableDataInfo<WfFormManageVo> queryPageList(WfFormManageBo bo, PageQuery pageQuery);

    /**
     * 查询表单管理列表.
     *
     * @return 结果
     */
    List<WfFormManageVo> selectList();

    /**
     * 查询表单管理列表.
     *
     * @param bo 参数
     * @return 结果
     */
    List<WfFormManageVo> queryList(WfFormManageBo bo);

    /**
     * 新增表单管理.
     *
     * @param bo 参数
     * @return 结果
     */
    Boolean insertByBo(WfFormManageBo bo);

    /**
     * 修改表单管理.
     *
     * @param bo 参数
     * @return 结果
     */
    Boolean updateByBo(WfFormManageBo bo);

    /**
     * 批量删除表单管理信息.
     *
     * @param ids 主键
     * @return 结果
     */
    Boolean deleteByIds(Collection<Long> ids);
}
