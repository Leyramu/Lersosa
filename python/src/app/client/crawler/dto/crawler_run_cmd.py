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


from __future__ import annotations

from typing import Optional, Dict, List, Any

from pydantic import BaseModel, Field


class CrawlerRunCmd(BaseModel):
    """爬虫运行命令请求对象"""

    class CrawlerTaskConfig(BaseModel):
        """任务配置"""

        class FetcherPluginConfig(BaseModel):
            """抓取器插件配置"""
            class CrawlerPageRange(BaseModel):
                start: int = Field(default=1, description="起始页码")
                end: int = Field(default=1, description="结束页码")

            strategy_name: Optional[str] = Field(default=None)
            mode: str = Field(default="html")
            url_template: str = Field(default="")
            pages: CrawlerPageRange = Field(default_factory=CrawlerPageRange)
            timeout_seconds: int = Field(default=15)
            wait_selector: Optional[str] = Field(default=None)
            wait_timeout: int = Field(default=30000)
            browser_options: Dict[str, Any] = Field(default_factory=dict)
            headless: bool = Field(default=True)
            screenshot_on_error: bool = Field(default=True)
            proxy_pool: List[str] = Field(default_factory=list)

        class ParserPluginConfig(BaseModel):
            """解析器插件配置"""
            strategy_name: Optional[str] = Field(default=None)
            selectors: Dict[str, str] = Field(default_factory=dict)
            font_mapping: Dict[str, str] = Field(default_factory=dict)

        class ConcurrencyPluginConfig(BaseModel):
            """并发控制插件配置"""
            strategy_name: Optional[str] = Field(default=None)
            max_concurrency: int = Field(default=5)
            min_concurrency: int = Field(default=1)
            init_concurrency: int = Field(default=5)
            adjustment_step: int = Field(default=1)
            adjustment_interval: int = Field(default=10)
            tokens_per_second: float = Field(default=5.0)
            bucket_capacity: int = Field(default=10)

        class RatePluginConfig(BaseModel):
            """速率控制插件配置"""
            strategy_name: Optional[str] = Field(default=None)
            base_delay: float = Field(default=1.0)
            max_delay: float = Field(default=60.0)
            jitter_min: float = Field(default=0.0)
            jitter_max: float = Field(default=0.5)
            backoff_factor: float = Field(default=2.0)
            min_delay: float = Field(default=0.5)
            init_delay: float = Field(default=1.0)
            adjustment_factor: float = Field(default=0.2)
            max_requests: int = Field(default=60)
            window_size: float = Field(default=60.0)

        class RecoveryPluginConfig(BaseModel):
            """异常恢复插件配置"""
            strategy_name: Optional[str] = Field(default=None)
            max_retries: int = Field(default=3)
            base_delay: float = Field(default=1.0)
            backoff_factor: float = Field(default=2.0)
            failure_threshold: int = Field(default=5)
            recovery_timeout: float = Field(default=60.0)
            success_threshold: int = Field(default=2)
            fallback_strategies: List[str] = Field(default_factory=list)
            save_html: bool = Field(default=True)
            save_headers: bool = Field(default=True)

        class RobotsPluginConfig(BaseModel):
            """Robots协议插件配置"""
            strategy_name: Optional[str] = Field(default="smart")
            crawl_delay: float = Field(default=0.0)
            cache_ttl: int = Field(default=3600)
            timeout: int = Field(default=10)

        class UserAgentPluginConfig(BaseModel):
            """User-Agent插件配置"""
            strategy_name: Optional[str] = Field(default="random_rotation")
            ua_list: List[str] = Field(default_factory=list)

        # ========== 任务基础信息 ==========
        name: str = Field(default="", description="任务名称")
        enabled: bool = Field(default=True, description="是否启用")
        source: str = Field(default="", description="数据源 URL")
        headers: Dict[str, str] = Field(default_factory=dict, description="请求头配置")

        # ========== 插件配置组合 ==========
        fetch: FetcherPluginConfig = Field(default_factory=FetcherPluginConfig)
        parse: ParserPluginConfig = Field(default_factory=ParserPluginConfig)
        concurrency: ConcurrencyPluginConfig = Field(default_factory=ConcurrencyPluginConfig)
        rate: RatePluginConfig = Field(default_factory=RatePluginConfig)
        recovery: RecoveryPluginConfig = Field(default_factory=RecoveryPluginConfig)
        robots: RobotsPluginConfig = Field(default_factory=RobotsPluginConfig)
        useragent: UserAgentPluginConfig = Field(default_factory=UserAgentPluginConfig)

    project: str = Field(default="crawler_project", description="项目名称")
    description: str = Field(default="", description="项目描述")
    tasks: List[CrawlerTaskConfig] = Field(default_factory=list, description="任务列表")
