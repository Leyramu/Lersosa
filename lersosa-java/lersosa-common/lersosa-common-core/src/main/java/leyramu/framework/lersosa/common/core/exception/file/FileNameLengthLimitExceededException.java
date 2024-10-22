package leyramu.framework.lersosa.common.core.exception.file;

import java.io.Serial;

/**
 * 文件名称超长限制异常类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class FileNameLengthLimitExceededException extends FileException {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     *
     * @param defaultFileNameLength 默认文件名长度
     */
    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength}, "the filename is too long");
    }
}
