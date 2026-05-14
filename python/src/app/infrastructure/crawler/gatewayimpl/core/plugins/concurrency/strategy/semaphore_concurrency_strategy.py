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
import time
from collections import defaultdict
from typing import Any

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.concurrency import ConcurrencyControlStrategyI


@Component
class SemaphoreConcurrencyStrategy(ConcurrencyControlStrategyI):
    """信号量并发控制策略"""

    def __init__(self, config: Any = None):
        if config:
            self._max_concurrency = getattr(config, 'max_concurrency', 5)
            self._fair = getattr(config, 'fair', True)
        else:
            self._max_concurrency = 5
            self._fair = True
            
        self._semaphore = threading.BoundedSemaphore(self._max_concurrency)
        self._active_tasks = set()
        self._waiting_tasks = defaultdict(float)
        self._lock = threading.RLock()

    @property
    def strategy_name(self) -> str:
        return "semaphore_concurrency"

    def acquire(self, task_id: str) -> bool:
        self._waiting_tasks[task_id] = time.time()

        acquired = self._semaphore.acquire(blocking=False)
        if acquired:
            with self._lock:
                del self._waiting_tasks[task_id]
                self._active_tasks.add(task_id)
        return acquired

    def release(self, task_id: str) -> None:
        with self._lock:
            if task_id in self._active_tasks:
                self._active_tasks.remove(task_id)
        self._semaphore.release()

    def get_current_concurrency(self) -> int:
        return self._max_concurrency

    def update_concurrency(self, metrics: dict[str, float]) -> None:
        with self._lock:
            error_rate = metrics.get("error_rate", 0.0)
            avg_wait_time = metrics.get("avg_wait_time", 0.0)

            if error_rate > 0.3:
                new_max = max(1, int(self._max_concurrency * 0.8))
            elif avg_wait_time > 5.0:
                new_max = min(50, int(self._max_concurrency * 1.2))
            else:
                return

            if new_max != self._max_concurrency:
                diff = new_max - self._max_concurrency
                if diff > 0:
                    for _ in range(diff):
                        self._semaphore.release()
                else:
                    pass

                self._max_concurrency = new_max

    def get_waiting_tasks_count(self) -> int:
        return len(self._waiting_tasks)
