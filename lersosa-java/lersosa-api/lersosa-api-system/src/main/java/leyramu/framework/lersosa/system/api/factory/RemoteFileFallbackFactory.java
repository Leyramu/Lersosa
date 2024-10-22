package leyramu.framework.lersosa.system.api.factory;

import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.system.api.RemoteFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 文件服务降级处理
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/22
 */
@Slf4j
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService> {

    /**
     * 处理
     *
     * @param throwable 异常
     * @return R
     */
    @Override
    public RemoteFileService create(Throwable throwable) {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return _ -> R.fail("上传文件失败:" + throwable.getMessage());
    }
}
