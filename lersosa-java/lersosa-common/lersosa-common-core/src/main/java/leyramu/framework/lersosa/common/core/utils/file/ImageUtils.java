package leyramu.framework.lersosa.common.core.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * 图片处理工具类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@Slf4j
public class ImageUtils {

    /**
     * 获取图片
     *
     * @param imagePath 图片地址
     * @return 图片
     */
    public static byte[] getImage(String imagePath) {
        InputStream is = getFile(imagePath);
        try {
            if (is != null) {
                return IOUtils.toByteArray(is);
            }
        } catch (Exception e) {
            log.error("图片加载异常", e);
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    /**
     * 获取图像输入流
     *
     * @param imagePath 图片地址
     * @return BufferedImage
     */
    public static InputStream getFile(String imagePath) {
        try {
            byte[] result = readFile(imagePath);
            if (result != null) {
                result = Arrays.copyOf(result, result.length);
            }
            if (result != null) {
                return new ByteArrayInputStream(result);
            }
        } catch (Exception e) {
            log.error("获取图片异常", e);
        }
        return null;
    }

    /**
     * 读取文件为字节数据
     *
     * @param url 地址
     * @return 字节数据
     */
    public static byte[] readFile(String url) {
        InputStream in = null;
        try {
            URI uri = new URI(url);
            URL urlObj = uri.toURL();
            URLConnection urlConnection = urlObj.openConnection();
            urlConnection.setConnectTimeout(30 * 1000);
            urlConnection.setReadTimeout(60 * 1000);
            urlConnection.setDoInput(true);
            in = urlConnection.getInputStream();
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            log.error("访问文件异常", e);
            return null;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
