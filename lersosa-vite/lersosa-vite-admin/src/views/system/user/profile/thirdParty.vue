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
  <div>
    <el-table :data="auths" style="width: 100%; height: 100%; font-size: 14px">
      <el-table-column label="序号" width="50" type="index" />
      <el-table-column label="绑定账号平台" width="140" align="center" prop="source" show-overflow-tooltip />
      <el-table-column label="头像" width="120" align="center" prop="avatar">
        <template #default="scope">
          <img :src="scope.row.avatar" style="width: 45px; height: 45px" alt="" />
        </template>
      </el-table-column>
      <el-table-column label="系统账号" width="180" align="center" prop="userName" :show-overflow-tooltip="true" />
      <el-table-column label="绑定时间" width="180" align="center" prop="createTime" />
      <el-table-column label="操作" width="80" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="small" type="text" @click="unlockAuth(scope.row)">解绑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div id="git-user-binding">
      <h4 class="provider-desc">你可以绑定以下第三方帐号</h4>
      <div id="authlist" class="user-bind">
        <a class="third-app" href="#" title="使用 微信 账号授权登录" @click="authUrl('wechat')">
          <div class="git-other-login-icon">
            <svg-icon icon-class="wechat" />
          </div>
          <span class="app-name">WeiXin</span>
        </a>
        <a class="third-app" href="#" title="使用 MaxKey 账号授权登录" @click="authUrl('maxkey')">
          <div class="git-other-login-icon">
            <svg-icon icon-class="maxkey" />
          </div>
          <span class="app-name">MaxKey</span>
        </a>
        <a class="third-app" href="#" title="使用 TopIam 账号授权登录" @click="authUrl('topiam')">
          <div class="git-other-login-icon">
            <svg-icon icon-class="topiam" />
          </div>
          <span class="app-name">TopIam</span>
        </a>
        <a class="third-app" href="#" title="使用 Gitee 账号授权登录" @click="authUrl('gitee')">
          <div class="git-other-login-icon">
            <svg-icon icon-class="gitee" />
          </div>
          <span class="app-name">Gitee</span>
        </a>
        <a class="third-app" href="#" title="使用 GitHub 账号授权登录" @click="authUrl('github')">
          <div class="git-other-login-icon">
            <svg-icon icon-class="github" />
          </div>
          <span class="app-name">Github</span>
        </a>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { authUnlock, authBinding } from '@/api/system/social/auth';
import { propTypes } from '@/utils/propTypes';
import useUserStore from '@/store/modules/user';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const props = defineProps({
  auths: propTypes.any.isRequired
});
const auths = computed(() => props.auths);

const unlockAuth = (row: any) => {
  ElMessageBox.confirm('您确定要解除"' + row.source + '"的账号绑定吗？')
    .then(() => {
      return authUnlock(row.id);
    })
    .then((res: any) => {
      if (res.code === 200) {
        proxy?.$modal.msgSuccess('解绑成功');
        proxy?.$tab.refreshPage();
      } else {
        proxy?.$modal.msgError(res.msg);
      }
    })
    .catch(() => {});
};

const authUrl = (source: string) => {
  authBinding(source, useUserStore().tenantId).then((res: any) => {
    if (res.code === 200) {
      window.location.href = res.data;
    } else {
      proxy?.$modal.msgError(res.msg);
    }
  });
};
</script>

<style>
.user-bind .third-app {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  min-width: 80px;
  float: left;
}

.user-bind {
  font-size: 1rem;
  text-align: start;
  height: 50px;
  margin-top: 10px;
}

.git-other-login-icon > img {
  height: 32px;
}

a {
  text-decoration: none;
  cursor: pointer;
  color: #005980;
}

.provider-desc {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol',
    'Liberation Sans', 'PingFang SC', 'Microsoft YaHei', 'Hiragino Sans GB', 'Wenquanyi Micro Hei', 'WenQuanYi Zen Hei', 'ST Heiti', SimHei, SimSun,
    'WenQuanYi Zen Hei Sharp', sans-serif;
  font-size: 1.071rem;
}

td > img {
  height: 20px;
  width: 20px;
  display: inline-block;
  border-radius: 50%;
  margin-right: 5px;
}
</style>
