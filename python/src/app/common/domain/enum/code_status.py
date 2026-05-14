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


from enum import IntEnum


# 状态码枚举
class CodeStatus(IntEnum):
    """HTTP状态码枚举类，用于定义常见的HTTP响应状态码及其对应数值

    Attributes:
        SUCCESS (int): 请求成功。状态码：200
        FAILURE (int): 一般性错误。状态码：400
        NOT_FOUND (int): 请求资源不存在。状态码：404
        INTERNAL_SERVER_ERROR (int): 服务器内部错误。状态码：500
        SERVICE_UNAVAILABLE (int): 服务器不可用。状态码：503
        UNAUTHORIZED (int): 未认证或认证失败。状态码：401
        FORBIDDEN (int): 权限不足，拒绝访问。状态码：403
        BAD_REQUEST (int): 请求参数错误。状态码：400
        NOT_IMPLEMENTED (int): 服务器不支持请求方法。状态码：501
        METHOD_NOT_ALLOWED (int): 请求方法不被允许。状态码：405
        CONFLICT (int): 请求与当前资源状态冲突。状态码：409
        UNPROCESSABLE_ENTITY (int): 请求格式正确但语义错误（如字段验证失败）。状态码：422
        TOO_MANY_REQUESTS (int): 客户端发送过多请求。状态码：429
    """
    SUCCESS = 200
    FAILURE = 400
    NOT_FOUND = 404
    INTERNAL_SERVER_ERROR = 500
    SERVICE_UNAVAILABLE = 503
    UNAUTHORIZED = 401
    FORBIDDEN = 403
    BAD_REQUEST = 400
    NOT_IMPLEMENTED = 501
    METHOD_NOT_ALLOWED = 405
    CONFLICT = 409
    UNPROCESSABLE_ENTITY = 422
    TOO_MANY_REQUESTS = 429
