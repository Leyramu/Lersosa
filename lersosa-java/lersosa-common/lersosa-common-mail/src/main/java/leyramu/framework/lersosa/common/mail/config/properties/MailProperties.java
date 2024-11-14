/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.mail.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JavaMail 配置属性.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    /**
     * 过滤开关.
     */
    private Boolean enabled;

    /**
     * SMTP服务器域名.
     */
    private String host;

    /**
     * SMTP服务端口.
     */
    private Integer port;

    /**
     * 是否需要用户名密码验证.
     */
    private Boolean auth;

    /**
     * 用户名.
     */
    private String user;

    /**
     * 密码.
     */
    private String pass;

    /**
     * 发送方，遵循RFC-822标准.
     */
    private String from;

    /**
     * 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口.
     */
    private Boolean starttlsEnable;

    /**
     * 使用 SSL安全连接.
     */
    private Boolean sslEnable;

    /**
     * SMTP超时时长，单位毫秒，缺省值不超时.
     */
    private Long timeout;

    /**
     * Socket连接超时值，单位毫秒，缺省值不超时.
     */
    private Long connectionTimeout;
}
