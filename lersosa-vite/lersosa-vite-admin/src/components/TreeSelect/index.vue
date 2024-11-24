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
  <div class="el-tree-select">
    <el-select
      ref="treeSelect"
      v-model="valueId"
      style="width: 100%"
      :filterable="true"
      :clearable="true"
      :filter-method="selectFilterData"
      :placeholder="placeholder"
      @clear="clearHandle"
    >
      <el-option :value="valueId" :label="valueTitle">
        <el-tree
          id="tree-option"
          ref="selectTree"
          :accordion="accordion"
          :data="options"
          :props="objMap"
          :node-key="objMap.value"
          :expand-on-click-node="false"
          :default-expanded-keys="defaultExpandedKey"
          :filter-node-method="filterNode"
          @node-click="handleNodeClick"
        ></el-tree>
      </el-option>
    </el-select>
  </div>
</template>

<script setup lang="ts">
interface ObjMap {
  value: string;
  label: string;
  children: string;
}

interface Props {
  objMap: ObjMap;
  accordion: boolean;
  value: string | number;
  options: any[];
  placeholder: string;
}

const props = withDefaults(defineProps<Props>(), {
  objMap: () => {
    return {
      value: 'id',
      label: 'label',
      children: 'children'
    };
  },
  accordion: false,
  value: '',
  options: () => [],
  placeholder: ''
});

const selectTree = ref<ElTreeSelectInstance>();

const emit = defineEmits(['update:value']);

const valueId = computed({
  get: () => props.value,
  set: (val) => {
    emit('update:value', val);
  }
});
const valueTitle = ref('');
const defaultExpandedKey = ref<any[]>([]);

const initHandle = () => {
  nextTick(() => {
    const selectedValue = valueId.value;
    if (selectedValue !== null && typeof selectedValue !== 'undefined') {
      const node = selectTree.value?.getNode(selectedValue);
      if (node) {
        valueTitle.value = node.data[props.objMap.label];
        selectTree.value?.setCurrentKey(selectedValue); // 设置默认选中
        defaultExpandedKey.value = [selectedValue]; // 设置默认展开
      }
    } else {
      clearHandle();
    }
  });
};
const handleNodeClick = (node: any) => {
  valueTitle.value = node[props.objMap.label];
  valueId.value = node[props.objMap.value];
  defaultExpandedKey.value = [];
  selectTree.value?.blur();
  selectFilterData('');
};
const selectFilterData = (val: any) => {
  selectTree.value?.filter(val);
};
const filterNode = (value: any, data: any) => {
  if (!value) return true;
  return data[props.objMap['label']].indexOf(value) !== -1;
};
const clearHandle = () => {
  valueTitle.value = '';
  valueId.value = '';
  defaultExpandedKey.value = [];
  clearSelected();
};
const clearSelected = () => {
  const allNode = document.querySelectorAll('#tree-option .el-tree-node');
  allNode.forEach((element) => element.classList.remove('is-current'));
};

onMounted(() => {
  initHandle();
});

watch(valueId, () => {
  initHandle();
});
</script>

<style lang="scss" scoped>
@use '@/assets/styles/variables.module.scss' as *;

.el-scrollbar .el-scrollbar__view .el-select-dropdown__item {
  padding: 0;
  background-color: #fff;
  height: auto;
}

.el-select-dropdown__item.selected {
  font-weight: normal;
}

ul li .el-tree .el-tree-node__content {
  height: auto;
  padding: 0 20px;
  box-sizing: border-box;
}

:deep(.el-tree-node__content:hover),
:deep(.el-tree-node__content:active),
:deep(.is-current > div:first-child),
:deep(.el-tree-node__content:focus) {
  background-color: mix(#fff, $--color-primary, 90%);
  color: $--color-primary;
}
</style>
