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
    <el-collapse v-model="currentCollapseItem">
      <el-collapse-item name="1">
        <template #title>
          <div class="collapse__title">
            <el-icon>
              <InfoFilled />
            </el-icon>
            常规
          </div>
        </template>
        <div>
          <el-form ref="formRef" :model="formData" :rules="formRules" label-width="90px">
            <el-form-item prop="id" label="节点 ID">
              <el-input v-model="formData.id" @change="idChange"> </el-input>
            </el-form-item>
            <el-form-item prop="name" label="节点名称">
              <el-input v-model="formData.name" @change="nameChange"> </el-input>
            </el-form-item>
            <el-form-item prop="conditionExpression" label="跳转条件">
              <el-input v-model="formData.conditionExpressionValue" @change="conditionExpressionChange"> </el-input>
            </el-form-item>
            <el-form-item prop="skipExpression" label="跳过表达式">
              <el-input v-model="formData.skipExpression" @change="skipExpressionChange"> </el-input>
            </el-form-item>
          </el-form>
        </div>
      </el-collapse-item>

      <el-collapse-item name="2">
        <template #title>
          <div class="collapse__title">
            <el-icon>
              <BellFilled />
            </el-icon>
            执行监听器
          </div>
        </template>
        <div>
          <ExecutionListener :element="element"></ExecutionListener>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script setup lang="ts">
import useParseElement from '../hooks/useParseElement';
import useModelerStore from '@/store/modules/modeler';
import usePanel from '../hooks/usePanel';
import ExecutionListener from './property/ExecutionListener.vue';
import type { Modeler, ModdleElement } from 'bpmn';
import type { SequenceFlowPanel } from 'bpmnDesign';

interface PropType {
  element: ModdleElement;
}
const props = withDefaults(defineProps<PropType>(), {});
const { nameChange, idChange, updateProperties } = usePanel({
  element: toRaw(props.element)
});
const { parseData } = useParseElement({
  element: toRaw(props.element)
});
const moddle = useModelerStore().getModdle();
const currentCollapseItem = ref(['1', '2']);
const formData = ref(parseData<SequenceFlowPanel>());

const formRules = ref<ElFormRules>({
  processCategory: [{ required: true, message: '请选择', trigger: 'blur' }],
  id: [{ required: true, message: '请输入', trigger: 'blur' }],
  name: [{ required: true, message: '请输入', trigger: 'blur' }]
});

const conditionExpressionChange = (val: string) => {
  if (val) {
    const newCondition = moddle.create('bpmn:FormalExpression', { body: val });
    updateProperties({ conditionExpression: newCondition });
  } else {
    updateProperties({ conditionExpression: null });
  }
};

const skipExpressionChange = (val: string) => {
  updateProperties({ 'flowable:skipExpression': val });
};

onBeforeMount(() => {
  if (formData.value.conditionExpression) {
    formData.value.conditionExpressionValue = formData.value.conditionExpression.body;
  }
});
</script>

<style lang="scss" scoped></style>
