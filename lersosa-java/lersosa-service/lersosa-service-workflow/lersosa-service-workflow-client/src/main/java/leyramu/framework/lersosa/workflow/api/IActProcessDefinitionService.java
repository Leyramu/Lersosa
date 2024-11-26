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
import leyramu.framework.lersosa.workflow.domain.bo.ProcessDefinitionBo;
import leyramu.framework.lersosa.workflow.domain.vo.ProcessDefinitionVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 流程定义 服务层.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface IActProcessDefinitionService {
    /**
     * 分页查询.
     *
     * @param processDefinitionBo 参数
     * @param pageQuery           分页
     * @return 返回分页列表
     */
    TableDataInfo<ProcessDefinitionVo> page(ProcessDefinitionBo processDefinitionBo, PageQuery pageQuery);

    /**
     * 查询历史流程定义列表.
     *
     * @param key 流程定义key
     * @return 结果
     */
    List<ProcessDefinitionVo> getListByKey(String key);

    /**
     * 查看流程定义图片.
     *
     * @param processDefinitionId 流程定义id
     * @return 结果
     */
    String definitionImage(String processDefinitionId);

    /**
     * 查看流程定义xml文件.
     *
     * @param processDefinitionId 流程定义id
     * @return 结果
     */
    String definitionXml(String processDefinitionId);

    /**
     * 删除流程定义.
     *
     * @param deploymentIds        部署id
     * @param processDefinitionIds 流程定义id
     * @return 结果
     */
    boolean deleteDeployment(List<String> deploymentIds, List<String> processDefinitionIds);

    /**
     * 激活或者挂起流程定义.
     *
     * @param processDefinitionId 流程定义id
     * @return 结果
     */
    boolean updateDefinitionState(String processDefinitionId);

    /**
     * 迁移流程定义.
     *
     * @param currentProcessDefinitionId 当前流程定义id
     * @param fromProcessDefinitionId    需要迁移到的流程定义id
     * @return 结果
     */
    boolean migrationDefinition(String currentProcessDefinitionId, String fromProcessDefinitionId);

    /**
     * 流程定义转换为模型.
     *
     * @param processDefinitionId 流程定义id
     * @return 结果
     */
    boolean convertToModel(String processDefinitionId);

    /**
     * 通过zip或xml部署流程定义.
     *
     * @param file         文件
     * @param categoryCode 分类
     */
    void deployByFile(MultipartFile file, String categoryCode);
}
