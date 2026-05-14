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


from typing import Dict, List, Optional, Any

from pydantic import BaseModel, Field


# 数据提取器领域对象
class CrawlerEntity(BaseModel):
    """爬虫领域实体
    
    职责：
    
    Notes：
        - 封装爬虫配置的核心业务规则
        - 提供领域行为方法（非贫血模型）
        - 不依赖任何基础设施
        - 充血模型：包含业务规则和行为方法
        - 自校验：提供 validate 方法确保数据合法性
        - 无副作用：所有方法都是纯函数
    """

    class CrawlerTaskConfig(BaseModel):
        """任务配置"""

        class FetcherPluginConfig(BaseModel):
            """抓取器插件配置"""

            class CrawlerPageRange(BaseModel):
                """页面范围配置"""
                start: int = Field(default=1, description="起始页码")
                end: int = Field(default=1, description="结束页码")

            strategy_name: Optional[str] = Field(default=None,
                                                 description="抓取策略名称 (static_page/dynamic_render/anti_scraping/unstructured_data)")
            mode: str = Field(default="html", description="爬取模式：html/json")
            url_template: str = Field(default="", description="URL模板")
            pages: CrawlerPageRange = Field(default_factory=CrawlerPageRange, description="页面范围")
            timeout_seconds: int = Field(default=15, description="超时时间（秒）")
            wait_selector: Optional[str] = Field(default=None, description="动态渲染等待选择器")
            wait_timeout: int = Field(default=30000, description="等待超时时间（毫秒）")
            browser_options: Dict[str, Any] = Field(default_factory=dict, description="浏览器启动选项")
            
            # DynamicRenderStrategy 专用
            headless: bool = Field(default=True, description="是否无头模式")
            screenshot_on_error: bool = Field(default=True, description="错误时是否截图")
            
            # AntiScrapingStrategy 专用
            proxy_pool: List[str] = Field(default_factory=list, description="代理池列表")

        class ParserPluginConfig(BaseModel):
            """解析器插件配置"""
            strategy_name: Optional[str] = Field(default=None,
                                                 description="解析策略名称 (static_html/unstructured_data/anti_scraping)")
            selectors: Dict[str, str] = Field(default_factory=dict, description="CSS/XPath选择器映射")
            
            # AntiScrapingStrategy 专用
            font_mapping: Dict[str, str] = Field(default_factory=dict, description="字体加密映射表")

        class ConcurrencyPluginConfig(BaseModel):
            """并发控制插件配置"""
            strategy_name: Optional[str] = Field(default=None,
                                                 description="并发策略名称 (fixed/adaptive/semaphore/token_bucket)")

            # AdaptiveConcurrencyStrategy & FixedConcurrencyStrategy & SemaphoreConcurrencyStrategy 通用
            max_concurrency: int = Field(default=5, description="最大并发数")

            # SemaphoreConcurrencyStrategy 专用
            fair: bool = Field(default=True, description="是否公平模式")

            # AdaptiveConcurrencyStrategy 专用
            min_concurrency: int = Field(default=1, description="最小并发数")
            init_concurrency: int = Field(default=5, description="初始并发数")
            adjustment_step: int = Field(default=1, description="调整步长")
            adjustment_interval: int = Field(default=10, description="调整间隔（请求次数）")

            # TokenBucketStrategy 专用
            tokens_per_second: float = Field(default=5.0, description="每秒令牌生成数")
            bucket_capacity: int = Field(default=10, description="令牌桶容量")

        class RatePluginConfig(BaseModel):
            """速率控制插件配置"""
            strategy_name: Optional[str] = Field(default=None,
                                                 description="速率策略名称 (fixed_delay/adaptive_delay/exponential_backoff/sliding_window)")

            # FixedDelayStrategy & ExponentialBackoffStrategy 通用
            base_delay: float = Field(default=1.0, description="基础延迟时间（秒）")

            # AdaptiveDelayStrategy & ExponentialBackoffStrategy 通用
            max_delay: float = Field(default=60.0, description="最大延迟时间（秒）")

            # FixedDelayStrategy 专用
            jitter_min: float = Field(default=0.0, description="最小抖动时间（秒）")
            jitter_max: float = Field(default=0.5, description="最大抖动时间（秒）")

            # ExponentialBackoffStrategy 专用
            backoff_factor: float = Field(default=2.0, description="退避因子")

            # AdaptiveDelayStrategy 专用
            min_delay: float = Field(default=0.5, description="最小延迟时间（秒）")
            init_delay: float = Field(default=1.0, description="初始延迟时间（秒）")
            adjustment_factor: float = Field(default=0.2, description="延迟调整因子")

            # SlidingWindowStrategy 专用
            max_requests: int = Field(default=60, description="窗口内最大请求数")
            window_size: float = Field(default=60.0, description="滑动窗口大小（秒）")

        class RecoveryPluginConfig(BaseModel):
            """异常恢复插件配置"""
            strategy_name: Optional[str] = Field(default=None,
                                                 description="恢复策略名称 (retry/circuit_breaker/fallback/snapshot)")

            # RetryRecoveryStrategy 专用
            max_retries: int = Field(default=3, description="最大重试次数")
            base_delay: float = Field(default=1.0, description="重试基础延迟（秒）")
            backoff_factor: float = Field(default=2.0, description="重试退避因子")

            # CircuitBreakerStrategy 专用
            failure_threshold: int = Field(default=5, description="熔断失败阈值")
            recovery_timeout: float = Field(default=60.0, description="熔断恢复超时（秒）")
            success_threshold: int = Field(default=2, description="熔断成功恢复阈值")

            # FallbackRecoveryStrategy 专用
            fallback_strategies: List[str] = Field(default_factory=list, description="备选降级策略列表")

            # SnapshotRecoveryStrategy 专用
            save_html: bool = Field(default=True, description="是否保存HTML快照")
            save_headers: bool = Field(default=True, description="是否保存请求头快照")

        class RobotsPluginConfig(BaseModel):
            """Robots协议插件配置"""
            strategy_name: Optional[str] = Field(default="smart", description="Robots策略名称 (smart/disabled)")

            # DisabledRobotsProtocol & SmartRobotsProtocol 通用
            crawl_delay: float = Field(default=0.0, description="爬取延迟（秒，0表示无限制）")

            # SmartRobotsProtocol 专用
            cache_ttl: int = Field(default=3600, description="robots.txt 缓存时间（秒）")
            timeout: int = Field(default=10, description="获取 robots.txt 超时时间（秒）")

        class UserAgentPluginConfig(BaseModel):
            """User-Agent插件配置"""
            strategy_name: Optional[str] = Field(default="random_rotation",
                                                 description="UA策略名称 (random_rotation/sequential/browser_fingerprint)")
            ua_list: List[str] = Field(default_factory=list, description="自定义UA列表")

        # ========== 任务基础信息 ==========
        name: str = Field(default="", description="任务名称")
        enabled: bool = Field(default=True, description="是否启用")
        source: str = Field(default="", description="数据源 URL")
        headers: Dict[str, str] = Field(default_factory=dict, description="请求头配置")

        # ========== 插件配置组合 ==========
        fetch: FetcherPluginConfig = Field(default_factory=FetcherPluginConfig, description="抓取器配置")
        parse: ParserPluginConfig = Field(default_factory=ParserPluginConfig, description="解析器配置")
        concurrency: ConcurrencyPluginConfig = Field(default_factory=ConcurrencyPluginConfig,
                                                     description="并发控制配置")
        rate: RatePluginConfig = Field(default_factory=RatePluginConfig, description="速率控制配置")
        recovery: RecoveryPluginConfig = Field(default_factory=RecoveryPluginConfig, description="异常恢复配置")
        robots: RobotsPluginConfig = Field(default_factory=RobotsPluginConfig, description="Robots协议配置")
        useragent: UserAgentPluginConfig = Field(default_factory=UserAgentPluginConfig, description="User-Agent配置")

    project: str = Field(default="crawler_project", description="项目名称")
    description: str = Field(default="", description="项目描述")
    tasks: List[CrawlerTaskConfig] = Field(default_factory=list, description="任务列表")

    # ========== 领域行为方法 ==========

    def validate_all_tasks(self) -> List[str]:
        """校验所有任务的合法性"""
        errors = []
        enabled_tasks = [t for t in self.tasks if t.enabled]
        if not enabled_tasks:
            errors.append("至少需要有一个启用的任务")

        for idx, task in enumerate(self.tasks):
            if not task.name:
                errors.append(f"任务[{idx}] 名称不能为空")
            if not task.source:
                errors.append(f"任务[{idx}] 数据源不能为空")
            if not task.fetch.url_template:
                errors.append(f"任务[{idx}] URL模板不能为空")
            if task.fetch.pages.start > task.fetch.pages.end:
                errors.append(f"任务[{idx}] 起始页码({task.fetch.pages.start})不能大于结束页码({task.fetch.pages.end})")
        return errors

    def get_enabled_tasks(self) -> List[CrawlerTaskConfig]:
        """获取已启用的任务"""
        return [task for task in self.tasks if task.enabled]
