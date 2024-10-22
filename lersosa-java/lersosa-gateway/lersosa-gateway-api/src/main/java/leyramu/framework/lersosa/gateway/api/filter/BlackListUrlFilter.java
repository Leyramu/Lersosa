package leyramu.framework.lersosa.gateway.api.filter;

import leyramu.framework.lersosa.common.core.utils.ServletUtils;
import lombok.Getter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 黑名单过滤器
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/18
 */
@Component
public class BlackListUrlFilter extends AbstractGatewayFilterFactory<BlackListUrlFilter.Config> {

    /**
     * 配置过滤器
     *
     * @param config 配置
     * @return {@link GatewayFilter}
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String url = exchange.getRequest().getURI().getPath();
            if (config.matchBlacklist(url)) {
                return ServletUtils.webFluxResponseWriter(exchange.getResponse(), "请求地址不允许访问");
            }

            return chain.filter(exchange);
        };
    }

    /**
     * 初始化配置
     */
    public BlackListUrlFilter() {
        super(Config.class);
    }

    /**
     * 配置类
     */
    public static class Config {

        /**
         * 黑名单地址
         */
        @Getter
        private List<String> blacklistUrl;

        /**
         * 黑名单地址正则表达式
         */
        private final List<Pattern> blacklistUrlPattern = new ArrayList<>();

        /**
         * 匹配黑名单地址
         *
         * @param url url
         * @return boolean
         */
        public boolean matchBlacklist(String url) {
            return !blacklistUrlPattern.isEmpty() && blacklistUrlPattern.stream().anyMatch(p -> p.matcher(url).find());
        }

        /**
         * 设置黑名单地址
         *
         * @param blacklistUrl 黑名单地址
         */
        public void setBlacklistUrl(List<String> blacklistUrl) {
            this.blacklistUrl = blacklistUrl;
            this.blacklistUrlPattern.clear();
            this.blacklistUrl.forEach(url -> this.blacklistUrlPattern.add(Pattern.compile(url.replaceAll("\\*\\*", "(.*?)"), Pattern.CASE_INSENSITIVE)));
        }
    }
}
