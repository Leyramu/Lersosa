package leyramu.framework.lersosa.gateway.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.captcha")
public class CaptchaProperties {

    /**
     * 验证码开关
     * -- GETTER --
     * 获取验证码开关
     * -- SETTER --
     * 设置验证码开关
     */
    private Boolean enabled;

    /**
     * 验证码类型（math 数组计算 char 字符）
     * -- GETTER --
     * 获取验证码类型
     * -- SETTER --
     * 设置验证码类型
     */
    private String type;
}
