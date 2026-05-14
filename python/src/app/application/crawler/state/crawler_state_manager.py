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

from datetime import datetime, timezone
from threading import Lock
from typing import Any

from app.core.container.annotations import Component


@Component
class CrawlerStateManager:
    """爬虫状态管理器
    
    Notes：
    - 管理爬取执行生命周期状态
    - 提供并发控制（防止重复执行）
    - 记录执行历史和错误信息
    """

    def __init__(self) -> None:
        """初始化状态管理器"""
        self._lock = Lock()
        self._is_running = False
        self._last_summary: dict[str, Any] | None = None
        self._last_error: str = ""
        self._last_started_at: str = ""
        self._last_finished_at: str = ""

    def try_start(self) -> tuple[bool, dict[str, Any] | None]:
        """尝试开始执行
            
        Returns:
            (是否成功, 错误信息字典)
            - 如果已经在运行，返回
            - 如果可以运行，返回
        """
        with self._lock:
            if self._is_running:
                return False, {
                    "status": "running",
                    "message": "爬虫已在运行中",
                    "last_summary": self._last_summary,
                }

            self._is_running = True
            self._last_error = ""
            self._last_started_at = datetime.now(timezone.utc).isoformat()
            return True, None

    def mark_finished(self, summary: dict[str, Any]) -> None:
        """标记执行完成
        
        Args:
            summary: 执行结果摘要
        """
        self._last_summary = summary
        self._last_finished_at = datetime.now(timezone.utc).isoformat()
        with self._lock:
            self._is_running = False

    def mark_failed(self, error: str) -> None:
        """标记执行失败
        
        Args:
            error: 错误信息
        """
        self._last_error = error
        self._last_finished_at = datetime.now(timezone.utc).isoformat()
        with self._lock:
            self._is_running = False

    def try_stop(self) -> tuple[bool, dict[str, Any] | None]:
        """尝试停止执行
        
        Returns:
            (是否成功, 错误信息字典)
            - 如果未在运行，返回 (False, 错误信息)
            - 如果可以停止，返回 (True, None)
        """
        with self._lock:
            if not self._is_running:
                return False, {
                    "status": "stopped",
                    "message": "爬虫未在运行中"
                }

            self._is_running = False
            self._last_finished_at = datetime.now(timezone.utc).isoformat()
            return True, None

    def get_status(self) -> dict[str, Any]:
        """获取当前状态
        
        Returns:
            状态信息字典
        """
        return {
            "is_running": self._is_running,
            "last_started_at": self._last_started_at if self._last_started_at else None,
            "last_finished_at": self._last_finished_at if self._last_finished_at else None,
            "last_error": self._last_error if self._last_error else None,
            "last_summary": self._last_summary,
            "total_records": self._last_summary.get("records", 0) if self._last_summary else 0,
        }
