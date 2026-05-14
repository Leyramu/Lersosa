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

from abc import ABC, abstractmethod
from typing import TYPE_CHECKING, Any

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerResponseData

from app.core.container.core import BeanContainerManager


class ParserStrategyI(ABC):
    """数据解析策略接口

    Notes：
        - 策略模式：不同场景使用不同的解析实现
        - 开闭原则：可以扩展新的解析策略
        - 依赖倒置：上层依赖此接口而非具体实现
    """

    @property
    @abstractmethod
    def strategy_name(self) -> str:
        """策略名称标识

        Returns:
            策略的唯一标识符
        """
        pass

    @abstractmethod
    def parse(self, response: "CrawlerResponseData", **kwargs: Any) -> list[dict[str, Any]]:
        """执行数据解析
        
        Args:
            response: 响应数据对象
            **kwargs: 额外参数
            
        Returns:
            提取的结构化数据列表
            
        Raises:
            ValueError: 当解析失败时
        """
        pass

    @staticmethod
    def get_strategy(strategy_name: str, config: Any) -> "ParserStrategyI":
        """根据策略名称获取对应的策略实例
        
        Args:
            strategy_name: 策略名称
            config: 领域实体中的插件配置对象

        Returns:
            对应的策略实例

        Raises:
            ValueError: 当类型不支持或获取 Bean 失败时
        """
        bean_container = BeanContainerManager.get_instance()
        all_strategies = bean_container.get_beans_by_type(ParserStrategyI)

        strategy_map = {s.strategy_name: type(s) for s in all_strategies}
        target_cls = strategy_map.get(strategy_name)

        if not target_cls:
            raise ValueError(f"策略 '{strategy_name}' 未注册")

        return target_cls(config=config)
