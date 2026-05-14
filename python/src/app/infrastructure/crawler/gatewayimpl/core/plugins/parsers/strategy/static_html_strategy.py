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
from datetime import datetime, timezone
from typing import TYPE_CHECKING, Any

from bs4 import BeautifulSoup
from fastapi.logger import logger

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.parsers import ParserStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerResponseData


@Component
class StaticHtmlStrategy(ParserStrategyI):
    """HTML 解析器

    
    Notes：
        - 传统静态 HTML 页面
        - 复杂的 DOM 结构
        - 需要 CSS 选择器或 XPath
        - 支持灵活的字段映射配置
        - 自动处理缺失字段
        - 批量提取列表数据
    """

    def __init__(self, config: Any = None):
        pass

    @property
    def strategy_name(self) -> str:
        return "static_html"

    def parse(self, response: "CrawlerResponseData", **kwargs: Any) -> list[dict[str, Any]]:
        """解析 HTML 响应
        
        Args:
            response: 响应数据对象
            **kwargs: 额外参数
                - selectors: CSS 选择器映射字典
                    - item: 列表项选择器（如 'article.product_pod'）
                    - title: 标题选择器（如 'h3 a'）
                    - price: 价格选择器（如 'p.price_color'）
                    - 其他自定义字段...
                - source: 数据源标识
                
        Returns:
            提取的结构化数据列表
            
        Raises:
            ValueError: 当选择器配置无效时
        """
        if not response.text:
            logger.warning("HTML 内容为空，无法解析")
            return []

        selectors = kwargs.get("selectors", {})
        source = kwargs.get("source", "unknown")

        item_selector = selectors.get("item", "div.item")
        
        logger.debug(f"开始解析: URL={response.url}, item_selector={item_selector}")
        logger.debug(f"选择器配置: {list(selectors.keys())}")
        
        soup = BeautifulSoup(response.text, "html.parser")
        items = soup.select(item_selector)
        
        logger.debug(f"找到 {len(items)} 个匹配项 (selector: {item_selector})")

        records = []
        for item in items:
            record = self._extract_fields(item, selectors, response.url, source)
            if record:
                records.append(record)

        logger.info(f"解析完成: URL={response.url}, 找到 {len(items)} 个项, 成功提取 {len(records)} 条记录")
        return records

    @staticmethod
    def _extract_fields(
        item: Any,
        selectors: dict[str, str],
        url: str,
        source: str,
    ) -> dict[str, Any] | None:
        """从单个元素中提取字段
        
        Args:
            item: BeautifulSoup 元素
            selectors: 字段选择器映射
            url: 当前URL
            source: 数据源
            
        Returns:
            提取的字段字典，如果关键字段缺失则返回 None
        """
        record = {
            "url": url,
            "crawl_time": datetime.now(timezone.utc).isoformat(),
        }

        # 提取每个配置的字段
        for field_name, selector in selectors.items():
            if field_name == "item":
                continue

            element = item.select_one(selector)
            if element:
                # 优先使用 title 属性，否则使用文本
                value = element.get("title") or element.get_text(strip=True)
                record[field_name] = value
            else:
                record[field_name] = ""

        # 检查是否有标题字段
        title_value = record.get("title", "")
        
        if not title_value:
            return None

        # 生成记录 ID
        raw_id = f"{source}|{url}|{str(title_value)[:50]}".encode("utf-8")
        record["record_id"] = hashlib.md5(raw_id).hexdigest()  # noqa: S324
        record["source"] = source

        return record
