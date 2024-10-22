package leyramu.framework.lersosa.common.core.exception.user;

import java.io.Serial;

/**
 * 验证码失效异常类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class CaptchaExpireException extends UserException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     */
    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}
