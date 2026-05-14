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

from app.application.crawler.state import CrawlerStateManager
from app.client.crawler.dto import CrawlerStatusQry
from app.client.crawler.dto.clientobject import CrawlerStatusCO
from app.core.container.annotations import Component
from app.core.container.decorators import RequiredArgsConstructor


@Component
@RequiredArgsConstructor
class CrawlerStatusQryExe:
    """爬虫状态查询执行器

    Notes：
        - 从状态管理器获取当前状态
        - 将内部状态转换为 Client Object
        - 不包含状态管理
    """

    # 状态管理器
    state_manager: CrawlerStateManager

    def execute(self, _qry: CrawlerStatusQry | None) -> dict[str, Any]:
        """执行状态查询
        
        Args:
            _qry: 状态查询对象（可选）
            
        Returns:
            包含状态信息的字典
        """
        # 从状态管理器获取状态
        status_data = self.state_manager.get_status()

        # 构建状态 Client Object
        status_co = CrawlerStatusCO(
            is_running=status_data["is_running"],
            last_started_at=status_data["last_started_at"],
            last_finished_at=status_data["last_finished_at"],
            last_error=status_data["last_error"],
            last_summary=status_data["last_summary"],
            total_records=status_data["total_records"],
        )

        return status_co.model_dump()
