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
from urllib.parse import urlparse

import requests

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.robots import RobotsProtocolStrategyI


@Component
class SmartRobotsProtocol(RobotsProtocolStrategyI):
    """智能 robots.txt 策略"""

    def __init__(self, config: Any = None):
        if config:
            self._cache_ttl = getattr(config, 'cache_ttl', 3600)
            self._timeout = getattr(config, 'timeout', 10)
            self._crawl_delay = getattr(config, 'crawl_delay', 0.0)
        else:
            self._cache_ttl = 3600
            self._timeout = 10
            self._crawl_delay = 0.0
        self._cache: dict[str, dict[str, Any]] = {}

    @property
    def strategy_name(self) -> str:
        return "smart"

    def can_fetch(self, url: str, user_agent: str = "*") -> bool:
        domain = self._extract_domain(url)

        if domain not in self._cache:
            self.refresh(domain)

        cache_entry = self._cache.get(domain)
        if not cache_entry:
            return True

        if time.time() - cache_entry["timestamp"] > self._cache_ttl:
            self.refresh(domain)
            cache_entry = self._cache.get(domain)

        if not cache_entry:
            return True

        rules = cache_entry["rules"]
        return self._check_rules(url, user_agent, rules)

    def get_crawl_delay(self, user_agent: str = "*") -> float:
        return self._crawl_delay

    def refresh(self, domain: str) -> None:
        try:
            robots_url = f"https://{domain}/robots.txt"
            response = requests.get(robots_url, timeout=self._timeout)
            
            if response.status_code == 200:
                rules = self._parse_robots_txt(response.text)
                self._cache[domain] = {"rules": rules, "timestamp": time.time()}
            else:
                self._cache[domain] = {"rules": [], "timestamp": time.time()}
        except requests.exceptions.RequestException:
            self._cache[domain] = {"rules": [], "timestamp": time.time()}

    @staticmethod
    def _extract_domain(url: str) -> str:
        try:
            parsed = urlparse(url)
            return parsed.netloc.lower()
        except (ValueError, AttributeError):
            return ""

    @staticmethod
    def _parse_robots_txt(content: str) -> list[dict[str, Any]]:
        rules = []
        current_agents = []
        
        for line in content.splitlines():
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            
            if line.lower().startswith("user-agent:"):
                agent = line.split(":", 1)[1].strip()
                current_agents = [agent] if agent != "*" else ["*"]
            elif line.lower().startswith("disallow:"):
                path = line.split(":", 1)[1].strip()
                for agent in current_agents:
                    rules.append({"useragent": agent, "path": path, "allow": False})
            elif line.lower().startswith("allow:"):
                path = line.split(":", 1)[1].strip()
                for agent in current_agents:
                    rules.append({"useragent": agent, "path": path, "allow": True})
        
        return rules

    @staticmethod
    def _check_rules(url: str, user_agent: str, rules: list[dict[str, Any]]) -> bool:
        parsed = urlparse(url)
        path = parsed.path
        
        matched_rules = []
        for rule in rules:
            if rule["useragent"] == user_agent or rule["useragent"] == "*":
                if path.startswith(rule["path"]) or rule["path"] == "*":
                    matched_rules.append(rule)
        
        if not matched_rules:
            return True
        
        for rule in matched_rules:
            if rule["allow"]:
                return True
        
        for rule in matched_rules:
            if not rule["allow"]:
                return False
        
        return True
