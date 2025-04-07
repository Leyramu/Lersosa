package leyramu.framework.lersosa.common.core.validate.enumd;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义枚举校验.
 *
 * @author 秋辞未寒
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(EnumPattern.List.class)
@Constraint(validatedBy = {EnumPatternValidator.class})
public @interface EnumPattern {

    /**
     * 需要校验的枚举类型.
     */
    Class<? extends Enum<?>> type();

    /**
     * 枚举类型校验值字段名称.
     */
    String fieldName();

    /**
     * 默认错误提示信息.
     */
    String message() default "输入值不在枚举范围内";

    /**
     * 分组.
     */
    Class<?>[] groups() default {};

    /**
     * 负载.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 默认分组.
     */
    @Documented
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @interface List {
        EnumPattern[] value();
    }
}
