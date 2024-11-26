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

package leyramu.framework.lersosa.gen.constant;

/**
 * 代码生成通用常量.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface GenConstants {

    /**
     * 树编码字段.
     */
    String TREE_CODE = "treeCode";

    /**
     * 树父编码字段.
     */
    String TREE_PARENT_CODE = "treeParentCode";

    /**
     * 树名称字段.
     */
    String TREE_NAME = "treeName";

    /**
     * 上级菜单ID字段.
     */
    String PARENT_MENU_ID = "parentMenuId";

    /**
     * 上级菜单名称字段.
     */
    String PARENT_MENU_NAME = "parentMenuName";

    /**
     * 数据库字符串类型.
     */
    String[] COLUMNTYPE_STR = {"char", "varchar", "enum", "set", "nchar", "nvarchar", "varchar2", "nvarchar2"};

    /**
     * 数据库文本类型.
     */
    String[] COLUMNTYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext", "binary", "varbinary", "blob",
        "ntext", "image", "bytea"};

    /**
     * 数据库时间类型.
     */
    String[] COLUMNTYPE_TIME = {"datetime", "time", "date", "timestamp", "year", "interval",
        "smalldatetime", "datetime2", "datetimeoffset", "timestamptz"};

    /**
     * 数据库数字类型.
     */
    String[] COLUMNTYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "int2", "int4", "int8", "number", "integer",
        "bit", "bigint", "float", "float4", "float8", "double", "decimal", "numeric", "real", "double precision",
        "smallserial", "serial", "bigserial", "money", "smallmoney"};

    /**
     * BO对象 不需要添加字段.
     */
    String[] COLUMNNAME_NOT_ADD = {"create_dept", "create_by", "create_time", "del_flag", "update_by",
        "update_time", "version", "tenant_id"};

    /**
     * BO对象 不需要编辑字段.
     */
    String[] COLUMNNAME_NOT_EDIT = {"create_dept", "create_by", "create_time", "del_flag", "update_by",
        "update_time", "version", "tenant_id"};

    /**
     * VO对象 不需要返回字段.
     */
    String[] COLUMNNAME_NOT_LIST = {"create_dept", "create_by", "create_time", "del_flag", "update_by",
        "update_time", "version", "tenant_id"};

    /**
     * BO对象 不需要查询字段.
     */
    String[] COLUMNNAME_NOT_QUERY = {"id", "create_dept", "create_by", "create_time", "del_flag", "update_by",
        "update_time", "remark", "version", "tenant_id"};

    /**
     * 文本框.
     */
    String HTML_INPUT = "input";

    /**
     * 文本域.
     */
    String HTML_TEXTAREA = "textarea";

    /**
     * 下拉框.
     */
    String HTML_SELECT = "select";

    /**
     * 单选框.
     */
    String HTML_RADIO = "radio";

    /**
     * 复选框.
     */
    String HTML_CHECKBOX = "checkbox";

    /**
     * 日期控件.
     */
    String HTML_DATETIME = "datetime";

    /**
     * 图片上传控件.
     */
    String HTML_IMAGE_UPLOAD = "imageUpload";

    /**
     * 文件上传控件.
     */
    String HTML_FILE_UPLOAD = "fileUpload";

    /**
     * 富文本控件.
     */
    String HTML_EDITOR = "editor";

    /**
     * 字符串类型.
     */
    String TYPE_STRING = "String";

    /**
     * 整型.
     */
    String TYPE_INTEGER = "Integer";

    /**
     * 长整型.
     */
    String TYPE_LONG = "Long";

    /**
     * 浮点型.
     */
    @SuppressWarnings("unused")
    String TYPE_DOUBLE = "Double";

    /**
     * 高精度计算类型.
     */
    String TYPE_BIGDECIMAL = "BigDecimal";

    /**
     * 时间类型.
     */
    String TYPE_DATE = "Date";

    /**
     * 模糊查询.
     */
    String QUERY_LIKE = "LIKE";

    /**
     * 相等查询.
     */
    String QUERY_EQ = "EQ";

    /**
     * 需要.
     */
    String REQUIRE = "1";
}
