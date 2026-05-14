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

import importlib
import inspect
import pkgutil

from fastapi.logger import logger

from app.core.container.annotations import AnnotationRegistry


class ComponentScanner:
    """组件扫描器，自动扫描指定包下所有带注解的类并注册"""

    def __init__(self, base_packages: list[str] | None = None) -> None:
        """初始化组件扫描器

        Args:
            base_packages: 要扫描的基础包列表，例如 ["app.application", "app.adapter.web"]
        """
        self._base_packages = base_packages or []
        self._scanned_classes: list[type] = []

    def scan(self) -> int:
        """执行组件扫描

            扫描所有基础包及其子包，查找带注解的类并自动注册

        Returns:
            扫描到的组件数量
        """
        logger.info("开始组件扫描，基础包: %s", self._base_packages)

        total_count = 0
        for package_name in self._base_packages:
            count = self._scan_package(package_name)
            total_count += count

        logger.info("组件扫描完成，共发现 %d 个组件", total_count)
        return total_count

    def _scan_package(self, package_name: str) -> int:
        """扫描单个包及其子包

        Args:
            package_name: 包名称，例如 "app.application"

        Returns:
            扫描到的组件数量
        """
        count = 0

        try:
            # 导入包
            package = importlib.import_module(package_name)
            package_path = getattr(package, "__path__", None)

            if package_path is None:
                # 不是包，只是一个模块
                count += self._scan_module(package_name)
                return count

            # 遍历包中的所有模块
            for importer, module_name, is_pkg in pkgutil.walk_packages(
                    path=package_path,
                    prefix=package.__name__ + ".",
            ):
                # 跳过测试模块和私有模块
                if "_test" in module_name or module_name.endswith(".__main__"):
                    continue

                count += self._scan_module(module_name)

        except Exception as exc:
            logger.warning("扫描包 '%s' 时出错: %s", package_name, exc)

        return count

    def _scan_module(self, module_name: str) -> int:
        """扫描单个模块中的类

        Args:
            module_name: 模块名称，例如 "app.application.crawler.service.crawler_service_impl"

        Returns:
            扫描到的组件数量
        """
        count = 0

        try:
            # 导入模块
            module = importlib.import_module(module_name)

            # 遍历模块中的所有成员
            for name, obj in inspect.getmembers(module):
                # 只处理类
                if not inspect.isclass(obj):
                    continue

                # 检查是否在当前模块中定义（排除导入的类）
                if obj.__module__ != module_name:
                    continue

                # 检查是否有注解
                metadata = AnnotationRegistry.get_metadata(obj)
                if metadata is not None:
                    logger.debug("发现组件: %s.%s", module_name, name)
                    self._scanned_classes.append(obj)
                    count += 1

        except Exception as exc:
            logger.warning("扫描模块 '%s' 时出错: %s", module_name, exc)

        return count

    def get_scanned_classes(self) -> list[type]:
        """获取所有扫描到的类

        Returns:
            扫描到的类列表
        """
        return self._scanned_classes.copy()

    @staticmethod
    def scan_and_register(base_packages: list[str] | None = None) -> int:
        """静态方法：扫描并注册组件

            这是一个便捷方法，创建扫描器、执行扫描并返回结果

        Args:
            base_packages: 要扫描的基础包列表

        Returns:
            扫描到的组件数量
        """
        scanner = ComponentScanner(base_packages)
        return scanner.scan()
