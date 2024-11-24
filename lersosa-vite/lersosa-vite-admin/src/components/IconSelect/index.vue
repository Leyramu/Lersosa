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
  <div class="relative" :style="{ 'width': width }">
    <el-input v-model="modelValue" readonly placeholder="点击选择图标" @click="visible = !visible">
      <template #prepend>
        <svg-icon :icon-class="modelValue" />
      </template>
    </el-input>

    <el-popover shadow="none" :visible="visible" placement="bottom-end" trigger="click" :width="450">
      <template #reference>
        <div class="cursor-pointer text-[#999] absolute right-[10px] top-0 height-[32px] leading-[32px]" @click="visible = !visible">
          <i-ep-caret-top v-show="visible"></i-ep-caret-top>
          <i-ep-caret-bottom v-show="!visible"></i-ep-caret-bottom>
        </div>
      </template>

      <el-input v-model="filterValue" class="p-2" placeholder="搜索图标" clearable @input="filterIcons" />

      <el-scrollbar height="w-[200px]">
        <ul class="icon-list">
          <el-tooltip v-for="(iconName, index) in iconNames" :key="index" :content="iconName" placement="bottom" effect="light">
            <li :class="['icon-item', { active: modelValue == iconName }]" @click="selectedIcon(iconName)">
              <svg-icon color="var(--el-text-color-regular)" :icon-class="iconName" />
            </li>
          </el-tooltip>
        </ul>
      </el-scrollbar>
    </el-popover>
  </div>
</template>

<script setup lang="ts">
import icons from '@/components/IconSelect/requireIcons';
import { propTypes } from '@/utils/propTypes';

const props = defineProps({
  modelValue: propTypes.string.isRequired,
  width: propTypes.string.def('400px')
});

const emit = defineEmits(['update:modelValue']);
const visible = ref(false);
const { modelValue, width } = toRefs(props);
const iconNames = ref<string[]>(icons);

const filterValue = ref('');

/**
 * 筛选图标
 */
const filterIcons = () => {
  if (filterValue.value) {
    iconNames.value = icons.filter((iconName) => iconName.includes(filterValue.value));
  } else {
    iconNames.value = icons;
  }
};
/**
 * 选择图标
 * @param iconName 选择的图标名称
 */
const selectedIcon = (iconName: string) => {
  emit('update:modelValue', iconName);
  visible.value = false;
};
</script>

<style scoped lang="scss">
.el-scrollbar {
  max-height: calc(50vh - 100px) !important;
  overflow-y: auto;
}
.el-divider--horizontal {
  margin: 10px auto !important;
}
.icon-list {
  display: flex;
  flex-wrap: wrap;
  padding-left: 10px;
  margin-top: 10px;

  .icon-item {
    cursor: pointer;
    width: 10%;
    margin: 0 10px 10px 0;
    padding: 5px;
    display: flex;
    flex-direction: column;
    justify-items: center;
    align-items: center;
    border: 1px solid #ccc;
    &:hover {
      border-color: var(--el-color-primary);
      color: var(--el-color-primary);
      transition: all 0.2s;
      transform: scaleX(1.1);
    }
  }
  .active {
    border-color: var(--el-color-primary);
    color: var(--el-color-primary);
  }
}
</style>
