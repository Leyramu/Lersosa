package leyramu.framework.lersosa.common.core.exception.auth;

import java.io.Serial;

/**
 * 未能通过的登录认证异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class NotLoginException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造
     *
     * @param message 异常消息
     */
    public NotLoginException(String message) {
        super(message);
    }
}
