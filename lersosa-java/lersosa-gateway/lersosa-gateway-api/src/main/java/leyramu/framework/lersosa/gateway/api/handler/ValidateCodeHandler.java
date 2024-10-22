package leyramu.framework.lersosa.gateway.api.handler;

import leyramu.framework.lersosa.common.core.exception.CaptchaException;
import leyramu.framework.lersosa.common.core.web.domain.AjaxResult;
import leyramu.framework.lersosa.gateway.api.service.ValidateCodeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * 验证码获取
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/18
 */
@Component
@RequiredArgsConstructor
public class ValidateCodeHandler implements HandlerFunction<ServerResponse> {

    /**
     * 验证码生成
     */
    private final ValidateCodeService validateCodeService;

    /**
     * 获取验证码
     *
     * @param serverRequest 请求参数
     * @return Mono<ServerResponse>
     */
    @Override
    @NonNull
    public Mono<ServerResponse> handle(@NonNull ServerRequest serverRequest) {
        AjaxResult ajax;
        try {
            ajax = validateCodeService.createCaptcha();
        } catch (CaptchaException | IOException e) {
            return Mono.error(e);
        }
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(ajax));
    }
}
