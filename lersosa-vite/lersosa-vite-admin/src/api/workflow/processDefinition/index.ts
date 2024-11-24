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

import request from '@/utils/request';
import { ProcessDefinitionQuery, ProcessDefinitionVO, definitionXmlVO } from '@/api/workflow/processDefinition/types';
import { AxiosPromise } from 'axios';

/**
 * 获取流程定义列表
 * @param query 流程实例id
 * @returns
 */
export const listProcessDefinition = (query: ProcessDefinitionQuery): AxiosPromise<ProcessDefinitionVO[]> => {
  return request({
    url: `/workflow/processDefinition/list`,
    method: 'get',
    params: query
  });
};
/**
 * 按照流程定义key获取流程定义
 * @returns
 * @param key
 */
export const getListByKey = (key: string) => {
  return request({
    url: `/workflow/processDefinition/getListByKey/${key}`,
    method: 'get'
  });
};

/**
 * 通过流程定义id获取流程图
 */
export const definitionImage = (processDefinitionId: string): AxiosPromise<any> => {
  return request({
    url: `/workflow/processDefinition/definitionImage/${processDefinitionId}` + '?t' + Math.random(),
    method: 'get'
  });
};

/**
 * 通过流程定义id获取xml
 * @param processDefinitionId 流程定义id
 * @returns
 */
export const definitionXml = (processDefinitionId: string): AxiosPromise<definitionXmlVO> => {
  return request({
    url: `/workflow/processDefinition/definitionXml/${processDefinitionId}`,
    method: 'get'
  });
};

/**
 * 删除流程定义
 * @param deploymentId 部署id
 * @param processDefinitionId 流程定义id
 * @returns
 */
export const deleteProcessDefinition = (deploymentId: string | string[], processDefinitionId: string | string[]) => {
  return request({
    url: `/workflow/processDefinition/${deploymentId}/${processDefinitionId}`,
    method: 'delete'
  });
};

/**
 * 挂起/激活
 * @param processDefinitionId 流程定义id
 * @returns
 */
export const updateDefinitionState = (processDefinitionId: string) => {
  return request({
    url: `/workflow/processDefinition/updateDefinitionState/${processDefinitionId}`,
    method: 'put'
  });
};

/**
 * 流程定义转换为模型
 * @param processDefinitionId 流程定义id
 * @returns
 */
export const convertToModel = (processDefinitionId: string) => {
  return request({
    url: `/workflow/processDefinition/convertToModel/${processDefinitionId}`,
    method: 'put'
  });
};

/**
 * 通过zip或xml部署流程定义
 * @returns
 */
export function deployProcessFile(data: any) {
  return request({
    url: '/workflow/processDefinition/deployByFile',
    method: 'post',
    data: data,
    headers: {
      repeatSubmit: false
    }
  });
}

/**
 * 迁移流程
 * @param currentProcessDefinitionId
 * @param fromProcessDefinitionId
 * @returns
 */
export const migrationDefinition = (currentProcessDefinitionId: string, fromProcessDefinitionId: string) => {
  return request({
    url: `/workflow/processDefinition/migrationDefinition/${currentProcessDefinitionId}/${fromProcessDefinitionId}`,
    method: 'put'
  });
};
