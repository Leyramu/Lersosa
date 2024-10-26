<!--
  - Copyright (c) 2024 Leyramu. All rights reserved.
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
        <span
            v-if="(item.elTagType == 'default' || item.elTagType == '') && (item.elTagClass == '' || item.elTagClass == null)"
            :key="item.value"
            :class="item.elTagClass"
            :index="index"
        >{{ item.label + " " }}</span>
        <el-tag
            v-else
            :key="item.value + ''"
            :class="item.elTagClass"
            :disable-transitions="true"
            :index="index"
            :type="item.elTagType === 'primary' ? '' : item.elTagType"
        >{{ item.label + " " }}
        </el-tag>
      </template>
    </template>
    <template v-if="unmatch && showValue">
      {{ unmatchArray | handleArray }}
    </template>
  </div>
</template>

<script setup>
// 记录未匹配的项
const unmatchArray = ref([]);

const props = defineProps({
  // 数据
  options: {
    type: Array,
    default: null,
  },
  // 当前的值
  value: [Number, String, Array],
  // 当未找到匹配的数据时，显示value
  showValue: {
    type: Boolean,
    default: true,
  },
  separator: {
    type: String,
    default: ",",
  }
});

const values = computed(() => {
  if (props.value === null || typeof props.value === 'undefined' || props.value === '') return [];
  return Array.isArray(props.value) ? props.value.map(item => '' + item) : String(props.value).split(props.separator);
});

const unmatch = computed(() => {
  unmatchArray.value = [];
  // 没有value不显示
  if (props.value === null || typeof props.value === 'undefined' || props.value === '' || props.options.length === 0) return false
  // 传入值为数组
  let unmatch = false // 添加一个标志来判断是否有未匹配项
  values.value.forEach(item => {
    if (!props.options.some(v => v.value === item)) {
      unmatchArray.value.push(item)
      unmatch = true // 如果有未匹配项，将标志设置为true
    }
  })
  return unmatch // 返回标志的值
});

function handleArray(array) {
  if (array.length === 0) return "";
  return array.reduce((pre, cur) => {
    return pre + " " + cur;
  });
}
</script>

<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
