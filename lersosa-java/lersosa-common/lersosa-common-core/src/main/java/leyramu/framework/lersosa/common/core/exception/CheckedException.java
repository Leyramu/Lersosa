package leyramu.framework.lersosa.common.core.exception;

import java.io.Serial;

/**
 * 检查异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class CheckedException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造一个新创建的 CheckedException
     *
     * @param message 异常信息
     */
    public CheckedException(String message) {
        super(message);
    }

    /**
     * 构造一个新创建的 CheckedException
     *
     * @param cause 异常信息
     */
    public CheckedException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个新创建的 CheckedException
     *
     * @param message 异常信息
     * @param cause   异常信息
     */
    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个新创建的 CheckedException
     *
     * @param message            异常信息
     * @param cause              异常信息
     * @param enableSuppression  是否启用 suppression
     * @param writableStackTrace 是否启用 writableStackTrace
     */
    public CheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
