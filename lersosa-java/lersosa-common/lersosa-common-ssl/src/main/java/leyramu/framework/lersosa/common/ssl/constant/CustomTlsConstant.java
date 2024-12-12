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

package leyramu.framework.lersosa.common.ssl.constant;

import lombok.NoArgsConstructor;

/**
 * 自定义 TLS 配置.
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/12/9
 */
@NoArgsConstructor
public class CustomTlsConstant {

    public static final String RPC_SERVER_TLS_ENABLE = "nacos.remote.server.rpc.tls.enableTls";
    public static final String RPC_SERVER_TLS_AUTH = "nacos.remote.server.rpc.tls.mutualAuthEnable";
    public static final String RPC_SERVER_TLS_COMPATIBILITY = "nacos.remote.server.rpc.tls.compatibility";
    public static final String RPC_SERVER_TLS_CERT_PATH = "nacos.remote.server.rpc.tls.certChainFile";
    public static final String RPC_SERVER_TLS_PRIVATE_KEY = "nacos.remote.server.rpc.tls.certPrivateKey";
    public static final String RPC_SERVER_TLS_PRIVATE_KEY_PASSWORD = "nacos.remote.server.rpc.tls.certPrivateKeyPassword";
    public static final String RPC_SERVER_TLS_TRUST_CERT = "nacos.remote.server.rpc.tls.trustCollectionCertFile";

    public static final String RPC_CLIENT_TLS_ENABLE = "nacos.remote.client.rpc.tls.enable";
    public static final String RPC_CLIENT_TLS_AUTH = "nacos.remote.client.rpc.tls.mutualAuth";
    public static final String RPC_CLIENT_TLS_CERT_PATH = "nacos.remote.client.rpc.tls.certChainFile";
    public static final String RPC_CLIENT_TLS_PRIVATE_KEY = "nacos.remote.client.rpc.tls.certPrivateKey";
    public static final String RPC_CLIENT_TLS_PRIVATE_KEY_PASSWORD = "nacos.remote.client.rpc.tls.certPrivateKeyPassword";
    public static final String RPC_CLIENT_TLS_TRUST_CERT = "nacos.remote.client.rpc.tls.trustCollectionCertFile";

    public static final String TLS_ENABLE = "tls.enable";
    public static final String CLIENT_AUTH = "tls.client.authServer";
    public static final String CLIENT_CERT_PATH = "tls.client.trustCertPath";
}
