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
  <div ref="propertyPanel">
    <div v-if="nodeName" class="node-name">{{ nodeName }}</div>
    <component :is="component" v-if="element" :element="element" />
  </div>
</template>
<script setup lang="ts" name="PropertyPanel">
import { NodeName } from '../assets/lang/zh';
import TaskPanel from './TaskPanel.vue';
import ProcessPanel from './ProcessPanel.vue';
import StartEndPanel from './StartEndPanel.vue';
import GatewayPanel from './GatewayPanel.vue';
import SequenceFlowPanel from './SequenceFlowPanel.vue';
import ParticipantPanel from './ParticipantPanel.vue';
import SubProcessPanel from './SubProcessPanel.vue';
import type { Modeler, ModdleElement } from 'bpmn';
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
interface propsType {
  modeler: Modeler;
}
const props = withDefaults(defineProps<propsType>(), {});

const element = ref<ModdleElement>();
const processElement = ref<ModdleElement>();

const startEndType = ['bpmn:IntermediateThrowEvent', 'bpmn:StartEvent', 'bpmn:EndEvent'];
const taskType = [
  'bpmn:UserTask',
  'bpmn:Task',
  'bpmn:SendTask',
  'bpmn:ReceiveTask',
  'bpmn:ManualTask',
  'bpmn:BusinessRuleTask',
  'bpmn:ServiceTask',
  'bpmn:ScriptTask'
];
const sequenceType = ['bpmn:SequenceFlow'];
const gatewayType = ['bpmn:InclusiveGateway', 'bpmn:ExclusiveGateway', 'bpmn:ParallelGateway', 'bpmn:EventBasedGateway', 'bpmn:ComplexGateway'];
const processType = ['bpmn:Process'];

// 组件计算
const component = computed(() => {
  if (!element.value) return null;
  const type = element.value.type;
  if (startEndType.includes(type)) return StartEndPanel;
  if (taskType.includes(type)) return TaskPanel;
  if (sequenceType.includes(type)) return SequenceFlowPanel;
  if (gatewayType.includes(type)) return GatewayPanel;
  if (processType.includes(type)) return ProcessPanel;
  if (type === 'bpmn:Participant') return ParticipantPanel;
  if (type === 'bpmn:SubProcess') return SubProcessPanel;
  //return proxy?.$modal.msgWarning('面板开发中....');
  return undefined;
});

const nodeName = computed(() => {
  if (element.value) {
    const bizObj = element.value.businessObject;
    const type = bizObj?.eventDefinitions && bizObj?.eventDefinitions.length > 0 ? bizObj.eventDefinitions[0].$type : bizObj.$type;
    return NodeName[type] || type;
  }
  return '';
});

const handleModeler = () => {
  props.modeler.on('root.added', (e: any) => {
    element.value = null;
    if (e.element.type === 'bpmn:Process') {
      nextTick(() => {
        element.value = e.element;
        processElement.value = e.element;
      });
    }
  });
  props.modeler.on('element.click', (e: any) => {
    if (e.element.type === 'bpmn:Process') {
      nextTick(() => {
        element.value = e.element;
        processElement.value = e.element;
      });
    }
  });
  props.modeler.on('selection.changed', (e: any) => {
    // 先给null为了让vue刷新
    element.value = null;
    const newElement = e.newSelection[0];
    if (newElement) {
      nextTick(() => {
        element.value = newElement;
      });
    } else {
      nextTick(() => {
        element.value = processElement.value;
      });
    }
  });
};

onMounted(() => {
  handleModeler();
});
</script>

<style scoped lang="scss">
.node-name {
  font-size: 16px;
  font-weight: bold;
  padding: 10px;
}
</style>
