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


import asyncio

from nacos import NacosClient


# Nacos客户端包装类，继承自NacosClient
class NacosClientWrapper(NacosClient):
    """Nacos客户端包装类，用于扩展基础NacosClient功能
        继承自NacosClient，提供异步服务注册、心跳维护和注销功能

    Attributes:
        继承父类 NacosClient 的所有属性
    """

    def __init__(self, server_addresses: str, namespace: str, username: str, password: str) -> None:
        """初始化Nacos客户端包装实例

        Args:
            server_addresses (str): Nacos服务器地址，格式为"host:port"
            namespace (str): 命名空间ID
            username (str): 认证用户名
            password (str): 认证密码
        """
        super().__init__(
            server_addresses=server_addresses,
            namespace=namespace,
            username=username,
            password=password,
        )

    # 注册服务
    async def register_service(self, service_name: str, group_name: str, ip: str, port: int) -> None:
        """注册服务实例到Nacos服务器

        Args:
            service_name (str): 要注册的服务名称
            group_name (str): 服务所属的分组名称
            ip (str): 实例IP地址
            port (int): 实例服务端口号

        Returns:
            None: 无返回值
        """
        self.add_naming_instance(
            service_name=service_name,
            group_name=group_name,
            ip=ip,
            port=port
        )

    # 发送心跳
    async def service_heartbeat(self, service_name: str, group_name: str, ip: str, port: int,
                                health_interval: int) -> None:
        """维护服务实例的心跳连接

        Args:
            service_name (str): 服务名称
            group_name (str): 服务所属分组名称
            ip (str): 实例IP地址
            port (int): 实例服务端口
            health_interval (int): 心跳发送间隔时间（单位：秒）
        Returns:

            None: 无返回值

        Notes:
            - 使用无限循环持续发送心跳
            - 捕获并处理发送过程中的异常
        """
        while True:
            try:
                self.send_heartbeat(
                    service_name=service_name,
                    group_name=group_name,
                    ip=ip,
                    port=port
                )
            except Exception as e:
                print(f"实例连接失败： {e}")
            await asyncio.sleep(health_interval)

    # 注销服务
    async def unregister_service(self, service_name: str, group_name: str, ip: str, port: int) -> None:
        """从Nacos服务器注销服务实例

        Args:
            service_name (str): 要注销的服务名称
            group_name (str): 服务所属的分组名称
            ip (str): 实例IP地址
            port (int): 实例服务端口号

        Returns:
            None: 无返回值
        """
        self.remove_naming_instance(
            service_name=service_name,
            group_name=group_name,
            ip=ip,
            port=port
        )
