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

package leyramu.framework.lersosa.common.excel.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.metadata.FieldCache;
import com.alibaba.excel.metadata.FieldWrapper;
import com.alibaba.excel.util.ClassUtils;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import leyramu.framework.lersosa.common.core.exception.ServiceException;
import leyramu.framework.lersosa.common.core.service.DictService;
import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.core.utils.StreamUtils;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.excel.annotation.ExcelDictFormat;
import leyramu.framework.lersosa.common.excel.annotation.ExcelEnumFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Excel表格下拉选操作.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Slf4j
public class ExcelDownHandler implements SheetWriteHandler {

    /**
     * Excel表格中的列名英文.
     */
    private static final String EXCEL_COLUMN_NAME = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 单选数据Sheet名.
     */
    private static final String OPTIONS_SHEET_NAME = "options";
    /**
     * 联动选择数据Sheet名的头.
     */
    private static final String LINKED_OPTIONS_SHEET_NAME = "linkedOptions";
    /**
     * 下拉可选项.
     */
    private final List<DropDownOptions> dropDownOptions;
    private final DictService dictService;
    /**
     * 当前单选进度.
     */
    private int currentOptionsColumnIndex;
    /**
     * 当前联动选择进度.
     */
    private int currentLinkedOptionsSheetIndex;

    public ExcelDownHandler(List<DropDownOptions> options) {
        this.dropDownOptions = options;
        this.currentOptionsColumnIndex = 0;
        this.currentLinkedOptionsSheetIndex = 0;
        this.dictService = SpringUtils.getBean(DictService.class);
    }

    /**
     * 开始创建下拉数据.
     */
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        // 开始设置下拉框 HSSFWorkbook
        DataValidationHelper helper = sheet.getDataValidationHelper();
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        FieldCache fieldCache = ClassUtils.declaredFields(writeWorkbookHolder.getClazz(), writeWorkbookHolder);
        for (Map.Entry<Integer, FieldWrapper> entry : fieldCache.getSortedFieldMap().entrySet()) {
            Integer index = entry.getKey();
            FieldWrapper wrapper = entry.getValue();
            Field field = wrapper.getField();
            // 循环实体中的每个属性
            // 可选的下拉值
            List<String> options = new ArrayList<>();
            if (field.isAnnotationPresent(ExcelDictFormat.class)) {
                // 如果指定了@ExcelDictFormat，则使用字典的逻辑
                ExcelDictFormat format = field.getDeclaredAnnotation(ExcelDictFormat.class);
                String dictType = format.dictType();
                String converterExp = format.readConverterExp();
                if (StringUtils.isNotBlank(dictType)) {
                    // 如果传递了字典名，则依据字典建立下拉
                    Collection<String> values = Optional.ofNullable(dictService.getAllDictByDictType(dictType))
                        .orElseThrow(() -> new ServiceException(String.format("字典 %s 不存在", dictType)))
                        .values();
                    options = new ArrayList<>(values);
                } else if (StringUtils.isNotBlank(converterExp)) {
                    // 如果指定了确切的值，则直接解析确切的值
                    List<String> strList = StringUtils.splitList(converterExp, format.separator());
                    options = StreamUtils.toList(strList, s -> StringUtils.split(s, "=")[1]);
                }
            } else if (field.isAnnotationPresent(ExcelEnumFormat.class)) {
                // 否则如果指定了@ExcelEnumFormat，则使用枚举的逻辑
                ExcelEnumFormat format = field.getDeclaredAnnotation(ExcelEnumFormat.class);
                List<Object> values = EnumUtil.getFieldValues(format.enumClass(), format.textField());
                options = StreamUtils.toList(values, String::valueOf);
            }
            if (ObjectUtil.isNotEmpty(options)) {
                // 仅当下拉可选项不为空时执行
                if (options.size() > 20) {
                    // 这里限制如果可选项大于20，则使用额外表形式
                    dropDownWithSheet(helper, workbook, sheet, index, options);
                } else {
                    // 否则使用固定值形式
                    dropDownWithSimple(helper, sheet, index, options);
                }
            }
        }
        if (CollUtil.isEmpty(dropDownOptions)) {
            return;
        }
        dropDownOptions.forEach(everyOptions -> {
            // 如果传递了下拉框选择器参数
            if (!everyOptions.getNextOptions().isEmpty()) {
                // 当二级选项不为空时，使用额外关联表的形式
                dropDownLinkedOptions(helper, workbook, sheet, everyOptions);
            } else if (everyOptions.getOptions().size() > 10) {
                // 当一级选项参数个数大于10，使用额外表的形式
                dropDownWithSheet(helper, workbook, sheet, everyOptions.getIndex(), everyOptions.getOptions());
            } else if (!everyOptions.getOptions().isEmpty()) {
                // 当一级选项个数不为空，使用默认形式
                dropDownWithSimple(helper, sheet, everyOptions.getIndex(), everyOptions.getOptions());
            }
        });
    }

    /**
     * 简单下拉框.
     *
     * @param celIndex 列index
     * @param value    下拉选可选值
     */
    private void dropDownWithSimple(DataValidationHelper helper, Sheet sheet, Integer celIndex, List<String> value) {
        if (ObjectUtil.isEmpty(value)) {
            return;
        }
        this.markOptionsToSheet(helper, sheet, celIndex, helper.createExplicitListConstraint(ArrayUtil.toArray(value, String.class)));
    }

    /**
     * 额外表格形式的级联下拉框.
     *
     * @param options 额外表格形式存储的下拉可选项
     */
    private void dropDownLinkedOptions(DataValidationHelper helper, Workbook workbook, Sheet sheet, DropDownOptions options) {
        String linkedOptionsSheetName = String.format("%s_%d", LINKED_OPTIONS_SHEET_NAME, currentLinkedOptionsSheetIndex);
        // 创建联动下拉数据表
        Sheet linkedOptionsDataSheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(linkedOptionsSheetName));
        // 将下拉表隐藏
        workbook.setSheetHidden(workbook.getSheetIndex(linkedOptionsDataSheet), true);
        // 完善横向的一级选项数据表
        List<String> firstOptions = options.getOptions();
        Map<String, List<String>> secoundOptionsMap = options.getNextOptions();

        // 创建名称管理器
        Name name = workbook.createName();
        // 设置名称管理器的别名
        name.setNameName(linkedOptionsSheetName);
        // 以横向第一行创建一级下拉拼接引用位置
        String firstOptionsFunction = String.format("%s!$%s$1:$%s$1",
            linkedOptionsSheetName,
            getExcelColumnName(0),
            getExcelColumnName(firstOptions.size())
        );
        // 设置名称管理器的引用位置
        name.setRefersToFormula(firstOptionsFunction);
        // 设置数据校验为序列模式，引用的是名称管理器中的别名
        this.markOptionsToSheet(helper, sheet, options.getIndex(), helper.createFormulaListConstraint(linkedOptionsSheetName));

        for (int columIndex = 0; columIndex < firstOptions.size(); columIndex++) {
            // 先提取主表中一级下拉的列名
            String firstOptionsColumnName = getExcelColumnName(columIndex);
            // 一次循环是每一个一级选项
            int finalI = columIndex;
            // 本次循环的一级选项值
            String thisFirstOptionsValue = firstOptions.get(columIndex);
            // 创建第一行的数据
            Optional.ofNullable(linkedOptionsDataSheet.getRow(0))
                // 如果不存在则创建第一行
                .orElseGet(() -> linkedOptionsDataSheet.createRow(finalI))
                // 第一行当前列
                .createCell(columIndex)
                // 设置值为当前一级选项值
                .setCellValue(thisFirstOptionsValue);

            // 第二行开始，设置第二级别选项参数
            List<String> secondOptions = secoundOptionsMap.get(thisFirstOptionsValue);
            if (CollUtil.isEmpty(secondOptions)) {
                // 必须保证至少有一个关联选项，否则将导致Excel解析错误
                secondOptions = Collections.singletonList("暂无_0");
            }

            // 以该一级选项值创建子名称管理器
            Name sonName = workbook.createName();
            // 设置名称管理器的别名
            sonName.setNameName(thisFirstOptionsValue);
            // 以第二行该列数据拼接引用位置
            String sonFunction = String.format("%s!$%s$2:$%s$%d",
                linkedOptionsSheetName,
                firstOptionsColumnName,
                firstOptionsColumnName,
                secondOptions.size() + 1
            );
            // 设置名称管理器的引用位置
            sonName.setRefersToFormula(sonFunction);
            // 数据验证为序列模式，引用到每一个主表中的二级选项位置
            // 创建子项的名称管理器，只是为了使得Excel可以识别到数据
            String mainSheetFirstOptionsColumnName = getExcelColumnName(options.getIndex());
            for (int i = 0; i < 100; i++) {
                // 以一级选项对应的主体所在位置创建二级下拉
                String secondOptionsFunction = String.format("=INDIRECT(%s%d)", mainSheetFirstOptionsColumnName, i + 1);
                // 二级只能主表每一行的每一列添加二级校验
                markLinkedOptionsToSheet(helper, sheet, i, options.getNextIndex(), helper.createFormulaListConstraint(secondOptionsFunction));
            }

            for (int rowIndex = 0; rowIndex < secondOptions.size(); rowIndex++) {
                // 从第二行开始填充二级选项
                int finalRowIndex = rowIndex + 1;
                int finalColumIndex = columIndex;

                Row row = Optional.ofNullable(linkedOptionsDataSheet.getRow(finalRowIndex))
                    // 没有则创建
                    .orElseGet(() -> linkedOptionsDataSheet.createRow(finalRowIndex));
                Optional
                    // 在本级一级选项所在的列
                    .ofNullable(row.getCell(finalColumIndex))
                    // 不存在则创建
                    .orElseGet(() -> row.createCell(finalColumIndex))
                    // 设置二级选项值
                    .setCellValue(secondOptions.get(rowIndex));
            }
        }

        currentLinkedOptionsSheetIndex++;
    }

    /**
     * 额外表格形式的普通下拉框.
     *
     * @param celIndex 下拉选
     * @param value    下拉选可选值
     */
    private void dropDownWithSheet(DataValidationHelper helper, Workbook workbook, Sheet sheet, Integer celIndex, List<String> value) {
        // 创建下拉数据表
        Sheet simpleDataSheet = Optional.ofNullable(workbook.getSheet(WorkbookUtil.createSafeSheetName(OPTIONS_SHEET_NAME)))
            .orElseGet(() -> workbook.createSheet(WorkbookUtil.createSafeSheetName(OPTIONS_SHEET_NAME)));
        // 将下拉表隐藏
        workbook.setSheetHidden(workbook.getSheetIndex(simpleDataSheet), true);
        // 完善纵向的一级选项数据表
        for (int i = 0; i < value.size(); i++) {
            int finalI = i;
            // 获取每一选项行，如果没有则创建
            Row row = Optional.ofNullable(simpleDataSheet.getRow(i))
                .orElseGet(() -> simpleDataSheet.createRow(finalI));
            // 获取本级选项对应的选项列，如果没有则创建
            Cell cell = Optional.ofNullable(row.getCell(currentOptionsColumnIndex))
                .orElseGet(() -> row.createCell(currentOptionsColumnIndex));
            // 设置值
            cell.setCellValue(value.get(i));
        }

        // 创建名称管理器
        Name name = workbook.createName();
        // 设置名称管理器的别名
        String nameName = String.format("%s_%d", OPTIONS_SHEET_NAME, celIndex);
        name.setNameName(nameName);
        // 以纵向第一列创建一级下拉拼接引用位置
        String function = String.format("%s!$%s$1:$%s$%d",
            OPTIONS_SHEET_NAME,
            getExcelColumnName(currentOptionsColumnIndex),
            getExcelColumnName(currentOptionsColumnIndex),
            value.size());
        // 设置名称管理器的引用位置
        name.setRefersToFormula(function);
        // 设置数据校验为序列模式，引用的是名称管理器中的别名
        this.markOptionsToSheet(helper, sheet, celIndex, helper.createFormulaListConstraint(nameName));
        currentOptionsColumnIndex++;
    }

    /**
     * 挂载下拉的列，仅限一级选项.
     */
    private void markOptionsToSheet(DataValidationHelper helper, Sheet sheet, Integer celIndex,
                                    DataValidationConstraint constraint) {
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList addressList = new CellRangeAddressList(1, 1000, celIndex, celIndex);
        markDataValidationToSheet(helper, sheet, constraint, addressList);
    }

    /**
     * 挂载下拉的列，仅限二级选项.
     */
    private void markLinkedOptionsToSheet(
        DataValidationHelper helper,
        Sheet sheet, Integer rowIndex,
        Integer celIndex, DataValidationConstraint constraint) {
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, celIndex, celIndex);
        markDataValidationToSheet(helper, sheet, constraint, addressList);
    }

    /**
     * 应用数据校验.
     */
    private void markDataValidationToSheet(DataValidationHelper helper, Sheet sheet,
                                           DataValidationConstraint constraint, CellRangeAddressList addressList) {
        // 数据有效性对象
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        // 处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            //数据校验
            dataValidation.setSuppressDropDownArrow(true);
            //错误提示
            dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            dataValidation.createErrorBox("提示", "此值与单元格定义数据不一致");
            dataValidation.setShowErrorBox(true);
            //选定提示
            dataValidation.createPromptBox("填写说明：", "填写内容只能为下拉中数据，其他数据将导致导入失败");
            dataValidation.setShowPromptBox(true);
            sheet.addValidationData(dataValidation);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * 依据列index获取列名英文.
     *
     * @param columnIndex 列index
     * @return 列index所在得英文名
     */
    private String getExcelColumnName(int columnIndex) {
        // 26一循环的次数
        int columnCircleCount = columnIndex / 26;
        // 26一循环内的位置
        int thisCircleColumnIndex = columnIndex % 26;
        // 26一循环的次数大于0，则视为栏名至少两位
        String columnPrefix = columnCircleCount == 0
            ? StrUtil.EMPTY
            : StrUtil.subWithLength(EXCEL_COLUMN_NAME, columnCircleCount - 1, 1);
        // 从26一循环内取对应的栏位名
        String columnNext = StrUtil.subWithLength(EXCEL_COLUMN_NAME, thisCircleColumnIndex, 1);
        // 将二者拼接即为最终的栏位名
        return columnPrefix + columnNext;
    }
}
