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

    @staticmethod
    def _load_proto(directory):
        proto_files = LoadProto.scan_proto_files(directory)
        proto_info_dict = LoadProto.load_proto_files(proto_files)
        return proto_info_dict

    def call_method(self, method_name, request_params):

        # 查找匹配的proto方法信息
        for proto_name, proto_info in self.proto_info_dict.items():
            method_info = next((m for m in proto_info['methods'] if m['method'] == method_name), None)
            if method_info:
                request_type = method_info['request']

                # 动态导入对应的 _pb2 模块
                pb2_module = __import__(f'proto.{proto_name}_pb2', fromlist=[request_type])
                request_class = getattr(pb2_module, request_type)

                # 动态获取方法
                pb2_grpc_module = __import__(f'proto.{proto_name}_pb2_grpc', fromlist=[method_name])

                # 动态获取 Stub 类
                stub_class_name = proto_name.capitalize() + 'Stub'
                stub_class = getattr(pb2_grpc_module, stub_class_name)

                # 创建 Stub 实例
                stub = stub_class(self.channel)

                # 从 Stub 实例中获取具体的方法
                method = getattr(stub, method_name)

                # 动态创建请求对象
                request = request_class(name=request_params)

                # 调用方法
                response = method(request)
                return response

        # 如果没有找到匹配的方法，抛出异常
        raise ValueError(f"Method '{method_name}' not found in proto_info_dict")

    def close(self):
        # 关闭通道
        self.channel.close()
