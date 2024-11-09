/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.workflow.service;

import jakarta.servlet.http.HttpServletResponse;
import leyramu.framework.lersosa.common.mybatis.core.page.PageQuery;
import leyramu.framework.lersosa.common.mybatis.core.page.TableDataInfo;
import leyramu.framework.lersosa.workflow.domain.bo.ModelBo;
import leyramu.framework.lersosa.workflow.domain.vo.ModelVo;
import org.flowable.engine.repository.Model;

import java.util.List;


/**
 * 模型管理 服务层
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface IActModelService {
    /**
     * 分页查询模型
     *
     * @param modelBo   模型参数
     * @param pageQuery 参数
     * @return 返回分页列表
     */
    TableDataInfo<Model> page(ModelBo modelBo, PageQuery pageQuery);

    /**
     * 新增模型
     *
     * @param modelBo 模型请求对象
     * @return 结果
     */
    boolean saveNewModel(ModelBo modelBo);

    /**
     * 查询模型
     *
     * @param modelId 模型id
     * @return 模型数据
     */
    ModelVo getInfo(String modelId);

    /**
     * 修改模型信息
     *
     * @param modelBo 模型数据
     * @return 结果
     */
    boolean update(ModelBo modelBo);

    /**
     * 编辑模型XML
     *
     * @param modelBo 模型数据
     * @return 结果
     */
    boolean editModelXml(ModelBo modelBo);

    /**
     * 模型部署
     *
     * @param id 模型id
     * @return 结果
     */
    boolean modelDeploy(String id);

    /**
     * 导出模型zip压缩包
     *
     * @param modelIds 模型id
     * @param response 响应
     */
    void exportZip(List<String> modelIds, HttpServletResponse response);

    /**
     * 复制模型
     *
     * @param modelBo 模型数据
     * @return 结果
     */
    boolean copyModel(ModelBo modelBo);
}
