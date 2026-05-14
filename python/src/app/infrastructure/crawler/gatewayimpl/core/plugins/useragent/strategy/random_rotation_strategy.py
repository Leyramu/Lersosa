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

import random
from typing import Any

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.useragent import UserAgentStrategyI


@Component
class RandomRotationStrategy(UserAgentStrategyI):
    """随机轮换 User-Agent 策略"""

    def __init__(self, config: Any = None):
        default_uas = self.get_default_user_agents()
        if config:
            custom_uas = getattr(config, 'ua_list', [])
            self._user_agents = custom_uas if custom_uas else default_uas
        else:
            self._user_agents = default_uas

    @property
    def strategy_name(self) -> str:
        return "random_rotation"

    def get_user_agent(self, url: str = "") -> str:
        return random.choice(self._user_agents)

    def rotate(self) -> None:
        pass

    def add_user_agent(self, ua: str) -> None:
        if ua not in self._user_agents:
            self._user_agents.append(ua)
