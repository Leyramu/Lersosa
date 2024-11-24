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

package leyramu.framework.lersosa.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import leyramu.framework.lersosa.common.core.enums.UserStatus;
import leyramu.framework.lersosa.common.core.utils.StreamUtils;
import leyramu.framework.lersosa.common.excel.core.DropDownOptions;
import leyramu.framework.lersosa.common.excel.utils.ExcelUtil;
import leyramu.framework.lersosa.demo.domain.vo.ExportDemoVo;
import leyramu.framework.lersosa.demo.service.IExportExcelService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导出下拉框Excel示例.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Service
@RequiredArgsConstructor
public class ExportExcelServiceImpl implements IExportExcelService {

    @Override
    public void exportWithOptions(HttpServletResponse response) {
        // 创建表格数据，业务中一般通过数据库查询
        List<ExportDemoVo> excelDataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            // 模拟数据库中的一条数据
            ExportDemoVo everyRowData = new ExportDemoVo();
            everyRowData.setNickName("用户-" + i);
            everyRowData.setUserStatus(UserStatus.OK.getCode());
            everyRowData.setGender("1");
            everyRowData.setPhoneNumber(String.format("175%08d", i));
            everyRowData.setEmail(String.format("175%08d", i) + "@163.com");
            everyRowData.setProvinceId(i);
            everyRowData.setCityId(i);
            everyRowData.setAreaId(i);
            excelDataList.add(everyRowData);
        }

        // 通过@ExcelIgnoreUnannotated配合@ExcelProperty合理显示需要的列
        // 并通过@DropDown注解指定下拉值，或者通过创建ExcelOptions来指定下拉框
        // 使用ExcelOptions时建议指定列index，防止出现下拉列解析不对齐

        // 首先从数据库中查询下拉框内的可选项
        // 这里模拟查询结果
        List<DemoCityData> provinceList = getProvinceList(),
            cityList = getCityList(provinceList),
            areaList = getAreaList(cityList);
        int provinceIndex = 5, cityIndex = 6, areaIndex = 7;

        DropDownOptions provinceToCity = DropDownOptions.buildLinkedOptions(
            provinceList,
            provinceIndex,
            cityList,
            cityIndex,
            DemoCityData::getId,
            DemoCityData::getPid,
            everyOptions -> DropDownOptions.createOptionValue(
                everyOptions.getName(),
                everyOptions.getId()
            )
        );

        DropDownOptions cityToArea = DropDownOptions.buildLinkedOptions(
            cityList,
            cityIndex,
            areaList,
            areaIndex,
            DemoCityData::getId,
            DemoCityData::getPid,
            everyOptions -> DropDownOptions.createOptionValue(
                everyOptions.getName(),
                everyOptions.getId()
            )
        );

        // 把所有的下拉框存储
        List<DropDownOptions> options = new ArrayList<>();
        options.add(provinceToCity);
        options.add(cityToArea);

        // 到此为止所有的下拉框可选项已全部配置完毕

        // 接下来需要将Excel中的展示数据转换为对应的下拉选
        List<ExportDemoVo> outList = StreamUtils.toList(excelDataList, everyRowData -> {
            // 只需要处理没有使用@ExcelDictFormat注解的下拉框
            // 一般来说，可以直接在数据库查询即查询出省市县信息，这里通过模拟操作赋值
            everyRowData.setProvince(buildOptions(provinceList, everyRowData.getProvinceId()));
            everyRowData.setCity(buildOptions(cityList, everyRowData.getCityId()));
            everyRowData.setArea(buildOptions(areaList, everyRowData.getAreaId()));
            return everyRowData;
        });

        ExcelUtil.exportExcel(outList, "下拉框示例", ExportDemoVo.class, response, options);
    }

    private String buildOptions(List<DemoCityData> cityDataList, Integer id) {
        Map<Integer, List<DemoCityData>> groupByIdMap =
            cityDataList.stream().collect(Collectors.groupingBy(DemoCityData::getId));
        if (groupByIdMap.containsKey(id)) {
            DemoCityData demoCityData = groupByIdMap.get(id).getFirst();
            return DropDownOptions.createOptionValue(demoCityData.getName(), demoCityData.getId());
        } else {
            return StrUtil.EMPTY;
        }
    }

    /**
     * 模拟查询数据库操作.
     *
     * @return /
     */
    private List<DemoCityData> getProvinceList() {
        List<DemoCityData> provinceList = new ArrayList<>();

        // 实际业务中一般采用数据库读取的形式，这里直接拼接创建
        provinceList.add(new DemoCityData(0, null, "安徽省"));
        provinceList.add(new DemoCityData(1, null, "江苏省"));

        return provinceList;
    }

    /**
     * 模拟查找数据库操作，需要连带查询出省的数据.
     *
     * @param provinceList 模拟的父省数据
     * @return /
     */
    private List<DemoCityData> getCityList(List<DemoCityData> provinceList) {
        List<DemoCityData> cityList = new ArrayList<>();

        // 实际业务中一般采用数据库读取的形式，这里直接拼接创建
        cityList.add(new DemoCityData(0, 0, "合肥市"));
        cityList.add(new DemoCityData(1, 0, "芜湖市"));
        cityList.add(new DemoCityData(2, 1, "南京市"));
        cityList.add(new DemoCityData(3, 1, "无锡市"));
        cityList.add(new DemoCityData(4, 1, "徐州市"));

        selectParentData(provinceList, cityList);

        return cityList;
    }

    /**
     * 模拟查找数据库操作，需要连带查询出市的数据.
     *
     * @param cityList 模拟的父市数据
     * @return /
     */
    private List<DemoCityData> getAreaList(List<DemoCityData> cityList) {
        List<DemoCityData> areaList = new ArrayList<>();

        // 实际业务中一般采用数据库读取的形式，这里直接拼接创建
        areaList.add(new DemoCityData(0, 0, "瑶海区"));
        areaList.add(new DemoCityData(1, 0, "庐江区"));
        areaList.add(new DemoCityData(2, 1, "南宁县"));
        areaList.add(new DemoCityData(3, 1, "镜湖区"));
        areaList.add(new DemoCityData(4, 2, "玄武区"));
        areaList.add(new DemoCityData(5, 2, "秦淮区"));
        areaList.add(new DemoCityData(6, 3, "宜兴市"));
        areaList.add(new DemoCityData(7, 3, "新吴区"));
        areaList.add(new DemoCityData(8, 4, "鼓楼区"));
        areaList.add(new DemoCityData(9, 4, "丰县"));

        selectParentData(cityList, areaList);

        return areaList;
    }

    /**
     * 模拟数据库的查询父数据操作.
     *
     * @param parentList /
     * @param sonList    /
     */
    private void selectParentData(List<DemoCityData> parentList, List<DemoCityData> sonList) {
        Map<Integer, List<DemoCityData>> parentGroupByIdMap =
            parentList.stream().collect(Collectors.groupingBy(DemoCityData::getId));

        sonList.forEach(everySon -> {
            if (parentGroupByIdMap.containsKey(everySon.getPid())) {
                everySon.setPData(parentGroupByIdMap.get(everySon.getPid()).getFirst());
            }
        });
    }

    /**
     * 模拟的数据库省市县.
     */
    @Data
    private static class DemoCityData {
        /**
         * 数据库id字段.
         */
        private Integer id;
        /**
         * 数据库pid字段.
         */
        private Integer pid;
        /**
         * 数据库name字段.
         */
        private String name;
        /**
         * MyBatisPlus连带查询父数据.
         */
        private DemoCityData pData;

        public DemoCityData(Integer id, Integer pid, String name) {
            this.id = id;
            this.pid = pid;
            this.name = name;
        }
    }
}
