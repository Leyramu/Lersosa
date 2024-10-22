package leyramu.framework.lersosa.common.core.exception.file;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serial;

/**
 * 文件上传异常类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class FileUploadException extends Exception {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 异常堆栈
     */
    private final Throwable cause;

    /**
     * 默认构造函数
     */
    public FileUploadException() {
        this(null, null);
    }

    /**
     * 构造函数
     *
     * @param msg 异常信息
     */
    public FileUploadException(final String msg) {
        this(msg, null);
    }

    /**
     * 构造函数
     *
     * @param msg   异常信息
     * @param cause 异常堆栈
     */
    public FileUploadException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    /**
     * 重写printStackTrace方法，打印异常堆栈信息
     *
     * @param stream 异常堆栈信息
     */
    @Override
    public void printStackTrace(PrintStream stream) {
        super.printStackTrace(stream);
        if (cause != null) {
            stream.println("Caused by:");
            cause.printStackTrace(stream);
        }
    }

    /**
     * 重写printStackTrace方法，打印异常堆栈信息
     *
     * @param writer 异常堆栈信息
     */
    @Override
    public void printStackTrace(PrintWriter writer) {
        super.printStackTrace(writer);
        if (cause != null) {
            writer.println("Caused by:");
            cause.printStackTrace(writer);
        }
    }

    /**
     * 获取异常堆栈信息
     *
     * @return 异常堆栈信息
     */
    @Override
    public Throwable getCause() {
        return cause;
    }
}
