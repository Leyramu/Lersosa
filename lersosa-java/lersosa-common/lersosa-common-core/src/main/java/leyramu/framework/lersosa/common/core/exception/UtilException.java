package leyramu.framework.lersosa.common.core.exception;

import java.io.Serial;

/**
 * 工具类异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class UtilException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 8247610319171014183L;

    /**
     * 工具类异常
     *
     * @param e 异常信息
     */
    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    /**
     * 工具类异常
     *
     * @param message 异常信息
     */
    public UtilException(String message) {
        super(message);
    }

    /**
     * 工具类异常
     *
     * @param message   异常信息
     * @param throwable 异常
     */
    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
