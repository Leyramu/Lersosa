#  Copyright (c) 2024 Leyramu. All rights reserved.
#  This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
#  For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
#  The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
#  By using this project, users acknowledge and agree to abide by these terms and conditions.


from inspect import iscoroutinefunction

from fastapi import APIRouter


class BaseController(APIRouter):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.register_routes()

    @staticmethod
    def get(path, *args, **kwargs):
        def decorator(func):
            func.__router_method__ = ('get', path, args, kwargs)
            return func

        return decorator

    @staticmethod
    def post(path, *args, **kwargs):
        def decorator(func):
            func.__router_method__ = ('post', path, args, kwargs)
            return func

        return decorator

    @staticmethod
    def put(path, *args, **kwargs):
        def decorator(func):
            func.__router_method__ = ('put', path, args, kwargs)
            return func

        return decorator

    @staticmethod
    def delete(path, *args, **kwargs):
        def decorator(func):
            func.__router_method__ = ('delete', path, args, kwargs)
            return func

        return decorator

    def register_routes(self):
        for name in dir(self):
            method = getattr(self, name)
            if iscoroutinefunction(method) and hasattr(method, '__router_method__'):
                route_method, path, args, kwargs = method.__router_method__
                if route_method == 'get':
                    self.add_api_route(path, method, methods=["GET"], **kwargs)
                elif route_method == 'post':
                    self.add_api_route(path, method, methods=["POST"], **kwargs)
                elif route_method == 'put':
                    self.add_api_route(path, method, methods=["PUT"], **kwargs)
                elif route_method == 'delete':
                    self.add_api_route(path, method, methods=["DELETE"], **kwargs)


Get = BaseController.get
Post = BaseController.post
Put = BaseController.put
Delete = BaseController.delete
