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


from fastapi import FastAPI, Request, HTTPException
from starlette.responses import JSONResponse

from app.common.domain import RepoResult
from app.common.domain.enum import CodeStatus


# 异常处理类
class ExceptionHandlers:
    """FastAPI 全局异常处理器管理类
        提供统一异常处理机制，封装HTTP异常和全局异常的自定义处理逻辑
    """

    # 初始化
    def __init__(self, app: FastAPI) -> None:
        """初始化异常处理器
        Args:
            app: FastAPI应用实例，用于注册异常处理器
        """
        self.app = app

    # 添加异常处理器
    def add_exception_handlers(self) -> None:
        """注册所有异常处理器到FastAPP应用
        Notes：
            - 处理HTTPException的标准异常
            - 处理其他所有未捕获异常的全局处理器
        """

        # 自定义异常处理器
        @self.app.exception_handler(HTTPException)
        async def http_exception_handler(_request: Request, exc: HTTPException) -> JSONResponse:
            """HTTP异常处理函数
            Args:
                _request: 请求对象（使用下划线表示未使用）
                exc: 捕获的HTTP异常实例
            Returns:
                JSONResponse: 标准化错误响应，包含状态码和错误信息
            """
            return RepoResult.error(code=exc.status_code, msg=exc.detail or "未知错误")

        # 全局异常处理器
        @self.app.exception_handler(Exception)
        async def global_exception_handler(_request: Request, exc: Exception) -> JSONResponse:
            """全局异常捕获函数
            Args:
                _request: 请求对象（使用下划线表示未使用）
                exc: 捕获的未处理异常实例
            Returns:
                JSONResponse: 标准化错误响应，包含500状态码和异常信息
            """
            return RepoResult.error(code=CodeStatus.INTERNAL_SERVER_ERROR.value, msg=str(exc))
