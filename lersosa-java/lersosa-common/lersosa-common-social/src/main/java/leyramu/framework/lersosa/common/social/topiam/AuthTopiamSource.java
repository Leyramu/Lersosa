/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package leyramu.framework.lersosa.common.social.topiam;

import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthDefaultRequest;

/**
 * Oauth2 默认接口说明
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/11/6
 */
public enum AuthTopiamSource implements AuthSource {

    /**
     * 测试
     */
    TOPIAM {
        /**
         * 授权的api
         */
        @Override
        public String authorize() {
            return AuthTopIamRequest.SERVER_URL + "/oauth2/auth";
        }

        /**
         * 获取accessToken的api
         */
        @Override
        public String accessToken() {
            return AuthTopIamRequest.SERVER_URL + "/oauth2/token";
        }

        /**
         * 获取用户信息的api
         */
        @Override
        public String userInfo() {
            return AuthTopIamRequest.SERVER_URL + "/oauth2/userinfo";
        }

        /**
         * 平台对应的 AuthRequest 实现类，必须继承自 {@link AuthDefaultRequest}
         */
        @Override
        public Class<? extends AuthDefaultRequest> getTargetClass() {
            return AuthTopIamRequest.class;
        }

    }
}
