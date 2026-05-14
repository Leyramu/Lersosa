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
from infrastructure.crawler.gatewayimpl.core.plugins.useragent import UserAgentStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRequestContext, CrawlerResponseData


class UserAgentRotationHandler(CrawlerPipelineHandler):
    """User-Agent 轮换处理器
    
    Notes：
        - 从策略中获取 User-Agent
        - 验证 UA 有效性并提供降级方案
        - 设置到请求头中继续责任链
    """

    def __init__(self, ua_strategy: UserAgentStrategyI):
        super().__init__()
        self._ua_strategy = ua_strategy

    def handle(self, ctx: CrawlerRequestContext, **kwargs: Any) -> CrawlerResponseData | None:
        # 获取 UA
        ua = self._ua_strategy.get_user_agent(ctx.url)
        
        # 验证 UA 有效性并提供降级方案
        if not ua or not isinstance(ua, str):
            logger.warning("获取到无效的 User-Agent，使用默认值")
            ua = UserAgentStrategyI.get_default_user_agents()[0]
        
        # 设置到请求头
        ctx.headers["User-Agent"] = ua
        logger.debug("使用 User-Agent: %s...", ua[:50])
        
        # 继续责任链
        return self._handle_next(ctx, **kwargs)
