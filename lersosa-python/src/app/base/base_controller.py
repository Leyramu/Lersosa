#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


from inspect import iscoroutinefunction

from fastapi import APIRouter


# 路由控制器
class BaseController(APIRouter):

    # 初始化
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.register_routes()

    # 装饰器工厂方法
    @classmethod
    def _create_decorator(cls, method_type):
        def decorator(path, *args, **kwargs):
            def wrapper(func):
                func.__router_method__ = (method_type, path, args, kwargs)
                return func

            return wrapper

        return decorator

    # Get 装饰器
    @classmethod
    def get(cls, path, *args, **kwargs):
        """装饰器用于定义 GET 请求的路由"""
        return cls._create_decorator('get')(path, *args, **kwargs)

    # Post 装饰器
    @classmethod
    def post(cls, path, *args, **kwargs):
        """装饰器用于定义 POST 请求的路由"""
        return cls._create_decorator('post')(path, *args, **kwargs)

    # Put 装饰器
    @classmethod
    def put(cls, path, *args, **kwargs):
        """装饰器用于定义 PUT 请求的路由"""
        return cls._create_decorator('put')(path, *args, **kwargs)

    # Delete 装饰器
    @classmethod
    def delete(cls, path, *args, **kwargs):
        """装饰器用于定义 DELETE 请求的路由"""
        return cls._create_decorator('delete')(path, *args, **kwargs)

    # WebSocket 装饰器
    @classmethod
    def websocket(cls, path, *args, **kwargs):
        """装饰器用于定义 WebSocket 路由"""
        return cls._create_decorator('websocket')(path, *args, **kwargs)

    # 路由注册
    def register_routes(self):
        """注册所有带有装饰器的方法为路由"""
        for name in dir(self):
            method = getattr(self, name)
            if iscoroutinefunction(method) and hasattr(method, '__router_method__'):
                route_method, path, args, kwargs = method.__router_method__
                if route_method == 'get':
                    self.add_api_route(path, method, methods=["GET"], **kwargs)
                elif route_method == 'post':
                    self.add_api_route(path, method, methods=["POST"], **kwargs)
                elif route_method == 'put':
                    self.add_api_route(path, method, methods=["PUT"], **kwargs)
                elif route_method == 'delete':
                    self.add_api_route(path, method, methods=["DELETE"], **kwargs)
                elif route_method == 'websocket':
                    self.add_api_websocket_route(path, method, **kwargs)


# 导出装饰器
Get = BaseController.get
Post = BaseController.post
Put = BaseController.put
Delete = BaseController.delete
WebSocket = BaseController.websocket
