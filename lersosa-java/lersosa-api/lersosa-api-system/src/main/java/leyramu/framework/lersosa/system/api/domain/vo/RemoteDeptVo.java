package leyramu.framework.lersosa.system.api.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 部门.
 *
 * @author AprilWind
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
@Data
@NoArgsConstructor
public class RemoteDeptVo implements Serializable {

    /**
     * 序列化.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID.
     */
    private Long deptId;

    /**
     * 父部门ID.
     */
    private Long parentId;

    /**
     * 部门名称.
     */
    private String deptName;
}
