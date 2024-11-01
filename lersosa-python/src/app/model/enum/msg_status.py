#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


from enum import Enum


# 状态消息枚举
class MsgStatus(Enum):
    INTERNAL_SERVER_ERROR_MESSAGE = "服务器内部错误"
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
