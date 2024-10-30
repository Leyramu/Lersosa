#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Digitalization Education), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.

from fastapi import FastAPI, Response
from fastapi.requests import Request

from app import routes

# 创建FastAPI应用实例
app = FastAPI(lifespan=routes.lifespan)


# 自定义路由装饰器
def custom_route(path: str, **kwargs):
    def decorator(func):
        return app.add_api_route(path, func, **kwargs)

    return decorator


# 使用自定义路由装饰器



async def proxy_to_service(request: Request, service_name: str):
    route = routes.ServiceDiscoveryRoute(endpoint=proxy_to_service, path="/{service_name:path}")
    return await route.dispatch(request)


# 根路由
@app.get("/")
async def root():
    return {"message": "欢迎使用 Leyramu 内部网关！"}


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="127.0.0.1", port=8001, log_level="info")
