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

from app.domain.crawler.model import CrawlerEntity


class ConfigParamValidatorExtPt(ABC):
    """配置参数校验扩展点接口
    
    Notes：
    - 支持不同场景的校验逻辑（保存/修改/运行）
    - 遵循开闭原则，可扩展新的校验规则
    - 通过依赖注入选择合适的校验器
    
    Examples：
        # 定义扩展点实现
        @Component("saveConfigValidator")
        @Extension(bizId="DEFAULT", useCase="SAVE")
        class SaveConfigValidator(ConfigParamValidatorExtPt):
            def validate(self, entity):
                # 保存时的校验逻辑
                ...
        
        # 在 Command Executor 中使用
        class ConfigSaveCmdExe:
            validator: ConfigParamValidatorExtPt
            
            def execute(self, cmd):
                entity = self.convertor.to_entity(cmd)
                self.validator.validate(entity)  # 自动注入对应的校验器
                ...
    """
    
    @abstractmethod
    def validate(self, entity: CrawlerEntity) -> None:
        """校验配置参数
        
        Args:
            entity: 爬虫领域实体
            
        Raises:
            ValueError: 校验失败时抛出，包含错误信息
        """
        pass
