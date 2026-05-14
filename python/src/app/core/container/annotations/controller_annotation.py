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


from inspect import iscoroutinefunction
from typing import Callable

from fastapi import APIRouter
from fastapi.logger import logger

from app.common.utils import NameUtils
from .annotation_registry import AnnotationRegistry
from .component_metadata import ComponentMetadata


class ControllerAnnotation:
    """@Controller注解 - 标记一个类为控制器组件

    用于标识Web层的控制器类，可以指定URL前缀、标签和响应配置
    自动继承APIRouter并处理初始化
    """

    @staticmethod
    def create(
            prefix: str = "",
            name: str = "",
            tags: list[str] | None = None,
            responses: dict | None = None,
    ) -> Callable[[type], type]:
        """创建@Controller装饰器

        Args:
            prefix: URL前缀，例如 "/api"、"/crawler"
            name: Bean名称，默认为类名的小驼峰形式
            tags: FastAPI路由标签列表
            responses: FastAPI响应配置字典

        Returns:
            装饰器函数

        Notes:
            @ControllerAnnotation.create(prefix="/api")
            class UserController:
                pass

            @ControllerAnnotation.create(
                prefix="/crawler",
                tags=["crawler"],
                responses={404: {"description": "Not found"}}
            )
            class CrawlerController:
                pass
        """

        def decorator(cls: type) -> type:
            # 保存原始类的__init__方法
            original_init = cls.__init__ if hasattr(cls, "__init__") and cls.__init__ is not object.__init__ else None

            # 检查是否已经继承APIRouter
            if not issubclass(cls, APIRouter):
                # 动态创建新类，继承APIRouter
                original_cls = cls

                # 保存原始类的属性和方法
                original_attrs = {
                    key: value
                    for key, value in cls.__dict__.items()
                    if not key.startswith("__")
                }

                # 创建新的类，继承APIRouter
                cls = type(
                    cls.__name__,
                    (APIRouter,),
                    {"__module__": cls.__module__, "__doc__": cls.__doc__},
                )

                # 将原始类的属性和方法复制到新类
                for key, value in original_attrs.items():
                    setattr(cls, key, value)

            # 重写__init__方法，自动调用APIRouter.__init__并注册路由
            def new_init(self, *args, **kwargs) -> None:
                # 准备APIRouter的初始化参数
                router_kwargs = {}
                if tags:
                    router_kwargs["tags"] = tags
                if responses:
                    router_kwargs["responses"] = responses

                # 调用APIRouter.__init__
                APIRouter.__init__(self, **router_kwargs)

                # 如果原始类有__init__，也调用它
                if original_init:
                    original_init(self, *args, **kwargs)

                # 直接注册路由
                for name in dir(self):
                    method = getattr(self, name, None)
                    if method and iscoroutinefunction(method) and hasattr(method, '__router_method__'):
                        route_info = method.__router_method__
                        route_method = route_info[0]
                        path = route_info[1]
                        kwargs = route_info[2] if len(route_info) > 2 else {}

                        if route_method == 'get':
                            self.add_api_route(path, method, methods=["GET"], **kwargs)
                        elif route_method == 'post':
                            self.add_api_route(path, method, methods=["POST"], **kwargs)
                        elif route_method == 'put':
                            self.add_api_route(path, method, methods=["PUT"], **kwargs)
                        elif route_method == 'delete':
                            self.add_api_route(path, method, methods=["DELETE"], **kwargs)
                        elif route_method == 'patch':
                            self.add_api_route(path, method, methods=["PATCH"], **kwargs)
                        elif route_method == 'websocket':
                            self.add_api_websocket_route(path, method, **kwargs)

            cls.__init__ = new_init

            # 设置Bean元数据
            bean_name = name or NameUtils.to_camel_case(cls.__name__)
            metadata = ComponentMetadata(
                name=bean_name,
                singleton=True,  # 控制器通常是单例
                lazy_init=False,
            )

            # 将prefix存储为类属性
            cls._controller_prefix = prefix
            AnnotationRegistry.register(cls, metadata)
            logger.info(
                "@Controller: 注册控制器 '%s' -> '%s' (prefix=%s)",
                cls.__name__,
                bean_name,
                prefix,
            )
            return cls

        return decorator
