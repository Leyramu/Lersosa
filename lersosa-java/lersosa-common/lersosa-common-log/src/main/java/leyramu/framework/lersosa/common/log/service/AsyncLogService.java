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
