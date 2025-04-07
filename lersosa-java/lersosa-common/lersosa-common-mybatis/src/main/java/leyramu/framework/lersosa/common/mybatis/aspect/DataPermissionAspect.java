package leyramu.framework.lersosa.common.mybatis.aspect;

import leyramu.framework.lersosa.common.mybatis.annotation.DataPermission;
import leyramu.framework.lersosa.common.mybatis.helper.DataPermissionHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 数据权限处理.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/3/27
 */
@Slf4j
@Aspect
@SuppressWarnings("unused")
public class DataPermissionAspect {

    /**
     * 处理请求前执行.
     */
    @Before(value = "@annotation(dataPermission)")
    public void doBefore(JoinPoint joinPoint, DataPermission dataPermission) {
        DataPermissionHelper.setPermission(dataPermission);
    }

    /**
     * 处理完请求后执行.
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(dataPermission)")
    public void doAfterReturning(JoinPoint joinPoint, DataPermission dataPermission) {
        DataPermissionHelper.removePermission();
    }

    /**
     * 拦截异常操作.
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(dataPermission)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, DataPermission dataPermission, Exception e) {
        DataPermissionHelper.removePermission();
    }
}
