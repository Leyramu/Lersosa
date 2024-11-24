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
import { ProcessInstanceQuery, ProcessInstanceVO } from '@/api/workflow/processInstance/types';
import { AxiosPromise } from 'axios';

/**
 * 查询运行中实例列表
 * @param query
 * @returns {*}
 */
export const getPageByRunning = (query: ProcessInstanceQuery): AxiosPromise<ProcessInstanceVO[]> => {
  return request({
    url: '/workflow/processInstance/getPageByRunning',
    method: 'get',
    params: query
  });
};

/**
 * 查询已完成实例列表
 * @param query
 * @returns {*}
 */
export const getPageByFinish = (query: ProcessInstanceQuery): AxiosPromise<ProcessInstanceVO[]> => {
  return request({
    url: '/workflow/processInstance/getPageByFinish',
    method: 'get',
    params: query
  });
};

/**
 * 通过业务id获取历史流程图
 */
export const getHistoryImage = (businessKey: string) => {
  return request({
    url: `/workflow/processInstance/getHistoryImage/${businessKey}` + '?t' + Math.random(),
    method: 'get'
  });
};

/**
 * 通过业务id获取历史流程图运行中，历史等节点
 */
export const getHistoryList = (businessKey: string): AxiosPromise<Record<string, any>> => {
  return request({
    url: `/workflow/processInstance/getHistoryList/${businessKey}` + '?t' + Math.random(),
    method: 'get'
  });
};

/**
 * 获取审批记录
 * @param businessKey 业务id
 * @returns
 */
export const getHistoryRecord = (businessKey: string | number) => {
  return request({
    url: `/workflow/processInstance/getHistoryRecord/${businessKey}`,
    method: 'get'
  });
};

/**
 * 作废
 * @param data 参数
 * @returns
 */
export const deleteRunInstance = (data: object) => {
  return request({
    url: `/workflow/processInstance/deleteRunInstance`,
    method: 'post',
    data: data
  });
};

/**
 * 运行中的实例 删除程实例，删除历史记录，删除业务与流程关联信息
 * @param businessKey 业务id
 * @returns
 */
export const deleteRunAndHisInstance = (businessKey: string | string[]) => {
  return request({
    url: `/workflow/processInstance/deleteRunAndHisInstance/${businessKey}`,
    method: 'delete'
  });
};

/**
 * 已完成的实例 删除程实例，删除历史记录，删除业务与流程关联信息
 * @param businessKey 业务id
 * @returns
 */
export const deleteFinishAndHisInstance = (businessKey: string | string[]) => {
  return request({
    url: `/workflow/processInstance/deleteFinishAndHisInstance/${businessKey}`,
    method: 'delete'
  });
};

/**
 * 分页查询当前登录人单据
 * @param query
 * @returns {*}
 */
export const getPageByCurrent = (query: ProcessInstanceQuery): AxiosPromise<ProcessInstanceVO[]> => {
  return request({
    url: '/workflow/processInstance/getPageByCurrent',
    method: 'get',
    params: query
  });
};

/**
 * 撤销流程
 * @param businessKey 业务id
 * @returns
 */
export const cancelProcessApply = (businessKey: string) => {
  return request({
    url: `/workflow/processInstance/cancelProcessApply/${businessKey}`,
    method: 'post'
  });
};

export default {
  getPageByRunning,
  getPageByFinish,
  getHistoryImage,
  getHistoryList,
  getHistoryRecord,
  deleteRunInstance,
  deleteRunAndHisInstance,
  deleteFinishAndHisInstance,
  getPageByCurrent,
  cancelProcessApply
};
