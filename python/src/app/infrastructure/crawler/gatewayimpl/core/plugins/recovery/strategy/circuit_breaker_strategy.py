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

from fastapi.logger import logger

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.recovery import ExceptionRecoveryStrategyI
from app.common.utils import SnapshotHelper


@Component
class CircuitBreakerStrategy(ExceptionRecoveryStrategyI):
    """熔断器恢复策略"""

    def __init__(self, config: Any = None):
        """初始化熔断器策略
        
        Args:
            config: 领域实体中的插件配置对象，支持以下属性：
                - failure_threshold: 失败阈值，达到此值后熔断器打开（默认5）
                - recovery_timeout: 恢复超时时间（秒），之后进入半开状态（默认60.0）
                - success_threshold: 成功阈值，半开状态下成功次数达到此值后关闭熔断器（默认2）
                - max_probe_attempts: 半开状态下的最大试探次数（默认3）
        """
        if config:
            self._failure_threshold = getattr(config, 'failure_threshold', 5)
            self._recovery_timeout = getattr(config, 'recovery_timeout', 60.0)
            self._success_threshold = getattr(config, 'success_threshold', 2)
            self._max_probe_attempts = getattr(config, 'max_probe_attempts', 3)
        else:
            self._failure_threshold = 5
            self._recovery_timeout = 60.0
            self._success_threshold = 2
            self._max_probe_attempts = 3

        self._state = "closed"  # closed, open, half-open
        self._failure_count = 0
        self._success_count = 0
        self._last_failure_time = 0.0

    @property
    def strategy_name(self) -> str:
        return "circuit_breaker"

    def _check_state(self) -> bool:
        """检查当前状态是否允许执行
        
        Returns:
            bool: True 如果允许执行，False 如果处于熔断状态
        """
        if self._state == "open":
            # 检查是否过了恢复超时时间
            if time.time() - self._last_failure_time > self._recovery_timeout:
                self._state = "half-open"
                self._success_count = 0
                logger.info("熔断器进入半开状态，允许试探性请求")
                return True
            else:
                logger.warning(
                    f"熔断器处于开启状态，拒绝执行。剩余恢复时间: {self._recovery_timeout - (time.time() - self._last_failure_time):.1f}s")
                return False
        return True

    def _on_success(self) -> None:
        """处理成功请求，更新状态"""
        if self._state == "half-open":
            self._success_count += 1
            if self._success_count >= self._success_threshold:
                self._state = "closed"
                self._failure_count = 0
                self._success_count = 0
                logger.info("熔断器已关闭，服务恢复正常")
        elif self._state == "closed":
            # 在关闭状态下，成功请求可以重置失败计数（可选策略）
            self._failure_count = 0

    def _on_exception(self, exception: Exception, context: dict[str, Any]) -> dict[str, Any]:
        """处理异常，更新状态并返回建议"""
        self._failure_count += 1
        self._last_failure_time = time.time()

        if self._state == "half-open":
            # 半开状态下再次失败，立即重新熔断
            self._state = "open"
            logger.warning("半开状态下请求失败，重新进入熔断状态")
            return {
                "action": "abort",
                "message": "半开试探失败，重新熔断"
            }

        if self._failure_count >= self._failure_threshold:
            self._state = "open"
            return {
                "action": "circuit_break",
                "message": f"失败次数达到阈值({self._failure_threshold})，熔断器已打开"
            }

        return {
            "action": "retry",
            "message": f"失败计数: {self._failure_count}/{self._failure_threshold}"
        }



    def execute(self, func: Callable[..., Any], context: dict[str, Any]) -> Any:
        """在熔断逻辑中执行函数，支持熔断状态下的试探性重试
        
        Args:
            func: 需要执行的函数
            context: 执行上下文，包含 url、task_name、headers 等信息
            
        Returns:
            任务执行结果，如果熔断或所有试探失败则返回 None
        """
        retry_count = 0

        while True:
            # 检查熔断器状态
            if not self._check_state():
                logger.warning(f"熔断器处于开启状态，拒绝执行请求")
                SnapshotHelper.save_snapshot(
                    context, 
                    "熔断器开启，请求被拒绝",
                    snapshot_type="circuit_breaker_reject",
                    extra_data={"state": self._state}
                )
                return None

            try:
                # 执行下游责任链
                result = func()

                # 执行成功，更新熔断器状态
                self._on_success()
                logger.info(f"请求成功，熔断器状态: {self._state}")
                return result

            except Exception as exc:
                # 执行失败，调用决策逻辑
                advice = self._on_exception(exc, context)
                SnapshotHelper.save_snapshot(
                    context, 
                    str(exc),
                    snapshot_type="circuit_breaker_error",
                    extra_data={"state": self._state, "failure_count": self._failure_count}
                )
                logger.error(f"熔断器策略: {advice.get('message')}")

                # 根据建议决定下一步
                action = advice.get("action")

                if action == "abort":
                    # 半开状态试探失败，立即终止
                    logger.warning("半开试探失败，终止重试")
                    return None

                elif action == "circuit_break":
                    # 达到失败阈值，熔断器打开，终止重试
                    logger.warning("熔断器已打开，终止重试")
                    return None

                elif action == "retry":
                    # 未达到阈值，可以继续试探
                    retry_count += 1

                    # 在半开状态下限制试探次数
                    if self._state == "half-open" and retry_count >= self._max_probe_attempts:
                        logger.warning(f"半开状态下已达最大试探次数({self._max_probe_attempts})，重新熔断")
                        self._state = "open"
                        self._last_failure_time = time.time()
                        return None

                    # 计算退避延迟（指数退避，最大10秒）
                    delay = min(1.0 * (2 ** retry_count), 10.0)
                    logger.info(f"将在 {delay:.2f} 秒后进行第 {retry_count} 次试探")
                    time.sleep(delay)

                else:
                    # 未知动作，终止
                    logger.error(f"未知的熔断器动作: {action}")
                    return None
