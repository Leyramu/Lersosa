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

import time
from typing import Any, Callable

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.recovery import ExceptionRecoveryStrategyI
from app.common.utils import SnapshotHelper
from fastapi.logger import logger


@Component
class RetryRecoveryStrategy(ExceptionRecoveryStrategyI):
    """重试恢复策略
    
    Notes：
        - 支持配置最大重试次数
        - 指数退避延迟机制
        - 可配置的重试异常类型
        - 自动保存失败快照
    """

    def __init__(self, config: Any = None):
        """初始化重试策略
        
        Args:
            config: 领域实体中的插件配置对象，支持以下属性：
                - max_retries: 最大重试次数（默认3）
                - base_delay: 基础延迟时间（秒，默认1.0）
                - backoff_factor: 退避因子（默认2.0，延迟 = base_delay * backoff_factor^retry_count）
                - retryable_exceptions: 可重试的异常类型元组（默认ConnectionError, TimeoutError, OSError）
        """
        if config:
            self._max_retries = getattr(config, 'max_retries', 3)
            self._base_delay = getattr(config, 'base_delay', 1.0)
            self._backoff_factor = getattr(config, 'backoff_factor', 2.0)
            self._retryable_exceptions = getattr(config, 'retryable_exceptions', 
                                                (ConnectionError, TimeoutError, OSError))
        else:
            self._max_retries = 3
            self._base_delay = 1.0
            self._backoff_factor = 2.0
            # 包含 Playwright 超时异常
            try:
                from playwright.async_api import TimeoutError as PlaywrightTimeoutError
                self._retryable_exceptions = (ConnectionError, TimeoutError, OSError, PlaywrightTimeoutError)
            except ImportError:
                self._retryable_exceptions = (ConnectionError, TimeoutError, OSError)

    @property
    def strategy_name(self) -> str:
        return "retry_recovery"

    def _on_exception(self, exception: Exception, context: dict[str, Any]) -> dict[str, Any]:
        """处理异常，判断是否应该重试并返回建议
        
        Args:
            exception: 捕获的异常
            context: 异常上下文，包含 retry_count 等信息
            
        Returns:
            恢复策略建议，包含 action、delay、message 等字段
        """
        retry_count = context.get("retry_count", 0)
        
        # 检查是否达到最大重试次数
        if retry_count >= self._max_retries:
            # 保存快照
            SnapshotHelper.save_snapshot(
                context, 
                str(exception), 
                snapshot_type="retry_abort",
                extra_data={"retry_count": retry_count, "reason": "max_retries_reached"}
            )
            return {
                "action": "abort",
                "message": f"达到最大重试次数({self._max_retries})，停止重试"
            }
        
        # 检查异常类型是否可重试
        exc_type_name = type(exception).__name__.lower()
        is_timeout_related = 'timeout' in exc_type_name or 'timeout' in str(exception).lower()
        
        if not isinstance(exception, self._retryable_exceptions) and not is_timeout_related:
            # 保存快照
            SnapshotHelper.save_snapshot(
                context, 
                str(exception), 
                snapshot_type="retry_abort",
                extra_data={"retry_count": retry_count, "reason": "non_retryable_exception"}
            )
            return {
                "action": "abort",
                "message": f"异常类型不可重试: {type(exception).__name__}"
            }
        
        # 计算指数退避延迟
        delay = self._base_delay * (self._backoff_factor ** retry_count)
        
        return {
            "action": "retry",
            "delay": delay,
            "retry_count": retry_count + 1,
            "message": f"将在 {delay:.2f} 秒后重试（第 {retry_count + 1}/{self._max_retries} 次）"
        }

    def execute(self, func: Callable[..., Any], context: dict[str, Any]) -> Any:
        """在重试逻辑中执行函数
        
        Args:
            func: 需要执行的函数
            context: 执行上下文，包含 url、task_name、headers 等信息
            
        Returns:
            任务执行结果，如果所有重试都失败则返回 None
        """
        retry_count = 0
        
        while True:
            try:
                # 执行下游责任链
                result = func()
                
                # 检查结果有效性
                if result is not None:
                    logger.info(f"请求成功（重试{retry_count}次后）")
                    return result
                
                # 结果为空，视为业务失败，触发重试
                raise RuntimeError("下游执行返回空结果")
                    
            except Exception as exc:
                # 调用决策方法判断是否继续
                advice = self._on_exception(exc, {**context, "retry_count": retry_count})
                action = advice.get("action")
                
                if action == "retry":
                    # 执行重试
                    delay = advice.get("delay", 1.0)
                    logger.warning(f"重试策略: {advice.get('message')}")
                    time.sleep(delay)
                    retry_count += 1
                    
                elif action == "abort":
                    # 终止重试
                    logger.error(f"重试策略终止: {advice.get('message')}")
                    return None
                    
                else:
                    # 未知动作，终止
                    logger.error(f"未知的重试动作: {action}")
                    return None
