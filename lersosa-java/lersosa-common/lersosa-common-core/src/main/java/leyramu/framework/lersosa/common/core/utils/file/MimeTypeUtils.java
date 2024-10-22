package leyramu.framework.lersosa.common.core.utils.file;

/**
 * 媒体类型工具类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class MimeTypeUtils {

    /**
     * 图片类型 PNG
     */
    public static final String IMAGE_PNG = "image/png";

    /**
     * 图片类型 JPG
     */
    public static final String IMAGE_JPG = "image/jpg";

    /**
     * 图片类型 JPEG
     */
    public static final String IMAGE_JPEG = "image/jpeg";

    /**
     * 图片类型 BMP
     */
    public static final String IMAGE_BMP = "image/bmp";

    /**
     * 图片类型 GIF
     */
    public static final String IMAGE_GIF = "image/gif";

    /**
     * 图片后缀
     */
    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    /**
     * 音视频类型
     */
    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    /**
     * 媒体类型
     */
    public static final String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb"};

    /**
     * 视频类型
     */
    public static final String[] VIDEO_EXTENSION = {"mp4", "avi", "rmvb"};

    /**
     * 默认允许上传的文件扩展名
     */
    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            "bmp", "gif", "jpg", "jpeg", "png",
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            "rar", "zip", "gz", "bz2",
            "mp4", "avi", "rmvb",
            "pdf"};

    /**
     * 获取文件类型
     *
     * @param prefix 前缀
     * @return 后缀
     */
    public static String getExtension(String prefix) {
        return switch (prefix) {
            case IMAGE_PNG -> "png";
            case IMAGE_JPG -> "jpg";
            case IMAGE_JPEG -> "jpeg";
            case IMAGE_BMP -> "bmp";
            case IMAGE_GIF -> "gif";
            default -> "";
        };
    }
}
