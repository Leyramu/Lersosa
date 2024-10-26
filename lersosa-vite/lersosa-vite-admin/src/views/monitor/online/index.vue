<!--
  - Copyright (c) 2024 Leyramu. All rights reserved.
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>
  <div class="app-container">
    <el-form ref="queryRef" :inline="true" :model="queryParams">
      <el-form-item label="登录地址" prop="ipaddr">
        <el-input
            v-model="queryParams.ipaddr"
            clearable
            placeholder="请输入登录地址"
            style="width: 200px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input
            v-model="queryParams.userName"
            clearable
            placeholder="请输入用户名称"
            style="width: 200px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table
        v-loading="loading"
        :data="onlineList.slice((pageNum - 1) * pageSize, pageNum * pageSize)"
        style="width: 100%;"
    >
      <el-table-column align="center" label="序号" type="index" width="50">
        <template #default="scope">
          <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" align="center" label="会话编号" prop="tokenId"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="登录名称" prop="userName"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="主机" prop="ipaddr"/>
      <el-table-column align="center" label="登录时间" prop="loginTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template #default="scope">
          <el-button v-hasPermi="['monitor:online:forceLogout']" icon="Delete" link type="primary"
                     @click="handleForceLogout(scope.row)">强退
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" v-model:limit="pageSize" v-model:page="pageNum" :total="total"/>
  </div>
</template>

<script name="Online" setup>
import {forceLogout, list as initData} from "@/api/monitor/online";

const {proxy} = getCurrentInstance();

const onlineList = ref([]);
const loading = ref(true);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);

const queryParams = ref({
  ipaddr: undefined,
  userName: undefined
});

/** 查询登录日志列表 */
function getList() {
  loading.value = true;
  initData(queryParams.value).then(response => {
    onlineList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  pageNum.value = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 强退按钮操作 */
function handleForceLogout(row) {
  proxy.$modal.confirm('是否确认强退名称为"' + row.userName + '"的用户?').then(function () {
    return forceLogout(row.tokenId);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

getList();
</script>
