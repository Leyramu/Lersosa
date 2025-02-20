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
  <div :class="{ show: show }" class="header-search">
    <svg-icon class-name="search-icon" icon-class="search" @click.stop="click" />
    <el-select
      ref="headerSearchSelectRef"
      v-model="search"
      :remote-method="querySearch"
      filterable
      default-first-option
      remote
      placeholder="Search"
      class="header-search-select"
      @change="change"
    >
      <el-option v-for="option in options" :key="option.item.path" :value="option.item" :label="option.item.title.join(' > ')" />
    </el-select>
  </div>
</template>

<script setup lang="ts" name="HeaderSearch">
import Fuse from 'fuse.js';
import { getNormalPath } from '@/utils/lersosa';
import { isHttp } from '@/utils/validate';
import usePermissionStore from '@/store/modules/permission';
import { RouteRecordRaw } from 'vue-router';

type Router = Array<{
  path: string;
  title: string[];
}>;

const search = ref('');
const options = ref<any>([]);
const searchPool = ref<Router>([]);
const show = ref(false);
const fuse = ref();
const headerSearchSelectRef = ref<ElSelectInstance>();
const router = useRouter();
const routes = computed(() => usePermissionStore().getRoutes());

const click = () => {
  show.value = !show.value;
  if (show.value) {
    headerSearchSelectRef.value && headerSearchSelectRef.value.focus();
  }
};
const close = () => {
  headerSearchSelectRef.value && headerSearchSelectRef.value.blur();
  options.value = [];
  show.value = false;
};
const change = (val: any) => {
  const path = val.path;
  const query = val.query;
  if (isHttp(path)) {
    // http(s):// 路径新窗口打开
    const pindex = path.indexOf('http');
    window.open(path.substring(pindex, path.length), '_blank');
  } else {
    if (query) {
      router.push({ path: path, query: JSON.parse(query) });
    } else {
      router.push(path);
    }
  }
  search.value = '';
  options.value = [];
  nextTick(() => {
    show.value = false;
  });
};
const initFuse = (list: Router) => {
  fuse.value = new Fuse(list, {
    shouldSort: true,
    threshold: 0.4,
    location: 0,
    distance: 100,
    minMatchCharLength: 1,
    keys: [
      {
        name: 'title',
        weight: 0.7
      },
      {
        name: 'path',
        weight: 0.3
      }
    ]
  });
};
// Filter out the routes that can be displayed in the sidebar
// And generate the internationalized title
const generateRoutes = (routes: RouteRecordRaw[], basePath = '', prefixTitle: string[] = []) => {
  let res: Router = [];
  routes.forEach((r) => {
    // skip hidden router
    if (!r.hidden) {
      const p = r.path.length > 0 && r.path[0] === '/' ? r.path : '/' + r.path;
      const data = {
        path: !isHttp(r.path) ? getNormalPath(basePath + p) : r.path,
        title: [...prefixTitle],
        query: ''
      };
      if (r.meta && r.meta.title) {
        data.title = [...data.title, r.meta.title];
        if (r.redirect !== 'noRedirect') {
          // only push the routes with title
          // special case: need to exclude parent router without redirect
          res.push(data);
        }
      }

      if (r.query) {
        data.query = r.query;
      }

      // recursive child routes
      if (r.children) {
        const tempRoutes = generateRoutes(r.children, data.path, data.title);
        if (tempRoutes.length >= 1) {
          res = [...res, ...tempRoutes];
        }
      }
    }
  });
  return res;
};
const querySearch = (query: string) => {
  if (query !== '') {
    options.value = fuse.value.search(query);
  } else {
    options.value = [];
  }
};

onMounted(() => {
  searchPool.value = generateRoutes(routes.value);
});

// watchEffect(() => {
//     searchPool.value = generateRoutes(routes.value)
// })

watch(show, (value) => {
  if (value) {
    document.body.addEventListener('click', close);
  } else {
    document.body.removeEventListener('click', close);
  }
});

watch(searchPool, (list: Router) => {
  initFuse(list);
});
</script>

<style lang="scss" scoped>
.header-search {
  font-size: 0 !important;

  .search-icon {
    cursor: pointer;
    font-size: 18px;
    vertical-align: middle;
  }

  .header-search-select {
    font-size: 18px;
    transition: width 0.2s;
    width: 0;
    overflow: hidden;
    background: transparent;
    border-radius: 0;
    display: inline-block;
    vertical-align: middle;

    :deep(.el-input__inner) {
      border-radius: 0;
      border: 0;
      padding-left: 0;
      padding-right: 0;
      box-shadow: none !important;
      border-bottom: 1px solid #d9d9d9;
      vertical-align: middle;
    }
  }

  &.show {
    .header-search-select {
      width: 210px;
      margin-left: 10px;
    }
  }
}
</style>
