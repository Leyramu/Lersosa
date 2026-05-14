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


# Nacos 配置类
class NacosConfig:
    """Nacos配置管理类，用于集中管理服务注册与配置中心相关参数
    Attributes：
        NACOS_SERVER_ADDR (str):
            Nacos服务器地址，从环境变量NACOS_SERVER_ADDR读取，默认localhost:8848
        NACOS_NAMESPACE (str):
            Nacos命名空间ID，从环境变量NACOS_NAMESPACE读取，默认开发环境命名空间
        NACOS_GROUP (str):
            配置分组名称，从环境变量NACOS_GROUP读取，默认SERVICE_GROUP
        NACOS_USERNAME (str):
            Nacos认证用户名，从环境变量NACOS_USERNAME读取，默认nacos
        NACOS_PASSWORD (str):
            Nacos认证密码，从环境变量NACOS_PASSWORD读取，默认测试环境密码
        NACOS_DATA_ID (str):
            配置集的唯一ID，从环境变量NACOS_DATA_ID读取，默认lersosa-service-python.yml
        NACOS_SERVICE_NAME (str):
            注册到Nacos的服务名称，从环境变量NACOS_SERVICE_NAME读取，默认lersosa-service-python
        HEARTBEAT_INTERVAL (int):
            服务心跳间隔时间（秒），从环境变量HEARTBEAT_INTERVAL读取后转为整型，默认10秒
    """
    NACOS_SERVER_ADDR: str = os.getenv('NACOS_SERVER_ADDR', 'lersosa-nacos:8848')
    NACOS_NAMESPACE: str = os.getenv('NACOS_NAMESPACE', '356d484c-399c-4a23-9419-e200e8edbff9')
    NACOS_GROUP: str = os.getenv('NACOS_GROUP', 'SERVICE_GROUP')
    NACOS_USERNAME: str = os.getenv('NACOS_USERNAME', 'nacos')
    NACOS_PASSWORD: str = os.getenv('NACOS_PASSWORD', 'Zcx@223852//')
    NACOS_DATA_ID: str = os.getenv('NACOS_DATA_ID', 'lersosa-service-python.yml')
    NACOS_SERVICE_NAME: str = os.getenv('NACOS_SERVICE_NAME', 'lersosa-service-python')
    HEARTBEAT_INTERVAL: any = int(os.getenv('HEARTBEAT_INTERVAL', '10'))
