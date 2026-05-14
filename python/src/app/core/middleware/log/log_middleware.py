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


import time

from fastapi.logger import logger


class LogMiddleware:
    """日志中间件，记录 HTTP 请求和响应日志
    
    此类实现了 ASGI 中间件接口，用于记录所有 HTTP 请求的日志
    """

    def __init__(self, app):
        """初始化日志中间件。
        
        参数:
            app: ASGI 应用（FastAPI 或其他中间件包装后的应用）
        """
        self.app = app

    async def __call__(self, scope, receive, send):
        """实现 ASGI 中间件接口，记录 HTTP 请求日志
        
        参数:
            scope: ASGI scope
            receive: ASGI receive callable
            send: ASGI send callable
        """
        # 只处理 HTTP 请求
        if scope["type"] != "http":
            await self.app(scope, receive, send)
            return

        start_time = time.time()

        # 获取请求信息
        method = scope.get("method", "UNKNOWN")
        path = scope.get("path", "/")

        # 记录请求日志
        logger.info(f"请求： {method} {path}")

        # 捕获响应状态码
        response_started = False
        status_code = None

        async def send_wrapper(message):
            nonlocal response_started, status_code
            if message["type"] == "http.response.start":
                status_code = message.get("status", 0)
                response_started = True
            await send(message)

        # 调用下一个应用/中间件
        await self.app(scope, receive, send_wrapper)

        # 记录响应日志
        if response_started and status_code is not None:
            process_time = (time.time() - start_time) * 1000  # 转换为毫秒
            logger.info(f"响应状态：{status_code}，耗时时间：{process_time:.2f}ms")
