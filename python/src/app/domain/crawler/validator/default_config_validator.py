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


from app.core.container.annotations import Component
from app.domain.crawler.model import CrawlerEntity
from app.domain.crawler.validator import ConfigParamValidatorExtPt


@Component
class DefaultConfigValidator(ConfigParamValidatorExtPt):
    """默认配置参数校验器

    Notes：
        - 提供通用的爬虫配置校验逻辑
        - 委托给领域实体的 validate_all_tasks() 方法
        - 可被其他场景特定的校验器替换
        - 爬虫运行时的配置校验
        - 配置保存时的基础校验
        - 作为其他校验器的基类
    """

    def validate(self, entity: CrawlerEntity) -> None:
        """校验配置参数
        
        Notes：
            - 至少有一个启用的任务
            - 每个任务必须有名称和数据源
            - URL 模板不能为空
            - 起始页码不能大于结束页码
            - 解析器类型必须有效
            - 选择器映射不能为空
        
        Args:
            entity: 爬虫领域实体
            
        Raises:
            ValueError: 校验失败时抛出，包含详细错误信息
        """
        errors = entity.validate_all_tasks()

        if errors:
            error_message = f"爬虫配置校验失败:\n" + "\n".join(f"  - {err}" for err in errors)
            raise ValueError(error_message)
