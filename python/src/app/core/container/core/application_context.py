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


from __future__ import annotations

import logging
from typing import Any, Type

from fastapi import FastAPI

from .bean_definition import BeanDefinition

logger = logging.getLogger(__name__)


# 应用上下文容器
class ApplicationContext:
    """应用上下文容器，管理所有Bean的创建、依赖注入和生命周期

    Notes：
        - Bean注册和管理
        - 自动依赖注入
        - 单例/原型作用域
        - 生命周期回调
    """

    def __init__(self, app: FastAPI) -> None:
        """初始化应用上下文

        Args:
            app: FastAPI应用实例
        """
        self._app = app
        self._bean_definitions: dict[str, BeanDefinition] = {}
        self._bean_instances: dict[str, Any] = {}
        self._initialized = False

    def register_bean(
            self,
            name: str,
            bean_class: Type[Any],
            singleton: bool = True,
            lazy_init: bool = False,
            dependencies: list[str] | None = None,
    ) -> None:
        """注册Bean定义

        Args:
            name: Bean的唯一标识名称
            bean_class: Bean的类类型
            singleton: 是否为单例
            lazy_init: 是否延迟初始化
            dependencies: 依赖的其他Bean名称列表
        """
        if name in self._bean_definitions:
            logger.debug("Bean '%s' 已存在，跳过重复注册", name)
            return

        self._bean_definitions[name] = BeanDefinition(
            bean_class=bean_class,
            singleton=singleton,
            lazy_init=lazy_init,
            dependencies=dependencies or [],
        )
        logger.debug("已注册 Bean: %s -> %s", name, bean_class.__name__)

    def get_bean(self, name: str) -> Any:
        """获取Bean实例

            如果Bean是单例且已创建，直接返回缓存实例,否则创建新实例并进行依赖注入

        Args:
            name: Bean名称

        Returns
            Bean实例

        Exception:
            ValueError: Bean未注册
            RuntimeError: 依赖解析失败
        """
        # 检查Bean是否已注册
        if name not in self._bean_definitions:
            raise ValueError(f"Bean '{name}' 未注册于 ApplicationContext")

        bean_def = self._bean_definitions[name]

        # 如果是单例且已创建，直接返回
        if bean_def.singleton and name in self._bean_instances:
            return self._bean_instances[name]

        # 创建Bean实例
        instance = self._create_bean(bean_def)

        # 如果是单例，缓存实例
        if bean_def.singleton:
            self._bean_instances[name] = instance

        return instance

    def _create_bean(self, bean_def: BeanDefinition) -> Any:
        """创建Bean实例并自动注入依赖

            通过检查构造函数的参数名，自动从容器中查找匹配的Bean进行注入

        Args:
            bean_def: Bean定义

        Returns:
            创建的Bean实例
        """
        import inspect

        logger.debug("已创建 Bean: %s", bean_def.bean_class.__name__)

        # 特殊处理：如果bean_class是lambda或函数，直接调用
        if callable(bean_def.bean_class) and not inspect.isclass(bean_def.bean_class):
            try:
                instance = bean_def.bean_class()
                logger.debug("Bean 已成功创建（可调用）：%s", bean_def.bean_class.__name__)
                return instance
            except Exception as exc:
                raise RuntimeError(
                    f"构造 Bean '{bean_def.bean_class.__name__}' 失败: {exc}"
                ) from exc

        # 获取构造函数签名
        sig = inspect.signature(bean_def.bean_class.__init__)
        params = list(sig.parameters.keys())

        # 移除 'self' 参数
        if 'self' in params:
            params.remove('self')

        # 跳过 *args 和 **kwargs 参数
        params = [
            p for p in params
            if sig.parameters[p].kind not in (
                inspect.Parameter.VAR_POSITIONAL,
                inspect.Parameter.VAR_KEYWORD,
            )
        ]

        # 自动解析依赖：根据参数名在容器中查找匹配的Bean
        dependencies = {}
        for param_name in params:
            # 优先使用显式声明的依赖名称
            dep_name = param_name

            # 尝试从容器中获取该Bean
            if self.contains_bean(dep_name):
                try:
                    dependencies[param_name] = self.get_bean(dep_name)
                    logger.debug(
                        "自动注入依赖 '%s' -> '%s'",
                        param_name,
                        dep_name,
                    )
                except Exception as exc:
                    raise RuntimeError(
                        f"依赖 '{dep_name}' 为参数 "
                        f"'{param_name}' 在 Bean '{bean_def.bean_class.__name__}' 解析失败: {exc}"
                    ) from exc
            else:
                # 如果找不到对应的Bean，且没有默认值，则报错
                param = sig.parameters[param_name]
                if param.default is inspect.Parameter.empty:
                    raise RuntimeError(
                        f"未找到参数 '{param_name}' 在 "
                        f"'{bean_def.bean_class.__name__}.__init__()'. "
                        f"可用 Beans: {list(self._bean_definitions.keys())}"
                    )

        # 创建实例
        try:
            if dependencies:
                instance = bean_def.bean_class(**dependencies)
            else:
                instance = bean_def.bean_class()
        except Exception as exc:
            raise RuntimeError(
                f"未能构建 Bean '{bean_def.bean_class.__name__}': {exc}"
            ) from exc

        logger.debug("Bean 被成功构建：%s", bean_def.bean_class.__name__)
        return instance

    def initialize(self) -> None:
        """初始化所有非延迟加载的Bean

            在FastAPI启动时调用，确保所有必需的Bean都已创建
        """
        if self._initialized:
            logger.warning("ApplicationContext 已初始化")
            return

        logger.info("初始化 ApplicationContext...")

        # 初始化所有非延迟加载的Bean
        for name, bean_def in self._bean_definitions.items():
            if not bean_def.lazy_init:
                try:
                    self.get_bean(name)
                except Exception as exc:
                    logger.error("初始化 Bean 失败 '%s': %s", name, exc)
                    raise

        self._initialized = True
        logger.info(
            "ApplicationContext 初始化为 %d Beans",
            len(self._bean_instances),
        )

    def shutdown(self) -> None:
        """关闭应用上下文，清理所有Bean资源

            在FastAPI停止时调用
        """
        if not self._initialized:
            return

        logger.info("关闭 ApplicationContext...")

        # 清理所有单例Bean
        for name, instance in self._bean_instances.items():
            try:
                # 如果Bean有shutdown方法，调用它
                if hasattr(instance, "shutdown") and callable(instance.shutdown):
                    instance.shutdown()
                    logger.debug("Bean '%s' 成功关闭", name)
            except Exception as exc:
                logger.error("错误 Bean 关闭 '%s': %s", name, exc)

        # 清空缓存
        self._bean_instances.clear()
        self._initialized = False

        logger.info("ApplicationContext 关闭完成")

    @property
    def app(self) -> FastAPI:
        """获取FastAPI应用实例

        Returns:
            FastAPI应用实例
        """
        return self._app

    @property
    def is_initialized(self) -> bool:
        """检查上下文是否已初始化

        Returns:
            True表示已初始化，False表示未初始化
        """
        return self._initialized

    def get_all_bean_names(self) -> list[str]:
        """获取所有已注册的Bean名称

        Returns:
            Bean名称列表
        """
        return list(self._bean_definitions.keys())

    def contains_bean(self, name: str) -> bool:
        """检查是否包含指定的Bean

        Args:
            name: Bean名称

        Returns:
            True表示存在，False表示不存在
        """
        return name in self._bean_definitions
