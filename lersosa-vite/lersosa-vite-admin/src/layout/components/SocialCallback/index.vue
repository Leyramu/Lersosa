<!--
  - Copyright (c) 2024 Leyramu Group. All rights reserved.
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -      http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  -
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  -
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  -
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>
  <div v-loading="loading" class="social-callback"></div>
</template>

<script setup lang="ts">
import { login, callback } from '@/api/login';
import { setToken, getToken } from '@/utils/auth';
import { LoginData } from '@/api/types';

const route = useRoute();
const loading = ref(true);

/**
 * 接收Route传递的参数
 * @param {Object} route.query.
 */
const code = route.query.code as string;
const state = route.query.state as string;
const source = route.query.source as string;
const stateJson = JSON.parse(atob(state));
const tenantId = (stateJson.tenantId as string) ? (stateJson.tenantId as string) : '000000';
const domain = stateJson.domain as string;

const processResponse = async (res: any) => {
  if (res.code !== 200) {
    throw new Error(res.msg);
  }
  if (res.data !== null) {
    setToken(res.data.access_token);
  }
  ElMessage.success(res.msg);
  setTimeout(() => {
    location.href = import.meta.env.VITE_APP_CONTEXT_PATH + 'index';
  }, 2000);
};

const handleError = (error: any) => {
  ElMessage.error(error.message);
  setTimeout(() => {
    location.href = import.meta.env.VITE_APP_CONTEXT_PATH + 'index';
  }, 2000);
};

const callbackByCode = async (data: LoginData) => {
  try {
    const res = await callback(data);
    await processResponse(res);
    loading.value = false;
  } catch (error) {
    handleError(error);
  }
};

const loginByCode = async (data: LoginData) => {
  try {
    const res = await login(data);
    await processResponse(res);
    loading.value = false;
  } catch (error) {
    handleError(error);
  }
};

const init = async () => {
  // 如果域名不相等 则重定向处理
  let host = window.location.host;
  if (domain !== host) {
    let urlFull = new URL(window.location.href);
    urlFull.host = domain;
    window.location.href = urlFull.toString();
    return;
  }

  const data: LoginData = {
    socialCode: code,
    socialState: state,
    tenantId: tenantId,
    source: source,
    clientId: import.meta.env.VITE_APP_CLIENT_ID,
    grantType: 'social'
  };

  if (!getToken()) {
    await loginByCode(data);
  } else {
    await callbackByCode(data);
  }
};

onMounted(() => {
  nextTick(() => {
    init();
  });
});
</script>
