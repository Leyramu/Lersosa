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


import logging
import time
from functools import wraps
from typing import Callable, Optional


class CircuitBreaker:
    """
    熔断器核心类，提供熔断、恢复、降级能力
    """

    # 默认配置
    DEFAULT_FAILURE_THRESHOLD = 5
    DEFAULT_RECOVERY_TIMEOUT = 10  # 单位：秒
    DEFAULT_FAIL_ON_ERRORS = (Exception,)

    def __init__(
            self,
            failure_threshold: int = DEFAULT_FAILURE_THRESHOLD,
            recovery_timeout: int = DEFAULT_RECOVERY_TIMEOUT,
            fail_on_errors: tuple = DEFAULT_FAIL_ON_ERRORS,
            fallback: Optional[Callable] = None
    ):
        """
        初始化熔断器
        :param failure_threshold: 失败阈值（连续失败多少次后熔断）
        :param recovery_timeout: 熔断恢复等待时间（单位：秒）
        :param fail_on_errors: 触发熔断的异常类型列表
        :param fallback: 降级函数
        """
        self.failure_threshold = failure_threshold
        self.recovery_timeout = recovery_timeout
        self.fail_on_errors = fail_on_errors
        self.fallback = fallback

        self._failure_count = 0
        self._last_failure_time = 0
        self._is_open = False

    def __call__(self, func: Callable) -> Callable:
        """
        作为装饰器调用
        :param func: 被装饰的方法
        :return: 包装后的方法
        """

        @wraps(func)
        def wrapper(instance, *args, **kwargs):
            cb_key = f"{func.__module__}.{func.__qualname__}"

            if not hasattr(instance, '_circuit_breakers'):
                instance._circuit_breakers = {}  # noqa

            if cb_key not in instance._circuit_breakers:
                instance._circuit_breakers[cb_key] = self.__class__(
                    failure_threshold=self.failure_threshold,
                    recovery_timeout=self.recovery_timeout,
                    fail_on_errors=self.fail_on_errors,
                    fallback=self.fallback
                )

            cb = instance._circuit_breakers[cb_key]

            try:
                result = func(instance, *args, **kwargs)
                cb.success()
                return result
            except Exception as e:
                if isinstance(e, cb.fail_on_errors):
                    cb.failure()
                    if cb.fallback:
                        logging.warning(f"[CircuitBreaker] 触发降级处理: {func.__name__}")
                        return cb.fallback(instance, *args, **kwargs)
                    else:
                        raise
                else:
                    raise

        return wrapper

    def reset(self):
        """重置熔断状态"""
        self._failure_count = 0
        self._is_open = False
        self._last_failure_time = 0

    def success(self):
        """标记一次成功"""
        self._last_failure_time = 0

    def failure(self):
        """标记一次失败"""
        self._failure_count += 1
        self._last_failure_time = time.time()

        if self._failure_count >= self.failure_threshold:
            self._is_open = True
            logging.error(f"[CircuitBreaker] 熔断触发，暂停服务: {self._get_caller_name()}")

    def _get_caller_name(self):
        """获取被装饰方法名"""
        return self.fallback.__name__ if self.fallback else "Unknown"
