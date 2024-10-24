<!--
  - Copyright (c) 2024 Leyramu. All rights reserved.
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>
  <div class="app-container">
    <h4 class="form-header h4">基本信息</h4>
    <el-form :model="form" label-width="80px">
      <el-row>
        <el-col :offset="2" :span="8">
          <el-form-item label="用户昵称" prop="nickName">
            <el-input v-model="form.nickName" disabled/>
          </el-form-item>
        </el-col>
        <el-col :offset="2" :span="8">
          <el-form-item label="登录账号" prop="userName">
            <el-input v-model="form.userName" disabled/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <h4 class="form-header h4">角色信息</h4>
    <el-table ref="roleRef" v-loading="loading" :data="roles.slice((pageNum - 1) * pageSize, pageNum * pageSize)" :row-key="getRowKey"
              @row-click="clickRow"
              @selection-change="handleSelectionChange">
      <el-table-column align="center" label="序号" type="index" width="55">
        <template #default="scope">
          <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column :reserve-selection="true" type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="角色编号" prop="roleId"/>
      <el-table-column align="center" label="角色名称" prop="roleName"/>
      <el-table-column align="center" label="权限字符" prop="roleKey"/>
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" v-model:limit="pageSize" v-model:page="pageNum" :total="total"/>

    <el-form label-width="100px">
      <div style="text-align: center;margin-left:-120px;margin-top:30px;">
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="close()">返回</el-button>
      </div>
    </el-form>
  </div>
</template>

<script name="AuthRole" setup>
import {getAuthRole, updateAuthRole} from "@/api/system/user";

const route = useRoute();
const {proxy} = getCurrentInstance();

const loading = ref(true);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);
const roleIds = ref([]);
const roles = ref([]);
const form = ref({
  nickName: undefined,
  userName: undefined,
  userId: undefined
});

/** 单击选中行数据 */
function clickRow(row) {
  proxy.$refs["roleRef"].toggleRowSelection(row);
};

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  roleIds.value = selection.map(item => item.roleId);
};

/** 保存选中的数据编号 */
function getRowKey(row) {
  return row.roleId;
};

/** 关闭按钮 */
function close() {
  const obj = {path: "/system/user"};
  proxy.$tab.closeOpenPage(obj);
};

/** 提交按钮 */
function submitForm() {
  const userId = form.value.userId;
  const rIds = roleIds.value.join(",");
  updateAuthRole({userId: userId, roleIds: rIds}).then(response => {
    proxy.$modal.msgSuccess("授权成功");
    close();
  });
};

(() => {
  const userId = route.params && route.params.userId;
  if (userId) {
    loading.value = true;
    getAuthRole(userId).then(response => {
      form.value = response.user;
      roles.value = response.roles;
      total.value = roles.value.length;
      nextTick(() => {
        roles.value.forEach(row => {
          if (row.flag) {
            proxy.$refs["roleRef"].toggleRowSelection(row);
          }
        });
      });
      loading.value = false;
    });
  }
})();
</script>
