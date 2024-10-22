package leyramu.framework.lersosa.common.core.exception.auth;

import org.apache.commons.lang3.StringUtils;

import java.io.Serial;

/**
 * 未能通过的角色认证异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class NotRoleException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造
     *
     * @param role 角色
     */
    public NotRoleException(String role) {
        super(role);
    }

    /**
     * 构造
     *
     * @param roles 角色组
     */
    public NotRoleException(String[] roles) {
        super(StringUtils.join(roles, ","));
    }
}
