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
import os
import re
from typing import Dict, LiteralString, Any

from app.core.rpc.proto_parser import ProtoParser

# 创建日志记录器
logger = logging.getLogger(__name__)


# 加载 proto 文件
class LoadProto(ProtoParser):
    """Proto文件加载器，提供扫描和解析能力"""

    # 扫描 proto 文件
    @staticmethod
    def scan_proto_files(directory: str) -> tuple[list[LiteralString | str | bytes], list[Any]]:
        """扫描目录获取proto文件信息
        Args:
            directory: 要扫描的目录路径
        Returns:
            tuple: (文件路径列表, 文件名列表)
        """
        proto_paths = []
        proto_names = []
        for root, _, files in os.walk(directory):
            for file in files:
                if file.endswith(".proto"):
                    proto_paths.append(os.path.join(root, file))
                    proto_names.append(os.path.splitext(file)[0])
        return proto_paths, proto_names

    # 解析 proto 文件
    @classmethod
    def load_proto_files(cls: Any, proto_files: tuple[list[LiteralString | str | bytes], list[Any]]) -> Dict[str, dict]:
        """解析proto文件获取服务信息
        Args:
            proto_files: scan_proto_files返回的文件列表
        Returns:
            服务信息字典
        """
        proto_info = {}
        for proto_path, proto_name in zip(*proto_files):
            try:
                with open(proto_path, 'r', encoding='utf-8') as f:
                    content = f.read()

                service_info = cls._parse_service(content)
                if service_info:
                    proto_info.update(service_info)

                message_types = cls._collect_message_types(content)
                for service in service_info.values():
                    service['message_types'] = list(message_types)

            except (IOError, re.error) as e:
                logger.error(f"Proto解析失败 [{proto_path}]", exc_info=True)
                proto_info[proto_name] = {'error': str(e)}

        return proto_info

    # 解析服务定义
    @classmethod
    def _parse_service(cls: Any, content: str) -> Dict[str, dict]:
        """解析服务定义
        Args:
            content: 解析内容
        Returns:
            服务解析信息字典
        """
        services = {}
        in_service = False
        service_content = []
        current_service = None

        for line in content.split('\n'):
            line = line.strip()
            if line.startswith('service'):
                if match := cls.SERVICE_PATTERN.match(line):
                    current_service = match.group(1)
                    in_service = True
                    service_content = []
            elif line == '}' and in_service:
                in_service = False
                services[current_service] = {
                    'methods': cls._parse_methods(''.join(service_content)),
                    'message_types': []
                }
            elif in_service:
                service_content.append(line)

        return services
