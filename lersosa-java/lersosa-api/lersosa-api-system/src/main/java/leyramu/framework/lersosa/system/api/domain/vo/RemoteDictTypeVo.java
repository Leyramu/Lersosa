package leyramu.framework.lersosa.system.api.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 字典类型视图对象 sys_dict_type.
 *
 * @author Michelle.Chung
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
@Data
public class RemoteDictTypeVo implements Serializable {

    /**
     * 序列化.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典主键.
     */
    private Long dictId;

    /**
     * 字典名称.
     */
    private String dictName;

    /**
     * 字典类型.
     */
    private String dictType;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 创建时间.
     */
    private Date createTime;
}
