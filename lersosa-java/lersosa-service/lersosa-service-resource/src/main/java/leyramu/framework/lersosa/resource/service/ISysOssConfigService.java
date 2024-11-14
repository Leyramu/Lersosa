/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.resource.service;


import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.resource.domain.bo.SysOssConfigBo;
import leyramu.framework.lersosa.resource.domain.vo.SysOssConfigVo;

import java.util.Collection;

/**
 * 对象存储配置Service接口.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface ISysOssConfigService {

    /**
     * 初始化OSS配置.
     */
    void init();

    /**
     * 查询单个.
     */
    SysOssConfigVo queryById(Long ossConfigId);

    /**
     * 查询列表.
     */
    TableDataInfo<SysOssConfigVo> queryPageList(SysOssConfigBo bo, PageQuery pageQuery);


    /**
     * 根据新增业务对象插入对象存储配置.
     *
     * @param bo 对象存储配置新增业务对象
     * @return 结果
     */
    Boolean insertByBo(SysOssConfigBo bo);

    /**
     * 根据编辑业务对象修改对象存储配置.
     *
     * @param bo 对象存储配置编辑业务对象
     * @return 结果
     */
    Boolean updateByBo(SysOssConfigBo bo);

    /**
     * 校验并删除数据.
     *
     * @param ids     主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 启用停用状态.
     */
    int updateOssConfigStatus(SysOssConfigBo bo);
}
