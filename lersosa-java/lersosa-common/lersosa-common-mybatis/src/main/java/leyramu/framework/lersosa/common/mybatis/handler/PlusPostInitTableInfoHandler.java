package leyramu.framework.lersosa.common.mybatis.handler;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.handlers.PostInitTableInfoHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import leyramu.framework.lersosa.common.core.utils.SpringUtils;
import leyramu.framework.lersosa.common.core.utils.reflect.ReflectUtils;
import org.apache.ibatis.session.Configuration;

/**
 * 修改表信息初始化方式.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
public class PlusPostInitTableInfoHandler implements PostInitTableInfoHandler {

    /**
     * 修改表信息初始化方式.
     *
     * @param tableInfo     表信息
     * @param configuration 配置
     */
    @Override
    public void postTableInfo(TableInfo tableInfo, Configuration configuration) {
        String flag = SpringUtils.getProperty("mybatis-plus.enableLogicDelete", "true");
        // 只有关闭时 统一设置false 为true时mp自动判断不处理
        if (!Convert.toBool(flag)) {
            ReflectUtils.setFieldValue(tableInfo, "withLogicDelete", false);
        }
    }
}
