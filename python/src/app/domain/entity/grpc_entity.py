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


import json
from typing import Optional, Any

from google.protobuf.json_format import MessageToJson
from pydantic import BaseModel


# gRPC 实体
class GrpcEntity(BaseModel):
    """gRPC 实体，负责传输 gRPC 参数
    Attributes:
        data: 可选的gRPC消息对象实例，允许为空。应为protobuf生成的python类实例
    """
    data: Optional[Any] = None

    # 将 gRPC 实体转换为 JSON
    def to_json(self):
        """将gRPC消息对象转换为JSON可序列化字典
            使用protobuf内置的MessageToJson进行转换，保留原始proto字段命名规范，
            禁用JSON缩进和键排序，允许非ASCII字符直接输出
        Returns:
            dict: 反序列化后的字典对象，可直接用于json.dumps等序列化操作
        """
        return json.loads(MessageToJson(
            message=self.data,
            preserving_proto_field_name=True,
            indent=None,
            sort_keys=False,
            ensure_ascii=False,
        ))
