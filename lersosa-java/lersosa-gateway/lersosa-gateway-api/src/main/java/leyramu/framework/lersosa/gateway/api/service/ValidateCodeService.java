package leyramu.framework.lersosa.gateway.api.service;

import leyramu.framework.lersosa.common.core.exception.CaptchaException;
import leyramu.framework.lersosa.common.core.web.domain.AjaxResult;

import java.io.IOException;

/**
 * 验证码处理
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/18
 */
public interface ValidateCodeService {

    /**
     * 生成验证码
     *
     * @return AjaxResult
     */
    AjaxResult createCaptcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     *
     * @param key   前端上送 key
     * @param value 前端上送待校验值
     */
    void checkCaptcha(String key, String value) throws CaptchaException;
}
