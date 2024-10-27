<!--
  - Copyright (c) 2024 Leyramu. All rights reserved.
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>
  <div :style="style" class="top-right-btn">
    <el-row>
      <el-tooltip v-if="search" :content="showSearch ? '隐藏搜索' : '显示搜索'" class="item" effect="dark"
                  placement="top">
        <el-button circle icon="Search" @click="toggleSearch()"/>
      </el-tooltip>
      <el-tooltip class="item" content="刷新" effect="dark" placement="top">
        <el-button circle icon="Refresh" @click="refresh()"/>
      </el-tooltip>
      <el-tooltip v-if="columns" class="item" content="显隐列" effect="dark" placement="top">
        <el-button v-if="showColumnsType === 'transfer'" circle icon="Menu" @click="showColumn()"/>
        <el-dropdown v-if="showColumnsType === 'checkbox'" :hide-on-click="false" style="padding-left: 12px"
                     trigger="click">
          <el-button circle icon="Menu"/>
          <template #dropdown>
            <el-dropdown-menu>
              <template v-for="item in columns" :key="item.key">
                <el-dropdown-item>
                  <el-checkbox :checked="item.visible" :label="item.label"
                               @change="checkboxChange($event, item.label)"/>
                </el-dropdown-item>
              </template>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-tooltip>
    </el-row>
    <el-dialog v-model="open" :title="title" append-to-body>
      <el-transfer
          v-model="value"
          :data="columns"
          :titles="['显示', '隐藏']"
          @change="dataChange"
      ></el-transfer>
    </el-dialog>
  </div>
</template>

<script setup>
const props = defineProps({
  /* 是否显示检索条件 */
  showSearch: {
    type: Boolean,
    default: true,
  },
  /* 显隐列信息 */
  columns: {
    type: Array,
  },
  /* 是否显示检索图标 */
  search: {
    type: Boolean,
    default: true,
  },
  /* 显隐列类型（transfer穿梭框、checkbox复选框） */
  showColumnsType: {
    type: String,
    default: "checkbox",
  },
  /* 右外边距 */
  gutter: {
    type: Number,
    default: 10,
  },
})

const emits = defineEmits(['update:showSearch', 'queryTable']);

// 显隐数据
const value = ref([]);
// 弹出层标题
const title = ref("显示/隐藏");
// 是否显示弹出层
const open = ref(false);

const style = computed(() => {
  const ret = {};
  if (props.gutter) {
    ret.marginRight = `${props.gutter / 2}px`;
  }
  return ret;
});

// 搜索
function toggleSearch() {
  emits("update:showSearch", !props.showSearch);
}

// 刷新
function refresh() {
  emits("queryTable");
}

// 右侧列表元素变化
function dataChange(data) {
  for (let item in props.columns) {
    const key = props.columns[item].key;
    props.columns[item].visible = !data.includes(key);
  }
}

// 打开显隐列dialog
function showColumn() {
  open.value = true;
}

if (props.showColumnsType === 'transfer') {
  // 显隐列初始默认隐藏列
  for (let item in props.columns) {
    if (props.columns[item].visible === false) {
      value.value.push(parseInt(item));
    }
  }
}

// 勾选
function checkboxChange(event, label) {
  props.columns.filter(item => item.label === label)[0].visible = event;
}

</script>

<style lang='scss' scoped>
:deep(.el-transfer__button) {
  border-radius: 50%;
  display: block;
  margin-left: 0;
}

:deep(.el-transfer__button:first-child) {
  margin-bottom: 10px;
}

:deep(.el-dropdown-menu__item) {
  line-height: 30px;
  padding: 0 17px;
}
</style>
