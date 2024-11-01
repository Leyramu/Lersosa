#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.controller import controllers
from app.core import NacosLifecycle
from app.exception import ExceptionHandlers


# 创建FastAPI应用实例，继承自FastAPI
class App(FastAPI):
    def __init__(self):
        super().__init__(
            lifespan=NacosLifecycle(self).lifespan
        )
        self._register_routes()
        self._add_middlewares()
        self._add_exception_handlers()
        self._add_root_route()

    # 注册路由
    def _register_routes(self):
        self.include_router(controllers)

    # 添加中间件
    def _add_middlewares(self):
        self.add_middleware(
            CORSMiddleware,  # type: ignore
            allow_origins=["*"],
            allow_credentials=True,
            allow_methods=["*"],
            allow_headers=["*"],
        )

    # 添加异常处理器
    def _add_exception_handlers(self):
        exception_handlers = ExceptionHandlers(self)
        exception_handlers.add_exception_handlers()

    # 添加根路由
    def _add_root_route(self):
        @self.get("/")
        async def root():
            return {"message": "欢迎使用 Leyramu 内部网关！"}


# 创建应用实例
application = App()
