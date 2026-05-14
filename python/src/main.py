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

import uvicorn
from fastapi.logger import logger

from app import Bootstrap
from app.common.utils import UvicornLogConfig

# 运行
if __name__ == "__main__":
    """环境运行配置
        根据 MODE 环境变量自动选择配置
    Attributes：
        Bootstrap - 主应用实例
        ENV_FILE - 环境变量配置文件路径
        HOST - 绑定主机地址
        PORT - 监听8000端口
        LOG_LEVEL - 设置日志级别
    """

    MODE = os.getenv("MODE", "dev")

    if MODE == "dev":
        ENV_FILE = ".env.development"
        HOST = "127.0.0.1"
        PORT = 8000
        LOG_LEVEL = "debug"
    elif MODE == "staging":
        ENV_FILE = ".env.staging"
        HOST = "211.64.41.145"
        PORT = 8000
        LOG_LEVEL = "info"
    elif MODE == "prod":
        ENV_FILE = ".env.production"
        HOST = "lersosa-python"
        PORT = 8000
        LOG_LEVEL = "info"
    else:
        raise ValueError(f"不支持的 MODE: {MODE}")

    logger.info("启动 %s 模式", MODE)

    # 运行 uvicorn
    uvicorn.run(
        app=Bootstrap(),
        env_file=ENV_FILE,
        host=HOST,
        port=PORT,
        log_level=LOG_LEVEL,
        log_config=UvicornLogConfig.get_config(LOG_LEVEL),
    )
