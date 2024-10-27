<!--
  - Copyright (c) 2024 Leyramu. All rights reserved.
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>
  <div class="app-container">
    <el-form
        v-show="showSearch"
        ref="queryRef"
        :inline="true"
        :model="queryParams">
      <el-form-item
          label="菜单名称"
          prop="menuName">
        <el-input
            v-model="queryParams.menuName"
            clearable
            placeholder="请输入菜单名称"
            style="width: 200px"
            @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item
          label="状态"
          prop="status">
        <el-select
            v-model="queryParams.status"
            clearable
            placeholder="菜单状态"
            style="width: 200px">
          <el-option
              v-for="dict in sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
            icon="Search"
            type="primary"
            @click="handleQuery">搜索
        </el-button>
        <el-button
            icon="Refresh"
            @click="resetQuery">重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row
        :gutter="10"
        class="mb8">
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['system:menu:add']"
            icon="Plus"
            plain
            type="primary"
            @click="handleAdd">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            icon="Sort"
            plain
            type="info"
            @click="toggleExpandAll">展开/折叠
        </el-button>
      </el-col>
      <right-toolbar
          v-model:showSearch="showSearch"
          @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="menuList"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        row-key="menuId">
      <el-table-column
          :show-overflow-tooltip="true"
          label="菜单名称"
          prop="menuName"
          width="160"></el-table-column>
      <el-table-column
          align="center"
          label="图标"
          prop="icon"
          width="100">
        <template #default="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column
          label="排序"
          prop="orderNum"
          width="60"></el-table-column>
      <el-table-column
          :show-overflow-tooltip="true"
          label="权限标识"
          prop="perms"></el-table-column>
      <el-table-column
          :show-overflow-tooltip="true"
          label="组件路径"
          prop="component"></el-table-column>
      <el-table-column
          label="状态"
          prop="status"
          width="80">
        <template #default="scope">
          <dict-tag
              :options="sys_normal_disable"
              :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          label="创建时间"
          prop="createTime"
          width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
          align="center"
          class-name="small-padding fixed-width"
          label="操作"
          width="210">
        <template #default="scope">
          <el-button
              v-hasPermi="['system:menu:edit']"
              icon="Edit"
              link
              type="primary"
              @click="handleUpdate(scope.row)">修改
          </el-button>
          <el-button
              v-hasPermi="['system:menu:add']"
              icon="Plus"
              link
              type="primary"
              @click="handleAdd(scope.row)">新增
          </el-button>
          <el-button
              v-hasPermi="['system:menu:remove']"
              icon="Delete"
              link
              type="primary"
              @click="handleDelete(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog
        v-model="open"
        :title="title"
        append-to-body
        width="680px">
      <el-form
          ref="menuRef"
          :model="form"
          :rules="rules"
          label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <el-tree-select
                  v-model="form.parentId"
                  :data="menuOptions"
                  :props="{ value: 'menuId', label: 'menuName', children: 'children' }"
                  check-strictly
                  placeholder="选择上级菜单"
                  value-key="menuId" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
                label="菜单类型"
                prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col
              v-if="form.menuType !== 'F'"
              :span="24">
            <el-form-item
                label="菜单图标"
                prop="icon">
              <el-popover
                  :width="540"
                  placement="bottom-start"
                  trigger="click">
                <template #reference>
                  <el-input
                      v-model="form.icon"
                      placeholder="点击选择图标"
                      readonly
                      @blur="showSelectIcon">
                    <template #prefix>
                      <svg-icon
                          v-if="form.icon"
                          :icon-class="form.icon"
                          class="el-input__icon"
                          style="height: 32px;width: 16px;" />
                      <el-icon
                          v-else
                          style="height: 32px;width: 16px;">
                        <search />
                      </el-icon>
                    </template>
                  </el-input>
                </template>
                <icon-select
                    ref="iconSelectRef"
                    :active-icon="form.icon"
                    @selected="selected" />
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                label="菜单名称"
                prop="menuName">
              <el-input
                  v-model="form.menuName"
                  placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
                label="显示排序"
                prop="orderNum">
              <el-input-number
                  v-model="form.orderNum"
                  :min="0"
                  controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col
              v-if="form.menuType !== 'F'"
              :span="12">
            <el-form-item>
              <template #label>
                <span>
                  <el-tooltip
                      content="选择是外链则路由地址需要以`http(s)://`开头"
                      placement="top">
                    <el-icon>
                      <question-filled />
                    </el-icon>
                  </el-tooltip>
                  是否外链
                </span>
              </template>
              <el-radio-group v-model="form.isFrame">
                <el-radio label="0">是</el-radio>
                <el-radio label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col
              v-if="form.menuType !== 'F'"
              :span="12">
            <el-form-item prop="path">
              <template #label>
                <span>
                  <el-tooltip
                      content="访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头"
                      placement="top">
                    <el-icon>
                      <question-filled />
                    </el-icon>
                  </el-tooltip>
                  路由地址
                </span>
              </template>
              <el-input
                  v-model="form.path"
                  placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>
          <el-col
              v-if="form.menuType === 'C'"
              :span="12">
            <el-form-item prop="component">
              <template #label>
                <span>
                  <el-tooltip
                      content="访问的组件路径，如：`system/user/index`，默认在`views`目录下"
                      placement="top">
                    <el-icon>
                      <question-filled />
                    </el-icon>
                  </el-tooltip>
                  组件路径
                </span>
              </template>
              <el-input
                  v-model="form.component"
                  placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col
              v-if="form.menuType !== 'M'"
              :span="12">
            <el-form-item>
              <el-input
                  v-model="form.perms"
                  maxlength="100"
                  placeholder="请输入权限标识" />
              <template #label>
                <span>
                  <el-tooltip
                      content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)"
                      placement="top">
                    <el-icon>
                      <question-filled />
                    </el-icon>
                  </el-tooltip>
                  权限字符
                </span>
              </template>
            </el-form-item>
          </el-col>
          <el-col
              v-if="form.menuType === 'C'"
              :span="12">
            <el-form-item>
              <el-input
                  v-model="form.query"
                  maxlength="255"
                  placeholder="请输入路由参数" />
              <template #label>
                <span>
                  <el-tooltip
                      content='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`'
                      placement="top">
                    <el-icon>
                      <question-filled />
                    </el-icon>
                  </el-tooltip>
                  路由参数
                </span>
              </template>
            </el-form-item>
          </el-col>
          <el-col
              v-if="form.menuType === 'C'"
              :span="12">
            <el-form-item>
              <template #label>
                <span>
                  <el-tooltip
                      content="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致"
                      placement="top">
                    <el-icon>
                      <question-filled />
                    </el-icon>
                  </el-tooltip>
                  是否缓存
                </span>
              </template>
              <el-radio-group v-model="form.isCache">
                <el-radio label="0">缓存</el-radio>
                <el-radio label="1">不缓存</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col
              v-if="form.menuType !== 'F'"
              :span="12">
            <el-form-item>
              <template #label>
                <span>
                  <el-tooltip
                      content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问"
                      placement="top">
                    <el-icon>
                      <question-filled />
                    </el-icon>
                  </el-tooltip>
                  显示状态
                </span>
              </template>
              <el-radio-group v-model="form.visible">
                <el-radio
                    v-for="dict in sys_show_hide"
                    :key="dict.value"
                    :label="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item>
              <template #label>
                <span>
                  <el-tooltip
                      content="选择停用则路由将不会出现在侧边栏，也不能被访问"
                      placement="top">
                    <el-icon>
                      <question-filled />
                    </el-icon>
                  </el-tooltip>
                  菜单状态
                </span>
              </template>
              <el-radio-group v-model="form.status">
                <el-radio
                    v-for="dict in sys_normal_disable"
                    :key="dict.value"
                    :label="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button
              type="primary"
              @click="submitForm">确 定
          </el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script
    name="Menu"
    setup>
  import {addMenu, delMenu, getMenu, listMenu, updateMenu} from "@/api/system/menu";
  import SvgIcon from "@/components/SvgIcon";
  import IconSelect from "@/components/IconSelect";

  const {proxy} = getCurrentInstance();
  const {sys_show_hide, sys_normal_disable} = proxy.useDict("sys_show_hide", "sys_normal_disable");

  const menuList = ref([]);
  const open = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const title = ref("");
  const menuOptions = ref([]);
  const isExpandAll = ref(false);
  const refreshTable = ref(true);
  const iconSelectRef = ref(null);

  const data = reactive({
    form: {},
    queryParams: {
      menuName: undefined,
      visible: undefined
    },
    rules: {
      menuName: [{required: true, message: "菜单名称不能为空", trigger: "blur"}],
      orderNum: [{required: true, message: "菜单顺序不能为空", trigger: "blur"}],
      path: [{required: true, message: "路由地址不能为空", trigger: "blur"}]
    },
  });

  const {queryParams, form, rules} = toRefs(data);

  /** 查询菜单列表 */
  function getList() {
    loading.value = true;
    listMenu(queryParams.value).then(response => {
      menuList.value = proxy.handleTree(response.data, "menuId");
      loading.value = false;
    });
  }

  /** 查询菜单下拉树结构 */
  function getTreeselect() {
    menuOptions.value = [];
    listMenu(undefined).then(response => {
      const menu = {menuId: 0, menuName: "主类目", children: []};
      menu.children = proxy.handleTree(response.data, "menuId");
      menuOptions.value.push(menu);
    });
  }

  /** 取消按钮 */
  function cancel() {
    open.value = false;
    reset();
  }

  /** 表单重置 */
  function reset() {
    form.value = {
      menuId: undefined,
      parentId: 0,
      menuName: undefined,
      icon: undefined,
      menuType: "M",
      orderNum: undefined,
      isFrame: "1",
      isCache: "0",
      visible: "0",
      status: "0"
    };
    proxy.resetForm("menuRef");
  }

  /** 展示下拉图标 */
  function showSelectIcon() {
    iconSelectRef.value.reset();
  }

  /** 选择图标 */
  function selected(name) {
    form.value.icon = name;
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
  }

  /** 新增按钮操作 */
  function handleAdd(row) {
    reset();
    getTreeselect();
    if (row != null && row.menuId) {
      form.value.parentId = row.menuId;
    } else {
      form.value.parentId = 0;
    }
    open.value = true;
    title.value = "添加菜单";
  }

  /** 展开/折叠操作 */
  function toggleExpandAll() {
    refreshTable.value = false;
    isExpandAll.value = !isExpandAll.value;
    nextTick(() => {
      refreshTable.value = true;
    });
  }

  /** 修改按钮操作 */
  async function handleUpdate(row) {
    reset();
    await getTreeselect();
    getMenu(row.menuId).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改菜单";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["menuRef"].validate(valid => {
      if (valid) {
        if (form.value.menuId !== undefined) {
          updateMenu(form.value).then(_response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          });
        } else {
          addMenu(form.value).then(_response => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          });
        }
      }
    });
  }

  /** 删除按钮操作 */
  function handleDelete(row) {
    proxy.$modal.confirm('是否确认删除名称为"' + row.menuName + '"的数据项?').then(function () {
      return delMenu(row.menuId);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {
    });
  }

  getList();
</script>
