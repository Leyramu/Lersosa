#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


from app.base import BaseController, Get
from app.common import RepoResult
from app.controller.request import TestRequest
from app.service import TestService


#  测试控制器
class TestController(BaseController):
    """
    TestController类继承自BaseController，用于处理与测试相关的API请求。
    """

    # 初始化路由
    def __init__(self):
        """
        初始化TestController类，设置路由前缀、标签和通用响应。
        """
        super().__init__(
            prefix="/test",
            tags=["test"],
            responses={404: {"description": "Not found"}},
        )
    @Get("")
    async def read_items(self, param: TestRequest = None):
        """
        处理GET请求，返回测试项的数据。

        Returns:
            RepoResult: 包含测试项数据的RepoResult对象。
        """
        return RepoResult.success(
            data=await TestService.read_items(param)
        )
