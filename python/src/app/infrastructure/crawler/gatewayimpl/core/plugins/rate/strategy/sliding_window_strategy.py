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
from collections import defaultdict
from typing import Any

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.rate import RateControlStrategyI


@Component
class SlidingWindowStrategy(RateControlStrategyI):
    """滑动窗口限流速率控制策略"""

    def __init__(self, config: Any = None):
        if config:
            self._max_requests = getattr(config, 'max_requests', 60)
            self._window_size = getattr(config, 'window_size', 60.0)
        else:
            self._max_requests = 60
            self._window_size = 60.0
            
        self._request_timestamps: dict[str, list[float]] = defaultdict(list)
        self._last_request_time: dict[str, float] = {}

    @property
    def strategy_name(self) -> str:
        return "sliding_window"

    def wait_before_request(self, url: str) -> None:
        current_time = time.time()
        
        self._cleanup_old_timestamps(url, current_time)
        
        timestamps = self._request_timestamps[url]
        if len(timestamps) >= self._max_requests:
            oldest_timestamp = timestamps[0]
            wait_time = oldest_timestamp + self._window_size - current_time
            if wait_time > 0:
                time.sleep(wait_time)

    def record_response(self, url: str, success: bool, latency: float) -> None:
        current_time = time.time()
        self._request_timestamps[url].append(current_time)
        self._last_request_time[url] = current_time

    def get_delay_seconds(self) -> float:
        return self._window_size / self._max_requests

    def _cleanup_old_timestamps(self, url: str, current_time: float) -> None:
        cutoff_time = current_time - self._window_size
        timestamps = self._request_timestamps[url]
        
        while timestamps and timestamps[0] < cutoff_time:
            timestamps.pop(0)
