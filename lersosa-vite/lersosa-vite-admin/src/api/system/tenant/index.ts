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
import { TenantForm, TenantQuery, TenantVO } from './types';
import { AxiosPromise } from 'axios';

// 查询租户列表
export function listTenant(query: TenantQuery): AxiosPromise<TenantVO[]> {
  return request({
    url: '/system/tenant/list',
    method: 'get',
    params: query
  });
}

// 查询租户详细
export function getTenant(id: string | number): AxiosPromise<TenantVO> {
  return request({
    url: '/system/tenant/' + id,
    method: 'get'
  });
}

// 新增租户
export function addTenant(data: TenantForm) {
  return request({
    url: '/system/tenant',
    method: 'post',
    headers: {
      isEncrypt: true,
      repeatSubmit: false
    },
    data: data
  });
}

// 修改租户
export function updateTenant(data: TenantForm) {
  return request({
    url: '/system/tenant',
    method: 'put',
    data: data
  });
}

// 租户状态修改
export function changeTenantStatus(id: string | number, tenantId: string | number, status: string) {
  const data = {
    id,
    tenantId,
    status
  };
  return request({
    url: '/system/tenant/changeStatus',
    method: 'put',
    data: data
  });
}

// 删除租户
export function delTenant(id: string | number | Array<string | number>) {
  return request({
    url: '/system/tenant/' + id,
    method: 'delete'
  });
}

// 动态切换租户
export function dynamicTenant(tenantId: string | number) {
  return request({
    url: '/system/tenant/dynamic/' + tenantId,
    method: 'get'
  });
}

// 清除动态租户
export function dynamicClear() {
  return request({
    url: '/system/tenant/dynamic/clear',
    method: 'get'
  });
}

// 同步租户套餐
export function syncTenantPackage(tenantId: string | number, packageId: string | number) {
  const data = {
    tenantId,
    packageId
  };
  return request({
    url: '/system/tenant/syncTenantPackage',
    method: 'get',
    params: data
  });
}

// 同步租户字典
export function syncTenantDict() {
  return request({
    url: '/system/tenant/syncTenantDict',
    method: 'get'
  });
}
