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

from typing import TYPE_CHECKING, Any

from fastapi.logger import logger

from infrastructure.crawler.gatewayimpl.core.chain import CrawlerPipelineHandler
from infrastructure.crawler.gatewayimpl.core.plugins.fetchers import FetcherStrategyI
from infrastructure.crawler.gatewayimpl.core.plugins.parsers import ParserStrategyI
from infrastructure.crawler.gatewayimpl.core.plugins.rate import RateControlStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRequestContext, CrawlerResponseData


class CoreExecutionHandler(CrawlerPipelineHandler):
    """核心执行处理器
    
    Notes：
        - 速率控制等待
        - 执行抓取
        - 解析数据
        - 记录响应并更新速率策略
    """

    def __init__(
        self,
        fetcher: FetcherStrategyI,
        parser: ParserStrategyI,
        rate_control: RateControlStrategyI,
        task_config: Any,
    ):
        super().__init__()
        self._fetcher = fetcher
        self._parser = parser
        self._rate_control = rate_control
        self._task_config = task_config

    def handle(self, ctx: CrawlerRequestContext, **kwargs: Any) -> CrawlerResponseData | None:
        # 速率控制等待
        self._rate_control.wait_before_request(ctx.url)

        # 执行抓取
        extra_params = {}
        if self._fetcher.strategy_name in ["dynamic_render", "anti_scraping"]:
            extra_params = {
                "wait_selector": self._task_config.fetch.wait_selector,
                "wait_timeout": self._task_config.fetch.wait_timeout,
                "browser_options": self._task_config.fetch.browser_options,
                "use_proxy": self._fetcher.strategy_name == "anti_scraping",
                "random_delay": (0.5, 2.0) if self._fetcher.strategy_name == "anti_scraping" else None,
            }
        else:
            extra_params["mode"] = self._task_config.fetch.mode

        response = self._fetcher.fetch(ctx=ctx, **{k: v for k, v in extra_params.items() if v is not None})
        
        if not response:
            raise Exception("Fetcher 返回空响应")

        # 记录响应并更新速率策略
        self._rate_control.record_response(ctx.url, success=True, latency=response.elapsed_seconds)

        # 解析数据
        try:
            parse_kwargs = {
                "response": response,
                "selectors": self._task_config.parse.selectors,
                "source": self._task_config.source,
            }
            if self._parser.strategy_name == "unstructured_data":
                parse_kwargs["data_type"] = "json"
                parse_kwargs.pop("selectors", None)
                parse_kwargs.pop("source", None)

            records_data = self._parser.parse(**parse_kwargs)
            response.parsed_records = records_data
            
        except Exception as exc:
            logger.warning("解析失败: %s", exc)
            response.parsed_records = []

        # 继续传递给下一个处理器
        next_result = self._handle_next(ctx, elapsed=response.elapsed_seconds)
        return next_result if next_result is not None else response
