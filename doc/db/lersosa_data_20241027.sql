/*
    #  Copyright (c) 2024 Leyramu. All rights reserved.
    #  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
    #  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
    #  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
    #  By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2024-10-22 08:43:22', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2024-10-22 08:43:22', 'admin', '2024-10-24 17:25:14', '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2024-10-22 08:43:22', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', '2024-10-22 08:43:22', 'admin', '2024-10-24 17:26:05', '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2024-10-22 08:43:22', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', 'Lersosa 科技', 0, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, 'Lersosa', '15888888888', 'leyramu@miraitowa.eu.org', '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2024-10-22 08:43:22', 'admin', '2024-10-27 14:20:54', '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2024-10-22 08:43:22', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'lersosaTask.lersosaNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2024-10-22 08:43:22', 'admin', '2024-10-23 18:53:34', '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'lersosaTask.lersosaParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2024-10-22 08:43:22', 'admin', '2024-10-23 18:53:42', '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'lersosaTask.lersosaMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2024-10-22 08:43:22', 'admin', '2024-10-23 18:53:48', '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
INSERT INTO `sys_job_log` VALUES (1, '系统默认（无参）', 'DEFAULT', 'lersosaTask.lersosaNoParams', '系统默认（无参） 总共耗时：1毫秒', '0', '', '2024-10-27 17:38:42');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示信息',
  `access_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`access_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (1, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:22:12');
INSERT INTO `sys_logininfor` VALUES (2, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:22:33');
INSERT INTO `sys_logininfor` VALUES (3, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:22:54');
INSERT INTO `sys_logininfor` VALUES (4, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:25:32');
INSERT INTO `sys_logininfor` VALUES (5, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:25:41');
INSERT INTO `sys_logininfor` VALUES (6, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 16:25:49');
INSERT INTO `sys_logininfor` VALUES (7, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:25:51');
INSERT INTO `sys_logininfor` VALUES (8, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:28:20');
INSERT INTO `sys_logininfor` VALUES (9, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:28:29');
INSERT INTO `sys_logininfor` VALUES (10, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:34:20');
INSERT INTO `sys_logininfor` VALUES (11, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:34:46');
INSERT INTO `sys_logininfor` VALUES (12, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:38:47');
INSERT INTO `sys_logininfor` VALUES (13, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:40:41');
INSERT INTO `sys_logininfor` VALUES (14, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:45:04');
INSERT INTO `sys_logininfor` VALUES (15, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 16:59:49');
INSERT INTO `sys_logininfor` VALUES (16, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 17:16:05');
INSERT INTO `sys_logininfor` VALUES (17, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 17:38:22');
INSERT INTO `sys_logininfor` VALUES (18, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 17:41:19');
INSERT INTO `sys_logininfor` VALUES (19, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 17:41:23');
INSERT INTO `sys_logininfor` VALUES (20, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 17:41:35');
INSERT INTO `sys_logininfor` VALUES (21, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 17:41:39');
INSERT INTO `sys_logininfor` VALUES (22, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 17:47:29');
INSERT INTO `sys_logininfor` VALUES (23, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 17:49:28');
INSERT INTO `sys_logininfor` VALUES (24, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 17:55:11');
INSERT INTO `sys_logininfor` VALUES (25, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 18:02:49');
INSERT INTO `sys_logininfor` VALUES (26, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 18:04:54');
INSERT INTO `sys_logininfor` VALUES (27, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 18:18:12');
INSERT INTO `sys_logininfor` VALUES (28, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 18:18:19');
INSERT INTO `sys_logininfor` VALUES (29, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 18:19:51');
INSERT INTO `sys_logininfor` VALUES (30, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 18:20:08');
INSERT INTO `sys_logininfor` VALUES (31, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 19:47:38');
INSERT INTO `sys_logininfor` VALUES (32, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 20:32:46');
INSERT INTO `sys_logininfor` VALUES (33, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 20:37:36');
INSERT INTO `sys_logininfor` VALUES (34, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 21:22:41');
INSERT INTO `sys_logininfor` VALUES (35, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 21:46:53');
INSERT INTO `sys_logininfor` VALUES (36, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 21:46:55');
INSERT INTO `sys_logininfor` VALUES (37, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 21:47:04');
INSERT INTO `sys_logininfor` VALUES (38, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 21:49:12');
INSERT INTO `sys_logininfor` VALUES (39, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 21:49:15');
INSERT INTO `sys_logininfor` VALUES (40, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 21:49:17');
INSERT INTO `sys_logininfor` VALUES (41, 'admin', '127.0.0.1', '1', '密码输入错误1次', '2024-10-27 21:49:26');
INSERT INTO `sys_logininfor` VALUES (42, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 21:49:30');
INSERT INTO `sys_logininfor` VALUES (43, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 21:49:34');
INSERT INTO `sys_logininfor` VALUES (44, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 21:50:01');
INSERT INTO `sys_logininfor` VALUES (45, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 21:50:03');
INSERT INTO `sys_logininfor` VALUES (46, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 21:50:08');
INSERT INTO `sys_logininfor` VALUES (47, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 21:53:31');
INSERT INTO `sys_logininfor` VALUES (48, 'admin', '127.0.0.1', '0', '退出成功', '2024-10-27 21:53:35');
INSERT INTO `sys_logininfor` VALUES (49, 'admin', '127.0.0.1', '0', '登录成功', '2024-10-27 21:54:44');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2024-10-22 08:43:21', 'admin', '2024-10-27 17:55:43', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2024-10-22 08:43:21', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2024-10-22 08:43:21', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (4, 'Lersosa 官网', 0, 4, 'http://lersosa .leyramu.top', NULL, '', '', 0, 0, 'M', '0', '0', '', 'guide', 'admin', '2024-10-22 08:43:21', '', NULL, '若依官网地址');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2024-10-22 08:43:21', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2024-10-22 08:43:21', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2024-10-22 08:43:21', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2024-10-22 08:43:21', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2024-10-22 08:43:21', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2024-10-22 08:43:21', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2024-10-22 08:43:21', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2024-10-22 08:43:21', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2024-10-22 08:43:21', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2024-10-22 08:43:21', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2024-10-22 08:43:21', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, 'Sentinel控制台', 2, 3, 'http://localhost:8972', '', '', '', 0, 0, 'C', '0', '0', 'monitor:sentinel:list', 'sentinel', 'admin', '2024-10-22 08:43:21', '', NULL, '流量控制菜单');
INSERT INTO `sys_menu` VALUES (112, 'Nacos控制台', 2, 4, 'http://localhost:8848/nacos', '', '', '', 0, 0, 'C', '0', '0', 'monitor:nacos:list', 'nacos', 'admin', '2024-10-22 08:43:21', '', NULL, '服务治理菜单');
INSERT INTO `sys_menu` VALUES (113, 'Admin控制台', 2, 5, 'http://localhost:9120/login', '', '', '', 0, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2024-10-22 08:43:21', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (114, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2024-10-22 08:43:21', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (115, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2024-10-22 08:43:21', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (116, '系统接口', 3, 3, 'http://localhost:8080/swagger-ui/index.html', '', '', '', 0, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2024-10-22 08:43:21', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'system/operlog/index', '', '', 1, 0, 'C', '0', '0', 'system:operlog:list', 'form', 'admin', '2024-10-22 08:43:21', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'system/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'system:logininfor:list', 'logininfor', 'admin', '2024-10-22 08:43:21', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:export', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:export', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:unlock', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 115, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 115, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 115, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 115, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 115, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 115, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2024-10-22 08:43:21', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2024-10-22 08:43:22', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2024-10-22 08:43:22', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 208 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (100, '定时任务', 2, 'leyramu.framework.lersosa.job.controller.SysJobController.edit()', 'PUT', 1, 'admin', NULL, '/schedule/job', '127.0.0.1', '', '{\"concurrent\":\"1\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"cronExpression\":\"0/10 * * * * ?\",\"invokeTarget\":\"lersosaTask.lersosaNoParams\",\"jobGroup\":\"DEFAULT\",\"jobId\":1,\"jobName\":\"系统默认（无参）\",\"misfirePolicy\":\"3\",\"nextValidTime\":\"2024-10-23 18:53:50\",\"params\":{},\"remark\":\"\",\"status\":\"1\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-23 18:53:34', 54);
INSERT INTO `sys_oper_log` VALUES (101, '定时任务', 2, 'leyramu.framework.lersosa.job.controller.SysJobController.edit()', 'PUT', 1, 'admin', NULL, '/schedule/job', '127.0.0.1', '', '{\"concurrent\":\"1\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"cronExpression\":\"0/15 * * * * ?\",\"invokeTarget\":\"lersosaTask.lersosaParams(\'ry\')\",\"jobGroup\":\"DEFAULT\",\"jobId\":2,\"jobName\":\"系统默认（有参）\",\"misfirePolicy\":\"3\",\"nextValidTime\":\"2024-10-23 18:54:00\",\"params\":{},\"remark\":\"\",\"status\":\"1\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-23 18:53:42', 17);
INSERT INTO `sys_oper_log` VALUES (102, '定时任务', 2, 'leyramu.framework.lersosa.job.controller.SysJobController.edit()', 'PUT', 1, 'admin', NULL, '/schedule/job', '127.0.0.1', '', '{\"concurrent\":\"1\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"cronExpression\":\"0/20 * * * * ?\",\"invokeTarget\":\"lersosaTask.lersosaMultipleParams(\'ry\', true, 2000L, 316.50D, 100)\",\"jobGroup\":\"DEFAULT\",\"jobId\":3,\"jobName\":\"系统默认（多参）\",\"misfirePolicy\":\"3\",\"nextValidTime\":\"2024-10-23 18:54:20\",\"params\":{},\"remark\":\"\",\"status\":\"1\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-23 18:53:48', 19);
INSERT INTO `sys_oper_log` VALUES (103, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/a8f8aa05-1c44-4965-b391-211c359c966a', '127.0.0.1', '', '\"a8f8aa05-1c44-4965-b391-211c359c966a\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-23 18:54:49', 10);
INSERT INTO `sys_oper_log` VALUES (104, '代码生成', 6, 'leyramu.framework.lersosa.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', NULL, '/code/gen/importTable', '127.0.0.1', '', '{\"tables\":\"sys_config\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-23 19:42:27', 97);
INSERT INTO `sys_oper_log` VALUES (105, '代码生成', 8, 'leyramu.framework.lersosa.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', NULL, '/code/gen/batchGenCode', '127.0.0.1', '', '{\"tables\":\"sys_config\"}', NULL, 0, NULL, '2024-10-23 19:42:31', 301);
INSERT INTO `sys_oper_log` VALUES (106, '代码生成', 3, 'leyramu.framework.lersosa.gen.controller.GenController.remove()', 'DELETE', 1, 'admin', NULL, '/code/gen/1', '127.0.0.1', '', '[1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-23 19:42:53', 14);
INSERT INTO `sys_oper_log` VALUES (107, '参数管理', 2, 'leyramu.framework.lersosa.system.controller.SysConfigController.edit()', 'PUT', 1, 'admin', NULL, '/system/config', '127.0.0.1', '', '{\"configId\":4,\"configKey\":\"sys.account.registerUser\",\"configName\":\"账号自助-是否开启用户注册功能\",\"configType\":\"Y\",\"configValue\":\"true\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"params\":{},\"remark\":\"是否开启注册用户功能（true开启，false关闭）\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-24 17:08:05', 25);
INSERT INTO `sys_oper_log` VALUES (108, '参数管理', 2, 'leyramu.framework.lersosa.system.controller.SysConfigController.edit()', 'PUT', 1, 'admin', NULL, '/system/config', '127.0.0.1', '', '{\"configId\":4,\"configKey\":\"sys.account.registerUser\",\"configName\":\"账号自助-是否开启用户注册功能\",\"configType\":\"N\",\"configValue\":\"true\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"params\":{},\"remark\":\"是否开启注册用户功能（true开启，false关闭）\",\"updateBy\":\"admin\",\"updateTime\":\"2024-10-24 17:08:05\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-24 17:10:09', 11);
INSERT INTO `sys_oper_log` VALUES (109, '参数管理', 2, 'leyramu.framework.lersosa.system.controller.SysConfigController.edit()', 'PUT', 1, 'admin', NULL, '/system/config', '127.0.0.1', '', '{\"configId\":4,\"configKey\":\"sys.account.registerUser\",\"configName\":\"账号自助-是否开启用户注册功能\",\"configType\":\"Y\",\"configValue\":\"true\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"params\":{},\"remark\":\"是否开启注册用户功能（true开启，false关闭）\",\"updateBy\":\"admin\",\"updateTime\":\"2024-10-24 17:10:09\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-24 17:24:24', 11);
INSERT INTO `sys_oper_log` VALUES (110, '参数管理', 2, 'leyramu.framework.lersosa.system.controller.SysConfigController.edit()', 'PUT', 1, 'admin', NULL, '/system/config', '127.0.0.1', '', '{\"configId\":2,\"configKey\":\"sys.user.initPassword\",\"configName\":\"用户管理-账号初始密码\",\"configType\":\"N\",\"configValue\":\"123456\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"params\":{},\"remark\":\"初始化密码 123456\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-24 17:24:35', 13);
INSERT INTO `sys_oper_log` VALUES (111, '参数管理', 9, 'leyramu.framework.lersosa.system.controller.SysConfigController.refreshCache()', 'DELETE', 1, 'admin', NULL, '/system/config/refreshCache', '127.0.0.1', '', '', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-24 17:24:42', 17);
INSERT INTO `sys_oper_log` VALUES (112, '参数管理', 2, 'leyramu.framework.lersosa.system.controller.SysConfigController.edit()', 'PUT', 1, 'admin', NULL, '/system/config', '127.0.0.1', '', '{\"configId\":4,\"configKey\":\"sys.account.registerUser\",\"configName\":\"账号自助-是否开启用户注册功能\",\"configType\":\"N\",\"configValue\":\"true\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"params\":{},\"remark\":\"是否开启注册用户功能（true开启，false关闭）\",\"updateBy\":\"admin\",\"updateTime\":\"2024-10-24 17:24:24\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-24 17:25:11', 15);
INSERT INTO `sys_oper_log` VALUES (113, '参数管理', 2, 'leyramu.framework.lersosa.system.controller.SysConfigController.edit()', 'PUT', 1, 'admin', NULL, '/system/config', '127.0.0.1', '', '{\"configId\":2,\"configKey\":\"sys.user.initPassword\",\"configName\":\"用户管理-账号初始密码\",\"configType\":\"Y\",\"configValue\":\"123456\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"params\":{},\"remark\":\"初始化密码 123456\",\"updateBy\":\"admin\",\"updateTime\":\"2024-10-24 17:24:34\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-24 17:25:14', 13);
INSERT INTO `sys_oper_log` VALUES (114, '参数管理', 2, 'leyramu.framework.lersosa.system.controller.SysConfigController.edit()', 'PUT', 1, 'admin', NULL, '/system/config', '127.0.0.1', '', '{\"configId\":4,\"configKey\":\"sys.account.registerUser\",\"configName\":\"账号自助-是否开启用户注册功能\",\"configType\":\"Y\",\"configValue\":\"true\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"params\":{},\"remark\":\"是否开启注册用户功能（true开启，false关闭）\",\"updateBy\":\"admin\",\"updateTime\":\"2024-10-24 17:25:11\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-24 17:26:05', 7);
INSERT INTO `sys_oper_log` VALUES (115, '用户管理', 3, 'leyramu.framework.lersosa.system.controller.SysUserController.remove()', 'DELETE', 1, 'admin', NULL, '/system/user/100', '127.0.0.1', '', '[100]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-26 14:37:52', 46);
INSERT INTO `sys_oper_log` VALUES (116, '参数管理', 9, 'leyramu.framework.lersosa.system.controller.SysConfigController.refreshCache()', 'DELETE', 1, 'admin', NULL, '/system/config/refreshCache', '127.0.0.1', '', '', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-26 17:11:37', 10);
INSERT INTO `sys_oper_log` VALUES (117, '字典类型', 2, 'leyramu.framework.lersosa.system.controller.SysDictTypeController.edit()', 'PUT', 1, 'admin', NULL, '/system/dict/type', '127.0.0.1', '', '{\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:22\",\"dictId\":1,\"dictName\":\"用户性别\",\"dictType\":\"sys_user_sex\",\"params\":{},\"remark\":\"用户性别列表\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 14:20:54', 40);
INSERT INTO `sys_oper_log` VALUES (118, '用户管理', 2, 'leyramu.framework.lersosa.system.controller.SysUserController.edit()', 'PUT', 1, 'admin', NULL, '/system/user', '127.0.0.1', '', '{\"admin\":false,\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:21\",\"delFlag\":\"0\",\"dept\":{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":105,\"deptName\":\"测试部门\",\"leader\":\"若依\",\"orderNum\":3,\"params\":{},\"parentId\":101,\"status\":\"0\"},\"deptId\":105,\"email\":\"ry@qq.com\",\"loginDate\":\"2024-10-22 08:43:21\",\"loginIp\":\"127.0.0.1\",\"nickName\":\"若依\",\"params\":{},\"phonenumber\":\"15666666666\",\"postIds\":[2],\"remark\":\"测试员\",\"roleIds\":[2],\"roles\":[{\"admin\":false,\"dataScope\":\"2\",\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":2,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"roleSort\":2,\"status\":\"0\"}],\"sex\":\"1\",\"status\":\"0\",\"updateBy\":\"admin\",\"userId\":2,\"userName\":\"ry\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 15:23:16', 31);
INSERT INTO `sys_oper_log` VALUES (119, '登录日志', 3, 'leyramu.framework.lersosa.system.controller.SysLogininforController.clean()', 'DELETE', 1, 'admin', NULL, '/system/logininfor/clean', '127.0.0.1', '', '', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 16:21:35', 98);
INSERT INTO `sys_oper_log` VALUES (120, '参数管理', 5, 'leyramu.framework.lersosa.system.controller.SysConfigController.export()', 'POST', 1, 'admin', NULL, '/system/config/export', '127.0.0.1', '', '{\"pageSize\":\"10\",\"pageNum\":\"1\"}', NULL, 0, NULL, '2024-10-27 17:30:11', 639);
INSERT INTO `sys_oper_log` VALUES (121, '定时任务', 2, 'leyramu.framework.lersosa.job.controller.SysJobController.run()', 'PUT', 1, 'admin', NULL, '/schedule/job/run', '127.0.0.1', '', '{\"jobGroup\":\"DEFAULT\",\"jobId\":1,\"misfirePolicy\":\"0\",\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:38:42', 49);
INSERT INTO `sys_oper_log` VALUES (122, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/bb6243ad-8816-4807-b4b3-544984ab635a', '127.0.0.1', '', '\"bb6243ad-8816-4807-b4b3-544984ab635a\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:39:54', 3);
INSERT INTO `sys_oper_log` VALUES (123, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/eeef9d3a-3e9b-4080-a0bb-fc1891fcc9c3', '127.0.0.1', '', '\"eeef9d3a-3e9b-4080-a0bb-fc1891fcc9c3\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:39:58', 2);
INSERT INTO `sys_oper_log` VALUES (124, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/b401bd1e-6c62-496b-a5ca-eed64e558dad', '127.0.0.1', '', '\"b401bd1e-6c62-496b-a5ca-eed64e558dad\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:39:59', 2);
INSERT INTO `sys_oper_log` VALUES (125, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/0f2207c8-ea9d-4d80-9145-258f37d75437', '127.0.0.1', '', '\"0f2207c8-ea9d-4d80-9145-258f37d75437\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:03', 2);
INSERT INTO `sys_oper_log` VALUES (126, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/4f064fcc-09a1-4810-a469-f422511c9c08', '127.0.0.1', '', '\"4f064fcc-09a1-4810-a469-f422511c9c08\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:05', 2);
INSERT INTO `sys_oper_log` VALUES (127, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/75a2cd7a-4fdc-4d74-8000-206569b92376', '127.0.0.1', '', '\"75a2cd7a-4fdc-4d74-8000-206569b92376\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:07', 2);
INSERT INTO `sys_oper_log` VALUES (128, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/2f7a7f04-16ae-4847-a862-52dfda4edb0b', '127.0.0.1', '', '\"2f7a7f04-16ae-4847-a862-52dfda4edb0b\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:10', 2);
INSERT INTO `sys_oper_log` VALUES (129, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/7dc6ceae-2526-4211-a5f7-c96ca425cfa0', '127.0.0.1', '', '\"7dc6ceae-2526-4211-a5f7-c96ca425cfa0\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:13', 2);
INSERT INTO `sys_oper_log` VALUES (130, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/3d66e70c-b2b1-4a24-927b-f146e6de8a21', '127.0.0.1', '', '\"3d66e70c-b2b1-4a24-927b-f146e6de8a21\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:15', 1);
INSERT INTO `sys_oper_log` VALUES (131, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/7ff68f0a-9e5f-4adf-a423-5e5bda00cde1', '127.0.0.1', '', '\"7ff68f0a-9e5f-4adf-a423-5e5bda00cde1\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:17', 1);
INSERT INTO `sys_oper_log` VALUES (132, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/05a6444b-ce15-4693-919d-46a4ca942908', '127.0.0.1', '', '\"05a6444b-ce15-4693-919d-46a4ca942908\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:18', 2);
INSERT INTO `sys_oper_log` VALUES (133, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/688159f0-4763-425b-b268-9ab0517ea732', '127.0.0.1', '', '\"688159f0-4763-425b-b268-9ab0517ea732\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:20', 2);
INSERT INTO `sys_oper_log` VALUES (134, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/6592d03b-717f-452d-88aa-8027ad6e0715', '127.0.0.1', '', '\"6592d03b-717f-452d-88aa-8027ad6e0715\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:21', 2);
INSERT INTO `sys_oper_log` VALUES (135, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/383c12a9-6a8c-471f-adfa-3638e2fc9eda', '127.0.0.1', '', '\"383c12a9-6a8c-471f-adfa-3638e2fc9eda\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:22', 1);
INSERT INTO `sys_oper_log` VALUES (136, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/7d4bea3a-8d01-4102-811d-0ad286de48ce', '127.0.0.1', '', '\"7d4bea3a-8d01-4102-811d-0ad286de48ce\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:22', 2);
INSERT INTO `sys_oper_log` VALUES (137, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/833149c8-7797-4105-ba7c-58d6b5f09a70', '127.0.0.1', '', '\"833149c8-7797-4105-ba7c-58d6b5f09a70\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:23', 2);
INSERT INTO `sys_oper_log` VALUES (138, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/86a183b2-ca67-40af-9ed1-3c352b966867', '127.0.0.1', '', '\"86a183b2-ca67-40af-9ed1-3c352b966867\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:40:24', 2);
INSERT INTO `sys_oper_log` VALUES (139, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/335b68ac-6c0b-4192-9a92-cc22f399f522', '127.0.0.1', '', '\"335b68ac-6c0b-4192-9a92-cc22f399f522\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:17', 2);
INSERT INTO `sys_oper_log` VALUES (140, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/4c1e9bdf-35e2-4889-8005-b8314fe0ac65', '127.0.0.1', '', '\"4c1e9bdf-35e2-4889-8005-b8314fe0ac65\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:17', 1);
INSERT INTO `sys_oper_log` VALUES (141, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/7fc6af74-0822-483d-a891-313244357f48', '127.0.0.1', '', '\"7fc6af74-0822-483d-a891-313244357f48\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:17', 1);
INSERT INTO `sys_oper_log` VALUES (142, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/7fc6af74-0822-483d-a891-313244357f48', '127.0.0.1', '', '\"7fc6af74-0822-483d-a891-313244357f48\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:17', 1);
INSERT INTO `sys_oper_log` VALUES (143, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/0e7114cf-0489-4dea-9a3c-ce442e0d94d0', '127.0.0.1', '', '\"0e7114cf-0489-4dea-9a3c-ce442e0d94d0\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:18', 2);
INSERT INTO `sys_oper_log` VALUES (144, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/60b091b5-fbb0-4624-b001-cb7c7047d340', '127.0.0.1', '', '\"60b091b5-fbb0-4624-b001-cb7c7047d340\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:18', 2);
INSERT INTO `sys_oper_log` VALUES (145, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/031b0f31-6b4d-4d3d-b553-48b47083fb72', '127.0.0.1', '', '\"031b0f31-6b4d-4d3d-b553-48b47083fb72\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:18', 2);
INSERT INTO `sys_oper_log` VALUES (146, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/902a90fb-24dc-4c24-97a7-e833110a50b8', '127.0.0.1', '', '\"902a90fb-24dc-4c24-97a7-e833110a50b8\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:18', 2);
INSERT INTO `sys_oper_log` VALUES (147, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/bbb56350-0f6f-4743-8407-9fbb49a10b7c', '127.0.0.1', '', '\"bbb56350-0f6f-4743-8407-9fbb49a10b7c\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:19', 2);
INSERT INTO `sys_oper_log` VALUES (148, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/bbb56350-0f6f-4743-8407-9fbb49a10b7c', '127.0.0.1', '', '\"bbb56350-0f6f-4743-8407-9fbb49a10b7c\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:19', 2);
INSERT INTO `sys_oper_log` VALUES (149, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/68276bd5-7b92-4644-aa7f-b7767714f36e', '127.0.0.1', '', '\"68276bd5-7b92-4644-aa7f-b7767714f36e\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:19', 1);
INSERT INTO `sys_oper_log` VALUES (150, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/239d2415-4329-401d-a8c5-8ba455f310b3', '127.0.0.1', '', '\"239d2415-4329-401d-a8c5-8ba455f310b3\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:30', 2);
INSERT INTO `sys_oper_log` VALUES (151, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/35543ef9-3c7d-4132-a237-a9d70dd13748', '127.0.0.1', '', '\"35543ef9-3c7d-4132-a237-a9d70dd13748\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:31', 2);
INSERT INTO `sys_oper_log` VALUES (152, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/35543ef9-3c7d-4132-a237-a9d70dd13748', '127.0.0.1', '', '\"35543ef9-3c7d-4132-a237-a9d70dd13748\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:31', 1);
INSERT INTO `sys_oper_log` VALUES (153, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/f5339c6d-21a2-48ce-b04f-f1d9e4edfe6e', '127.0.0.1', '', '\"f5339c6d-21a2-48ce-b04f-f1d9e4edfe6e\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:31', 2);
INSERT INTO `sys_oper_log` VALUES (154, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/f5339c6d-21a2-48ce-b04f-f1d9e4edfe6e', '127.0.0.1', '', '\"f5339c6d-21a2-48ce-b04f-f1d9e4edfe6e\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:31', 1);
INSERT INTO `sys_oper_log` VALUES (155, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/3395b250-ad36-4a4e-9b15-2786c25c5ea3', '127.0.0.1', '', '\"3395b250-ad36-4a4e-9b15-2786c25c5ea3\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:31', 1);
INSERT INTO `sys_oper_log` VALUES (156, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/c8770ff2-50ae-417d-8de5-18225f2737a0', '127.0.0.1', '', '\"c8770ff2-50ae-417d-8de5-18225f2737a0\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:32', 1);
INSERT INTO `sys_oper_log` VALUES (157, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/dfc97bd8-8cf3-43e9-8bca-6d32cafdb91d', '127.0.0.1', '', '\"dfc97bd8-8cf3-43e9-8bca-6d32cafdb91d\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:32', 2);
INSERT INTO `sys_oper_log` VALUES (158, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/736ae951-6c52-4b7a-85c2-b7905976fdf6', '127.0.0.1', '', '\"736ae951-6c52-4b7a-85c2-b7905976fdf6\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:32', 2);
INSERT INTO `sys_oper_log` VALUES (159, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/e8f10fe4-3e8a-4120-bc27-e1c75b0ce2ee', '127.0.0.1', '', '\"e8f10fe4-3e8a-4120-bc27-e1c75b0ce2ee\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:32', 2);
INSERT INTO `sys_oper_log` VALUES (160, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/255cff44-1c84-4633-ac97-5590e85f3c8a', '127.0.0.1', '', '\"255cff44-1c84-4633-ac97-5590e85f3c8a\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:32', 2);
INSERT INTO `sys_oper_log` VALUES (161, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/64aa1c21-1f87-4f55-a2d0-bd24480f5a67', '127.0.0.1', '', '\"64aa1c21-1f87-4f55-a2d0-bd24480f5a67\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:33', 2);
INSERT INTO `sys_oper_log` VALUES (162, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/d666cb1e-c09d-4440-8284-491267dee306', '127.0.0.1', '', '\"d666cb1e-c09d-4440-8284-491267dee306\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:33', 2);
INSERT INTO `sys_oper_log` VALUES (163, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/d666cb1e-c09d-4440-8284-491267dee306', '127.0.0.1', '', '\"d666cb1e-c09d-4440-8284-491267dee306\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:33', 2);
INSERT INTO `sys_oper_log` VALUES (164, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/5b72c482-711f-4623-96ee-a526d1838598', '127.0.0.1', '', '\"5b72c482-711f-4623-96ee-a526d1838598\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:33', 1);
INSERT INTO `sys_oper_log` VALUES (165, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/5b72c482-711f-4623-96ee-a526d1838598', '127.0.0.1', '', '\"5b72c482-711f-4623-96ee-a526d1838598\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:33', 1);
INSERT INTO `sys_oper_log` VALUES (166, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/4d54d81e-03f8-4bc9-acc4-87f7323e5cca', '127.0.0.1', '', '\"4d54d81e-03f8-4bc9-acc4-87f7323e5cca\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:33', 1);
INSERT INTO `sys_oper_log` VALUES (167, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/cb8629c8-73a3-4c65-ba45-0b9c38a43d88', '127.0.0.1', '', '\"cb8629c8-73a3-4c65-ba45-0b9c38a43d88\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:34', 2);
INSERT INTO `sys_oper_log` VALUES (168, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/06c42dbd-1446-427d-b6d3-8ada0f499e56', '127.0.0.1', '', '\"06c42dbd-1446-427d-b6d3-8ada0f499e56\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:34', 2);
INSERT INTO `sys_oper_log` VALUES (169, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/06c42dbd-1446-427d-b6d3-8ada0f499e56', '127.0.0.1', '', '\"06c42dbd-1446-427d-b6d3-8ada0f499e56\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:34', 1);
INSERT INTO `sys_oper_log` VALUES (170, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/660714fc-060b-4797-9af2-2d766318e9aa', '127.0.0.1', '', '\"660714fc-060b-4797-9af2-2d766318e9aa\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:34', 2);
INSERT INTO `sys_oper_log` VALUES (171, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/660714fc-060b-4797-9af2-2d766318e9aa', '127.0.0.1', '', '\"660714fc-060b-4797-9af2-2d766318e9aa\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:34', 1);
INSERT INTO `sys_oper_log` VALUES (172, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/668d0788-a426-47f9-91e3-2395a37378a2', '127.0.0.1', '', '\"668d0788-a426-47f9-91e3-2395a37378a2\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:34', 2);
INSERT INTO `sys_oper_log` VALUES (173, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/668d0788-a426-47f9-91e3-2395a37378a2', '127.0.0.1', '', '\"668d0788-a426-47f9-91e3-2395a37378a2\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:34', 1);
INSERT INTO `sys_oper_log` VALUES (174, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/059c4996-e68c-4019-a269-8ff6a2e08f89', '127.0.0.1', '', '\"059c4996-e68c-4019-a269-8ff6a2e08f89\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:35', 2);
INSERT INTO `sys_oper_log` VALUES (175, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/ea86e0fd-f61c-472d-b19c-624ffe647e10', '127.0.0.1', '', '\"ea86e0fd-f61c-472d-b19c-624ffe647e10\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:35', 2);
INSERT INTO `sys_oper_log` VALUES (176, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/ea86e0fd-f61c-472d-b19c-624ffe647e10', '127.0.0.1', '', '\"ea86e0fd-f61c-472d-b19c-624ffe647e10\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:35', 2);
INSERT INTO `sys_oper_log` VALUES (177, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/1da65d36-582c-4d68-8144-ff1b2af6561f', '127.0.0.1', '', '\"1da65d36-582c-4d68-8144-ff1b2af6561f\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:35', 2);
INSERT INTO `sys_oper_log` VALUES (178, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/cf1eda7b-db2b-472f-9404-3e89f8f4e1f9', '127.0.0.1', '', '\"cf1eda7b-db2b-472f-9404-3e89f8f4e1f9\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:42', 1);
INSERT INTO `sys_oper_log` VALUES (179, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/7b264c6c-3379-4b4b-a982-68bf37f38047', '127.0.0.1', '', '\"7b264c6c-3379-4b4b-a982-68bf37f38047\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:43', 1);
INSERT INTO `sys_oper_log` VALUES (180, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/bca83dc4-8bff-410d-a65d-e576a0a3d6cd', '127.0.0.1', '', '\"bca83dc4-8bff-410d-a65d-e576a0a3d6cd\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:44', 2);
INSERT INTO `sys_oper_log` VALUES (181, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/3b8bc1ce-48b5-4b50-8250-d26cb8655358', '127.0.0.1', '', '\"3b8bc1ce-48b5-4b50-8250-d26cb8655358\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:44', 2);
INSERT INTO `sys_oper_log` VALUES (182, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/26503045-7504-4d5f-a98b-0d8b5a846323', '127.0.0.1', '', '\"26503045-7504-4d5f-a98b-0d8b5a846323\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:45', 2);
INSERT INTO `sys_oper_log` VALUES (183, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/4cd22d7d-6483-4377-8609-3ee93423098b', '127.0.0.1', '', '\"4cd22d7d-6483-4377-8609-3ee93423098b\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:47', 2);
INSERT INTO `sys_oper_log` VALUES (184, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/78563aa2-3203-4928-a29e-10ffb32ec97d', '127.0.0.1', '', '\"78563aa2-3203-4928-a29e-10ffb32ec97d\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:48', 1);
INSERT INTO `sys_oper_log` VALUES (185, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/839937a4-e380-4fb3-934b-0cd9484e9da4', '127.0.0.1', '', '\"839937a4-e380-4fb3-934b-0cd9484e9da4\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:49', 2);
INSERT INTO `sys_oper_log` VALUES (186, '在线用户', 7, 'leyramu.framework.lersosa.system.controller.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', NULL, '/system/online/5e57b07a-4899-4155-b3a2-92c684e40f51', '127.0.0.1', '', '\"5e57b07a-4899-4155-b3a2-92c684e40f51\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:41:48', 2);
INSERT INTO `sys_oper_log` VALUES (187, '参数管理', 5, 'leyramu.framework.lersosa.system.controller.SysConfigController.export()', 'POST', 1, 'admin', NULL, '/system/config/export', '127.0.0.1', '', '{\"pageSize\":\"10\",\"pageNum\":\"1\"}', NULL, 0, NULL, '2024-10-27 17:47:42', 73);
INSERT INTO `sys_oper_log` VALUES (188, '参数管理', 9, 'leyramu.framework.lersosa.system.controller.SysConfigController.refreshCache()', 'DELETE', 1, 'admin', NULL, '/system/config/refreshCache', '127.0.0.1', '', '', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:55:29', 10);
INSERT INTO `sys_oper_log` VALUES (189, '菜单管理', 2, 'leyramu.framework.lersosa.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/system/menu', '127.0.0.1', '', '{\"children\":[],\"createTime\":\"2024-10-22 08:43:21\",\"icon\":\"system\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":1,\"menuName\":\"系统管理\",\"menuType\":\"M\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"system\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 17:55:43', 18);
INSERT INTO `sys_oper_log` VALUES (190, '岗位管理', 2, 'leyramu.framework.lersosa.system.controller.SysPostController.edit()', 'PUT', 1, 'admin', NULL, '/system/post', '127.0.0.1', '', '{\"createBy\":\"admin\",\"createTime\":\"2024-10-22 08:43:21\",\"flag\":false,\"params\":{},\"postCode\":\"ceo\",\"postId\":1,\"postName\":\"董事长\",\"postSort\":1,\"remark\":\"\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:03:26', 17);
INSERT INTO `sys_oper_log` VALUES (191, '角色管理', 2, 'leyramu.framework.lersosa.system.controller.SysRoleController.changeStatus()', 'PUT', 1, 'admin', NULL, '/system/role/changeStatus', '127.0.0.1', '', '{\"admin\":false,\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":2,\"status\":\"1\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:05:03', 10);
INSERT INTO `sys_oper_log` VALUES (192, '角色管理', 2, 'leyramu.framework.lersosa.system.controller.SysRoleController.changeStatus()', 'PUT', 1, 'admin', NULL, '/system/role/changeStatus', '127.0.0.1', '', '{\"admin\":false,\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":2,\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:05:04', 6);
INSERT INTO `sys_oper_log` VALUES (193, '角色管理', 2, 'leyramu.framework.lersosa.system.controller.SysRoleController.edit()', 'PUT', 1, 'admin', NULL, '/system/role', '127.0.0.1', '', '{\"admin\":false,\"createTime\":\"2024-10-22 08:43:21\",\"dataScope\":\"2\",\"delFlag\":\"0\",\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"menuIds\":[100,1000,1001,1002,1003,1004,1005,1006,101,1007,1008,1009,1010,1011,102,1012,1013,1014,1015,103,1016,1017,1018,1019,104,1020,1021,1022,1023,1024,105,1025,1026,1027,1028,1029,106,1030,1031,1032,1033,1034,107,1035,1036,1037,1038,108,500,1039,1040,1041,501,1042,1043,1044,1045,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,114,115,1055,1058,1056,1057,1059,1060,116,4],\"params\":{},\"remark\":\"普通角色\",\"roleId\":2,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"roleSort\":2,\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:05:16', 17);
INSERT INTO `sys_oper_log` VALUES (194, '个人信息', 2, 'leyramu.framework.lersosa.system.controller.SysProfileController.updateProfile()', 'PUT', 1, 'admin', NULL, '/system/user/profile', '127.0.0.1', '', '{\"admin\":false,\"email\":\"ry@163.com\",\"nickName\":\"若依\",\"params\":{},\"phonenumber\":\"15888888888\",\"sex\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:07:16', 14);
INSERT INTO `sys_oper_log` VALUES (195, '个人信息', 2, 'leyramu.framework.lersosa.system.controller.SysProfileController.updateProfile()', 'PUT', 1, 'admin', NULL, '/system/user/profile', '127.0.0.1', '', '{\"admin\":false,\"email\":\"ry@163.com\",\"nickName\":\"若依\",\"params\":{},\"phonenumber\":\"15888888888\",\"sex\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:07:18', 8);
INSERT INTO `sys_oper_log` VALUES (196, '个人信息', 2, 'leyramu.framework.lersosa.system.controller.SysProfileController.updateProfile()', 'PUT', 1, 'admin', NULL, '/system/user/profile', '127.0.0.1', '', '{\"admin\":false,\"email\":\"ry@163.com\",\"nickName\":\"若依\",\"params\":{},\"phonenumber\":\"15888888888\",\"sex\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:07:19', 9);
INSERT INTO `sys_oper_log` VALUES (197, '代码生成', 6, 'leyramu.framework.lersosa.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', NULL, '/code/gen/importTable', '127.0.0.1', '', '{\"tables\":\"sys_config\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:13:42', 80);
INSERT INTO `sys_oper_log` VALUES (198, '代码生成', 2, 'leyramu.framework.lersosa.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', NULL, '/code/gen', '127.0.0.1', '', '{\"businessName\":\"config\",\"className\":\"SysConfig\",\"columns\":[{\"capJavaField\":\"ConfigId\",\"columnComment\":\"参数主键\",\"columnId\":11,\"columnName\":\"config_id\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2024-10-27 18:13:42\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"javaField\":\"configId\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":true,\"tableId\":2,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"ConfigName\",\"columnComment\":\"参数名称\",\"columnId\":12,\"columnName\":\"config_name\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2024-10-27 18:13:42\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"configName\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"LIKE\",\"required\":false,\"sort\":2,\"superColumn\":true,\"tableId\":2,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"ConfigKey\",\"columnComment\":\"参数键名\",\"columnId\":13,\"columnName\":\"config_key\",\"columnType\":\"varchar(100)\",\"createBy\":\"admin\",\"createTime\":\"2024-10-27 18:13:42\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"configKey\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":true,\"tableId\":2,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"ConfigValue\",\"columnComment\":\"参数键值\",\"columnId\":14,\"columnName\":\"config_value\",\"columnType\":\"varchar(500)\",\"createBy\":\"admin\",\"createTime\":\"2024-10-27 18:13:42\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"configValue\",\"javaType\":\"S', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:13:53', 38);
INSERT INTO `sys_oper_log` VALUES (199, '代码生成', 8, 'leyramu.framework.lersosa.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', NULL, '/code/gen/batchGenCode', '127.0.0.1', '', '{\"tables\":\"sys_config\"}', NULL, 0, NULL, '2024-10-27 18:15:13', 63);
INSERT INTO `sys_oper_log` VALUES (200, '代码生成', 3, 'leyramu.framework.lersosa.gen.controller.GenController.remove()', 'DELETE', 1, 'admin', NULL, '/code/gen/2', '127.0.0.1', '', '[2]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 18:15:27', 13);
INSERT INTO `sys_oper_log` VALUES (201, '定时任务', 1, 'leyramu.framework.lersosa.job.controller.SysJobController.add()', 'POST', 1, 'admin', NULL, '/schedule/job', '127.0.0.1', '', '', '{\"msg\":\"新增任务\'1\'失败，Cron表达式不正确\",\"code\":500}', 0, NULL, '2024-10-27 21:22:54', 3);
INSERT INTO `sys_oper_log` VALUES (202, '定时任务', 1, 'leyramu.framework.lersosa.job.controller.SysJobController.add()', 'POST', 1, 'admin', NULL, '/schedule/job', '127.0.0.1', '', '{\"concurrent\":\"1\",\"cronExpression\":\"* * * * * ?\",\"invokeTarget\":\"1\",\"jobGroup\":\"DEFAULT\",\"jobName\":\"1\",\"misfirePolicy\":\"1\",\"nextValidTime\":\"2024-10-27 21:23:01\",\"params\":{},\"status\":\"0\"}', NULL, 1, 'No bean named \'1\' available', '2024-10-27 21:23:00', 6);
INSERT INTO `sys_oper_log` VALUES (203, '定时任务', 1, 'leyramu.framework.lersosa.job.controller.SysJobController.add()', 'POST', 1, 'admin', NULL, '/schedule/job', '127.0.0.1', '', '{\"concurrent\":\"1\",\"cronExpression\":\"* * * * * ?\",\"invokeTarget\":\"\'\'\",\"jobGroup\":\"DEFAULT\",\"jobName\":\"1\",\"misfirePolicy\":\"1\",\"nextValidTime\":\"2024-10-27 21:23:07\",\"params\":{},\"status\":\"0\"}', NULL, 1, 'No bean named \'\'\'\' available', '2024-10-27 21:23:06', 0);
INSERT INTO `sys_oper_log` VALUES (204, '定时任务', 1, 'leyramu.framework.lersosa.job.controller.SysJobController.add()', 'POST', 1, 'admin', NULL, '/schedule/job', '127.0.0.1', '', '{\"concurrent\":\"0\",\"createBy\":\"admin\",\"cronExpression\":\"* * * * * ?\",\"invokeTarget\":\"lersosaTask.lersosaNoParams\",\"jobGroup\":\"DEFAULT\",\"jobId\":100,\"jobName\":\"s\",\"misfirePolicy\":\"1\",\"nextValidTime\":\"2024-10-27 21:23:46\",\"params\":{},\"status\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 21:23:46', 25);
INSERT INTO `sys_oper_log` VALUES (205, '定时任务', 3, 'leyramu.framework.lersosa.job.controller.SysJobController.remove()', 'DELETE', 1, 'admin', NULL, '/schedule/job/100', '127.0.0.1', '', '[100]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 21:23:50', 14);
INSERT INTO `sys_oper_log` VALUES (206, '岗位管理', 1, 'leyramu.framework.lersosa.system.controller.SysPostController.add()', 'POST', 1, 'admin', NULL, '/system/post', '127.0.0.1', '', '{\"createBy\":\"admin\",\"flag\":false,\"params\":{},\"postCode\":\"d\",\"postId\":5,\"postName\":\"d\",\"postSort\":0,\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 21:38:05', 16);
INSERT INTO `sys_oper_log` VALUES (207, '岗位管理', 3, 'leyramu.framework.lersosa.system.controller.SysPostController.remove()', 'DELETE', 1, 'admin', NULL, '/system/post/5', '127.0.0.1', '', '[5]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-10-27 21:38:07', 12);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2024-10-22 08:43:21', 'admin', '2024-10-27 18:03:26', '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2024-10-22 08:43:21', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2024-10-22 08:43:21', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2024-10-22 08:43:21', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 0, 0, '0', '0', 'admin', '2024-10-22 08:43:21', 'admin', '2024-10-27 18:05:15', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2024-10-27 21:54:44', 'admin', '2024-10-22 08:43:21', '', '2024-10-27 21:54:44', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2024-10-27 15:49:47', 'admin', '2024-10-22 08:43:21', 'admin', '2024-10-27 15:49:47', '测试员');
INSERT INTO `sys_user` VALUES (100, NULL, 'admin1', 'admin1', '00', '', '', '0', '', '$2a$10$wsJtkMgFkd8VqKVvsVJB1.SGmvZsjUqfwODjrzUyZlRP0AWUZWsze', '0', '2', '127.0.0.1', '2024-10-24 17:08:48', '', '2024-10-24 17:08:28', '', '2024-10-24 17:08:36', NULL);

/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

SET FOREIGN_KEY_CHECKS = 1;
