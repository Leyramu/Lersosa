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
from app.client.crawler.dto import CrawlerRunCmd
from app.core.container.annotations import Component
from app.core.container.decorators import RequiredArgsConstructor
from app.domain.crawler.ability import CrawlerDomainService
from app.domain.crawler.validator import ConfigParamValidatorExtPt
from app.infrastructure.crawler.convertor import CrawlerConvertor


@Component
@RequiredArgsConstructor
class CrawlerRunCmdExe:
    """爬虫运行命令执行器
    
    Notes：
        - 并发控制：防止重复执行
        - 流程编排：协调 DTO、Convertor、Validator、Domain Service
        - 参数转换：CrawlerRunCmd → CrawlerEntity
        - 参数校验：通过扩展点 ConfigParamValidatorExtPt
        - 事务执行：调用领域服务执行爬取
        - 状态管理：记录执行结果和错误信息
    """

    # 状态管理器（Application 层）
    state_manager: CrawlerStateManager

    # 领域服务（Domain 层）
    crawler_domain_service: CrawlerDomainService

    # 对象转换器（Infrastructure 层）
    crawler_convertor: CrawlerConvertor

    # 参数校验器（扩展点）
    config_validator: ConfigParamValidatorExtPt

    def execute(self, cmd: CrawlerRunCmd) -> dict[str, Any]:
        """执行爬虫运行命令

        Args:
            cmd: 爬虫运行命令对象

        Returns:
            包含执行结果的字典

        Raises:
            ValueError: 当配置校验失败时
        """
        can_start, error_response = self.state_manager.try_start()
        if not can_start:
            return error_response  # type: ignore

        try:
            entity = self.crawler_convertor.to_entity(cmd)

            self.config_validator.validate(entity)

            result = self.crawler_domain_service.execute(entity)

            self.state_manager.mark_finished(result)

            return {
                "status": "finished",
                "summary": result,
            }

        except Exception as exc:
            error_message = str(exc)
            self.state_manager.mark_failed(error_message)

            return {
                "status": "failed",
                "error": error_message,
            }
