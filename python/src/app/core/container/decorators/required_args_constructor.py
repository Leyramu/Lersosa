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


from typing import Any, get_type_hints

from fastapi.logger import logger

from common.utils.name_utils import NameUtils
from app.core.container.core.bean_container_manager import BeanContainerManager


class RequiredArgsConstructorDecorator:
    """@RequiredArgsConstructor装饰器 - 为带有类型注解的字段自动生成构造函数。

    类似Spring的@RequiredArgsConstructor，会自动扫描类中带有类型注解的字段，
    并生成一个构造函数来注入这些依赖。
    """

    @staticmethod
    def decorate(cls: type) -> type:
        """应用@RequiredArgsConstructor装饰器。

        用法:
            @RequiredArgsConstructorDecorator.decorate
            class UserController:
                user_service: UserService  # 会自动注入

        参数:
            cls: 被装饰的类

        返回:
            装饰后的类，带有自动生成的__init__方法
        """
        # 获取类的类型注解
        try:
            type_hints = get_type_hints(cls)
        except Exception:
            type_hints = getattr(cls, '__annotations__', {})

        # 过滤出需要注入的字段（排除私有字段和特殊字段）
        injectable_fields = {
            name: hint for name, hint in type_hints.items()
            if not name.startswith('_') and name not in ('__module__', '__doc__')
        }

        if not injectable_fields:
            logger.warning("@RequiredArgsConstructor: 类 '%s' 没有可注入的字段", cls.__name__)
            return cls

        # 保存原始的__init__方法
        original_init = cls.__init__ if hasattr(cls, '__init__') and cls.__init__ is not object.__init__ else None

        # 生成新的__init__方法
        def new_init(self, **kwargs) -> None:
            """自动生成的构造函数，注入所有声明的依赖。"""
            for field_name, field_type in injectable_fields.items():
                if field_name in kwargs:
                    # 如果手动提供了参数，使用手动提供的
                    setattr(self, field_name, kwargs[field_name])
                else:
                    # 否则从容器中获取
                    bean = self._resolve_bean(field_name, field_type)
                    setattr(self, field_name, bean)
                    logger.debug(
                        "@RequiredArgsConstructor: 注入字段 '%s.%s' -> '%s'",
                        cls.__name__,
                        field_name,
                        type(bean).__name__,
                    )

            # 调用原始__init__（如果有）
            if original_init:
                original_init(self)

        # 添加Bean解析辅助方法到类中
        def _resolve_bean(self, field_name: str, field_type: type) -> Any:
            """解析Bean，支持接口到实现的自动映射。
            
            策略：
            1. 优先使用字段名作为Bean名称
            2. 尝试使用类型名称（小驼峰）作为Bean名称
            3. 如果是接口/抽象类，查找其实现类
            """
            from abc import ABC

            # 策略1: 直接使用字段名
            if BeanContainerManager._context and BeanContainerManager._context.contains_bean(field_name):
                return BeanContainerManager.get_bean(field_name)

            # 策略2: 使用类型名称（小驼峰）
            if hasattr(field_type, '__name__'):
                bean_name = NameUtils.to_camel_case(field_type.__name__)
                if BeanContainerManager._context and BeanContainerManager._context.contains_bean(bean_name):
                    return BeanContainerManager.get_bean(bean_name)

            # 策略3: 如果是接口/抽象类，查找实现类
            if isinstance(field_type, type) and issubclass(field_type, ABC):
                # 遍历所有已注册的Bean，找到实现该接口的类
                all_bean_names = BeanContainerManager._context.get_all_bean_names()
                for bean_name in all_bean_names:
                    try:
                        bean_def = BeanContainerManager._context._bean_definitions[bean_name]
                        bean_class = bean_def.bean_class
                        # 检查是否实现了该接口
                        if (isinstance(bean_class, type) and
                                issubclass(bean_class, field_type) and
                                bean_class != field_type):
                            logger.info(
                                "@RequiredArgsConstructor: 找到接口 '%s' 的实现类 '%s'",
                                field_type.__name__,
                                bean_class.__name__,
                            )
                            return BeanContainerManager.get_bean(bean_name)
                    except Exception:
                        continue

            # 所有策略都失败，抛出异常
            raise ValueError(
                f"无法解析Bean: 字段 '{field_name}' (类型: {field_type.__name__ if hasattr(field_type, '__name__') else field_type})。"
                f"请确保该Bean已注册到容器中。"
            )

        # 将辅助方法绑定到类
        cls._resolve_bean = _resolve_bean
        cls.__init__ = new_init

        logger.info(
            "@RequiredArgsConstructor: 为类 '%s' 生成构造函数，注入字段: %s",
            cls.__name__,
            list(injectable_fields.keys()),
        )

        return cls
