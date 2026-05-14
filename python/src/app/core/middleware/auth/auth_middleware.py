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


from fastapi import Request, HTTPException
from starlette.middleware.base import BaseHTTPMiddleware
from starlette.responses import Response

from app.core.redis import RedisClient


class AuthMiddleware(BaseHTTPMiddleware):
    SATOKEN = "Authorization"
    SATOKEN_PREFIX = "Bearer "
    SATOKEN_KEY_PREFIX = "global:Authorization:login:token"

    def __init__(self, app):
        super().__init__(app)
        self._redisClient = RedisClient()

    async def dispatch(self, request: Request, call_next) -> Response:
        # 白名单路径（无需鉴权）
        if request.url.path in ["", "/", "/v3/api-docs", "/openapi.json"]:
            return await call_next(request)

        # 提取 Token：优先从 header 获取
        authorization = request.headers.get(self.SATOKEN)
        token = None

        if authorization:
            if authorization.startswith(self.SATOKEN_PREFIX):
                token = authorization[len(self.SATOKEN_PREFIX):]
            else:
                raise HTTPException(status_code=401, detail="无效的 Token 格式")
        else:
            # 尝试从查询参数中获取 token
            authorization = request.query_params.get(self.SATOKEN)
            if authorization:
                if authorization.startswith(self.SATOKEN_PREFIX):
                    token = authorization[len(self.SATOKEN_PREFIX):]
                else:
                    raise HTTPException(status_code=401, detail="无效的 Token 格式")

        if not token:
            raise HTTPException(status_code=401, detail="缺少授权 Token")

        # 验证 Token 是否存在 Redis 中
        user_login_id = await self._redisClient.get(f"{self.SATOKEN_KEY_PREFIX}:{token}")
        if not user_login_id:
            raise HTTPException(status_code=401, detail="未登录或 Token 已失效")

        # 将 login_id 存入 request.state，供后续接口使用
        request.state.login_id = user_login_id

        # 继续执行后续中间件或路由函数
        response = await call_next(request)
        return response
