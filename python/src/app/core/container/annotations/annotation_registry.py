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


from fastapi.logger import logger

from .component_metadata import ComponentMetadata


class AnnotationRegistry:
    """注解注册表，管理所有带注解的类"""

    _components: dict[type, ComponentMetadata] = {}

    @classmethod
    def register(cls, clazz: type, metadata: ComponentMetadata) -> None:
        """注册带注解的类

        Args:
            clazz: 要注册的类
            metadata: 组件元数据
        """
        # 防止重复注册
        if clazz in cls._components:
            logger.debug(
                "组件 '%s' 已注册，跳过",
                clazz.__name__,
            )
            return
        
        cls._components[clazz] = metadata
        logger.debug("注册组件: %s -> %s", clazz.__name__, metadata.name or clazz.__name__)

    @classmethod
    def get_metadata(cls, clazz: type) -> ComponentMetadata | None:
        """获取类的组件元数据

        Args:
            clazz: 要查询的类

        Returns:
            组件元数据，如果未注册则返回None
        """
        return cls._components.get(clazz)

    @classmethod
    def get_all_components(cls) -> dict[type, ComponentMetadata]:
        """获取所有已注册的组件

        Returns:
            组件字典 {类: 元数据}
        """
        return cls._components.copy()

    @classmethod
    def clear(cls) -> None:
        """清空所有注册的组件"""
        cls._components.clear()
