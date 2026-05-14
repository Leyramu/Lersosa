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

from typing import Any, Callable

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.recovery import ExceptionRecoveryStrategyI
from app.common.utils import SnapshotHelper
from fastapi.logger import logger


@Component
class FallbackRecoveryStrategy(ExceptionRecoveryStrategyI):
    """降级恢复策略
    
    Notes：
        - 支持配置多个备选策略
        - 按顺序尝试备选策略
        - 所有策略失败后返回默认值
        - 自动保存降级快照
    """

    def __init__(self, config: Any = None):
        """初始化降级策略
        
        Args:
            config: 领域实体中的插件配置对象，支持以下属性：
                - fallback_strategies: 备选策略列表（默认空列表）
                  例如: ["static_page", "dynamic_render", "api_endpoint"]
        """
        if config:
            self._fallback_strategies = getattr(config, 'fallback_strategies', [])
        else:
            self._fallback_strategies = []

        self._current_strategy_index = 0

    @property
    def strategy_name(self) -> str:
        return "fallback_recovery"

    def _on_exception(self, exception: Exception, context: dict[str, Any]) -> dict[str, Any]:
        """处理异常，选择下一个备选策略
        
        Args:
            exception: 捕获的异常
            context: 异常上下文
            
        Returns:
            恢复策略建议，包含 action、fallback_strategy、message 等字段
        """
        # 检查是否还有备选策略
        if self._current_strategy_index < len(self._fallback_strategies):
            fallback = self._fallback_strategies[self._current_strategy_index]
            self._current_strategy_index += 1

            return {
                "action": "fallback",
                "fallback_strategy": fallback,
                "message": f"切换到备选策略: {fallback}"
            }
        else:
            return {
                "action": "use_default",
                "message": "所有备选策略已用完，使用默认值"
            }

    def execute(self, func: Callable[..., Any], context: dict[str, Any]) -> Any:
        """执行并尝试降级处理
        
        Args:
            func: 需要执行的函数
            context: 执行上下文，包含 url、task_name、headers 等信息
            
        Returns:
            任务执行结果，如果所有策略都失败则返回 None
            
        Note:
            当前实现仅记录日志，实际场景中应根据 advice 调用不同的备选抓取方法
        """
        try:
            result = func()
            logger.info("主策略执行成功")
            return result

        except Exception as exc:
            advice = self._on_exception(exc, context)
            action = advice.get("action")

            # 保存降级快照
            SnapshotHelper.save_snapshot(
                context,
                str(exc),
                snapshot_type="fallback",
                extra_data={
                    "fallback_strategies": self._fallback_strategies,
                    "current_index": self._current_strategy_index,
                    "action": action
                }
            )

            if action == "fallback":
                fallback_strategy = advice.get("fallback_strategy")
                logger.warning(f"降级策略: {advice.get('message')}")

                return None

            elif action == "use_default":
                # 所有备选策略已用完
                logger.error(f"降级策略: {advice.get('message')}")
                return None

            else:
                # 未知动作
                logger.error(f"未知的降级动作: {action}")
                return None
