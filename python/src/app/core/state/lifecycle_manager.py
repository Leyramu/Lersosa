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

from contextlib import asynccontextmanager
from typing import TYPE_CHECKING, AsyncGenerator, ClassVar

from logging import Logger
from app.core.container.annotations import Slf4j

if TYPE_CHECKING:
    from fastapi import FastAPI


# 生命周期管理器
@Slf4j
class LifecycleManager:
    """统一生命周期管理器，
        管理所有组件的生命周期

    Notes：
        lifecycle = LifecycleManager(app)
        app = FastAPI(lifespan=lifecycle.lifespan)
    """

    log: ClassVar[Logger]

    def __init__(self, app: FastAPI, register_sub_managers: bool = True) -> None:
        """初始化统一生命周期管理器

        Args:
            app: FastAPI应用实例
            register_sub_managers: 是否自动注册子管理器
        """
        self._app = app
        self._managers: list[LifecycleManager] = []
        self._initialized = False

        # 自动注册所有子生命周期管理器
        if register_sub_managers:
            self._register_sub_managers()

    def _register_sub_managers(self) -> None:
        """注册所有子生命周期管理器
            在此方法中添加所有需要管理的生命周期组件
        """
        # 注册Bean容器生命周期管理器
        try:
            from app.core.state.bean import BeanLifecycle

            bean_lifecycle = BeanLifecycle(self._app)
            self._managers.append(bean_lifecycle)
            self.log.info("已注册Bean容器生命周期管理器")
        except Exception as exc:
            self.log.error("Bean容器生命周期管理器注册失败: %s", exc)
            raise

        # 注册Nacos生命周期管理器
        try:
            from app.core.state.nacos import NacosLifecycle

            nacos_lifecycle = NacosLifecycle(self._app)
            self._managers.append(nacos_lifecycle)
            self.log.info("已注册Nacos生命周期管理器")
        except Exception as exc:
            self.log.warning("Nacos生命周期管理器注册失败: %s", exc)


    @asynccontextmanager
    async def lifespan(self, _app: FastAPI) -> AsyncGenerator[None, None]:
        """FastAPI lifespan上下文管理器
            此方法由FastAPI框架调用，管理所有组件的完整生命周期

        Args:
            _app: FastAPI应用实例

        Returns:
            None
        """
        try:
            await self._on_startup()
            self._initialized = True
            self.log.info("[LifecycleManager] 所有组件启动成功")
        except Exception as exc:
            self.log.error("[LifecycleManager] 启动失败: %s", exc, exc_info=True)
            self._initialized = False
            raise

        try:
            yield
        finally:
            self.log.info("[LifecycleManager] 开始关闭 %d 个组件...", len(self._managers))
            try:
                await self._on_shutdown()
                self._initialized = False
                self.log.info("[LifecycleManager] 所有组件关闭成功")
            except Exception as exc:
                self.log.error("[LifecycleManager] 关闭失败: %s", exc, exc_info=True)

    async def _on_startup(self) -> None:
        """按顺序启动所有子管理器
            如果某个管理器启动失败，会停止后续启动并抛出异常
        """
        for idx, manager in enumerate(self._managers, 1):
            self.log.info(
                "[LifecycleManager] 启动组件 %d/%d: %s",
                idx,
                len(self._managers),
                manager.__class__.__name__,
            )
            try:
                await manager._on_startup()
                self.log.info(
                    "[LifecycleManager] 组件 '%s' 启动成功",
                    manager.__class__.__name__,
                )
            except Exception as exc:
                self.log.error(
                    "[LifecycleManager] 组件 '%s' 启动失败，停止后续启动",
                    manager.__class__.__name__,
                    exc_info=True,
                )
                # 回滚已启动的管理器
                await self._rollback_startup(idx - 1)
                raise

    async def _on_shutdown(self) -> None:
        """按逆序关闭所有子管理器
            即使某个管理器关闭失败，也会继续关闭其他管理器
        """
        # 逆序关闭
        for idx, manager in enumerate(reversed(self._managers), 1):
            self.log.info(
                "[LifecycleManager] 关闭组件 %d/%d: %s",
                idx,
                len(self._managers),
                manager.__class__.__name__,
            )
            try:
                await manager._on_shutdown()
                self.log.info(
                    "[LifecycleManager] 组件 '%s' 关闭成功",
                    manager.__class__.__name__,
                )
            except Exception as exc:
                self.log.error(
                    "[LifecycleManager] 组件 '%s' 关闭失败，继续关闭其他组件",
                    manager.__class__.__name__,
                    exc_info=True,
                )

    async def _rollback_startup(self, count: int) -> None:
        """回滚已启动的管理器
            当某个管理器启动失败时，关闭之前已成功启动的管理器。

        Args:
            count: 已启动的管理器数量
        """
        if count == 0:
            return

        self.log.warning("[LifecycleManager] 回滚 %d 个已启动的组件...", count)

        # 逆序关闭已启动的管理器
        for manager in reversed(self._managers[:count]):
            try:
                await manager._on_shutdown()
                self.log.info(
                    "[LifecycleManager] 回滚组件 '%s' 成功",
                    manager.__class__.__name__,
                )
            except Exception as exc:
                self.log.error(
                    "[LifecycleManager] 回滚组件 '%s' 失败",
                    manager.__class__.__name__,
                    exc_info=True,
                )
