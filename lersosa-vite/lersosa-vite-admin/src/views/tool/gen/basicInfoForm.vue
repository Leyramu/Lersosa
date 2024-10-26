<!--
  - Copyright (c) 2024 Leyramu. All rights reserved.
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>
  <el-form ref="basicInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item label="表名称" prop="tableName">
          <el-input v-model="info.tableName" placeholder="请输入仓库名称"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="表描述" prop="tableComment">
          <el-input v-model="info.tableComment" placeholder="请输入"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="实体类名称" prop="className">
          <el-input v-model="info.className" placeholder="请输入"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="作者" prop="functionAuthor">
          <el-input v-model="info.functionAuthor" placeholder="请输入"/>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="备注" prop="remark">
          <el-input v-model="info.remark" :rows="3" type="textarea"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
defineProps({
  info: {
    type: Object,
    default: null
  }
});

// 表单校验
const rules = ref({
  tableName: [{required: true, message: "请输入表名称", trigger: "blur"}],
  tableComment: [{required: true, message: "请输入表描述", trigger: "blur"}],
  className: [{required: true, message: "请输入实体类名称", trigger: "blur"}],
  functionAuthor: [{required: true, message: "请输入作者", trigger: "blur"}]
});
</script>
