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


from abc import ABC, abstractmethod
from typing import Any

from app.client.crawler.dto import CrawlerStatusQry, CrawlerRunCmd


# 爬虫服务接口定义
class CrawlerServiceI(ABC):
    """爬虫服务接口

    定义与数据爬取相关的操作，包括执行爬取任务和查询状态
    
    Notes：
        - CQRS: 命令（写）和查询（读）分离
        - 单一职责: 只定义爬虫相关的操作
        - 开闭原则: 可扩展新的方法而不修改现有接口
    """

    @abstractmethod
    def run(self, cmd: CrawlerRunCmd) -> dict[str, Any]:
        """执行爬虫任务
        
        Args:
            cmd: 爬虫运行命令对象
            
        Returns:
            dict[str, Any]: 包含执行结果的字典，可能的返回结构：
                - status: "finished" | "failed" | "running"
                - summary: 爬取结果摘要（成功时）
                - error: 错误信息（失败时）
                - message: 提示信息
                - last_summary: 上次执行的摘要
                
        Raises:
            RuntimeError: 如果爬虫未正确绑定到FastAPI应用
            ValueError: 如果配置校验失败
        """
        pass

    @abstractmethod
    def status(self, qry: CrawlerStatusQry) -> dict[str, Any]:
        """获取爬虫执行状态
        
        Returns:
            dict[str, Any]: 包含状态信息的字典，包括：
                - is_running: 是否正在运行
                - last_started_at: 上次开始时间
                - last_finished_at: 上次结束时间
                - last_error: 上次错误信息
                - last_summary: 上次执行摘要
                - total_records: 总记录数
        """
        pass
