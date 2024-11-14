/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.excel.core;

import cn.hutool.core.util.StrUtil;
import leyramu.framework.lersosa.common.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Excel下拉可选项.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")
public class DropDownOptions {

    /**
     * 分隔符.
     */
    private static final String DELIMITER = "_";
    /**
     * 一级下拉所在列index，从0开始算.
     */
    private int index = 0;
    /**
     * 二级下拉所在的index，从0开始算，不能与一级相同.
     */
    private int nextIndex = 0;
    /**
     * 一级下拉所包含的数据.
     */
    private List<String> options = new ArrayList<>();
    /**
     * 二级下拉所包含的数据Map.
     */
    private Map<String, List<String>> nextOptions = new HashMap<>();

    /**
     * 创建只有一级的下拉选.
     */
    public DropDownOptions(int index, List<String> options) {
        this.index = index;
        this.options = options;
    }

    /**
     * 创建每个选项可选值.
     *
     * @param vars 可选值内包含的参数
     * @return 合规的可选值
     */
    public static String createOptionValue(Object... vars) {
        StringBuilder stringBuffer = new StringBuilder();
        String regex = "^[\\S\\d\\u4e00-\\u9fa5]+$";
        for (int i = 0; i < vars.length; i++) {
            String var = StrUtil.trimToEmpty(String.valueOf(vars[i]));
            if (!var.matches(regex)) {
                throw new ServiceException("选项数据不符合规则，仅允许使用中英文字符以及数字");
            }
            stringBuffer.append(var);
            if (i < vars.length - 1) {
                // 直至最后一个前，都以_作为切割线
                stringBuffer.append(DELIMITER);
            }
        }
        if (stringBuffer.toString().matches("^\\d_*$")) {
            throw new ServiceException("禁止以数字开头");
        }
        return stringBuffer.toString();
    }

    /**
     * 将处理后合理的可选值解析为原始的参数.
     *
     * @param option 经过处理后的合理的可选项
     * @return 原始的参数
     */
    public static List<String> analyzeOptionValue(String option) {
        return StrUtil.split(option, DELIMITER, true, true);
    }

    /**
     * 创建级联下拉选项.
     *
     * @param parentList                  父实体可选项原始数据
     * @param parentIndex                 父下拉选位置
     * @param sonList                     子实体可选项原始数据
     * @param sonIndex                    子下拉选位置
     * @param parentHowToGetIdFunction    父类如何获取唯一标识
     * @param sonHowToGetParentIdFunction 子类如何获取父类的唯一标识
     * @param howToBuildEveryOption       如何生成下拉选内容
     * @return 级联下拉选项
     */
    public static <T> DropDownOptions buildLinkedOptions(
        List<T> parentList,
        int parentIndex,
        List<T> sonList,
        int sonIndex,
        Function<T, Number> parentHowToGetIdFunction,
        Function<T, Number> sonHowToGetParentIdFunction,
        Function<T, String> howToBuildEveryOption) {
        DropDownOptions parentLinkSonOptions = new DropDownOptions();
        // 先创建父类的下拉
        parentLinkSonOptions.setIndex(parentIndex);
        parentLinkSonOptions.setOptions(
            parentList.stream()
                .map(howToBuildEveryOption)
                .collect(Collectors.toList())
        );
        // 提取父-子级联下拉
        Map<String, List<String>> sonOptions = new HashMap<>();
        // 父级依据自己的ID分组
        Map<Number, List<T>> parentGroupByIdMap =
            parentList.stream().collect(Collectors.groupingBy(parentHowToGetIdFunction));
        // 遍历每个子集，提取到Map中
        sonList.forEach(everySon -> {
            if (parentGroupByIdMap.containsKey(sonHowToGetParentIdFunction.apply(everySon))) {
                // 找到对应的上级
                T parentObj = parentGroupByIdMap.get(sonHowToGetParentIdFunction.apply(everySon)).getFirst();
                // 提取名称和ID作为Key
                String key = howToBuildEveryOption.apply(parentObj);
                // Key对应的Value
                List<String> thisParentSonOptionList;
                if (sonOptions.containsKey(key)) {
                    thisParentSonOptionList = sonOptions.get(key);
                } else {
                    thisParentSonOptionList = new ArrayList<>();
                    sonOptions.put(key, thisParentSonOptionList);
                }
                // 往Value中添加当前子集选项
                thisParentSonOptionList.add(howToBuildEveryOption.apply(everySon));
            }
        });
        parentLinkSonOptions.setNextIndex(sonIndex);
        parentLinkSonOptions.setNextOptions(sonOptions);
        return parentLinkSonOptions;
    }
}
