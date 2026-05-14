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


from typing import Any

from pydantic import BaseModel, Field


class CrawlerRecord(BaseModel):
    """爬虫记录 DTO

    Notes：
        - 作为解析器和存储器之间的数据传输载体
        - 支持动态字段
        - 提供序列化/反序列化能力
    """

    # 固定元数据字段
    record_id: str = Field(description="记录ID")
    source: str = Field(description="数据源")
    url: str = Field(description="URL")
    crawl_time: str = Field(description="爬取时间")

    fields: dict[str, Any] = Field(default_factory=dict, description="动态业务字段")

    def get(self, key: str, default: Any = None) -> Any:
        """获取字段值

        Args:
            key: 字段名
            default: 默认值

        Returns:
            字段值或默认值
        """
        return self.fields.get(key, default)

    def set_field(self, key: str, value: Any) -> None:
        """设置字段值

        Args:
            key: 字段名
            value: 字段值
        """
        self.fields[key] = value

    @property
    def title(self) -> str:
        """便捷访问标题字段"""
        return self.fields.get("title", "")

    def to_dict(self) -> dict[str, Any]:
        """转换为字典，用于序列化

        Returns:
            包含所有字段的字典
        """
        result = {
            "record_id": self.record_id,
            "source": self.source,
            "url": self.url,
            "crawl_time": self.crawl_time,
        }
        # 合并业务字段
        result.update(self.fields)
        return result

    @classmethod
    def create_from_selectors(
        cls,
        record_id: str,
        source: str,
        url: str,
        crawl_time: str,
        extracted_data: dict[str, Any],
    ) -> "CrawlerRecord":
        """从解析器提取的数据创建记录

        Args:
            record_id: 记录 ID
            source: 数据源
            url: URL
            crawl_time: 爬取时间
            extracted_data: 解析器根据 selectors 提取的字段数据

        Returns:
            CrawlerRecord 实例
        """
        return cls(
            record_id=record_id,
            source=source,
            url=url,
            crawl_time=crawl_time,
            fields=extracted_data,
        )
