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


from enum import StrEnum


# 状态消息枚举
class MsgStatus(StrEnum):
    """定义标准化的状态消息枚举类，用于统一的接口响应消息管理

    Attributes
        INTERNAL_SERVER_ERROR_MESSAGE: 对应HTTP 500状态码，服务器内部错误
        NOT_IMPLEMENTED_MESSAGE：对应HTTP 503状态码，服务不可用，请稍后再试
        NOT_FOUND_MESSAGE: 对应HTTP 404状态码，资源未找到
        UNAUTHORIZED_MESSAGE: 对应HTTP 401状态码，身份认证失败
        FORBIDDEN_MESSAGE: 对应HTTP 403状态码，权限不足禁止访问
        BAD_REQUEST_MESSAGE: 对应HTTP 400状态码，客户端请求格式错误
        NOT_IMPLEMENTED_MESSAGE: 对应HTTP 501状态码，未实现的功能
        METHOD_NOT_ALLOWED_MESSAGE: 对应HTTP 405状态码，不支持的HTTP方法
        CONFLICT_MESSAGE: 对应HTTP 409状态码，资源状态冲突
        UNPROCESSABLE_ENTITY_MESSAGE: 对应HTTP 422状态码，语义错误无法处理
        TOO_MANY_REQUESTS_MESSAGE: 对应HTTP 429状态码，请求频率过高
        SUCCESS_MESSAGE: 通用成功状态（非HTTP标准），操作成功
        FAILURE_MESSAGE: 通用失败状态（非HTTP标准），操作失败
    """
    INTERNAL_SERVER_ERROR_MESSAGE = "服务器内部错误"
    SERVICE_UNAVAILABLE = "服务不可用，请稍后再试"
    NOT_FOUND_MESSAGE = "未找到"
    UNAUTHORIZED_MESSAGE = "未授权"
    FORBIDDEN_MESSAGE = "禁止访问"
    BAD_REQUEST_MESSAGE = "请求无效"
    NOT_IMPLEMENTED_MESSAGE = "未实现"
    METHOD_NOT_ALLOWED_MESSAGE = "不允许的方法"
    CONFLICT_MESSAGE = "冲突"
    UNPROCESSABLE_ENTITY_MESSAGE = "无法处理的实体"
    TOO_MANY_REQUESTS_MESSAGE = "请求过多"
    SUCCESS_MESSAGE = "成功"
    FAILURE_MESSAGE = "失败"
