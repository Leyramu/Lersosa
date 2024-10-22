package leyramu.framework.lersosa.common.core.exception.auth;

import org.apache.commons.lang3.StringUtils;

import java.io.Serial;

/**
 * 未能通过的权限认证异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class NotPermissionException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建未通过权限认证异常
     *
     * @param permission 权限标识
     */
    public NotPermissionException(String permission) {
        super(permission);
    }

    /**
     * 创建未通过权限认证异常
     *
     * @param permissions 权限标识组
     */
    public NotPermissionException(String[] permissions) {
        super(StringUtils.join(permissions, ","));
    }
}
