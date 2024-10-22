package leyramu.framework.lersosa.system.api.factory;

import leyramu.framework.lersosa.common.core.domain.R;
import leyramu.framework.lersosa.system.api.RemoteUserService;
import leyramu.framework.lersosa.system.api.domain.SysUser;
import leyramu.framework.lersosa.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/22
 */
@Slf4j
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {

    /**
     * 调用失败处理
     *
     * @param throwable Throwable
     * @return RemoteUserService
     */
    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {

            /**
             * 获取用户信息
             *
             * @param username 用户名
             * @param source   请求来源
             * @return 结果
             */
            @Override
            public R<LoginUser> getUserInfo(String username, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            /**
             * 注册用户信息
             *
             * @param sysUser 用户信息
             * @param source  请求来源
             * @return 结果
             */
            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }

            /**
             * 记录登录信息
             *
             * @param sysUser 用户信息
             * @param source  请求来源
             * @return 结果
             */
            @Override
            public R<Boolean> recordUserLogin(SysUser sysUser, String source) {
                return R.fail("记录用户登录信息失败:" + throwable.getMessage());
            }
        };
    }
}
