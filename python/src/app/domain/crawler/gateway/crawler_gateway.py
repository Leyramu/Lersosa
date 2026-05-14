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

from app.domain.crawler.model import CrawlerEntity


class CrawlerGateway(ABC):
    """爬虫网关接口
    
    定义爬虫执行的核心能力契约
    
    Notes：
        - 依赖倒置：Domain 层依赖此接口，而非具体实现
        - 单一职责：只定义爬虫执行的核心行为
        - 开闭原则：可以扩展新的网关实现
    """

    @abstractmethod
    def execute(self, entity: CrawlerEntity) -> dict[str, Any]:
        """执行爬虫任务
        
        Args:
            entity: 爬虫领域实体对象
            
        Returns:
            执行结果摘要
            
        Raises:
            RuntimeError: 当执行失败时
        """
        pass
