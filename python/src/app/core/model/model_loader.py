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


import torch

from app.common.config import ModelConfig


class ModelLoader:
    _instance = None
    _model = None
    _device = None

    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super(ModelLoader, cls).__new__(cls)
        return cls._instance

    def load_model(self):
        """加载模型到指定设备"""
        self._device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self._model = torch.load(
            f=ModelConfig.MODEL_PATH,
            map_location=self._device,
            weights_only=False
        )
        self._model.eval()
        print("模型加载成功！")

    def get_model(self):
        """获取已加载的模型"""
        if self._model is None:
            raise RuntimeError("模型未加载！")
        return self._model

    def get_device(self):
        """获取当前设备（CPU/GPU）"""
        if self._device is None:
            raise RuntimeError("设备未初始化！")
        return self._device

    @classmethod
    def get_instance(cls):
        """获取 ModelLoader 单例实例"""
        if cls._instance is None:
            cls._instance = ModelLoader()
        return cls._instance
