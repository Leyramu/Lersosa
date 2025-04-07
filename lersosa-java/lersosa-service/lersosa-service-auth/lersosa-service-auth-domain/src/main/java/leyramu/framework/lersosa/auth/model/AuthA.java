/*
 * Copyright (c) 2025 Leyramu Group. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 *
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 *
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 *
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.auth.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证聚合.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2025/4/7
 */
@Data
public class AuthA implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请求体.
     */
    private String body;

    /**
     * 登录请求体.
     */
    private LoginBody loginBody;

    /**
     * 租户信息实体.
     */
    private TenantE tenantE;

    /**
     * 客户端信息值对象.
     */
    private ClientV clientV;

    /**
     * 社交信息值对象.
     */
    private SocialV socialV;

    /**
     * 构造函数.
     *
     * @param body 请求体
     */
    public AuthA(String body) {
        this.body = body;
    }

    /**
     * 更新登录请求体和客户端信息值对象.
     *
     * @param loginBody 登录请求体
     * @param clientV   客户端信息值对象
     */
    public void updateLoginObject(LoginBody loginBody, ClientV clientV) {
        this.loginBody = loginBody;
        this.clientV = clientV;
    }
}
