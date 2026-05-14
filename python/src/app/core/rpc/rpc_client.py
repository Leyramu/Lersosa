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
import random
from typing import Any, Iterable

import grpc
import nacos
from fastapi import HTTPException

from app.common.config import NacosConfig, GrpcConfig
from app.core.mock import CircuitBreaker
from app.core.rpc import LoadProto
from app.domain.enum import CodeStatus, MsgStatus


# RPC 客户端
class RpcClient:
    """gRPC客户端封装类，提供proto方法动态调用能力
    Attributes:
        channel: gRPC通信通道
        proto_info_dict: 从proto文件解析出的服务方法信息字典
        """

    _instance = None

    def __new__(cls, *args: Any, **kwargs: Any) -> Any:
        if not cls._instance:
            cls._instance = super().__new__(cls)
        return cls._instance

    # 初始化RPC客户端
    def __init__(self, proto_dir: str) -> None:
        """初始化RPC客户端（支持Nacos降级）
        Args:
            proto_dir: proto文件目录路径
        Notes:
            - 优先尝试从Nacos获取服务实例
            - 如果Nacos不可用，自动降级到本地配置
        """
        self.nacos_client = None
        self.is_nacos_available = False

        # 尝试连接Nacos并获取服务实例
        try:
            # 初始化Nacos客户端
            self.nacos_client = nacos.NacosClient(
                server_addresses=NacosConfig.NACOS_SERVER_ADDR,
                namespace=NacosConfig.NACOS_NAMESPACE,
                username=NacosConfig.NACOS_USERNAME,
                password=NacosConfig.NACOS_PASSWORD
            )

            # 从Nacos获取健康实例
            instance = self._get_healthy_instance(GrpcConfig.GRPC_SERVICE_NAME, NacosConfig.NACOS_GROUP)

            # 获取元数据中的gRPC端口（带校验逻辑）
            metadata = instance.get('metadata', {})
            grpc_port = metadata.get('gRPC_port')

            if not grpc_port:
                raise ValueError(f"服务实例 {instance['ip']}:{instance['port']} 未配置gRPC端口元数据")

            # 创建gRPC通道
            grpc_address = f"{instance['ip']}:{grpc_port}"
            self.is_nacos_available = True
            logging.info(f"[RPC] 成功从Nacos获取服务实例: {grpc_address}")

        except Exception as e:
            # Nacos不可用，降级到本地配置
            logging.warning(f"[RPC] Nacos连接失败 ({str(e)})，降级使用本地配置")
            grpc_address = f"{GrpcConfig.GRPC_HOST}:{GrpcConfig.GRPC_PORT}"
            self.is_nacos_available = False

        # 创建gRPC通道
        self.channel = grpc.insecure_channel(grpc_address)
        self.proto_info_dict = self._load_proto(fr'{proto_dir}')

    def _get_healthy_instance(self, service_name: str, group_name: str) -> dict:
        """获取健康服务实例"""
        if not self.nacos_client:
            raise ConnectionError("Nacos客户端未初始化")

        instances = self.nacos_client.list_naming_instance(
            service_name=service_name,
            group_name=group_name,
            healthy_only=True
        ).get("hosts", [])

        if not instances:
            raise ConnectionError(f"没有可用的服务实例：{service_name}")

        # 使用随机负载均衡策略
        return random.choice(instances)

    # 熔断降级处理
    def _http_exception_fallback(*args, **kwargs):
        raise HTTPException(
            status_code=CodeStatus.SERVICE_UNAVAILABLE.value,
            detail=MsgStatus.SERVICE_UNAVAILABLE
        )

    # 调用RPC方法
    @CircuitBreaker(
        failure_threshold=3,
        recovery_timeout=10,
        fail_on_errors=(grpc.RpcError, ConnectionError),
        fallback=_http_exception_fallback
    )
    def call_method(self, method_name: str, request_params: Any) -> grpc.Future:
        """调用远程方法
        Args:
            method_name: 需要调用的proto方法名称
            request_params: 请求参数，支持字典/对象/原始类型
        Returns:
            grpc.Future: 异步调用返回的Future对象
        Raises:
            ValueError: 当方法不存在时抛出
        """
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

    # 流式调用方法
    @CircuitBreaker(
        failure_threshold=3,
        recovery_timeout=10,
        fail_on_errors=(grpc.RpcError, ConnectionError),
        fallback=_http_exception_fallback
    )
    def call_stream_method(self, method_name: str, request_generator: Iterable) -> Any:
        """流式调用远程方法
        Args:
            method_name: 需要调用的proto方法名称
            request_generator: 请求参数生成器
        Returns:
            grpc.Future/响应对象: 异步调用返回结果
        Raises:
            ValueError: 当方法不存在时抛出
        """
        for proto_name, proto_info in self.proto_info_dict.items():
            method_info = next((m for m in proto_info['methods'] if m['method'] == method_name), None)
            if not method_info:
                continue

            # 动态导入模块
            pb2_module = __import__(f'proto.{proto_name}_pb2', fromlist=[method_info['request']])
            pb2_grpc_module = __import__(f'proto.{proto_name}_pb2_grpc', fromlist=[method_name])
            stub_class = getattr(pb2_grpc_module, proto_name.capitalize() + 'Stub')
            stub = stub_class(self.channel)
            method = getattr(stub, method_name)

            # 流式调用特殊处理
            return method(request_generator)

        raise ValueError(f"方法 '{method_name}' 未找到")

    # 关闭RPC客户端
    def close(self) -> None:
        """关闭gRPC通信通道"""
        self.channel.close()

    # 加载proto文件
    @staticmethod
    def _load_proto(directory: str) -> dict:
        """加载proto文件并解析服务方法信息
        Args:
            directory: proto文件目录路径
        Returns:
            dict: 解析得到的proto服务方法信息字典
        """
        proto_files = LoadProto.scan_proto_files(directory)
        proto_info_dict = LoadProto.load_proto_files(proto_files)
        return proto_info_dict

    # 参数标准化处理
    @staticmethod
    def _normalize_params(params: Any) -> dict:
        """参数标准化处理
        Args:
            params: 原始请求参数
        Returns:
            dict: 标准化后的参数字典
        """
        if hasattr(params, '__dict__'):
            return vars(params)
        if isinstance(params, dict):
            return params.copy()
        return {'data': params}

    # 字段匹配逻辑复用
    def _find_matching_key(self, request_class: Any, proto_fields: list[str], params_dict: dict) -> Any:
        """字段匹配逻辑复用（精确匹配 -> 格式转换匹配 -> 类型匹配）
        Args:
            request_class: protobuf生成的请求类
            proto_fields: proto消息定义的字段列表
            params_dict: 标准化后的参数字典
        Returns:
            str/None: 匹配成功的字段名，未找到返回None
        """
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

    # 二进制数据兜底策略
    @staticmethod
    def _handle_bytes_data(request_class: Any, proto_fields: list[str], data: bytes) -> Any:
        """处理二进制数据兜底策略
        Args:
            request_class: protobuf生成的请求类
            proto_fields: proto消息定义的字段列表
            data: 二进制数据内容
        Returns:
            构造好的请求对象
        """
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

    # 字段类型映射
    @staticmethod
    def _map_field_type(field_descriptor: Any) -> Any:
        """映射protobuf字段类型到Python类型
        Args:
            field_descriptor: proto字段描述符
        Returns:
            type: 对应的Python类型
        """
        type_map = {
            field_descriptor.TYPE_BYTES: (bytes, bytearray),
            field_descriptor.TYPE_STRING: str,
            field_descriptor.TYPE_INT64: int,
            field_descriptor.TYPE_MESSAGE: dict
        }
        return type_map.get(field_descriptor.type, object)

    # 下划线命名转换
    @staticmethod
    def to_snake_case(name: str) -> str:
        """命名格式转换：小驼峰 -> 下划线命名
        Args:
            name: 原始字段名
        Returns:
            str: 转换后的下划线格式名称
        """
        return ''.join(['_' + c.lower() if c.isupper() else c for c in name]).lstrip('_')
