/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import {ElLoading, ElMessage, ElMessageBox, ElNotification} from 'element-plus';

let loadingInstance: any;

export default {
    // 消息提示
    msg(content: string) {
        ElMessage.info(content);
    },
    // 错误消息
    msgError(content: string) {
        ElMessage.error(content);
    },
    // 成功消息
    msgSuccess(content: string) {
        ElMessage.success(content);
    },
    // 警告消息
    msgWarning(content: string) {
        ElMessage.warning(content);
    },
    // 弹出提示
    alert(content: string) {
        ElMessageBox.alert(content, "系统提示").then(_ => {});
    },
    // 错误提示
    alertError(content: string) {
        ElMessageBox.alert(content, "系统提示", {type: 'error'}).then(_ => {});
    },
    // 成功提示
    alertSuccess(content: string) {
        ElMessageBox.alert(content, "系统提示", {type: 'success'}).then(_ => {});
    },
    // 警告提示
    alertWarning(content: string) {
        ElMessageBox.alert(content, "系统提示", {type: 'warning'}).then(_ => {});
    },
    // 通知提示
    notify(content: string) {
        ElNotification.info({message: content});
    },
    // 错误通知
    notifyError(content: string) {
        ElNotification.error({message: content});
    },
    // 成功通知
    notifySuccess(content: string) {
        ElNotification.success({message: content});
    },
    // 警告通知
    notifyWarning(content: string) {
        ElNotification.warning({message: content});
    },
    // 确认窗体
    confirm(content: string): Promise<any> {
        return ElMessageBox.confirm(content, "系统提示", {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: "warning",
        });
    },
    // 提交内容
    prompt(content: string): Promise<any> {
        return ElMessageBox.prompt(content, "系统提示", {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: "warning",
        });
    },
    // 打开遮罩层
    loading(content: string) {
        loadingInstance = ElLoading.service({
            lock: true,
            text: content,
            background: "rgba(0, 0, 0, 0.7)",
        });
    },
    // 关闭遮罩层
    closeLoading() {
        if (loadingInstance) {
            loadingInstance.close();
        }
    }
};
