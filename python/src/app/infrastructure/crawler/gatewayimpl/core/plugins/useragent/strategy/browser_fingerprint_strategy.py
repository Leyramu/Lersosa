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

from typing import Any

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.useragent import UserAgentStrategyI


@Component
class BrowserFingerprintStrategy(UserAgentStrategyI):
    """浏览器指纹管理策略"""

    def __init__(self, config: Any = None):
        default_uas = self.get_default_user_agents()
        if config:
            custom_uas = getattr(config, 'ua_list', [])
            ua_list = custom_uas if custom_uas else default_uas
        else:
            ua_list = default_uas
            
        self._fingerprints = self._generate_fingerprints(ua_list)
        self._current_fingerprint_id = 0

    @property
    def strategy_name(self) -> str:
        return "browser_fingerprint"

    def get_user_agent(self, url: str = "") -> str:
        fingerprint = self._fingerprints[self._current_fingerprint_id]
        return fingerprint["user_agent"]

    def rotate(self) -> None:
        self._current_fingerprint_id = (
                                               self._current_fingerprint_id + 1
                                       ) % len(self._fingerprints)

    def add_user_agent(self, ua: str) -> None:
        new_fingerprint = {
            "user_agent": ua,
            "platform": "Win32",
            "language": "en-US,en;q=0.9",
        }
        self._fingerprints.append(new_fingerprint)

    def get_full_fingerprint(self) -> dict[str, str]:
        return self._fingerprints[self._current_fingerprint_id].copy()

    @staticmethod
    def _generate_fingerprints(user_agents: list[str]) -> list[dict[str, str]]:
        fingerprints = []
        platforms = ["Win32", "MacIntel", "Linux x86_64"]
        timezones = ["America/New_York", "America/Los_Angeles", "Europe/London", "Asia/Shanghai"]
        
        for i, ua in enumerate(user_agents):
            fingerprints.append({
                "user_agent": ua,
                "platform": platforms[i % len(platforms)],
                "language": "en-US,en;q=0.9",
                "timezone": timezones[i % len(timezones)],
            })
        return fingerprints
