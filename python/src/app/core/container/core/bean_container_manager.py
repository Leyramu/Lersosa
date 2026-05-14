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


import inspect
from typing import Any

from fastapi import FastAPI
from fastapi.logger import logger

from app.core.container.annotations import AnnotationRegistry
from .application_context import ApplicationContext
from .component_scanner import ComponentScanner


class BeanContainerManager:
    """Bean容器管理器，管理Spring风格的依赖注入容器

        此类作为单例存在，提供全局的Bean管理能力
    """

    _instance: "BeanContainerManager | None" = None
    _context: ApplicationContext | None = None

    def __init__(self) -> None:
        """初始化Bean容器管理器

        Exception:
            RuntimeError: 如果尝试创建多个实例
        """
        if BeanContainerManager._instance is not None:
            raise RuntimeError("BeanContainerManager是单例，请使用get_instance()方法")

        BeanContainerManager._instance = self

    @classmethod
    def get_instance(cls) -> "BeanContainerManager":
        """获取Bean容器管理器单例实例

        Returns:
            BeanContainerManager实例

        Exception:
            RuntimeError: 如果尚未初始化
        """
        if cls._instance is None:
            raise RuntimeError(
                "BeanContainerManager尚未初始化，请先调用initialize(app)方法"
            )
        return cls._instance

    @classmethod
    def initialize(cls, app: FastAPI) -> None:
        """初始化Bean容器并扫描所有带注解的组件

            在FastAPI启动时调用，自动扫描并注册所有带@Component、@Service、
            @Controller、@Bean注解的类。

        Args:
            app: FastAPI应用实例
        """
        if cls._instance is None:
            cls()

        if cls._context is not None:
            return

        # 创建应用上下文
        cls._context = ApplicationContext(app)

        # 首先注册FastAPI app本身作为Bean
        cls._context.register_bean(
            name="fastapi_app",
            bean_class=lambda: app,  # type: ignore
            singleton=True,
            lazy_init=False,
        )

        # 扫描所有带注解的组件
        scanner = ComponentScanner(
            base_packages=[
                # 服务层
                "app.application",
                # 控制器层
                "app.adapter",
                # 领域层
                "app.domain",
                # 基础设施层
                "app.infrastructure",
            ]
        )
        scanned_count = scanner.scan()

        # 从 AnnotationRegistry获取所有注册的组件并注册到ApplicationContext
        for clazz, metadata in AnnotationRegistry.get_all_components().items():
            cls._context.register_bean(
                name=metadata.name,
                bean_class=clazz,
                singleton=metadata.singleton,
                lazy_init=metadata.lazy_init,
                dependencies=metadata.dependencies,
            )

        # 初始化所有非延迟加载的Bean
        cls._context.initialize()

        # 自动注册所有@Controller的路由到FastAPI
        cls._register_controller_routes(app)

        logger.info(
            "BeanContainerManager初始化完成，扫描到 %d 个组件，注册 %d 个Bean",
            scanned_count,
            len(cls._context.get_all_bean_names()),
        )

    @classmethod
    def get_bean(cls, bean_name: str) -> Any:
        """通过Bean名称获取实例

        Args:
            bean_name: Bean名称

        Returns:
            Bean实例

        Exception:
            RuntimeError: 如果尚未初始化或Bean不存在
        """
        if cls._context is None:
            raise RuntimeError(
                "BeanContainerManager尚未初始化，请先调用initialize(app)方法"
            )

        return cls._context.get_bean(bean_name)

    @classmethod
    def get_beans_by_type(cls, interface_type: type) -> list[Any]:
        """获取所有实现指定接口的Bean实例

        Args:
            interface_type: 接口类型

        Returns:
            实现该接口的Bean实例列表
        """
        if cls._context is None:
            raise RuntimeError(
                "BeanContainerManager尚未初始化，请先调用initialize(app)方法"
            )

        result = []
        all_bean_names = cls._context.get_all_bean_names()

        for bean_name in all_bean_names:
            try:
                bean = cls._context.get_bean(bean_name)
                if isinstance(bean, interface_type):
                    result.append(bean)
            except Exception:
                # 跳过无法获取的Bean
                continue

        return result

    @classmethod
    def shutdown(cls) -> None:
        """关闭Bean容器管理器清理所有资源"""
        if cls._context is not None:
            cls._context.shutdown()
            cls._context = None

        cls._instance = None

    @classmethod
    def _register_controller_routes(cls, app: FastAPI) -> None:
        """自动注册所有@Controller的路由到FastAPI

            从ApplicationContext中获取所有带@Controller注解的Bean
            并将它们的路由注册到FastAPI应用中

        Args:
            app: FastAPI应用实例
        """
        from fastapi import APIRouter

        registered_count = 0

        # 遍历所有已注册的组件
        for clazz, metadata in AnnotationRegistry.get_all_components().items():
            # 检查是否是APIRouter的子类
            if inspect.isclass(clazz) and issubclass(clazz, APIRouter):
                try:
                    # 从容器中获取控制器实例
                    controller_router = cls._context.get_bean(metadata.name)

                    # 获取控制器的URL前缀
                    prefix = getattr(clazz, "_controller_prefix", "")

                    # 注册路由
                    app.include_router(controller_router, prefix=prefix)
                    registered_count += 1
                    logger.info(
                        "已注册控制器路由: %s (prefix=%s)",
                        clazz.__name__,
                        prefix or "/",
                    )
                except Exception as exc:
                    logger.error(
                        "注册控制器 '%s' 失败: %s",
                        clazz.__name__,
                        exc,
                        exc_info=True,
                    )

        logger.info("共注册 %d 个控制器路由", registered_count)
