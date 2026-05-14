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


from fastapi import FastAPI
from fastapi.logger import logger

from app.core.container.core import BeanContainerManager
from app.core.state import LifecycleManager


# Bean容器生命周期管理器
class BeanLifecycle(LifecycleManager):
    """Bean容器生命周期管理器

        管理依赖注入容器的初始化和清理
    """

    def __init__(self, app: FastAPI) -> None:
        """初始化Bean容器生命周期管理器
        
        Args:
            app: FastAPI应用实例
        """
        super().__init__(app, register_sub_managers=False)

    async def _on_startup(self) -> None:
        """在FastAPI启动时初始化Bean容器
        
        Notes：
            - 扫描所有带注解的组件
            - 注册Bean到容器
            - 自动注册控制器路由
            - 初始化依赖注入
        """
        logger.info("开始初始化Bean容器...")

        try:
            # 初始化Bean容器
            BeanContainerManager.initialize(self._app)
            # 将Bean容器绑定到FastAPI app.state
            self._app.state.bean_container = BeanContainerManager
            self._app.state.bean_container_initialized = True

            logger.info("Bean容器初始化成功")

        except Exception as exc:
            logger.error("Bean容器初始化失败: %s", exc, exc_info=True)
            self._app.state.bean_container_initialized = False
            raise

    async def _on_shutdown(self) -> None:
        """在FastAPI停止时清理Bean容器

            应用关闭时正确清理所有Bean资源
        """
        logger.info("正在关闭Bean容器...")

        try:
            # 清除app.state中的绑定
            if hasattr(self._app.state, "bean_container"):
                delattr(self._app.state, "bean_container")

            if hasattr(self._app.state, "bean_container_initialized"):
                self._app.state.bean_container_initialized = False

            BeanContainerManager.shutdown()
            logger.info("Bean容器关闭完成")

        except Exception as exc:
            logger.error("Bean容器关闭时出错: %s", exc, exc_info=True)
