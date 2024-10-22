package leyramu.framework.lersosa.gateway.api.config;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Swagger文档注册器
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/18
 */
@Component
@RequiredArgsConstructor
public class SwaggerDocRegister extends Subscriber<InstancesChangeEvent> {

    /**
     * SwaggerUI配置属性
     */
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    /**
     * 服务发现客户端
     */
    private final DiscoveryClient discoveryClient;

    /**
     * 排除路由
     */
    private final static String[] EXCLUDE_ROUTES = new String[]{"lersosa-gateway", "lersosa-service-auth", "lersosa-service-file", "lersosa-visual-monitor"};

    /**
     * 事件回调方法，处理InstancesChangeEvent事件
     *
     * @param event 事件对象
     */
    @Override
    public void onEvent(InstancesChangeEvent event) {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrlSet = discoveryClient.getServices()
                .stream()
                .flatMap(serviceId -> discoveryClient.getInstances(serviceId).stream())
                .filter(instance -> !StringUtils.equalsAnyIgnoreCase(instance.getServiceId(), EXCLUDE_ROUTES))
                .map(instance -> {
                    AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
                    swaggerUrl.setName(instance.getServiceId());
                    swaggerUrl.setUrl(String.format("/%s/v3/api-docs", instance.getServiceId()));
                    return swaggerUrl;
                })
                .collect(Collectors.toSet());

        swaggerUiConfigProperties.setUrls(swaggerUrlSet);
    }

    /**
     * 订阅类型方法，返回订阅的事件类型
     *
     * @return 订阅的事件类型
     */
    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }
}
