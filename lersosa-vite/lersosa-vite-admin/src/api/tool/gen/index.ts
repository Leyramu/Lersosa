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
import { DbTableQuery, DbTableVO, TableQuery, TableVO, GenTableVO, DbTableForm } from './types';
import { AxiosPromise } from 'axios';

// 查询生成表数据
export const listTable = (query: TableQuery): AxiosPromise<TableVO[]> => {
  return request({
    url: '/tool/gen/list',
    method: 'get',
    params: query
  });
};
// 查询db数据库列表
export const listDbTable = (query: DbTableQuery): AxiosPromise<DbTableVO[]> => {
  return request({
    url: '/tool/gen/db/list',
    method: 'get',
    params: query
  });
};

// 查询表详细信息
export const getGenTable = (tableId: string | number): AxiosPromise<GenTableVO> => {
  return request({
    url: '/tool/gen/' + tableId,
    method: 'get'
  });
};

// 修改代码生成信息
export const updateGenTable = (data: DbTableForm): AxiosPromise<GenTableVO> => {
  return request({
    url: '/tool/gen',
    method: 'put',
    data: data
  });
};

// 导入表
export const importTable = (data: { tables: string; dataName: string }): AxiosPromise<GenTableVO> => {
  return request({
    url: '/tool/gen/importTable',
    method: 'post',
    params: data
  });
};

// 预览生成代码
export const previewTable = (tableId: string | number) => {
  return request({
    url: '/tool/gen/preview/' + tableId,
    method: 'get'
  });
};

// 删除表数据
export const delTable = (tableId: string | number | Array<string | number>) => {
  return request({
    url: '/tool/gen/' + tableId,
    method: 'delete'
  });
};

// 生成代码（自定义路径）
export const genCode = (tableId: string | number) => {
  return request({
    url: '/tool/gen/genCode/' + tableId,
    method: 'get'
  });
};

// 同步数据库
export const synchDb = (tableId: string | number) => {
  return request({
    url: '/tool/gen/synchDb/' + tableId,
    method: 'get'
  });
};

// 获取数据源名称
export const getDataNames = () => {
  return request({
    url: '/tool/gen/getDataNames',
    method: 'get'
  });
};
