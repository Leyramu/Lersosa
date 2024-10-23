/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.gen.domain;

import jakarta.validation.constraints.NotBlank;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 代码生成业务字段表
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenTableColumn extends BaseEntity {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long columnId;

    /**
     * 归属表编号
     */
    private Long tableId;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列描述
     */
    private String columnComment;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * JAVA类型
     */
    private String javaType;

    /**
     * JAVA字段名
     */
    @NotBlank(message = "Java属性不能为空")
    private String javaField;

    /**
     * 是否主键（1是）
     */
    private String isPk;

    /**
     * 是否自增（1是）
     */
    private String isIncrement;

    /**
     * 是否必填（1是）
     */
    private String isRequired;

    /**
     * 是否为插入字段（1是）
     */
    private String isInsert;

    /**
     * 是否编辑字段（1是）
     */
    private String isEdit;

    /**
     * 是否列表字段（1是）
     */
    private String isList;

    /**
     * 是否查询字段（1是）
     */
    private String isQuery;

    /**
     * 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围）
     */
    private String queryType;

    /**
     * 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件）
     */
    private String htmlType;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否为插入修改字段（1是）
     *
     * @param javaField java字段名
     * @return boolean true：是，false：否
     */
    public static boolean isSuperColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField,
                "createBy", "createTime", "updateBy", "updateTime", "remark",
                "parentName", "parentId", "orderNum", "ancestors");
    }

    /**
     * 通用字段是否显示
     *
     * @param javaField java字段名
     * @return boolean true：是，false：否
     */
    public static boolean isUsableColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField, "parentId", "orderNum", "remark");
    }

    /**
     * 获取java对象属性名称
     *
     * @return java对象属性名称
     */
    public String getCapJavaField() {
        return StringUtils.capitalize(javaField);
    }

    /**
     * 是否是主键
     *
     * @return boolean true：是，false：否
     */
    public boolean isPk() {
        return isPk(this.isPk);
    }

    /**
     * 是否是自增列
     *
     * @return boolean true：是，false：否
     */
    public boolean isPk(String isPk) {
        return isPk != null && StringUtils.equals("1", isPk);
    }

    /**
     * 是否是自增列
     *
     * @return boolean true：是，false：否
     */
    public boolean isIncrement() {
        return isIncrement(this.isIncrement);
    }

    /**
     * 是否是自增列
     *
     * @return boolean true：是，false：否
     */
    public boolean isIncrement(String isIncrement) {
        return isIncrement != null && StringUtils.equals("1", isIncrement);
    }

    /**
     * 是否是必填字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isRequired() {
        return isRequired(this.isRequired);
    }

    /**
     * 是否是必填字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isRequired(String isRequired) {
        return isRequired != null && StringUtils.equals("1", isRequired);
    }

    /**
     * 是否是插入字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isInsert() {
        return isInsert(this.isInsert);
    }

    /**
     * 是否是插入字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isInsert(String isInsert) {
        return isInsert != null && StringUtils.equals("1", isInsert);
    }

    /**
     * 是否是编辑字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isEdit() {
        return isInsert(this.isEdit);
    }

    /**
     * 是否是编辑字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isEdit(String isEdit) {
        return isEdit != null && StringUtils.equals("1", isEdit);
    }

    /**
     * 是否是列表字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isList() {
        return isList(this.isList);
    }

    /**
     * 是否是列表字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isList(String isList) {
        return isList != null && StringUtils.equals("1", isList);
    }

    /**
     * 是否是查询字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isQuery() {
        return isQuery(this.isQuery);
    }

    /**
     * 是否是查询字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isQuery(String isQuery) {
        return isQuery != null && StringUtils.equals("1", isQuery);
    }

    /**
     * 是否是基础字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isSuperColumn() {
        return !isSuperColumn(this.javaField);
    }

    /**
     * 是否是通用字段
     *
     * @return boolean true：是，false：否
     */
    public boolean isUsableColumn() {
        return isUsableColumn(javaField);
    }

    /**
     * 获取字段注解信息
     *
     * @return 列注解信息
     */
    public String readConverterExp() {
        String remarks = StringUtils.substringBetween(this.columnComment, "（", "）");
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(remarks)) {
            for (String value : remarks.split(" ")) {
                if (StringUtils.isNotEmpty(value)) {
                    Object startStr = value.subSequence(0, 1);
                    String endStr = value.substring(1);
                    sb.append(startStr).append("=").append(endStr).append(",");
                }
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return this.columnComment;
        }
    }
}
