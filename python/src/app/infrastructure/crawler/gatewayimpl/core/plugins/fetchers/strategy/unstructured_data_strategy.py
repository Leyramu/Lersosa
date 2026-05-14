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

import logging
import time
from typing import TYPE_CHECKING, Any

import requests

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.fetchers import FetcherStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRequestContext, CrawlerResponseData

logger = logging.getLogger(__name__)


@Component
class UnstructuredDataFetcherStrategy(FetcherStrategyI):
    """非结构化/半结构化数据抓取器

    Notes：
        - 负责下载 JSON、CSV、PDF、Excel、Image 等文件
        - 自动检测文件类型并返回原始字节流
        - 不包含解析逻辑，解析由 Parser 层负责
    """

    # 支持的文件扩展名映射
    FILE_EXTENSIONS = {
        "json": [".json"],
        "csv": [".csv"],
        "pdf": [".pdf"],
        "excel": [".xlsx", ".xls"],
        "image": [".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff"],
    }

    def __init__(self, config: Any = None):
        """初始化非结构化数据抓取器
        
        Args:
            config: 领域实体中的插件配置对象
        """

    @property
    def strategy_name(self) -> str:
        return "unstructured_data"

    def fetch(self, ctx: CrawlerRequestContext, **kwargs: Any) -> CrawlerResponseData:
        """抓取非结构化数据原始内容

        Args:
            ctx: 请求上下文
            **kwargs: 额外参数

        Returns:
            响应数据对象（payload 包含原始字节和元数据）

        Raises:
            ConnectionError: 网络错误或超时（可重试）
            RuntimeError: 其他错误
        """
        from infrastructure.crawler.gatewayimpl.core.dto import CrawlerResponseData

        start_time = time.time()
        try:
            logger.debug(f"开始下载非结构化数据: URL={ctx.url}")

            response = requests.get(
                url=ctx.url,
                headers=ctx.headers,
                timeout=ctx.timeout_seconds,
                proxies=ctx.proxies,
            )
            response.raise_for_status()
            content = response.content
            elapsed = time.time() - start_time

            # 自动检测文件类型作为元数据存储
            file_type = self._detect_file_type(ctx.url, dict(response.headers))

            logger.info(f"下载完成: 类型={file_type}, 大小={len(content)} bytes, 耗时={elapsed:.2f}s")

            return CrawlerResponseData(
                url=ctx.url,
                status_code=response.status_code,
                elapsed_seconds=elapsed,
                payload={
                    "content": content,
                    "file_type": file_type,
                    "content_type": response.headers.get("Content-Type", ""),
                    "size": len(content)
                },
            )
        except requests.exceptions.Timeout as exc:
            logger.error(f"请求超时: {exc}")
            raise
        except requests.exceptions.ConnectionError as exc:
            logger.error(f"连接错误: {exc}")
            raise
        except Exception as exc:
            logger.error(f"非结构化数据下载失败: {exc}", exc_info=True)
            raise RuntimeError(f"非结构化数据下载失败: {exc}") from exc

    @staticmethod
    def _detect_file_type(url: str, headers: dict[str, str]) -> str:
        """检测文件类型

        Args:
            url: 文件URL
            headers: 响应头

        Returns:
            文件类型（json/csv/pdf/excel/image/unknown）
        """
        content_type = headers.get("Content-Type", "").lower()

        # 基于 Content-Type 检测
        if "application/json" in content_type or "text/json" in content_type:
            return "json"
        elif "text/csv" in content_type or "application/csv" in content_type:
            return "csv"
        elif "application/pdf" in content_type:
            return "pdf"
        elif ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" in content_type or
              "application/vnd.ms-excel" in content_type):
            return "excel"
        elif "image/" in content_type:
            return "image"

        # 基于 URL 扩展名检测
        url_lower = url.lower().split("?")[0]  # 移除查询参数
        for file_type, extensions in UnstructuredDataFetcherStrategy.FILE_EXTENSIONS.items():
            if any(url_lower.endswith(ext) for ext in extensions):
                return file_type

        logger.warning(f"无法检测文件类型: URL={url}, Content-Type={content_type}")
        return "unknown"
