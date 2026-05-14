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


from typing import Optional

from pydantic import BaseModel, Field


class CrawlerStatusCO(BaseModel):
    """爬虫状态客户端对象
    
    Notes：
        - 用于前端展示爬虫执行状态
        - 包含运行状态、时间戳、错误信息等
        - 扁平化设计，易于序列化
    """
    
    is_running: bool = Field(default=False, description="是否正在运行")
    last_started_at: Optional[str] = Field(None, description="上次开始时间（ISO格式）")
    last_finished_at: Optional[str] = Field(None, description="上次结束时间（ISO格式）")
    last_error: Optional[str] = Field(None, description="上次错误信息")
    last_summary: Optional[dict] = Field(None, description="上次执行摘要")
    total_records: int = Field(default=0, description="总记录数")
