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


from app.client.crawler.dto import CrawlerRunCmd
from app.core.container.annotations import Component
from app.domain.crawler.model import CrawlerEntity


@Component
class CrawlerConvertor:
    """爬虫对象转换器
    
    Notes：
    - Client DTO → Domain Entity 转换
    - Domain Entity → Client Object 转换
    - Domain Entity → Data Object 转换
    - Data Object → Client Object 转换
    """

    @staticmethod
    def to_entity(cmd: CrawlerRunCmd) -> CrawlerEntity:
        """Client DTO → Domain Entity

            将应用层的命令对象转换为领域层的实体对象
        """
        return CrawlerEntity.model_validate(cmd.model_dump())

    @staticmethod
    def to_client_object(entity: CrawlerEntity) -> dict:
        """Domain Entity → Client Object
        
        将领域实体转换为用于前端展示的字典对象
        
        Args:
            entity: Domain 层的爬虫领域实体
            
        Returns:
            用于前端展示的字典对象
        """
        return {
            "project": entity.project,
            "description": entity.description,
            "task_count": len(entity.tasks),
            "enabled_task_count": len(entity.get_enabled_tasks()),
            "tasks": [
                {
                    "name": task.name,
                    "enabled": task.enabled,
                    "source": task.source,
                    "fetch_mode": task.fetch.mode,
                    "url_template": task.fetch.url_template,
                    "page_range": {
                        "start": task.fetch.pages.start,
                        "end": task.fetch.pages.end
                    },
                    "parser": task.parse.parser,
                    "selector_count": len(task.parse.selectors)
                }
                for task in entity.tasks
            ]
        }

    @staticmethod
    def to_client_objects(entities: list[CrawlerEntity]) -> list[dict]:
        """批量转换 Entity → CO
        
        Args:
            entities: 领域实体列表
            
        Returns:
            客户端对象列表
        """
        return [CrawlerConvertor.to_client_object(e) for e in entities]
