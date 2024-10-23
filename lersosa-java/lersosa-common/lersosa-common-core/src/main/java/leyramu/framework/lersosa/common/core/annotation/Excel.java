/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.core.annotation;

import leyramu.framework.lersosa.common.core.utils.poi.ExcelHandlerAdapter;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义导出Excel数据注解
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {

    /**
     * 导出时在excel中排序
     *
     * @return 排序
     */
    int sort() default Integer.MAX_VALUE;

    /**
     * 导出到Excel中的名字
     *
     * @return 名称
     */
    String name() default "";

    /**
     * 日期格式, 如: yyyy-MM-dd
     *
     * @return 日期格式
     */
    String dateFormat() default "";

    /**
     * 读取内容转表达式 (如: 0=男,1=女,2=未知)
     *
     * @return 读取内容转表达式
     */
    String readConverterExp() default "";

    /**
     * 分隔符，读取字符串组内容
     *
     * @return 分隔符
     */
    String separator() default ",";

    /**
     * BigDecimal 精度 默认:-1(默认不开启BigDecimal格式化)
     *
     * @return 精度
     */
    int scale() default -1;

    /**
     * BigDecimal 舍入规则 默认:BigDecimal.ROUND_HALF_EVEN
     *
     * @return 舍入规则
     */
    int roundingMode() default 6;

    /**
     * 导出时在excel中每个列的高度
     *
     * @return 高度
     */
    double height() default 14;

    /**
     * 导出时在excel中每个列的宽度
     *
     * @return 宽度
     */
    double width() default 16;

    /**
     * 文字后缀,如% 90 变成90%
     *
     * @return 后缀
     */
    String suffix() default "";

    /**
     * 当值为空时,字段的默认值
     *
     * @return 默认值
     */
    String defaultValue() default "";

    /**
     * 提示信息
     *
     * @return 提示信息
     */
    String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容
     *
     * @return 列表值
     */
    String[] combo() default {};

    /**
     * 是否需要纵向合并单元格,应对需求:含有list集合单元格)
     *
     * @return 是否纵向合并单元格
     */
    boolean needMerge() default false;

    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,这是标题需要但内容需要用户手工填写
     *
     * @return 是否是数据
     */
    boolean isExport() default true;

    /**
     * 另一个类中的属性名称,支持多级获取,以小数点隔开
     *
     * @return 属性名称
     */
    String targetAttr() default "";

    /**
     * 是否自动统计数据,在最后追加一行统计数据总和
     *
     * @return 是否统计
     */
    boolean isStatistics() default false;

    /**
     * 导出类型（0是数字 1是字符串）
     *
     * @return 导出类型
     */
    ColumnType cellType() default ColumnType.STRING;

    /**
     * 导出列头背景颜色
     *
     * @return 导出列头背景颜色
     */
    IndexedColors headerBackgroundColor() default IndexedColors.GREY_50_PERCENT;

    /**
     * 导出列头字体颜色
     *
     * @return 导出列头字体颜色
     */
    IndexedColors headerColor() default IndexedColors.WHITE;

    /**
     * 导出单元格背景颜色
     *
     * @return 导出单元格背景颜色
     */
    IndexedColors backgroundColor() default IndexedColors.WHITE;

    /**
     * 导出单元格字体颜色
     *
     * @return 导出单元格字体颜色
     */
    IndexedColors color() default IndexedColors.BLACK;

    /**
     * 导出字段对齐方式
     *
     * @return 导出单元格对齐方式
     */
    HorizontalAlignment align() default HorizontalAlignment.CENTER;

    /**
     * 自定义数据处理器
     *
     * @return 数据处理器
     */
    Class<?> handler() default ExcelHandlerAdapter.class;

    /**
     * 自定义数据处理器参数
     *
     * @return 数据处理器参数
     */
    String[] args() default {};

    /**
     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
     *
     * @return 类型
     */
    Type type() default Type.ALL;

    /**
     * 提示错误信息
     */
    @AllArgsConstructor
    enum Type {

        /**
         * 默认全部显示
         */
        ALL(0),

        /**
         * 导出
         */
        EXPORT(1),

        /**
         * 导入
         */
        IMPORT(2);

        /**
         * 类型
         */
        private final int value;

        /**
         * 获取类型
         *
         * @return 类型
         */
        public int value() {
            return this.value;
        }
    }

    /**
     * 导出类型（0是数字 1是字符串）
     */
    @AllArgsConstructor
    enum ColumnType {

        /**
         * 数字
         */
        NUMERIC(0),

        /**
         * 字符串
         */
        STRING(1),

        /**
         * 图片
         */
        IMAGE(2),

        /**
         * 文本
         */
        TEXT(3);

        /**
         * 类型
         */
        private final int value;

        /**
         * 获取类型
         *
         * @return 类型
         */
        public int value() {
            return this.value;
        }
    }
}
