package leyramu.framework.lersosa.common.dubbo.handler;

import leyramu.framework.lersosa.common.core.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Dubbo异常处理器
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
@Slf4j
@RestControllerAdvice
public class DubboExceptionHandler {

    /**
     * 主键或UNIQUE索引，数据重复异常.
     *
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(RpcException.class)
    public Result<Void> handleDubboException(RpcException e) {
        log.error("RPC异常: {}", e.getMessage());
        return Result.fail("RPC异常，请联系管理员确认");
    }
}
