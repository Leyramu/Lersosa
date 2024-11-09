/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import leyramu.framework.lersosa.common.mybatis.core.mapper.BaseMapperPlus;
import leyramu.framework.lersosa.workflow.domain.vo.TaskVo;
import org.apache.ibatis.annotations.Param;


/**
 * 任务信息Mapper接口
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@InterceptorIgnore(tenantLine = "true")
public interface ActTaskMapper extends BaseMapperPlus<TaskVo, TaskVo> {
    /**
     * 获取待办信息
     *
     * @param page         分页
     * @param queryWrapper 条件
     * @return 结果
     */
    Page<TaskVo> getTaskWaitByPage(@Param("page") Page<TaskVo> page, @Param(Constants.WRAPPER) Wrapper<TaskVo> queryWrapper);

    /**
     * 获取已办
     *
     * @param page         分页
     * @param queryWrapper 条件
     * @return 结果
     */
    Page<TaskVo> getTaskFinishByPage(@Param("page") Page<TaskVo> page, @Param(Constants.WRAPPER) Wrapper<TaskVo> queryWrapper);

    /**
     * 查询当前用户的抄送
     *
     * @param page         分页
     * @param queryWrapper 条件
     * @return 结果
     */
    Page<TaskVo> getTaskCopyByPage(@Param("page") Page<TaskVo> page, @Param(Constants.WRAPPER) QueryWrapper<TaskVo> queryWrapper);
}
