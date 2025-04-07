package leyramu.framework.lersosa.common.core.validate.enumd;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import leyramu.framework.lersosa.common.core.utils.StringUtils;
import leyramu.framework.lersosa.common.core.utils.reflect.ReflectUtils;

/**
 * 自定义枚举校验注解实现
 *
 * @author 秋辞未寒
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
public class EnumPatternValidator implements ConstraintValidator<EnumPattern, String> {

    /**
     * 注解.
     */
    private EnumPattern annotation;

    /**
     * 初始化.
     *
     * @param annotation 注解
     */
    @Override
    public void initialize(EnumPattern annotation) {
        ConstraintValidator.super.initialize(annotation);
        this.annotation = annotation;
    }

    /**
     * 校验.
     *
     * @param value                   待校验的值
     * @param constraintValidatorContext 上下文
     * @return true/false
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isNotBlank(value)) {
            String fieldName = annotation.fieldName();
            for (Object e : annotation.type().getEnumConstants()) {
                if (value.equals(ReflectUtils.invokeGetter(e, fieldName))) {
                    return true;
                }
            }
        }
        return false;
    }
}
