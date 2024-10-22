package leyramu.framework.lersosa.gateway.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/18
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LersosaGatewayApplication {

    /**
     * 启动 API 网关 模块
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LersosaGatewayApplication.class, args);
        log.info("""
                网关模块 服务启动成功
                 ___       _______   ________  ________  ________  ________  ________    \s
                |\\  \\     |\\  ___ \\ |\\   __  \\|\\   ____\\|\\   __  \\|\\   ____\\|\\   __  \\   \s
                \\ \\  \\    \\ \\   __/|\\ \\  \\|\\  \\ \\  \\___|\\ \\  \\|\\  \\ \\  \\___|\\ \\  \\|\\  \\  \s
                 \\ \\  \\    \\ \\  \\_|/_\\ \\   _  _\\ \\_____  \\ \\  \\\\\\  \\ \\_____  \\ \\   __  \\ \s
                  \\ \\  \\____\\ \\  \\_|\\ \\ \\  \\\\  \\\\|____|\\  \\ \\  \\\\\\  \\|____|\\  \\ \\  \\ \\  \\\s
                   \\ \\_______\\ \\_______\\ \\__\\\\ _\\ ____\\_\\  \\ \\_______\\____\\_\\  \\ \\__\\ \\__\\
                    \\|_______|\\|_______|\\|__|\\|__|\\_________\\|_______|\\_________\\|__|\\|__|
                                                 \\|_________|        \\|_________|        \s
                """);
    }
}
