package leyramu.framework.lersosa.system.api.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务受让人.
 *
 * @author AprilWind
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
@Data
@NoArgsConstructor
public class RemoteTaskAssigneeVo implements Serializable {

    /**
     * 序列化.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总大小.
     */
    private Long total = 0L;

    /**
     * 任务受让人列表.
     */
    private List<TaskHandler> list;

    /**
     * 构造函数.
     *
     * @param total 总大小
     * @param list  任务受让人列表
     */
    public RemoteTaskAssigneeVo(Long total, List<TaskHandler> list) {
        this.total = total;
        this.list = list;
    }

    /**
     * 将源列表转换为 TaskHandler 列表.
     *
     * @param <T>              通用类型
     * @param sourceList       待转换的源列表
     * @param storageId        提取 storageId 的函数
     * @param handlerCode      提取 handlerCode 的函数
     * @param handlerName      提取 handlerName 的函数
     * @param groupName        提取 groupName 的函数
     * @param createTimeMapper 提取 createTime 的函数
     * @return 转换后的 TaskHandler 列表
     */
    public static <T> List<TaskHandler> convertToHandlerList(
        List<T> sourceList,
        Function<T, Long> storageId,
        Function<T, String> handlerCode,
        Function<T, String> handlerName,
        Function<T, Long> groupName,
        Function<T, Date> createTimeMapper) {
        return sourceList.stream()
            .map(item -> new TaskHandler(
                String.valueOf(storageId.apply(item)),
                handlerCode.apply(item),
                handlerName.apply(item),
                groupName != null ? String.valueOf(groupName.apply(item)) : null,
                createTimeMapper.apply(item)
            )).collect(Collectors.toList());
    }

    /**
     * 任务受让人.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskHandler implements Serializable {

        /**
         * 序列化.
         */
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 主键.
         */
        private String storageId;

        /**
         * 权限编码.
         */
        private String handlerCode;

        /**
         * 权限名称.
         */
        private String handlerName;

        /**
         * 权限分组.
         */
        private String groupName;

        /**
         * 创建时间.
         */
        private Date createTime;
    }
}
