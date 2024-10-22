package leyramu.framework.lersosa.common.core.exception;

import java.io.Serial;

/**
 * 内部认证异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class InnerAuthException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造
     *
     * @param message 异常信息
     */
    public InnerAuthException(String message) {
        super(message);
    }
}
