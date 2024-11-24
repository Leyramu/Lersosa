/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

export interface TableVO extends BaseEntity {
  createDept: number | string;
  tableId: string | number;
  dataName: string;
  tableName: string;
  tableComment: string;
  subTableName?: any;
  subTableFkName?: any;
  className: string;
  tplCategory: string;
  packageName: string;
  moduleName: string;
  businessName: string;
  functionName: string;
  functionAuthor: string;
  genType: string;
  genPath: string;
  pkColumn?: any;
  columns?: any;
  options?: any;
  remark?: any;
  treeCode?: any;
  treeParentCode?: any;
  treeName?: any;
  menuIds?: any;
  parentMenuId?: any;
  parentMenuName?: any;
  tree: boolean;
  crud: boolean;
}

export interface TableQuery extends PageQuery {
  tableName: string;
  tableComment: string;
  dataName: string;
}

export interface DbColumnVO extends BaseEntity {
  createDept?: any;
  columnId?: any;
  tableId?: any;
  columnName?: any;
  columnComment?: any;
  columnType?: any;
  javaType?: any;
  javaField?: any;
  isPk?: any;
  isIncrement?: any;
  isRequired?: any;
  isInsert?: any;
  isEdit?: any;
  isList?: any;
  isQuery?: any;
  queryType?: any;
  htmlType?: any;
  dictType?: any;
  sort?: any;
  increment: boolean;
  capJavaField?: any;
  usableColumn: boolean;
  superColumn: boolean;
  list: boolean;
  pk: boolean;
  insert: boolean;
  edit: boolean;
  query: boolean;
  required: boolean;
}

export interface DbTableVO {
  createDept?: any;
  tableId?: any;
  tableName: string;
  tableComment: string;
  subTableName?: any;
  subTableFkName?: any;
  className?: any;
  tplCategory?: any;
  packageName?: any;
  moduleName?: any;
  businessName?: any;
  functionName?: any;
  functionAuthor?: any;
  genType?: any;
  genPath?: any;
  pkColumn?: any;
  columns: DbColumnVO[];
  options?: any;
  remark?: any;
  treeCode?: any;
  treeParentCode?: any;
  treeName?: any;
  menuIds?: any;
  parentMenuId?: any;
  parentMenuName?: any;
  tree: boolean;
  crud: boolean;
}

export interface DbTableQuery extends PageQuery {
  dataName: string;
  tableName: string;
  tableComment: string;
}

export interface GenTableVO {
  info: DbTableVO;
  rows: DbColumnVO[];
  tables: DbTableVO[];
}

export interface DbColumnForm extends BaseEntity {
  createDept: number;
  columnId: string;
  tableId: string;
  columnName: string;
  columnComment: string;
  columnType: string;
  javaType: string;
  javaField: string;
  isPk: string;
  isIncrement: string;
  isRequired: string;
  isInsert?: any;
  isEdit: string;
  isList: string;
  isQuery?: any;
  queryType: string;
  htmlType: string;
  dictType: string;
  sort: number;
  increment: boolean;
  capJavaField: string;
  usableColumn: boolean;
  superColumn: boolean;
  list: boolean;
  pk: boolean;
  insert: boolean;
  edit: boolean;
  query: boolean;
  required: boolean;
}

export interface DbParamForm {
  treeCode?: any;
  treeName?: any;
  treeParentCode?: any;
  parentMenuId: string;
}

export interface DbTableForm extends BaseEntity {
  createDept?: any;
  tableId: string;
  tableName: string;
  tableComment: string;
  subTableName?: any;
  subTableFkName?: any;
  className: string;
  tplCategory: string;
  packageName: string;
  moduleName: string;
  businessName: string;
  functionName: string;
  functionAuthor: string;
  genType: string;
  genPath: string;
  pkColumn?: any;
  columns: DbColumnForm[];
  options: string;
  remark?: any;
  treeCode?: any;
  treeParentCode?: any;
  treeName?: any;
  menuIds?: any;
  parentMenuId: string;
  parentMenuName?: any;
  tree: boolean;
  crud: boolean;
  params: DbParamForm;
}
