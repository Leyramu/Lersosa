/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.service;

import leyramu.framework.lersosa.workflow.domain.WfNodeConfig;
import leyramu.framework.lersosa.workflow.domain.vo.WfNodeConfigVo;

import java.util.Collection;
import java.util.List;

/**
 * 节点配置Service接口.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface IWfNodeConfigService {

    /**
     * 查询节点配置.
     *
     * @param id 主键
     * @return 结果
     */
    WfNodeConfigVo queryById(Long id);

    /**
     * 保存节点配置.
     *
     * @param list 参数
     */
    void saveOrUpdate(List<WfNodeConfig> list);

    /**
     * 批量删除节点配置信息.
     *
     * @param ids 主键
     * @return 结果
     */
    Boolean deleteByIds(Collection<Long> ids);

    /**
     * 按照流程定义id删除.
     *
     * @param ids 流程定义id
     */
    void deleteByDefIds(Collection<String> ids);

    /**
     * 按照流程定义id查询.
     *
     * @param ids 流程定义id
     * @return 结果
     */
    List<WfNodeConfigVo> selectByDefIds(Collection<String> ids);
}
