package leyramu.framework.lersosa.common.core.exception;

import java.io.Serial;

/**
 * 验证码错误异常类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class CaptchaException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     *
     * @param msg 错误信息
     */
    public CaptchaException(String msg) {
        super(msg);
    }
}
