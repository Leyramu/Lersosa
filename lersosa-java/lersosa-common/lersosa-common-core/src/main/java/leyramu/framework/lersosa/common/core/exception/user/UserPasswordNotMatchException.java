package leyramu.framework.lersosa.common.core.exception.user;

import java.io.Serial;

/**
 * 用户密码不正确或不符合规范异常类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class UserPasswordNotMatchException extends UserException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     */
    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}
