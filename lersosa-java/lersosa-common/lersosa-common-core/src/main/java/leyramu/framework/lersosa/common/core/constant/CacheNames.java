/*
 * Copyright (c) 2024 Leyramu Group. All rights reserved.
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

package leyramu.framework.lersosa.common.core.constant;

/**
 * 缓存组名称常量.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public interface CacheNames {

    /**
     * 演示案例.
     */
    String DEMO_CACHE = "demo:cache#60s#10m#20";

    /**
     * 系统配置.
     */
    String SYS_CONFIG = "sys_config";

    /**
     * 数据字典.
     */
    String SYS_DICT = "sys_dict";

    /**
     * 租户.
     */
    String SYS_TENANT = GlobalConstants.GLOBAL_REDIS_KEY + "sys_tenant#30d";

    /**
     * 客户端.
     */
    String SYS_CLIENT = GlobalConstants.GLOBAL_REDIS_KEY + "sys_client#30d";

    /**
     * 用户账户.
     */
    String SYS_USER_NAME = "sys_user_name#30d";

    /**
     * 用户名称.
     */
    String SYS_NICKNAME = "sys_nickname#30d";

    /**
     * 部门.
     */
    String SYS_DEPT = "sys_dept#30d";

    /**
     * OSS内容.
     */
    String SYS_OSS = "sys_oss#30d";

    /**
     * OSS配置.
     */
    String SYS_OSS_CONFIG = GlobalConstants.GLOBAL_REDIS_KEY + "sys_oss_config";

    /**
     * 在线用户.
     */
    @SuppressWarnings("unused")
    String ONLINE_TOKEN = "online_tokens";
}
