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

package leyramu.framework.lersosa.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import leyramu.framework.lersosa.common.excel.annotation.ExcelDictFormat;
import leyramu.framework.lersosa.common.excel.convert.ExcelDictConvert;
import leyramu.framework.lersosa.system.domain.SysOperLog;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志记录视图对象 sys_oper_log.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysOperLog.class)
public class SysOperLogVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键.
     */
    @ExcelProperty(value = "日志主键")
    private Long operId;

    /**
     * 租户编号.
     */
    private String tenantId;

    /**
     * 模块标题.
     */
    @ExcelProperty(value = "操作模块")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）.
     */
    @ExcelProperty(value = "业务类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_oper_type")
    private Integer businessType;

    /**
     * 业务类型数组.
     */
    private Integer[] businessTypes;

    /**
     * 方法名称.
     */
    @ExcelProperty(value = "请求方法")
    private String method;

    /**
     * 请求方式.
     */
    @ExcelProperty(value = "请求方式")
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）.
     */
    @ExcelProperty(value = "操作类别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=其它,1=后台用户,2=手机端用户")
    private Integer operatorType;

    /**
     * 操作人员.
     */
    @ExcelProperty(value = "操作人员")
    private String operName;

    /**
     * 部门名称.
     */
    @ExcelProperty(value = "部门名称")
    private String deptName;

    /**
     * 请求URL.
     */
    @ExcelProperty(value = "请求地址")
    private String operUrl;

    /**
     * 主机地址.
     */
    @ExcelProperty(value = "操作地址")
    private String operIp;

    /**
     * 操作地点.
     */
    @ExcelProperty(value = "操作地点")
    private String operLocation;

    /**
     * 请求参数.
     */
    @ExcelProperty(value = "请求参数")
    private String operParam;

    /**
     * 返回参数.
     */
    @ExcelProperty(value = "返回参数")
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）.
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_common_status")
    private Integer status;

    /**
     * 错误消息.
     */
    @ExcelProperty(value = "错误消息")
    private String errorMsg;

    /**
     * 操作时间.
     */
    @ExcelProperty(value = "操作时间")
    private Date operTime;

    /**
     * 消耗时间.
     */
    @ExcelProperty(value = "消耗时间")
    private Long costTime;
}
