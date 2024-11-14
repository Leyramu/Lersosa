/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.demo.listener;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.excel.context.AnalysisContext;
import leyramu.framework.lersosa.common.core.utils.ValidatorUtils;
import leyramu.framework.lersosa.common.core.validate.AddGroup;
import leyramu.framework.lersosa.common.core.validate.EditGroup;
import leyramu.framework.lersosa.common.excel.core.DefaultExcelListener;
import leyramu.framework.lersosa.common.excel.core.DropDownOptions;
import leyramu.framework.lersosa.demo.domain.vo.ExportDemoVo;

import java.util.List;

/**
 * Excel带下拉框的解析处理器.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public class ExportDemoListener extends DefaultExcelListener<ExportDemoVo> {

    public ExportDemoListener() {
        // 显示使用构造函数，否则将导致空指针
        super(true);
    }

    @Override
    public void invoke(ExportDemoVo data, AnalysisContext context) {
        // 先校验必填
        ValidatorUtils.validate(data, AddGroup.class);

        // 处理级联下拉的部分
        String province = data.getProvince();
        String city = data.getCity();
        String area = data.getArea();
        // 本行用户选择的省
        List<String> thisRowSelectedProvinceOption = DropDownOptions.analyzeOptionValue(province);
        if (thisRowSelectedProvinceOption.size() == 2) {
            String provinceIdStr = thisRowSelectedProvinceOption.get(1);
            if (NumberUtil.isNumber(provinceIdStr)) {
                // 严格要求数据的话可以在这里做与数据库相关的判断
                // 例如判断省信息是否在数据库中存在等，建议结合RedisCache做缓存10s，减少数据库调用
                data.setProvinceId(Integer.parseInt(provinceIdStr));
            }
        }
        // 本行用户选择的市
        List<String> thisRowSelectedCityOption = DropDownOptions.analyzeOptionValue(city);
        if (thisRowSelectedCityOption.size() == 2) {
            String cityIdStr = thisRowSelectedCityOption.get(1);
            if (NumberUtil.isNumber(cityIdStr)) {
                data.setCityId(Integer.parseInt(cityIdStr));
            }
        }
        // 本行用户选择的县
        List<String> thisRowSelectedAreaOption = DropDownOptions.analyzeOptionValue(area);
        if (thisRowSelectedAreaOption.size() == 2) {
            String areaIdStr = thisRowSelectedAreaOption.get(1);
            if (NumberUtil.isNumber(areaIdStr)) {
                data.setAreaId(Integer.parseInt(areaIdStr));
            }
        }

        // 处理完毕以后判断是否符合规则
        ValidatorUtils.validate(data, EditGroup.class);

        // 添加到处理结果中
        getExcelResult().getList().add(data);
    }
}
