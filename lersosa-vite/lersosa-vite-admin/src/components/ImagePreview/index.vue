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
  <el-image :src="`${realSrc}`" fit="cover" :style="`width:${realWidth};height:${realHeight};`" :preview-src-list="realSrcList" preview-teleported>
    <template #error>
      <div class="image-slot">
        <el-icon><picture-filled /></el-icon>
      </div>
    </template>
  </el-image>
</template>

<script setup lang="ts">
import { propTypes } from '@/utils/propTypes';

const props = defineProps({
  src: propTypes.string.def(''),
  width: {
    type: [Number, String],
    default: ''
  },
  height: {
    type: [Number, String],
    default: ''
  }
});

const realSrc = computed(() => {
  if (!props.src) {
    return;
  }
  return props.src.split(',')[0];
});

const realSrcList = computed(() => {
  if (!props.src) {
    return [];
  }
  let real_src_list = props.src.split(',');
  let srcList: string[] = [];
  real_src_list.forEach((item: string) => {
    if (item.trim() === '') {
      return;
    }
    return srcList.push(item);
  });
  return srcList;
});

const realWidth = computed(() => (typeof props.width == 'string' ? props.width : `${props.width}px`));

const realHeight = computed(() => (typeof props.height == 'string' ? props.height : `${props.height}px`));
</script>

<style lang="scss" scoped>
.el-image {
  border-radius: 5px;
  background-color: #ebeef5;
  box-shadow: 0 0 5px 1px #ccc;

  :deep(.el-image__inner) {
    transition: all 0.3s;
    cursor: pointer;

    &:hover {
      transform: scale(1.2);
    }
  }

  :deep(.image-slot) {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    color: #909399;
    font-size: 30px;
  }
}
</style>
