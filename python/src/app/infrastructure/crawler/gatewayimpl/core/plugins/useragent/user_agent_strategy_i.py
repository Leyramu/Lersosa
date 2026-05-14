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
from typing import Any

from app.core.container.core import BeanContainerManager


class UserAgentStrategyI(ABC):
    """User-Agent 管理策略接口

    Notes：
        - 策略模式：不同场景使用不同的 UA 管理实现
        - 开闭原则：可以扩展新的 UA 管理策略
        - 依赖倒置：上层依赖此接口而非具体实现
    """

    @classmethod
    def get_default_user_agents(cls) -> list[str]:
        """获取默认 User-Agent 列表
        
        Returns:
            默认的 User-Agent 字符串列表
        """
        return [
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/120.0.0.0 Safari/537.36",
        ]

    @property
    @abstractmethod
    def strategy_name(self) -> str:
        """策略名称标识

        Returns:
            策略的唯一标识符
        """
        pass

    @abstractmethod
    def get_user_agent(self, url: str = "") -> str:
        """获取 User-Agent

        Args:
            url: 目标URL

        Returns:
            User-Agent 字符串
        """
        pass

    @abstractmethod
    def rotate(self) -> None:
        """轮换到下一个 User-Agent"""
        pass

    @abstractmethod
    def add_user_agent(self, ua: str) -> None:
        """添加 User-Agent 到池中

        Args:
            ua: User-Agent 字符串
        """
        pass

    @staticmethod
    def get_strategy(strategy_name: str, config: Any = None) -> "UserAgentStrategyI":
        """根据策略名称获取对应的策略实例
        
        Args:
            strategy_name: 策略名称
            config: 领域实体中的插件配置对象
            
        Returns:
            对应的策略实例
        """
        bean_container = BeanContainerManager.get_instance()
        all_strategies = bean_container.get_beans_by_type(UserAgentStrategyI)

        strategy_map = {s.strategy_name: type(s) for s in all_strategies}
        target_cls = strategy_map.get(strategy_name)
        
        if not target_cls:
            raise ValueError(f"策略 '{strategy_name}' 未注册")
            
        return target_cls(config=config)
