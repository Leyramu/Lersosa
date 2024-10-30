#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Digitalization Education), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.

import asyncio
from contextlib import asynccontextmanager
from typing import List, Dict

import httpx
from fastapi import FastAPI, Request, Response
from fastapi.routing import APIRoute

from app import config, services

# 用于存储服务实例列表
service_instances: Dict[str, List[dict]] = {}


# 生命周期管理
@asynccontextmanager
async def lifespan(app: FastAPI):
    # 获取当前服务的信息
    service_name = config.NacosConfig.NACOS_SERVICE_NAME
    group_name = config.NacosConfig.NACOS_GROUP
    ip = config.GatewayConfig.SERVER_IP
    port = config.GatewayConfig.SERVER_PORT

    # 注册服务到Nacos
    await services.nacos_client.register_service(service_name, group_name, ip, port)

    # 启动心跳任务
    heartbeat_task = asyncio.create_task(services.nacos_client.service_heartbeat(service_name, group_name, ip, port))

    # 启动服务变化监听器
    listener_client = asyncio.create_task(listen_service_changes())

    try:
        yield
    finally:
        # 取消心跳任务
        heartbeat_task.cancel()
        # 取消服务变化监听器
        listener_client.cancel()
        try:
            # 等待心跳任务完成或被取消
            await asyncio.wait_for(heartbeat_task, timeout=1)
            await asyncio.wait_for(listener_client, timeout=1)
        except asyncio.exceptions.CancelledError:
            # 如果心跳任务被取消，忽略错误
            pass
        except asyncio.exceptions.TimeoutError:
            # 如果等待超时，忽略错误
            pass

    # 注销服务
    await services.nacos_client.unregister_service(service_name, group_name, ip, port)


# 自定义路由类
class ServiceDiscoveryRoute(APIRoute):

    # def __init__(self, *args, **kwargs):
    #     super().__init__(*args, **kwargs)
    #
    # async def dispatch(self, request: Request) -> Response:
    #     # 获取请求的目标服务名称
    #     print('正在处理请求', request.url.path)
    #     target_service_name = request.url.path.split("/")[1]
    #
    #     # 从 Nacos 获取服务实例列表
    #     instances = await services.nacos_client.discover_service(target_service_name)
    #
    #     # 选择一个实例
    #     selected_instance = self.select_instance(target_service_name, instances)
    #
    #     # 构建新的 URL
    #     new_url = f"http://{selected_instance['ip']}:{selected_instance['port']}{request.url.path}"
    #
    #     print('请求转发到', new_url)
    #
    #     # 发送请求到选定的实例
    #     async with httpx.AsyncClient() as client:
    #         response = await client.request(
    #             request.method,
    #             new_url,
    #             headers=dict(request.headers),
    #             content=await request.body(),
    #             follow_redirects=True,
    #         )
    #
    #     # 将响应返回给客户端
    #     return Response(content=response.content, status_code=response.status_code, headers=response.headers)

    @staticmethod
    def select_instance(service_name: str, instances: List[Dict]) -> Dict:
        # 简单的轮询算法
        if not instances:
            raise ValueError(f"没有可用的实例: {service_name}")

        # 提取 hosts 列表
        hosts = instances['hosts']

        # 确保 hosts 列表不为空
        if not hosts:
            raise ValueError(f"没有可用的主机: {service_name}")

        # 更新或创建 service_instances 中的条目
        if service_name not in service_instances:
            service_instances[service_name] = hosts.copy()
        else:
            # 轮询算法：将列表的第一个元素移到末尾
            service_instances[service_name].append(service_instances[service_name].pop(0))

        # 返回当前轮询到的主机
        return service_instances[service_name][0]


# 监听服务变化
async def listen_service_changes():
    while True:
        for service_name, _ in service_instances.items():
            new_instances = await services.nacos_client.get_services(service_name)
            if new_instances != service_instances[service_name]:
                service_instances[service_name] = new_instances
                print(f"服务 {service_name} 的实例已更新")
        await asyncio.sleep(10)  # 每隔 10 秒检查一次
