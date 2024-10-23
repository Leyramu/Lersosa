/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.log.service;

import leyramu.framework.lersosa.common.core.constant.SecurityConstants;
import leyramu.framework.lersosa.system.api.RemoteLogService;
import leyramu.framework.lersosa.system.api.domain.SysOperLog;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/22
 */
@Service
@RequiredArgsConstructor
public class AsyncLogService {

    /**
     * 远程调用日志服务
     */
    private final RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     *
     * @param sysOperLog 日志实体
     * @throws Exception 异常
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) throws Exception {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
