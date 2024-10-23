/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.api.factory;

import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.system.api.RemoteLogService;
import leyramu.framework.lersosa.system.api.domain.SysLogininfor;
import leyramu.framework.lersosa.system.api.domain.SysOperLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 日志服务降级处理
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/22
 */
@Slf4j
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {

    /**
     * 处理
     *
     * @param throwable 异常
     * @return RemoteLogService
     */
    @Override
    public RemoteLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService() {

            /**
             * 保存操作日志
             *
             * @param sysOperLog 操作日志对象
             * @return 结果
             */
            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog, String source) {
                return R.fail("保存操作日志失败:" + throwable.getMessage());
            }

            /**
             * 保存访问日志
             *
             * @param sysLogininfor 访问日志对象
             * @return 结果
             */
            @Override
            public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source) {
                return R.fail("保存登录日志失败:" + throwable.getMessage());
            }
        };
    }
}
