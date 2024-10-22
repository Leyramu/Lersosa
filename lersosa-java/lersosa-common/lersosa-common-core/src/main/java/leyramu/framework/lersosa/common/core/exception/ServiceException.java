package leyramu.framework.lersosa.common.core.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 业务异常
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@NoArgsConstructor
public final class ServiceException extends RuntimeException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    @Getter
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    @Getter
    private String detailMessage;

    /**
     * 业务异常
     *
     * @param message 错误提示
     */
    public ServiceException(String message) {
        this.message = message;
    }

    /**
     * 业务异常
     *
     * @param message 错误提示
     * @param code    错误码
     */
    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    /**
     * 获取提示信息
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 设置提示信息
     *
     * @param message 提示信息
     * @return this
     */
    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置明细信息
     *
     * @param detailMessage 明细信息
     * @return this
     */
    public ServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}
