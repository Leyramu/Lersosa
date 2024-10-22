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
