#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


import grpc

from proto import Greeter_pb2
from proto import Greeter_pb2_grpc


class GreeterClient:
    def __init__(self, host, port):
        # 创建一个通道到服务器
        self.channel = grpc.insecure_channel(f'{host}:{port}')
        # 创建一个 stub（客户端）
        self.stub = Greeter_pb2_grpc.GreeterStub(self.channel)

    def call_method(self, method_name, request_params):
        # 动态获取方法
        method = getattr(self.stub, method_name, None)
        if not method:
            raise AttributeError(f"Method '{method_name}' not found in stub")

        # 创建请求消息
        request = Greeter_pb2.HelloRequest(name=request_params)
        # 调用方法
        response = method(request)
        # 返回响应
        return response

    def close(self):
        # 关闭通道
        self.channel.close()
