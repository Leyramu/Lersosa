/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

import axios from 'axios';
import {ElLoading, ElMessage} from 'element-plus';
import {saveAs} from 'file-saver';
import {getToken} from '@/utils/auth';
import errorCode from '@/utils/errorCode';
import {blobValidate} from '@/utils/lersosa';

const baseURL = import.meta.env.VITE_APP_BASE_API;
let downloadLoadingInstance: any;

export default {
    zip(url: string, name: string) {
        const fullUrl = baseURL + url;
        downloadLoadingInstance = ElLoading.service({text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)"});

        axios({
            method: 'get',
            url: fullUrl,
            responseType: 'blob',
            headers: {'Authorization': 'Bearer ' + getToken()}
        }).then((res: any) => {
            const isBlob = blobValidate(res.data);
            if (isBlob) {
                const blob = new Blob([res.data], {type: 'application/zip'});
                this.saveAs(blob, name);
            } else {
                this.printErrMsg(res.data).then(_ => {
                });
            }
            downloadLoadingInstance.close();
        }).catch((r: any) => {
            console.error(r);
            ElMessage.error('下载文件出现错误，请联系管理员！');
            downloadLoadingInstance.close();
        });
    },

    saveAs(text: Blob, name: string, opts?: any) {
        saveAs(text, name, opts);
    },

    async printErrMsg(data: Blob) {
        const resText = await data.text();
        const rspObj = JSON.parse(resText);
        const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default'];
        ElMessage.error(errMsg);
    }
};


