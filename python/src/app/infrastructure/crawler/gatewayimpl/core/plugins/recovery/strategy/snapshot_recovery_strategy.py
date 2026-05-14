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

from fastapi.logger import logger

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.recovery import ExceptionRecoveryStrategyI
from app.common.utils import SnapshotHelper


@Component
class SnapshotRecoveryStrategy(ExceptionRecoveryStrategyI):
    """快照保存恢复策略
    
    Notes：
        - 自动保存异常时的完整上下文
        - 支持配置是否保存 HTML 和 Headers
        - 智能截断过大的 HTML 内容
        - 适用于调试和问题排查场景
    """

    def __init__(self, config: Any = None):
        """初始化快照策略
        
        Args:
            config: 领域实体中的插件配置对象，支持以下属性：
                - save_html: 是否保存 HTML 内容（默认True）
                - save_headers: 是否保存请求头（默认True）
        """
        if config:
            self._save_html = getattr(config, 'save_html', True)
            self._save_headers = getattr(config, 'save_headers', True)
        else:
            self._save_html = True
            self._save_headers = True

    @property
    def strategy_name(self) -> str:
        return "snapshot_recovery"

    def _on_exception(self, exception: Exception, context: dict[str, Any]) -> dict[str, Any]:
        """处理异常，保存快照并返回建议
        
        Args:
            exception: 捕获的异常
            context: 异常上下文
            
        Returns:
            恢复策略建议，包含 action、snapshot_path、message 等字段
        """
        snapshot_path = SnapshotHelper.save_snapshot(
            context, 
            str(exception),
            snapshot_type="snapshot_recovery"
        )

        return {
            "action": "log_and_continue",
            "snapshot_path": snapshot_path,
            "message": f"已保存异常快照: {snapshot_path}"
        }



    def execute(self, func: Callable[..., Any], context: dict[str, Any]) -> Any:
        """执行并保存异常快照
        
        Args:
            func: 需要执行的函数
            context: 执行上下文，包含 url、task_name、headers、html 等信息
            
        Returns:
            任务执行结果，如果失败则返回 None
            
        Note:
            此策略主要用于调试和问题排查，不会进行重试或降级
        """
        try:
            result = func()
            logger.info("请求执行成功")
            return result
            
        except Exception as exc:
            advice = self._on_exception(exc, context)
            logger.warning(f"快照策略: {advice.get('message')}")
            return None
