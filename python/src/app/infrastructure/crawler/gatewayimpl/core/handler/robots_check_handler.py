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
from typing import TYPE_CHECKING, Any

from fastapi.logger import logger

from infrastructure.crawler.gatewayimpl.core.chain import CrawlerPipelineHandler
from infrastructure.crawler.gatewayimpl.core.plugins.robots import RobotsProtocolStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRequestContext, CrawlerResponseData


class RobotsCheckHandler(CrawlerPipelineHandler):
    """Robots 协议检查处理器
    
    Notes：
        - 检查目标URL是否允许爬取
        - 执行 Robots 协议要求的延迟
    """

    def __init__(self, robots_strategy: RobotsProtocolStrategyI):
        super().__init__()
        self._robots_strategy = robots_strategy

    def handle(self, ctx: CrawlerRequestContext, **kwargs: Any) -> CrawlerResponseData | None:
        # 检查是否可以爬取
        user_agent = ctx.headers.get("User-Agent", "*")
        can_fetch = self._robots_strategy.can_fetch(ctx.url, user_agent)
        
        logger.debug(
            "[Robots检查] url=%s ua=%s allowed=%s",
            ctx.url,
            user_agent[:50] if len(user_agent) > 50 else user_agent,
            can_fetch
        )
        
        if not can_fetch:
            logger.warning("根据 Robots 协议禁止爬取: %s", ctx.url)
            return None  # 拦截请求

        # 获取并执行延迟
        delay = self._robots_strategy.get_crawl_delay()
        if delay > 0:
            logger.info("执行 Robots 协议要求的延迟: %.2fs", delay)
            time.sleep(delay)
        else:
            logger.debug("Robots 协议无延迟要求")

        return self._handle_next(ctx, **kwargs)
