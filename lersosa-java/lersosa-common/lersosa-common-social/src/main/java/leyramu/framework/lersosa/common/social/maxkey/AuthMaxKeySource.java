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

package leyramu.framework.lersosa.common.social.maxkey;

import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthDefaultRequest;

/**
 * Oauth2 默认接口说明.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public enum AuthMaxKeySource implements AuthSource {

    /**
     * 自己搭建的 maxkey 私服.
     */
    MAXKEY {
        /**
         * 授权的api.
         */
        @Override
        public String authorize() {
            return AuthMaxKeyRequest.SERVER_URL + "/sign/authz/oauth/v20/authorize";
        }

        /**
         * 获取accessToken的api.
         */
        @Override
        public String accessToken() {
            return AuthMaxKeyRequest.SERVER_URL + "/sign/authz/oauth/v20/token";
        }

        /**
         * 获取用户信息的api.
         */
        @Override
        public String userInfo() {
            return AuthMaxKeyRequest.SERVER_URL + "/sign/api/oauth/v20/me";
        }

        /**
         * 平台对应的 AuthRequest 实现类，必须继承自 {@link AuthDefaultRequest}.
         */
        @Override
        public Class<? extends AuthDefaultRequest> getTargetClass() {
            return AuthMaxKeyRequest.class;
        }
    }
}
