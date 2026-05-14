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

import asyncio

from starlette.responses import JSONResponse

from app.client.crawler.api import CrawlerServiceI
from app.client.crawler.dto import CrawlerStatusQry, CrawlerRunCmd
from app.common.domain.enum import CodeStatus
from app.core.container.annotations import RestController
from app.core.container.decorators import GetMapping, PostMapping, RequiredArgsConstructor


# 数据采集控制器
@RestController(
    prefix="/crawler",
    tags=["crawler"],
    responses={CodeStatus.NOT_FOUND: {"description": "Not found"}},
)
@RequiredArgsConstructor
class CrawlerController:
    """爬虫控制器
        提供数据采集与挖掘操作的HTTP接口
    """

    # 数据采集服务接口
    crawler_service: CrawlerServiceI

    @PostMapping("/run")
    async def run(self, cmd: CrawlerRunCmd) -> JSONResponse:
        """运行数据爬取器

        Args:
            cmd: 数据参数命令对象

        Returns:
            JSON响应，包含执行结果
        """
        result = await asyncio.to_thread(
            self.crawler_service.run,
            cmd
        )
        return JSONResponse(status_code=200, content=result)

    @GetMapping("/status")
    async def status(self, qry: CrawlerStatusQry) -> JSONResponse:
        """返回最新的爬虫执行状态

        Args:
            qry: 爬虫执行状态查询

        Returns:
            JSON响应，包含状态信息
        """
        return JSONResponse(status_code=200, content=self.crawler_service.status(qry))
