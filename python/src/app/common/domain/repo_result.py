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


from typing import Any, Optional

from fastapi.responses import JSONResponse
from pydantic import BaseModel

from app.common.domain.enum import MsgStatus, CodeStatus


# 统一返回类
class RepoResult(BaseModel):
    """标准化API响应数据模型

    Attributes:
        code: 可选状态码，默认空值。建议使用CodeStatus枚举值
        msg: 可选消息描述，默认空值。建议使用MsgStatus预定义消息
        data: 可选响应数据主体，支持任意类型数据
    """
    code: Optional[int] = None
    msg: Optional[str] = None
    data: Optional[Any] = None

    # 成功方法
    @staticmethod
    def success(code: int = CodeStatus.SUCCESS.value, msg: str = MsgStatus.SUCCESS_MESSAGE.value,
                data: Any = None) -> 'JSONResponse':
        """构建成功响应对象
        Args:
            code: HTTP状态码，默认取成功状态枚举值
            msg: 成功描述信息，默认取预定义成功消息
            data: 需要返回的业务数据，默认空值
        Returns:
            配置了标准成功状态码和序列化响应体的JSONResponse对象
        """
        return JSONResponse(
            status_code=CodeStatus.SUCCESS.value,
            content=RepoResult(code=code, msg=msg, data=data).model_dump()
        )

    # 失败方法
    @staticmethod
    def fail(code: int = CodeStatus.FAILURE.value, msg: str = MsgStatus.FAILURE_MESSAGE.value,
             data: Any = None) -> 'JSONResponse':
        """构建错误响应对象
        Args:
            code: HTTP错误码，默认取失败状态枚举值
            msg: 错误描述信息，默认取预定义失败消息
            data: 可选的错误详情数据，默认空值
        Returns:
            配置了标准错误状态码和序列化错误信息的JSONResponse对象
        """
        return JSONResponse(
            status_code=CodeStatus.SUCCESS.value,
            content=RepoResult(code=code, msg=msg, data=data).model_dump()
        )

    @staticmethod
    def error(code: int = CodeStatus.INTERNAL_SERVER_ERROR.value,
              msg: str = MsgStatus.INTERNAL_SERVER_ERROR_MESSAGE.value,
              data: Any = None) -> 'JSONResponse':
        """构建错误响应对象
        Args:
            code: HTTP错误码，默认取失败状态枚举值
            msg: 错误描述信息，默认取预定义失败消息
            data: 可选的错误详情数据，默认空值
        Returns:
            配置了标准错误状态码和序列化错误信息的JSONResponse对象
        """
        return JSONResponse(
            status_code=CodeStatus.SUCCESS.value,
            content=RepoResult(code=code, msg=msg, data=data).model_dump()
        )
