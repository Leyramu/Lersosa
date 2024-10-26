<!--
  - Copyright (c) 2024 Leyramu. All rights reserved.
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>
  <el-image
      :preview-src-list="realSrcList"
      :src="`${realSrc}`"
      :style="`width:${realWidth};height:${realHeight};`"
      fit="cover"
      preview-teleported
  >
    <template #error>
      <div class="image-slot">
        <el-icon>
          <picture-filled/>
        </el-icon>
      </div>
    </template>
  </el-image>
</template>

<script setup>
const props = defineProps({
  src: {
    type: String,
    default: ""
  },
  width: {
    type: [Number, String],
    default: ""
  },
  height: {
    type: [Number, String],
    default: ""
  }
});

const realSrc = computed(() => {
  if (!props.src) {
    return;
  }
  let real_src = props.src.split(",")[0];
  return real_src;
});

const realSrcList = computed(() => {
  if (!props.src) {
    return;
  }
  let real_src_list = props.src.split(",");
  let srcList = [];
  real_src_list.forEach(item => {
    return srcList.push(item);
  });
  return srcList;
});

const realWidth = computed(() =>
    typeof props.width == "string" ? props.width : `${props.width}px`
);

const realHeight = computed(() =>
    typeof props.height == "string" ? props.height : `${props.height}px`
);
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
