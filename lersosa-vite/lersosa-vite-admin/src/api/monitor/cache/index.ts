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
import { CacheVO } from './types';

// 查询缓存详细
export function getCache(): AxiosPromise<CacheVO> {
  return request({
    url: '/monitor/cache',
    method: 'get'
  });
}

// 查询缓存名称列表
export function listCacheName() {
  return request({
    url: '/monitor/cache/getNames',
    method: 'get'
  });
}

// 查询缓存键名列表
export function listCacheKey(cacheName: string) {
  return request({
    url: '/monitor/cache/getKeys/' + cacheName,
    method: 'get'
  });
}

// 查询缓存内容
export function getCacheValue(cacheName: string, cacheKey: string) {
  return request({
    url: '/monitor/cache/getValue/' + cacheName + '/' + cacheKey,
    method: 'get'
  });
}

// 清理指定名称缓存
export function clearCacheName(cacheName: string) {
  return request({
    url: '/monitor/cache/clearCacheName/' + cacheName,
    method: 'delete'
  });
}

// 清理指定键名缓存
export function clearCacheKey(cacheName: string, cacheKey: string) {
  return request({
    url: '/monitor/cache/clearCacheKey/' + cacheName + '/' + cacheKey,
    method: 'delete'
  });
}

// 清理全部缓存
export function clearCacheAll() {
  return request({
    url: '/monitor/cache/clearCacheAll',
    method: 'delete'
  });
}
