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
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <span v-if="item.redirect === 'noRedirect' || index == levelList.length - 1" class="no-redirect">{{ item.meta?.title }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta?.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import { RouteLocationMatched } from 'vue-router';

const route = useRoute();
const router = useRouter();
const levelList = ref<RouteLocationMatched[]>([]);

const getBreadcrumb = () => {
  // only show routes with meta.title
  let matched = route.matched.filter((item) => item.meta && item.meta.title);
  const first = matched[0];
  // 判断是否为首页
  if (!isDashboard(first)) {
    matched = ([{ path: '/index', meta: { title: '首页' } }] as any).concat(matched);
  }
  levelList.value = matched.filter((item) => item.meta && item.meta.title && item.meta.breadcrumb !== false);
};
const isDashboard = (route: RouteLocationMatched) => {
  const name = route && (route.name as string);
  if (!name) {
    return false;
  }
  return name.trim() === 'Index';
};
const handleLink = (item: any) => {
  const { redirect, path } = item;
  redirect ? router.push(redirect) : router.push(path);
};

watchEffect(() => {
  // if you go to the redirect page, do not update the breadcrumbs
  if (route.path.startsWith('/redirect/')) return;
  getBreadcrumb();
});
onMounted(() => {
  getBreadcrumb();
});
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>
