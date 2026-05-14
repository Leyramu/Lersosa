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


from datetime import datetime, timezone
from typing import Optional

from pydantic import BaseModel, Field


class CrawlerRequestContext(BaseModel):
    """爬虫请求上下文
    
    用于在爬虫处理器管线中传递请求信息
    
    Attributes:
        task_name: 任务名称
        url: 请求 URL
        headers: 请求头字典
        proxies: 代理配置（可选）
        timeout_seconds: 超时时间（秒）
        started_at: 请求开始时间
    """
    
    task_name: str = Field(..., description="任务名称")
    url: str = Field(..., description="请求 URL")
    headers: dict[str, str] = Field(default_factory=dict, description="请求头字典")
    proxies: Optional[dict[str, str]] = Field(default=None, description="代理配置")
    timeout_seconds: int = Field(default=15, description="超时时间（秒）")
    started_at: datetime = Field(
        default_factory=lambda: datetime.now(timezone.utc),
        description="请求开始时间"
    )
