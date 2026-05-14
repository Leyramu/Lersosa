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


class UvicornLogConfig:
    """Uvicorn 日志配置类
    
    Notes：
        - 提供基于 colorlog 的彩色日志配置
        - 支持自定义日志级别
        - 统一日志格式，便于阅读和调试
    """
    
    @staticmethod
    def get_config(level: str = "info") -> dict:
        """获取支持 colorlog 的 uvicorn 日志配置

        Args:
            level: 日志级别 (debug, info, warning, error, critical)

        Returns:
            日志配置字典
        """
        return {
            "version": 1,
            "disable_existing_loggers": False,
            "formatters": {
                "color": {
                    "()": "colorlog.ColoredFormatter",
                    "format": "%(purple)s%(asctime)s%(reset)s | %(log_color)s%(levelname)s%(reset)s | %(blue)s%(name)s%(reset)s | %(message)s",
                    "datefmt": "%Y-%m-%d %H:%M:%S",
                    "log_colors": {
                        "DEBUG": "cyan",
                        "INFO": "green",
                        "WARNING": "yellow",
                        "ERROR": "red",
                        "CRITICAL": "bold_red",
                    },
                },
                "access": {
                    "()": "colorlog.ColoredFormatter",
                    "format": "%(purple)s%(asctime)s%(reset)s | %(log_color)s%(levelname)s%(reset)s | %(blue)s%(name)s%(reset)s | %(message)s",
                    "datefmt": "%Y-%m-%d %H:%M:%S",
                    "log_colors": {
                        "DEBUG": "cyan",
                        "INFO": "green",
                        "WARNING": "yellow",
                        "ERROR": "red",
                        "CRITICAL": "bold_red",
                    },
                },
            },
            "handlers": {
                "default": {
                    "formatter": "color",
                    "class": "logging.StreamHandler",
                    "stream": "ext://sys.stdout",
                },
                "access": {
                    "formatter": "access",
                    "class": "logging.StreamHandler",
                    "stream": "ext://sys.stdout",
                },
            },
            "loggers": {
                "": {
                    "handlers": ["default"],
                    "level": level.upper(),
                    "propagate": False,
                },
                "uvicorn": {
                    "handlers": ["default"],
                    "level": level.upper(),
                    "propagate": False,
                },
                "uvicorn.error": {
                    "handlers": ["default"],
                    "level": level.upper(),
                    "propagate": False,
                },
                "uvicorn.access": {
                    "handlers": ["access"],
                    "level": level.upper(),
                    "propagate": False,
                },
            },
        }
