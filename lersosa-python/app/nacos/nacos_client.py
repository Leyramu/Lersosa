#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.

import asyncio
from contextlib import asynccontextmanager

from fastapi import FastAPI
from nacos import NacosClient

from app.config import NacosConfig, ServerConfig


# Nacos客户端包装类
class NacosClientWrapper:
    def __init__(self):
        self.nacos_client = NacosClient(
            server_addresses=NacosConfig.NACOS_SERVER_ADDR,
            namespace=NacosConfig.NACOS_NAMESPACE,
            username=NacosConfig.NACOS_USERNAME,
            password=NacosConfig.NACOS_PASSWORD
        )

    # 注册服务
    async def register_service(self, service_name, group_name, ip, port):
        self.nacos_client.add_naming_instance(
            service_name=service_name,
            group_name=group_name,
            ip=ip,
            port=port
        )

    # 发送心跳
    async def service_heartbeat(self, service_name, group_name, ip, port):
        while True:
            try:
                self.nacos_client.send_heartbeat(
                    service_name=service_name,
                    group_name=group_name,
                    ip=ip,
                    port=port
                )
            except Exception as e:
                print(f"实例连接失败： {e}")
            await asyncio.sleep(NacosConfig.HEARTBEAT_INTERVAL)

    # 注销服务
    async def unregister_service(self, service_name, group_name, ip, port):
        self.nacos_client.remove_naming_instance(
            service_name=service_name,
            group_name=group_name,
            ip=ip,
            port=port
        )


# 创建Nacos客户端实例
nacos_client = NacosClientWrapper()


# 生命周期管理
@asynccontextmanager
async def lifespan(_app: FastAPI):
    # 获取当前服务的信息
    service_name = NacosConfig.NACOS_SERVICE_NAME
    group_name = NacosConfig.NACOS_GROUP
    ip = ServerConfig.SERVER_IP
    port = ServerConfig.SERVER_PORT

    # 注册服务到Nacos
    await nacos_client.register_service(service_name, group_name, ip, port)

    # 启动心跳任务
    heartbeat_task = asyncio.create_task(nacos_client.service_heartbeat(service_name, group_name, ip, port))

    try:
        yield
    finally:
        # 取消心跳任务
        heartbeat_task.cancel()
        try:
            # 等待心跳任务完成或被取消
            await asyncio.wait_for(heartbeat_task, timeout=1)
        except asyncio.exceptions.CancelledError:
            # 如果心跳任务被取消，忽略错误
            pass
        except asyncio.exceptions.TimeoutError:
            # 如果等待超时，忽略错误
            pass

    # 注销服务
    await nacos_client.unregister_service(service_name, group_name, ip, port)
