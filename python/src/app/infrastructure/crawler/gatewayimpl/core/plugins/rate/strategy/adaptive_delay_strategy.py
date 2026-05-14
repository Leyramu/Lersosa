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
from typing import Any

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.rate import RateControlStrategyI


@Component
class AdaptiveDelayStrategy(RateControlStrategyI):
    """自适应延迟速率控制策略"""

    def __init__(self, config: Any = None):
        if config:
            self._min_delay = getattr(config, 'min_delay', 0.5)
            self._max_delay = getattr(config, 'max_delay', 10.0)
            self._init_delay = getattr(config, 'init_delay', 1.0)
            self._adjustment_factor = getattr(config, 'adjustment_factor', 0.2)
        else:
            self._min_delay = 0.5
            self._max_delay = 10.0
            self._init_delay = 1.0
            self._adjustment_factor = 0.2

        self._last_request_time: dict[str, float] = {}
        self._recent_errors = []
        self._recent_latencies = []
        self._window_size = 20

    @property
    def strategy_name(self) -> str:
        return "adaptive_delay"

    def wait_before_request(self, url: str) -> None:
        current_time = time.time()
        last_time = self._last_request_time.get(url, 0)

        elapsed = current_time - last_time
        if elapsed < self._init_delay:
            time.sleep(self._init_delay - elapsed)

    def record_response(self, url: str, success: bool, latency: float) -> None:
        self._last_request_time[url] = time.time()

        self._recent_errors.append(0 if success else 1)
        self._recent_latencies.append(latency)

        if len(self._recent_errors) > self._window_size:
            self._recent_errors.pop(0)
            self._recent_latencies.pop(0)

        if len(self._recent_errors) >= 10:
            self._adjust_delay()

    def get_delay_seconds(self) -> float:
        return self._init_delay

    def _adjust_delay(self) -> None:
        error_rate = sum(self._recent_errors) / len(self._recent_errors)
        avg_latency = sum(self._recent_latencies) / len(self._recent_latencies)

        if error_rate > 0.3:
            self._init_delay = min(
                self._max_delay,
                self._init_delay * (1 + self._adjustment_factor)
            )
        elif avg_latency < 1.0 and error_rate < 0.1:
            self._init_delay = max(
                self._min_delay,
                self._init_delay * (1 - self._adjustment_factor)
            )
