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

from typing import Any

from app.application.crawler.command import CrawlerRunCmdExe
from app.application.crawler.command.query import CrawlerStatusQryExe
from app.client.crawler.api import CrawlerServiceI
from app.client.crawler.dto import CrawlerRunCmd, CrawlerStatusQry
from app.core.container.annotations import Service
from app.core.container.decorators import RequiredArgsConstructor


@Service
@RequiredArgsConstructor
class CrawlerServiceImpl(CrawlerServiceI):
    """爬虫服务实现类
    
    Notes：
    - 纯粹的流程编排
    - 委托给 Command/Query Executor 执行
    - 不包含任何业务逻辑和状态管理
    """

    # 爬虫运行命令执行器
    crawler_run_cmd_exe: CrawlerRunCmdExe
    # 爬虫状态查询执行器
    crawler_status_qry_exe: CrawlerStatusQryExe

    def run(self, cmd: CrawlerRunCmd) -> dict[str, Any]:
        """运行爬虫"""
        return self.crawler_run_cmd_exe.execute(cmd)

    def status(self, qry: CrawlerStatusQry) -> dict[str, Any]:
        """返回爬虫执行状态"""
        return self.crawler_status_qry_exe.execute(qry)
