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
from typing import TYPE_CHECKING, Any

import requests

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.fetchers import FetcherStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRequestContext, CrawlerResponseData

logger = logging.getLogger(__name__)


@Component
class StaticPageStrategy(FetcherStrategyI):
    """静态页面抓取器

    Notes：
        - 自动重试机制
        - 超时控制
        - 完全独立运行，无外部依赖
    """

    def __init__(self, config: Any = None):
        """初始化静态页面抓取器

        Args:
            config: 领域实体中的插件配置对象
        """
        self._session = requests.Session()

    @property
    def strategy_name(self) -> str:
        return "static_page"

    def fetch(self, ctx: CrawlerRequestContext, **kwargs: Any) -> "CrawlerResponseData":
        """抓取静态页面

        Args:
            ctx: 请求上下文
            **kwargs: 额外参数
                - method: HTTP 方法（默认 GET）
                - data: 请求体数据
                - mode: 响应模式（html/json，默认 html）

        Returns:
            响应数据对象

        Raises:
            RuntimeError: 当请求失败时
        """
        from infrastructure.crawler.gatewayimpl.core.dto import CrawlerResponseData

        method = kwargs.get("method", "GET").upper()
        data = kwargs.get("data")
        mode = kwargs.get("mode", "html")

        try:
            response = self._do_request(ctx, method, data)
            response.raise_for_status()

            if mode == "json":
                content = response.content
                return CrawlerResponseData(
                    url=ctx.url,
                    status_code=response.status_code,
                    elapsed_seconds=response.elapsed.total_seconds(),
                    payload={
                        "content": content,
                        "file_type": "json",
                        "content_type": response.headers.get("Content-Type", "application/json"),
                        "size": len(content)
                    },
                )
            else:
                return CrawlerResponseData(
                    url=ctx.url,
                    status_code=response.status_code,
                    elapsed_seconds=response.elapsed.total_seconds(),
                    text=response.text,
                )
        except Exception as exc:
            raise RuntimeError(f"静态页面抓取失败: {exc}") from exc

    def _do_request(
        self,
        ctx: CrawlerRequestContext,
        method: str,
        data: Any | None,
    ) -> requests.Response:
        """执行 HTTP 请求

        Args:
            ctx: 请求上下文
            method: HTTP 方法
            data: 请求体数据

        Returns:
            HTTP 响应对象
        """
        if method == "GET":
            return self._session.get(
                url=ctx.url,
                headers=ctx.headers,
                timeout=ctx.timeout_seconds,
                proxies=ctx.proxies,
            )
        else:
            return self._session.post(
                url=ctx.url,
                headers=ctx.headers,
                timeout=ctx.timeout_seconds,
                proxies=ctx.proxies,
                data=data,
            )
