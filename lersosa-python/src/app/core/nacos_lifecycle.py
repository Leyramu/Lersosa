#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


import asyncio
from contextlib import asynccontextmanager

from fastapi import FastAPI

from app.common.config import NacosConfig, ServerConfig
from app.common.nacos import NacosClientWrapper

#  Nacos 生命周期管理
class NacosLifecycle:
    def __init__(self, app: FastAPI):
        self.nacos_client = NacosClientWrapper()
        self.app = app
        self.service_name = NacosConfig.NACOS_SERVICE_NAME
        self.group_name = NacosConfig.NACOS_GROUP
        self.ip = ServerConfig.SERVER_IP
        self.port = ServerConfig.SERVER_PORT
        self.heartbeat_task = None

    # 服务启动
    async def start(self):
        # 注册服务到Nacos
        await self.nacos_client.register_service(self.service_name, self.group_name, self.ip, self.port)

        # 启动心跳任务
        self.heartbeat_task = asyncio.create_task(
            self.nacos_client.service_heartbeat(self.service_name, self.group_name, self.ip, self.port))

    # 停止服务
    async def stop(self):
        if self.heartbeat_task:
            # 取消心跳任务
            self.heartbeat_task.cancel()
            try:
                # 等待心跳任务完成或被取消
                await asyncio.wait_for(self.heartbeat_task, timeout=1)
            except (asyncio.exceptions.CancelledError, asyncio.exceptions.TimeoutError):
                # 如果心跳任务被取消或等待超时，忽略错误
                pass

        # 注销服务
        if hasattr(self, 'nacos_client'):
            await self.nacos_client.unregister_service(self.service_name, self.group_name, self.ip, self.port)

    # 生命周期管理
    @asynccontextmanager
    async def lifespan(self, _app: FastAPI):
        try:
            await self.start()
            yield
        finally:
            await self.stop()
