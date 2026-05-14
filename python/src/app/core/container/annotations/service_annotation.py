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


from typing import Callable

from fastapi.logger import logger

from app.common.utils import NameUtils
from .annotation_registry import AnnotationRegistry
from .component_metadata import ComponentMetadata


class ServiceAnnotation:
    """@Service注解 - 标记一个类为服务层组件

    这是@Component的特化，专门用于服务层
    """

    @staticmethod
    def create(name: str = "", singleton: bool = True, lazy_init: bool = False) -> Callable[[type], type]:
        """创建@Service装饰器

        Args:
            name: Bean名称，默认为类名的小驼峰形式
            singleton: 是否为单例，默认为True
            lazy_init: 是否延迟初始化，默认为False

        Returns:
            装饰器函数

        Notes:
            @ServiceAnnotation.create()
            class UserService:
                pass

            @ServiceAnnotation.create(name="customUserService")
            class UserService:
                pass
        """

        def decorator(cls: type) -> type:
            bean_name = name or NameUtils.to_camel_case(cls.__name__)
            metadata = ComponentMetadata(
                name=bean_name,
                singleton=singleton,
                lazy_init=lazy_init,
            )
            AnnotationRegistry.register(cls, metadata)
            logger.info("@Service: 注册服务 '%s' -> '%s'", cls.__name__, bean_name)
            return cls

        return decorator
