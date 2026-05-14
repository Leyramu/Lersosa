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
from typing import Any

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.concurrency import ConcurrencyControlStrategyI


@Component
class TokenBucketStrategy(ConcurrencyControlStrategyI):
    """令牌桶并发控制策略"""

    def __init__(self, config: Any = None):
        if config:
            self._tokens_per_second = getattr(config, 'tokens_per_second', 5.0)
            self._bucket_capacity = getattr(config, 'bucket_capacity', 10)
        else:
            self._tokens_per_second = 5.0
            self._bucket_capacity = 10
            
        self._current_tokens = float(self._bucket_capacity)
        self._last_refill_time = time.time()
        self._active_tasks = set()
        self._lock = threading.Lock()

    @property
    def strategy_name(self) -> str:
        return "tokens_bucket"

    def acquire(self, task_id: str) -> bool:
        with self._lock:
            current_time = time.time()
            self._refill_tokens(current_time)

            if self._current_tokens >= 1:
                self._current_tokens -= 1
                self._active_tasks.add(task_id)
                return True
            return False

    def release(self, task_id: str) -> None:
        with self._lock:
            if task_id in self._active_tasks:
                self._active_tasks.remove(task_id)

    def get_current_concurrency(self) -> int:
        with self._lock:
            return int(self._current_tokens)

    def update_concurrency(self, metrics: dict[str, float]) -> None:
        with self._lock:
            error_rate = metrics.get("error_rate", 0.0)
            if error_rate > 0.3:
                self._tokens_per_second = max(1.0, self._tokens_per_second * 0.8)
            elif error_rate < 0.1:
                self._tokens_per_second = min(
                    self._bucket_capacity, self._tokens_per_second * 1.1
                )

    def _refill_tokens(self, current_time: float) -> None:
        if current_time > self._last_refill_time:
            elapsed = current_time - self._last_refill_time
            tokens_to_add = elapsed * self._tokens_per_second
            self._current_tokens = min(
                self._bucket_capacity, self._current_tokens + tokens_to_add
            )
            self._last_refill_time = current_time
