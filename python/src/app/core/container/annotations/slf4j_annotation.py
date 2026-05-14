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


from logging import getLogger
from typing import Callable

from fastapi.logger import logger


class Slf4jAnnotation:
    """@Slf4j注解 - 为类自动注入日志对象。"""

    @staticmethod
    def create(name: str = "", field_name: str = "log") -> Callable[[type], type]:
        """创建@Slf4j装饰器。

        Args:
            name: 日志器名称，默认使用"模块名.类名"
            field_name: 注入到类上的日志字段名，默认"log"

        Returns:
            装饰器函数
        """

        def decorator(cls: type) -> type:
            logger_name = name or f"{cls.__module__}.{cls.__name__}"

            if hasattr(cls, field_name):
                logger.warning(
                    "@Slf4j: 类 '%s' 已存在字段 '%s'，跳过日志器注入",
                    cls.__name__,
                    field_name,
                )
                return cls

            setattr(cls, field_name, getLogger(logger_name))
            logger.info(
                "@Slf4j: 为类 '%s' 注入日志器 '%s' 到字段 '%s'",
                cls.__name__,
                logger_name,
                field_name,
            )
            return cls

        return decorator

