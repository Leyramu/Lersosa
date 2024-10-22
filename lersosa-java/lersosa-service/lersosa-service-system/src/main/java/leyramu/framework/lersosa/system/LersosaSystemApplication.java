package leyramu.framework.lersosa.system;

import leyramu.framework.lersosa.common.security.annotation.EnableCustomConfig;
import leyramu.framework.lersosa.common.security.annotation.EnableLersosaFeignClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统模块 启动类
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
@Slf4j
@EnableCustomConfig
@EnableLersosaFeignClients
@SpringBootApplication
public class LersosaSystemApplication {

    /**
     * 启动 系统模块 服务
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LersosaSystemApplication.class, args);
        log.info("""
                系统模块 服务启动成功
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
