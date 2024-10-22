package leyramu.framework.lersosa.common.security.annotation;

import java.lang.annotation.*;

/**
 * 内部认证注解
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerAuth {

    /**
     * 是否校验用户信息
     */
    boolean isUser() default false;
}
