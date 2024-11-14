/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import leyramu.framework.lersosa.common.log.event.OperLogEvent;
import leyramu.framework.lersosa.system.domain.SysOperLog;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志记录业务对象 sys_oper_log.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@AutoMappers({
    @AutoMapper(target = SysOperLog.class, reverseConvertGenerate = false),
    @AutoMapper(target = OperLogEvent.class)
})
public class SysOperLogBo {

    /**
     * 日志主键.
     */
    private Long operId;

    /**
     * 租户编号.
     */
    private String tenantId;

    /**
     * 模块标题.
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）.
     */
    private Integer businessType;

    /**
     * 业务类型数组.
     */
    private Integer[] businessTypes;

    /**
     * 方法名称.
     */
    private String method;

    /**
     * 请求方式.
     */
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）.
     */
    private Integer operatorType;

    /**
     * 操作人员.
     */
    private String operName;

    /**
     * 部门名称.
     */
    private String deptName;

    /**
     * 请求URL.
     */
    private String operUrl;

    /**
     * 主机地址.
     */
    private String operIp;

    /**
     * 操作地点.
     */
    private String operLocation;

    /**
     * 请求参数.
     */
    private String operParam;

    /**
     * 返回参数.
     */
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）.
     */
    private Integer status;

    /**
     * 错误消息.
     */
    private String errorMsg;

    /**
     * 操作时间.
     */
    private Date operTime;

    /**
     * 消耗时间.
     */
    private Long costTime;

    /**
     * 请求参数.
     */
    private Map<String, Object> params = new HashMap<>();
}
