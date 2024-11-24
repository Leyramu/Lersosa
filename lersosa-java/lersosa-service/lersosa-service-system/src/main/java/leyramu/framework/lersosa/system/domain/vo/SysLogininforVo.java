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
import leyramu.framework.lersosa.system.domain.SysLogininfor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统访问记录视图对象 sys_logininfor.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysLogininfor.class)
public class SysLogininforVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 访问ID.
     */
    @ExcelProperty(value = "序号")
    private Long infoId;

    /**
     * 租户编号.
     */
    private String tenantId;

    /**
     * 用户账号.
     */
    @ExcelProperty(value = "用户账号")
    private String userName;

    /**
     * 客户端.
     */
    @ExcelProperty(value = "客户端")
    private String clientKey;

    /**
     * 设备类型.
     */
    @ExcelProperty(value = "设备类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_device_type")
    private String deviceType;

    /**
     * 登录状态（0成功 1失败）.
     */
    @ExcelProperty(value = "登录状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_common_status")
    private String status;

    /**
     * 登录IP地址.
     */
    @ExcelProperty(value = "登录地址")
    private String ipaddr;

    /**
     * 登录地点.
     */
    @ExcelProperty(value = "登录地点")
    private String loginLocation;

    /**
     * 浏览器类型.
     */
    @ExcelProperty(value = "浏览器")
    private String browser;

    /**
     * 操作系统.
     */
    @ExcelProperty(value = "操作系统")
    private String os;


    /**
     * 提示消息.
     */
    @ExcelProperty(value = "提示消息")
    private String msg;

    /**
     * 访问时间.
     */
    @ExcelProperty(value = "访问时间")
    private Date loginTime;
}
