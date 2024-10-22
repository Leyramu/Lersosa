package leyramu.framework.lersosa.common.core.exception;

import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 权限异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@NoArgsConstructor
public class PreAuthorizeException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;
}
