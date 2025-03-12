#  Copyright (c) 2025 Leyramu Group. All rights reserved.
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
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


import os
import re


class LoadProto:
    @staticmethod
    def scan_proto_files(directory):
        proto_file_dir = []
        proto_file = []
        for root, dirs, files in os.walk(directory):
            for file in files:
                if file.endswith(".proto"):
                    proto_file_dir.append(os.path.join(root, file))
                    proto_file.append(os.path.splitext(file)[0])
        return proto_file_dir, proto_file

    @staticmethod
    def load_proto_files(proto_files: tuple[list[str], list[str]]):
        proto_info_dict = {}
        method_pattern = re.compile(
            r'rpc\s+(\w+)\s*\(\s*(stream\s+)?([\w.]+)\s*\)\s*returns\s*\(\s*(stream\s+)?([\w.]+)\s*\)',
            re.MULTILINE
        )

        def clean_type(type_str):
            """清洗类型字符串，移除包名前缀和stream修饰符"""
            return type_str.split('.')[-1].replace('stream', '').strip()

        for proto_path, proto_name in zip(proto_files[0], proto_files[1]):
            try:
                with open(proto_path, 'r') as file:
                    content = file.read()

                    # 识别service定义块
                    service_blocks = re.findall(
                        r'service\s+\w+\s*{([^}]+)}',
                        content,
                        re.DOTALL
                    )

                    methods = []
                    for block in service_blocks:
                        for match in method_pattern.finditer(block):
                            method_name = match.group(1)
                            is_client_stream = bool(match.group(2))
                            request_type = clean_type(match.group(3))
                            is_server_stream = bool(match.group(4))
                            response_type = clean_type(match.group(5))

                            methods.append({
                                'method': method_name,
                                'request': request_type,
                                'response': response_type,
                                'stream_type': {
                                    'client_stream': is_client_stream,
                                    'server_stream': is_server_stream
                                }
                            })

                    proto_info_dict[proto_name] = {
                        'methods': methods,
                        'message_types': list(set(
                            [msg for msg in re.findall(
                                r'message\s+(\w+)\s*{',
                                content
                            )]
                        ))
                    }

            except Exception as e:
                print(f"处理 {proto_path} 发生错误: {str(e)}")
                continue

        return proto_info_dict
