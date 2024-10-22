package leyramu.framework.lersosa.gateway.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * XSS跨站脚本配置
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/18
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.xss")
public class XssProperties {

    /**
     * Xss开关
     * -- GETTER --
     * 获取是否开启Xss
     * -- SETTER --
     * 设置是否开启Xss
     */
    private Boolean enabled;

    /**
     * 排除路径
     * -- GETTER --
     * 获取排除路径
     * -- SETTER --
     * 设置排除路径
     */
    private List<String> excludeUrls = new ArrayList<>();
}
