package leyramu.framework.lersosa.common.core.exception.user;

import leyramu.framework.lersosa.common.core.exception.base.BaseException;

import java.io.Serial;

/**
 * 用户信息异常类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class UserException extends BaseException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     *
     * @param code 异常代码
     * @param args 异常参数
     */
    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
