package leyramu.framework.lersosa.common.log.filter;

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import lombok.NoArgsConstructor;

/**
 * 排除JSON敏感属性
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/22
 */
@NoArgsConstructor
public class PropertyPreExcludeFilter extends SimplePropertyPreFilter {

    /**
     * 添加排除属性
     *
     * @param filters 过滤属性
     */
    public PropertyPreExcludeFilter addExcludes(String... filters) {
        for (String filter : filters) {
            this.getExcludes().add(filter);
        }
        return this;
    }
}
