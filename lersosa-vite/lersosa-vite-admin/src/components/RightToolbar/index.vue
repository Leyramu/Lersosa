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
  <div class="top-right-btn" :style="style">
    <el-row>
      <el-tooltip v-if="search" class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
        <el-button circle icon="Search" @click="toggleSearch()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="刷新" placement="top">
        <el-button circle icon="Refresh" @click="refresh()" />
      </el-tooltip>
      <el-tooltip v-if="columns" class="item" effect="dark" content="显示/隐藏列" placement="top">
        <div class="show-btn">
          <el-popover placement="bottom" trigger="click">
            <div class="tree-header">显示/隐藏列</div>
            <el-tree
              ref="columnRef"
              :data="columns"
              show-checkbox
              node-key="key"
              :props="{ label: 'label', children: 'children' }"
              @check="columnChange"
            ></el-tree>
            <template #reference>
              <el-button circle icon="Menu" />
            </template>
          </el-popover>
        </div>
      </el-tooltip>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { propTypes } from '@/utils/propTypes';

const props = defineProps({
  showSearch: propTypes.bool.def(true),
  columns: propTypes.fieldOption,
  search: propTypes.bool.def(true),
  gutter: propTypes.number.def(10)
});

const columnRef = ref<ElTreeInstance>();
const emits = defineEmits(['update:showSearch', 'queryTable']);

const style = computed(() => {
  const ret: any = {};
  if (props.gutter) {
    ret.marginRight = `${props.gutter / 2}px`;
  }
  return ret;
});

// 搜索
function toggleSearch() {
  emits('update:showSearch', !props.showSearch);
}

// 刷新
function refresh() {
  emits('queryTable');
}

// 更改数据列的显示和隐藏
function columnChange(...args: any[]) {
  props.columns?.forEach((item) => {
    item.visible = args[1].checkedKeys.includes(item.key);
  });
}

// 显隐列初始默认隐藏列
onMounted(() => {
  props.columns?.forEach((item) => {
    if (item.visible) {
      columnRef.value?.setChecked(item.key, true, false);
      // value.value.push(item.key);
    }
  });
});
</script>

<style lang="scss" scoped>
:deep(.el-transfer__button) {
  border-radius: 50%;
  display: block;
  margin-left: 0;
}
:deep(.el-transfer__button:first-child) {
  margin-bottom: 10px;
}

.my-el-transfer {
  text-align: center;
}
.tree-header {
  width: 100%;
  line-height: 24px;
  text-align: center;
}
.show-btn {
  margin-left: 12px;
}
</style>
