#  Copyright (c) 2024 Leyramu Group. All rights reserved.
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


import grpc

from app.core.rpc import LoadProto


class RpcClient:

    def __init__(self, host, port, proto_dir):
        # 创建一个通道到服务器
        self.channel = grpc.insecure_channel(f'{host}:{port}')
        self.proto_info_dict = self._load_proto(fr'{proto_dir}')

    def call_method(self, method_name, request_params):
        # 查找匹配的proto方法信息
        for proto_name, proto_info in self.proto_info_dict.items():
            method_info = next((m for m in proto_info['methods'] if m['method'] == method_name), None)
            if not method_info:
                continue

            request_type = method_info['request']

            # 动态导入模块
            pb2_module = __import__(f'proto.{proto_name}_pb2', fromlist=[request_type])
            request_class = getattr(pb2_module, request_type)
            pb2_grpc_module = __import__(f'proto.{proto_name}_pb2_grpc', fromlist=[method_name])
            stub_class = getattr(pb2_grpc_module, proto_name.capitalize() + 'Stub')
            stub = stub_class(self.channel)
            method = getattr(stub, method_name)

            # 动态创建请求对象
            proto_fields = request_class.DESCRIPTOR.fields_by_name.keys()

            # 智能参数处理
            params_dict = self._normalize_params(request_params)
            matched_key = self._find_matching_key(request_class, proto_fields, params_dict)

            # 构造请求对象
            if matched_key:
                request = request_class(**{matched_key: params_dict[matched_key]})
            else:
                # 处理二进制数据兜底
                if isinstance(request_params, (bytes, bytearray)):
                    request = self._handle_bytes_data(request_class, proto_fields, request_params)
                else:
                    # 最终兜底策略
                    fallback_field = next(iter(proto_fields), 'data')
                    request = request_class(**{fallback_field: request_params})

            return method(request)

        raise ValueError(f"方法 '{method_name}' 未找到")

    def close(self):
        # 关闭通道
        self.channel.close()

    @staticmethod
    def _load_proto(directory):
        proto_files = LoadProto.scan_proto_files(directory)
        proto_info_dict = LoadProto.load_proto_files(proto_files)
        return proto_info_dict

    @staticmethod
    def _normalize_params(params):
        """参数标准化处理"""
        if hasattr(params, '__dict__'):
            return vars(params)
        if isinstance(params, dict):
            return params.copy()
        return {'data': params}

    def _find_matching_key(self, request_class, proto_fields, params_dict):
        """字段匹配逻辑复用"""
        # 首轮精确匹配
        matched_key = next((k for k in proto_fields if k in params_dict), None)
        if matched_key:
            return matched_key

        # 次轮下划线格式匹配
        snake_case_mapping = {self.to_snake_case(k): k for k in params_dict}
        for proto_field in proto_fields:
            if proto_field in snake_case_mapping:
                return proto_field

        # 第三轮值类型匹配
        for proto_field in proto_fields:
            field_descriptor = request_class.DESCRIPTOR.fields_by_name[proto_field]
            expected_type = self._map_field_type(field_descriptor)
            if any(isinstance(v, expected_type) for v in params_dict.values()):
                return proto_field

        return None

    @staticmethod
    def _handle_bytes_data(request_class, proto_fields, data):
        """处理二进制数据兜底策略"""
        # 尝试常见二进制字段名
        for field in ['data', 'content', 'bytes']:
            if field in proto_fields:
                return request_class(**{field: data})

        # 匹配第一个bytes类型字段
        for field in proto_fields:
            field_desc = request_class.DESCRIPTOR.fields_by_name[field]
            if field_desc.type == field_desc.TYPE_BYTES:
                return request_class(**{field: data})

        # 最终强制使用第一个字段
        fallback_field = next(iter(proto_fields), 'data')
        return request_class(**{fallback_field: data})

    @staticmethod
    def _map_field_type(field_descriptor):
        """映射protobuf字段类型到Python类型"""
        type_map = {
            field_descriptor.TYPE_BYTES: (bytes, bytearray),
            field_descriptor.TYPE_STRING: str,
            field_descriptor.TYPE_INT64: int,
            field_descriptor.TYPE_MESSAGE: dict
        }
        return type_map.get(field_descriptor.type, object)

    @staticmethod
    def to_snake_case(name):
        """将小驼峰转换为下划线命名"""
        return ''.join(['_' + c.lower() if c.isupper() else c for c in name]).lstrip('_')
