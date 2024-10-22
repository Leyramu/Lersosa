package leyramu.framework.lersosa.common.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务调度通用常量
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public class ScheduleConstants {

    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /**
     * 执行目标key
     */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /**
     * 默认
     */
    public static final String MISFIRE_DEFAULT = "0";

    /**
     * 立即触发执行
     */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /**
     * 触发一次执行
     */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /**
     * 不触发立即执行
     */
    public static final String MISFIRE_DO_NOTHING = "3";

    @Getter
    @AllArgsConstructor
    public enum Status {

        /**
         * 正常
         */
        NORMAL("0"),

        /**
         * 暂停
         */
        PAUSE("1");

        private final String value;
    }
}
