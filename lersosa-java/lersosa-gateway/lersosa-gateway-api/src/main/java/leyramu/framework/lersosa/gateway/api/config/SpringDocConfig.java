package leyramu.framework.lersosa.gateway.api.config;

import com.alibaba.nacos.common.notify.NotifyCenter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc配置类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/18
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnProperty(value = "springdoc.api-docs.enabled", matchIfMissing = true)
public class SpringDocConfig implements InitializingBean {

    /**
     * Swagger UI配置属性
     */
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    /**
     * 服务发现客户端
     */
    private final DiscoveryClient discoveryClient;

    /**
     * 在初始化后调用的方法
     */
    @Override
    public void afterPropertiesSet() {
        NotifyCenter.registerSubscriber(new SwaggerDocRegister(swaggerUiConfigProperties, discoveryClient));
    }

    /**
     * 创建并注册 SwaggerDocRegister Bean
     */
    @Bean
    public SwaggerDocRegister swaggerDocRegister() {
        return new SwaggerDocRegister(swaggerUiConfigProperties, discoveryClient);
    }
}
