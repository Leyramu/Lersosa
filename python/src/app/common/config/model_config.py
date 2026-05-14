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


import os


# 模型配置类
class ModelConfig:
    """模型配置类，用于管理模型相关路径配置
    Attributes:
        MODEL_PATH (str): 模型文件路径，优先从环境变量MODEL_PATH读取
            默认值为 'model/yolo11n.pt'
        MODEL_SOURCES (str): 模型输入数据源路径，优先从环境变量MODEL_SOURCES读取
            默认值为 'algo/data/raw/bus.jpg'
    """
    MODEL_PATH: str = os.getenv('MODEL_PATH', '/lersosa/service/python/src/model/pulsar_scores_v1.2.pth')
    MODEL_SOURCES: str = os.getenv('MODEL_SOURCES', 'algo/data/raw/bus.jpg')
    MODEL_SAVE_PATH: str = os.getenv('MODEL_SAVE_PATH', 'model/pulsar_scores_v1.2.pth')
    MODEL_IMAGE_SAVE_PATH: str = os.getenv('MODEL_IMAGE_SAVE_PATH', 'algo/data/processed/train/img')
    MODEL_TRAIN_POSITIVE_SAMPLE_DIR: str = os.getenv('MODEL_TRAIN_POSITIVE_SAMPLE_DIR', 'algo/data/processed/train/pos')
    MODEL_TRAIN_NEGATIVE_SAMPLE_DIR: str = os.getenv('MODEL_TRAIN_NEGATIVE_SAMPLE_DIR', 'algo/data/processed/train/neg')
    MODEL_TRAIN_NETWORK_TYPE: int = int(os.getenv('MODEL_TRAIN_NETWORK_TYPE', 0))
    MODEL_TRAIN_BATCH_SIZE: int = int(os.getenv('MODEL_TRAIN_BATCH_SIZE', 16))
    MODEL_TRAIN_EPOCHS: int = int(os.getenv('MODEL_TRAIN_EPOCHS', 40))
    MODEL_TRAIN_LEARNING_RATE: float = float(os.getenv('MODEL_TRAIN_LEARNING_RATE', 0.0001))
    MODEL_TRAIN_OD: bool = bool(os.getenv('MODEL_TRAIN_OD', False))
