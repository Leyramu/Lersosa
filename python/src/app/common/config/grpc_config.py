#  Copyright (c) 2020-2026 Leyramu Group. All rights reserved.
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
#  This project (Lersosa), including its source code, documentation,
#  and any associated materials, is the intellectual property of Leyramu.
#  No part of this software may be reproduced, distributed,
#  or transmitted in any form or by any means, including photocopying,
#  recording, or other electronic or mechanical methods,
#  without the prior written permission of the copyright owner,
#  Miraitowa_zcx, except in the case of brief quotations embodied in
#   critical reviews and certain other noncommercial uses permitted by copyright law.
#
#  For inquiries related to licensing or usage outside the scope of this notice,
#  please contact the copyright holder at 2038322151@qq.com.
#
#  The author disclaims all warranties, express or implied,
#  including but not limited to the warranties of merchantability and
#  fitness for a particular purpose. Under no circumstances shall the author
#  be liable for any special, incidental, indirect, or consequential damages
#  arising from the use of this software.
#
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


import os


# gRPC 配置参数类
class GrpcConfig:
    """gRPC配置参数类，用于从环境变量中读取并存储gRPC相关配置
    Attributes:
        GRPC_HOST (str): gRPC服务主机地址，从环境变量GRPC_HOST获取
            默认值 'localhost'
        GRPC_PORT (int): gRPC服务端口号，从环境变量GRPC_PORT获取
            默认值6001
        GRPC_PROTO_DIR (str): proto文件存放目录路径，从环境变量GRPC_PROTO_DIR获取
            默认值 'proto/rpc'
    """
    GRPC_HOST: str = os.getenv('GRPC_HOST', 'lersosa-python')
    GRPC_PORT: int = int(os.getenv('GRPC_PORT', '6001'))
    GRPC_SERVICE_NAME: str = os.getenv('GRPC_SERVICE_NAME', 'lersosa-service-grpc')
    GRPC_PROTO_DIR: str = os.getenv('GRPC_PROTO_DIR', '/lersosa/service/python/src/proto/rpc')
