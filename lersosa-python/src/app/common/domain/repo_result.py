#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


from typing import Any, Optional

from fastapi.responses import JSONResponse
from pydantic import BaseModel

from app.domain.enum import MsgStatus, CodeStatus


# 统一返回类
class RepoResult(BaseModel):
    code: Optional[int] = None
    msg: Optional[str] = None
    data: Optional[Any] = None

    # 成功方法
    @staticmethod
    def success(code: int = CodeStatus.SUCCESS.value, msg: str = MsgStatus.SUCCESS_MESSAGE.value,
                data: Any = None) -> 'JSONResponse':
        return JSONResponse(
            status_code=CodeStatus.SUCCESS.value,
            content=RepoResult(code=code, msg=msg, data=data).model_dump()
        )

    # 失败方法
    @staticmethod
    def error(code: int = CodeStatus.FAILURE.value, msg: str = MsgStatus.FAILURE_MESSAGE.value,
              data: Any = None) -> 'JSONResponse':
        return JSONResponse(
            status_code=CodeStatus.FAILURE.value,
            content=RepoResult(code=code, msg=msg, data=data).model_dump()
        )
