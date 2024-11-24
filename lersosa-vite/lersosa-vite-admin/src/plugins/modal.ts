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

import { MessageBoxData } from 'element-plus';
import { LoadingInstance } from 'element-plus/es/components/loading/src/loading';
let loadingInstance: LoadingInstance;
export default {
  // 消息提示
  msg(content: any) {
    ElMessage.info(content);
  },
  // 错误消息
  msgError(content: any) {
    ElMessage.error(content);
  },
  // 成功消息
  msgSuccess(content: any) {
    ElMessage.success(content);
  },
  // 警告消息
  msgWarning(content: any) {
    ElMessage.warning(content);
  },
  // 弹出提示
  alert(content: any) {
    ElMessageBox.alert(content, '系统提示').then((_) => {});
  },
  // 错误提示
  alertError(content: any) {
    ElMessageBox.alert(content, '系统提示', { type: 'error' }).then((_) => {});
  },
  // 成功提示
  alertSuccess(content: any) {
    ElMessageBox.alert(content, '系统提示', { type: 'success' }).then((_) => {});
  },
  // 警告提示
  alertWarning(content: any) {
    ElMessageBox.alert(content, '系统提示', { type: 'warning' }).then((_) => {});
  },
  // 通知提示
  notify(content: any) {
    ElNotification.info(content);
  },
  // 错误通知
  notifyError(content: any) {
    ElNotification.error(content);
  },
  // 成功通知
  notifySuccess(content: any) {
    ElNotification.success(content);
  },
  // 警告通知
  notifyWarning(content: any) {
    ElNotification.warning(content);
  },
  // 确认窗体
  confirm(content: any): Promise<MessageBoxData> {
    return ElMessageBox.confirm(content, '系统提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });
  },
  // 提交内容
  prompt(content: any) {
    return ElMessageBox.prompt(content, '系统提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });
  },
  // 打开遮罩层
  loading(content: string) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: content,
      background: 'rgba(0, 0, 0, 0.7)'
    });
  },
  // 关闭遮罩层
  closeLoading() {
    loadingInstance.close();
  }
};
