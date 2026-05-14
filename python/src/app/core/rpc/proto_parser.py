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


import logging
import re
from typing import Set, List, Any

# 创建日志记录器
logger = logging.getLogger(__name__)


# Proto 解析基类
class ProtoParser:
    """Proto解析基类，
        用于解析proto文件中的服务定义、方法参数和消息类型
    Attributes：
        SERVICE_PATTERN: 服务定义正则模式，匹配service关键字及服务名
        METHOD_PATTERN: 方法定义正则模式，匹配rpc方法定义及其参数
        MESSAGE_PATTERN: 消息类型正则模式，匹配message关键字及类型名
    """
    SERVICE_PATTERN: any = re.compile(r'service\s+(\w+)\s*{')
    METHOD_PATTERN: any = re.compile(
        r'rpc\s+(\w+)\s*\(\s*(stream\s+)?([\w.]+)\s*\)\s*returns\s*\(\s*(stream\s+)?([\w.]+)\s*\)',
        re.MULTILINE
    )
    MESSAGE_PATTERN: any = re.compile(r'message\s+(\w+)\s*{', re.MULTILINE)

    # 清洗类型字符串
    @staticmethod
    def _clean_type(type_str: str) -> str:
        """清洗类型字符串，移除包名前缀和stream修饰符
        Args:
            type_str: 原始类型字符串，可能包含包名前缀或stream关键字
        Returns:
            处理后的纯净类型名称，如将"stream example.Request"转为"Request"
        """
        return type_str.split('.')[-1].replace('stream', '').strip()

    # 解析方法定义
    @classmethod
    def _parse_methods(cls: Any, service_block: str) -> List[dict]:
        """解析服务块中的方法定义
        Args:
            service_block: 包含服务方法定义的代码块字符串
        Returns:
            方法字典列表，每个字典包含：
                method: 方法名称
                request: 请求类型（已清洗）
                response: 响应类型（已清洗）
                stream_type: 流类型标记字典，包含client_stream/server_stream布尔值
        """
        methods = []
        for block in service_block.split(';'):
            for match in cls.METHOD_PATTERN.finditer(block):
                methods.append({
                    'method': match.group(1),
                    'request': cls._clean_type(match.group(3)),
                    'response': cls._clean_type(match.group(5)),
                    'stream_type': {
                        'client_stream': bool(match.group(2)),
                        'server_stream': bool(match.group(4))
                    }
                })
        return methods

    # 收集消息类型
    @classmethod
    def _collect_message_types(cls: Any, content: str) -> Set[str]:
        """收集proto文件中定义的所有消息类型
        Args:
            content: proto文件内容字符串
        Returns:
            包含所有消息类型名称的集合（已清洗后的类型名）
        """
        return {cls._clean_type(msg) for msg in cls.MESSAGE_PATTERN.findall(content)}
