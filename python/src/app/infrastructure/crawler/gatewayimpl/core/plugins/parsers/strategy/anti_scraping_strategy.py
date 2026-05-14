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

import hashlib
import re
from datetime import datetime, timezone
from typing import TYPE_CHECKING, Any

from bs4 import BeautifulSoup

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.parsers import ParserStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerResponseData


@Component
class AntiScrapingParserStrategy(ParserStrategyI):
    """反爬对抗解析器
    
    Notes：
        - 字体映射表管理
        - CSS 类名混淆还原
        - 动态参数提取
    """

    def __init__(self, config: Any = None):
        """初始化反爬对抗解析器
        
        Args:
            config: 领域实体中的插件配置对象
        """
        if config:
            self._font_mapping = getattr(config, 'font_mapping', {})
        else:
            self._font_mapping = {}

    @property
    def strategy_name(self) -> str:
        return "anti_scraping"

    def parse(self, response: "CrawlerResponseData", **kwargs: Any) -> list[dict[str, Any]]:
        """解析受保护的页面数据
        
        Args:
            response: 响应数据对象
            **kwargs: 额外参数
                - selectors: CSS 选择器映射
                - decrypt_fonts: 是否解密密体
                - extract_tokens: 是否提取动态令牌
                - source: 数据源
                
        Returns:
            提取的结构化数据列表
        """
        if not response.text:
            return []

        selectors = kwargs.get("selectors", {})
        decrypt_fonts = kwargs.get("decrypt_fonts", True)
        extract_tokens = kwargs.get("extract_tokens", False)
        source = kwargs.get("source", "unknown")

        html = response.text

        if decrypt_fonts and self._font_mapping:
            html = self._decrypt_fonts(html)

        tokens = {}
        if extract_tokens:
            tokens = self._extract_dynamic_tokens(html)

        soup = BeautifulSoup(html, "html.parser")
        item_selector = selectors.get("item", "div.item")
        items = soup.select(item_selector)

        records = []
        for item in items:
            record = self._extract_fields_with_obfuscation(item, selectors)
            if record:
                if tokens:
                    record.update(tokens)

                record["source"] = source
                record["url"] = response.url
                record["crawl_time"] = datetime.now(timezone.utc).isoformat()

                title = record.get("title", "")
                raw_id = f"{source}|{response.url}|{str(title)[:50]}".encode("utf-8")
                record["record_id"] = hashlib.md5(raw_id).hexdigest()  # noqa: S324

                records.append(record)

        return records

    def _decrypt_fonts(self, html: str) -> str:
        """解密字体加密"""
        result = html
        for encoded, decoded in self._font_mapping.items():
            result = result.replace(encoded, decoded)
        return result

    @staticmethod
    def _extract_dynamic_tokens(html: str) -> dict[str, str]:
        """提取动态令牌"""
        tokens = {}

        csrf_match = re.search(r'name="csrf_token"\s+value="([^"]+)"', html)
        if csrf_match:
            tokens["csrf_token"] = csrf_match.group(1)

        session_match = re.search(r'name="session_id"\s+value="([^"]+)"', html)
        if session_match:
            tokens["session_id"] = session_match.group(1)

        return tokens

    @staticmethod
    def _extract_fields_with_obfuscation(item: Any, selectors: dict[str, str]) -> dict[str, Any] | None:
        """提取字段"""
        record = {}

        for field_name, selector in selectors.items():
            if field_name == "item":
                continue

            element = item.select_one(selector)
            if element:
                value = element.get_text(strip=True)

                value = re.sub(r'\s+', ' ', value).strip()

                record[field_name] = value
            else:
                record[field_name] = ""

        if "title" not in record or not record["title"]:
            return None

        return record
