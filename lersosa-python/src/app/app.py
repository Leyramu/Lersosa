#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.common import RepoResult
from app.controller import controllers
from app.enum import CodeStatus
from app.nacos import lifespan


# 创建FastAPI应用实例
class App:
    def __init__(self):
        self.app = FastAPI(lifespan=lifespan)
        self._register_routes()
        self._add_middlewares()
        self._add_exception_handlers()
        self._add_root_route()

    def _register_routes(self):
        self.app.include_router(controllers)

    def _add_middlewares(self):
        self.app.add_middleware(
            CORSMiddleware, # type: ignore
            allow_origins=["*"],
            allow_credentials=True,
            allow_methods=["*"],
            allow_headers=["*"],
        )

    def _add_exception_handlers(self):
        @self.app.exception_handler(Exception)
        async def global_exception_handler(exc: Exception):
            return RepoResult.error(code=CodeStatus.FAILURE, msg=str(exc))

    def _add_root_route(self):
        @self.app.get("/")
        async def root():
            return {"message": "欢迎使用 Leyramu 内部网关！"}


# 创建应用实例
app = App().app
