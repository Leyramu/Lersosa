/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.system.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 当前在线会话.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
@Data
@NoArgsConstructor
public class SysUserOnline implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会话编号.
     */
    private String tokenId;

    /**
     * 部门名称.
     */
    private String deptName;

    /**
     * 用户名称.
     */
    private String userName;

    /**
     * 客户端.
     */
    private String clientKey;

    /**
     * 设备类型.
     */
    private String deviceType;

    /**
     * 登录IP地址.
     */
    private String ipaddr;

    /**
     * 登录地址.
     */
    private String loginLocation;

    /**
     * 浏览器类型.
     */
    private String browser;

    /**
     * 操作系统.
     */
    private String os;

    /**
     * 登录时间.
     */
    private Long loginTime;
}
