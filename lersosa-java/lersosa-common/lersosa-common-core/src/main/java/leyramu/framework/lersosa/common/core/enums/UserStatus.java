package leyramu.framework.lersosa.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    /**
     * 正常
     */
    OK("0", "正常"),

    /**
     * 停用
     */
    DISABLE("1", "停用"),

    /**
     * 删除
     */
    DELETED("2", "删除");

    /**
     * 类型
     */
    private final String code;

    /**
     * 信息
     */
    private final String info;
}
