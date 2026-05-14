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

import csv
import json
from pathlib import Path

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.dto.crawler_record_dto import CrawlerRecord


@Component
class FileStorage:
    """文件存储器实现

    Notes：
        - 保存记录到 JSONL 和 CSV 文件
        - 保存错误快照用于调试
    """

    @staticmethod
    def save_records(records: list[CrawlerRecord], output_dir: Path) -> dict[str, Path]:
        """保存爬取记录到指定目录

        Args:
            records: 记录列表
            output_dir: 输出目录

        Returns:
            生成的文件路径字典

        Raises:
            IOError: 当文件写入失败时
        """
        # 确保输出目录存在
        output_dir.mkdir(parents=True, exist_ok=True)

        json_path = output_dir / "crawl_sample.jsonl"
        csv_path = output_dir / "crawl_sample.csv"

        try:
            # 写入 JSONL 文件
            with json_path.open("w", encoding="utf-8") as file:
                for record in records:
                    file.write(json.dumps(record.to_dict(), ensure_ascii=False) + "\n")

            # 写入 CSV 文件
            if records:
                # 获取所有字段名
                all_fieldnames = set()
                for record in records:
                    all_fieldnames.update(record.to_dict().keys())

                basic_fields = ["record_id", "source", "url", "crawl_time", "title"]
                ordered_fieldnames = []
                for field in basic_fields:
                    if field in all_fieldnames:
                        ordered_fieldnames.append(field)
                        all_fieldnames.remove(field)

                ordered_fieldnames.extend(sorted(list(all_fieldnames)))

                with csv_path.open("w", encoding="utf-8", newline="") as file:
                    writer = csv.DictWriter(file, fieldnames=ordered_fieldnames)
                    writer.writeheader()
                    for record in records:
                        writer.writerow(record.to_dict())

            return {"jsonl": json_path, "csv": csv_path}

        except Exception as exc:
            raise IOError(f"保存记录失败: {str(exc)}") from exc
