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
import { AxiosPromise } from 'axios';
import { DefinitionConfigVO, DefinitionConfigForm } from '@/api/workflow/definitionConfig/types';

/**
 * 查询表单配置详细
 * @param definitionId
 */
export const getByDefId = (definitionId: string | number): AxiosPromise<DefinitionConfigVO> => {
  return request({
    url: '/workflow/definitionConfig/getByDefId/' + definitionId,
    method: 'get'
  });
};

/**
 * 新增表单配置
 * @param data
 */
export const saveOrUpdate = (data: DefinitionConfigForm) => {
  return request({
    url: '/workflow/definitionConfig/saveOrUpdate',
    method: 'post',
    data: data
  });
};

/**
 * 删除表单配置
 * @param id
 */
export const deldefinitionConfig = (id: string | number | Array<string | number>) => {
  return request({
    url: '/workflow/definitionConfig/' + id,
    method: 'delete'
  });
};

/**
 * 查询流程定义配置排除当前查询的流程定义
 * @param tableName
 * @param definitionId
 */
export const getByTableNameNotDefId = (tableName: string, definitionId: string | number) => {
  return request({
    url: `/workflow/definitionConfig/getByTableNameNotDefId/${tableName}/${definitionId}`,
    method: 'get'
  });
};
