package leyramu.framework.lersosa.common.core.exception.file;

import leyramu.framework.lersosa.common.core.exception.base.BaseException;

import java.io.Serial;

/**
 * 文件信息异常类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class FileException extends BaseException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     *
     * @param code 错误码
     * @param args 错误参数
     * @param msg  错误信息
     */
    public FileException(String code, Object[] args, String msg) {
        super("file", code, args, msg);
    }
}
