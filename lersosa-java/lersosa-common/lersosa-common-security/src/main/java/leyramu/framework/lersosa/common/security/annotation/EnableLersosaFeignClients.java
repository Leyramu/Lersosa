package leyramu.framework.lersosa.common.security.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * 自定义feign注解，添加basePackages路径
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableLersosaFeignClients {

    /**
     * {@link #basePackages（）} 属性的别名
     *
     * @return 要扫描的基础软件包
     */
    String[] value() default {};

    /**
     * 要扫描的基础软件包
     *
     * @return 要扫描的基础软件包
     */
    String[] basePackages() default {"leyramu.framework.lersosa"};

    /**
     * 要扫描的基础包
     *
     * @return 要扫描的基础包
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * 默认配置
     *
     * @return 默认配置
     */
    Class<?>[] defaultConfiguration() default {};

    /**
     * Feign客户端
     *
     * @return Feign客户端
     */
    Class<?>[] clients() default {};
}
