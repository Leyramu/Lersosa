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

import threading
from typing import Any

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.concurrency import ConcurrencyControlStrategyI


@Component
class AdaptiveConcurrencyStrategy(ConcurrencyControlStrategyI):
    """自适应并发数控制策略"""

    def __init__(self, config: Any = None):
        if config:
            self._min_concurrency = getattr(config, 'min_concurrency', 1)
            self._max_concurrency = getattr(config, 'max_concurrency', 20)
            self._init_concurrency = getattr(config, 'init_concurrency', 5)
            self._adjustment_step = getattr(config, 'adjustment_step', 1)
            self._adjustment_interval = getattr(config, 'adjustment_interval', 10)
        else:
            self._min_concurrency = 1
            self._max_concurrency = 20
            self._init_concurrency = 5
            self._adjustment_step = 1
            self._adjustment_interval = 10
            
        self._current_concurrency = self._init_concurrency
        self._request_count = 0
        self._semaphore = threading.Semaphore(self._init_concurrency)
        self._active_tasks = set()
        self._lock = threading.Lock()

    @property
    def strategy_name(self) -> str:
        return "adaptive_concurrency"

    def acquire(self, task_id: str) -> bool:
        acquired = self._semaphore.acquire(blocking=False)
        if acquired:
            self._active_tasks.add(task_id)
        return acquired

    def release(self, task_id: str) -> None:
        if task_id in self._active_tasks:
            self._active_tasks.remove(task_id)
        self._semaphore.release()

    def get_current_concurrency(self) -> int:
        return self._current_concurrency

    def update_concurrency(self, metrics: dict[str, float]) -> None:
        with self._lock:
            self._request_count += 1

            if self._request_count % self._adjustment_interval != 0:
                return

            avg_latency = metrics.get("avg_latency", 0.0)
            error_rate = metrics.get("error_rate", 0.0)

            if error_rate > 0.3:
                new_concurrency = max(
                    self._min_concurrency,
                    self._current_concurrency - self._adjustment_step
                )
            elif avg_latency > 5.0:
                new_concurrency = max(
                    self._min_concurrency,
                    self._current_concurrency - self._adjustment_step
                )
            elif avg_latency < 1.0 and error_rate < 0.1:
                new_concurrency = min(
                    self._max_concurrency,
                    self._current_concurrency + self._adjustment_step
                )
            else:
                return

            if new_concurrency != self._current_concurrency:
                diff = new_concurrency - self._current_concurrency
                if diff > 0:
                    for _ in range(diff):
                        self._semaphore.release()
                else:
                    if len(self._active_tasks) <= new_concurrency:
                        for _ in range(-diff):
                            self._semaphore.acquire()

                self._current_concurrency = new_concurrency
