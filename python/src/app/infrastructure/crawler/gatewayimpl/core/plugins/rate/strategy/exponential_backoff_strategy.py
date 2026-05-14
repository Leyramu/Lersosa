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
class ExponentialBackoffStrategy(RateControlStrategyI):
    """指数退避速率控制策略"""

    def __init__(self, config: Any = None):
        if config:
            self._base_delay = getattr(config, 'base_delay', 1.0)
            self._max_delay = getattr(config, 'max_delay', 60.0)
            self._backoff_factor = getattr(config, 'backoff_factor', 2.0)
        else:
            self._base_delay = 1.0
            self._max_delay = 60.0
            self._backoff_factor = 2.0

        self._consecutive_failures: dict[str, int] = defaultdict(int)
        self._last_request_time: dict[str, float] = {}

    @property
    def strategy_name(self) -> str:
        return "exponential_backoff"

    def wait_before_request(self, url: str) -> None:
        current_time = time.time()
        last_time = self._last_request_time.get(url, 0)

        failures = self._consecutive_failures.get(url, 0)
        delay = min(
            self._max_delay,
            self._base_delay * (self._backoff_factor ** failures)
        )

        elapsed = current_time - last_time
        if elapsed < delay:
            time.sleep(delay - elapsed)

    def record_response(self, url: str, success: bool, latency: float) -> None:
        self._last_request_time[url] = time.time()

        if success:
            self._consecutive_failures[url] = 0
        else:
            self._consecutive_failures[url] += 1

    def get_delay_seconds(self) -> float:
        return self._base_delay
