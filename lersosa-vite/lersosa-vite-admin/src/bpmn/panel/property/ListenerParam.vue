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
  <vxe-toolbar>
    <template #buttons>
      <el-button icon="Plus" @click="insertRow">新增</el-button>
    </template>
  </vxe-toolbar>
  <vxe-table
    ref="tableRef"
    :height="height"
    border
    show-overflow
    keep-source
    :data="tableData"
    :edit-rules="tableRules"
    :edit-config="{ trigger: 'click', mode: 'row', showStatus: true }"
  >
    <vxe-column type="seq" width="40"></vxe-column>
    <vxe-column field="type" title="类型" :edit-render="{}">
      <template #default="slotParams">
        <span>{{ typeSelect.find((e) => e.value === slotParams.row.type)?.label }}</span>
      </template>
      <template #edit="slotParams">
        <vxe-select v-model="slotParams.row.type">
          <vxe-option v-for="item in typeSelect" :key="item.id" :value="item.value" :label="item.label"></vxe-option>
        </vxe-select>
      </template>
    </vxe-column>
    <vxe-column field="name" title="名称" :edit-render="{}">
      <template #edit="slotParams">
        <vxe-input v-model="slotParams.row.name" type="text"></vxe-input>
      </template>
    </vxe-column>
    <vxe-column field="value" title="值" :edit-render="{}">
      <template #edit="slotParams">
        <vxe-input v-model="slotParams.row.value" type="text"></vxe-input>
      </template>
    </vxe-column>
    <vxe-column title="操作" width="100" show-overflow align="center">
      <template #default="slotParams">
        <el-tooltip content="删除" placement="top">
          <el-button link type="danger" icon="Delete" @click="removeRow(slotParams.row)"></el-button>
        </el-tooltip>
      </template>
    </vxe-column>
  </vxe-table>
</template>

<script setup lang="ts">
import { VXETable, VxeTableInstance, VxeTablePropTypes } from 'vxe-table';
import type { ParamVO } from 'bpmnDesign';
import useDialog from '@/hooks/useDialog';

interface PropType {
  height?: string;
  tableData?: ParamVO[];
}

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const props = withDefaults(defineProps<PropType>(), {
  height: '200px',
  tableData: () => []
});

const tableRules = ref<VxeTablePropTypes.EditRules>({
  type: [{ required: true, message: '请选择', trigger: 'blur' }],
  name: [{ required: true, message: '请输入', trigger: 'blur' }],
  value: [{ required: true, message: '请输入', trigger: 'blur' }]
});

const { title, visible, openDialog, closeDialog } = useDialog({
  title: '监听器参数'
});
const typeSelect = [
  { id: '742fdeb7-23b4-416b-ac66-cd4ec8b901b7', label: '字符串', value: 'stringValue' },
  { id: '660c9c46-8fae-4bae-91a0-0335420019dc', label: '表达式', value: 'expression' }
];

const tableRef = ref<VxeTableInstance<ParamVO>>();

const getTableData = () => {
  const $table = tableRef.value;
  if ($table) {
    return $table.getTableData().fullData;
  }
  return [];
};

const insertRow = async () => {
  const $table = tableRef.value;
  if ($table) {
    const { row: newRow } = await $table.insertAt({}, -1);
    // 插入一条数据并触发校验
    await $table.validate(newRow);
  }
};

const removeRow = async (row: ParamVO) => {
  await proxy?.$modal.confirm('您确定要删除该数据?');
  const $table = tableRef.value;
  if ($table) {
    await $table.remove(row);
  }
};

const validate = async () => {
  const $table = tableRef.value;
  if ($table) {
    return await $table.validate(true);
  }
};

defineExpose({
  closeDialog,
  openDialog,
  validate,
  getTableData
});
</script>

<style scoped lang="scss"></style>
