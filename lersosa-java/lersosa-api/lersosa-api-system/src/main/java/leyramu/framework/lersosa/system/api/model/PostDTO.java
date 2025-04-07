package leyramu.framework.lersosa.system.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 岗位
 *
 * @author AprilWind
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
@Data
@NoArgsConstructor
public class PostDTO implements Serializable {

    /**
     * 序列化.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID.
     */
    private Long postId;

    /**
     * 部门id.
     */
    private Long deptId;

    /**
     * 岗位编码.
     */
    private String postCode;

    /**
     * 岗位名称.
     */
    private String postName;

    /**
     * 岗位类别编码.
     */
    private String postCategory;
}
