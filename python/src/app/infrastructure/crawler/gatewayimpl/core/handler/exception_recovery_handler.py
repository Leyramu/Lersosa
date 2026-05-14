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
from infrastructure.crawler.gatewayimpl.core.plugins.recovery import ExceptionRecoveryStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRequestContext, CrawlerResponseData


class RecoveryHandler(CrawlerPipelineHandler):
    """异常恢复处理器
    
    Notes：
        - 将下游执行逻辑交由恢复策略管理
        - 捕获异常并执行恢复策略
        - 统一处理异常后的流程
    """

    def __init__(self, recovery_strategy: ExceptionRecoveryStrategyI, task_config: Any):
        super().__init__()
        self._recovery = recovery_strategy
        self._task_config = task_config

    def handle(self, ctx: CrawlerRequestContext, **kwargs: Any) -> CrawlerResponseData | None:
        def next_step():
            return self._handle_next(ctx, **kwargs)

        context = {
            "url": ctx.url,
            "task_name": ctx.task_name,
            "headers": ctx.headers,
        }

        try:
            result = self._recovery.execute(next_step, context)
            return result
        except Exception as exc:
            logger.error("恢复策略执行最终失败: %s", exc)
            return None
