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

import logging
import random
import time
from typing import TYPE_CHECKING, Any

import requests

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.fetchers import FetcherStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRequestContext, CrawlerResponseData

logger = logging.getLogger(__name__)


@Component
class AntiScrapingFetcherStrategy(FetcherStrategyI):
    """反爬对抗抓取器

    Notes：
        - TLS 指纹伪装：使用 curl_cffi 模拟真实浏览器
        - 代理池管理：自动轮换和健康检查
        - 行为模拟：随机延迟、自然请求间隔
        - 智能降级：curl_cffi 不可用时自动降级到 requests
    """

    # 支持的 TLS 指纹类型
    SUPPORTED_TLS_PROFILES = ["chrome", "firefox", "safari", "edge"]

    def __init__(self, config: Any = None):
        """初始化反爬对抗抓取器

        Args:
            config: 领域实体中的插件配置对象，支持以下属性：
                - proxy_pool: 代理池列表（默认空列表）
                  例如: ["http://proxy1:8080", "http://proxy2:8080"]
                - max_proxy_failures: 代理最大失败次数（默认3）
        """
        if config:
            self._proxy_pool = getattr(config, 'proxy_pool', [])
            self._max_proxy_failures = getattr(config, 'max_proxy_failures', 3)
        else:
            self._proxy_pool = []
            self._max_proxy_failures = 3

        self._current_proxy_index = 0
        self._proxy_failure_count = {}  # 记录每个代理的失败次数

    @property
    def strategy_name(self) -> str:
        return "anti_scraping"

    def fetch(self, ctx: CrawlerRequestContext, **kwargs: Any) -> CrawlerResponseData:
        """抓取受保护的页面

        Args:
            ctx: 请求上下文
            **kwargs: 额外参数
                - use_proxy: 是否使用代理（默认 True）
                - random_delay: 随机延迟范围（秒，如 (1, 3)，默认 None）
                - tls_impersonate: TLS 指纹模仿类型（chrome/firefox/safari/edge，默认 chrome）

        Returns:
            响应数据对象

        Raises:
            ConnectionError: 网络错误或超时
            RuntimeError: 其他错误
        """
        from infrastructure.crawler.gatewayimpl.core.dto import CrawlerResponseData

        use_proxy: bool = kwargs.get("use_proxy", True)
        random_delay: tuple[float, float] | None = kwargs.get("random_delay")
        tls_impersonate: str = kwargs.get("tls_impersonate", "chrome")

        # 验证 TLS 指纹类型
        if tls_impersonate not in self.SUPPORTED_TLS_PROFILES:
            logger.warning(f"不支持的 TLS 指纹类型: {tls_impersonate}，使用默认值 chrome")
            tls_impersonate = "chrome"

        # 随机延迟（模拟人类行为）
        if random_delay is not None:
            delay = random.uniform(random_delay[0], random_delay[1])
            logger.debug(f"随机延迟 {delay:.2f}s")
            time.sleep(delay)

        # 选择代理
        proxies: dict[str, str] | None = None
        if use_proxy and self._proxy_pool:
            proxy = self._get_next_proxy()
            proxies = {"http": proxy, "https": proxy}
            logger.debug(f"使用代理: {proxy}")
        elif use_proxy and not self._proxy_pool:
            logger.warning("启用了代理但未配置代理池")

        start_time = time.time()
        try:
            logger.debug(f"开始反爬对抗请求: URL={ctx.url}, TLS={tls_impersonate}")
            
            text = self._do_anti_scraping_request(ctx, proxies, tls_impersonate)
            elapsed = time.time() - start_time

            logger.info(f"反爬对抗请求成功: 耗时={elapsed:.2f}s, 内容长度={len(text)} chars")

            return CrawlerResponseData(
                url=ctx.url,
                status_code=200,
                elapsed_seconds=elapsed,
                text=text,
            )
        except requests.exceptions.Timeout as exc:
            # 超时异常
            logger.error(f"请求超时: {exc}")
            raise
        except requests.exceptions.ConnectionError as exc:
            # 连接错误
            if proxies:
                self._mark_proxy_failed(proxies.get("https", ""))
            logger.error(f"连接错误: {exc}")
            raise
        except Exception as exc:
            logger.error(f"反爬对抗抓取失败: {exc}", exc_info=True)
            raise RuntimeError(f"反爬对抗抓取失败: {exc}") from exc

    def _do_anti_scraping_request(
            self,
            ctx: CrawlerRequestContext,
            proxies: dict[str, str] | None,
            tls_impersonate: str,
    ) -> str:
        """执行反爬对抗请求

        优先使用 curl_cffi 进行 TLS 指纹伪装，不可用时降级到普通 requests

        Args:
            ctx: 请求上下文
            proxies: 代理配置
            tls_impersonate: TLS 指纹模仿类型

        Returns:
            响应文本

        Raises:
            requests.RequestException: 当请求失败时
        """
        # 尝试使用 curl_cffi（TLS 指纹伪装）
        try:
            from curl_cffi import requests as cffi_requests

            logger.debug(f"使用 curl_cffi 进行 TLS 指纹伪装: {tls_impersonate}")
            session = cffi_requests.Session(impersonate=tls_impersonate)
            response = session.get(
                url=ctx.url,
                headers=ctx.headers,
                timeout=ctx.timeout_seconds,
                proxies=proxies,
            )
            response.raise_for_status()
            logger.debug(f"curl_cffi 请求成功: 状态码={response.status_code}")
            return response.text

        except ImportError:
            logger.info("curl_cffi 未安装，降级使用普通 requests 库")
            return self._do_standard_request(ctx, proxies)

        except Exception as exc:
            logger.warning(f"curl_cffi 请求失败: {exc}，降级使用普通 requests")
            return self._do_standard_request(ctx, proxies)

    @staticmethod
    def _do_standard_request(
            ctx: CrawlerRequestContext,
            proxies: dict[str, str] | None,
    ) -> str:
        """执行标准 HTTP 请求

        Args:
            ctx: 请求上下文
            proxies: 代理配置

        Returns:
            响应文本

        Raises:
            requests.RequestException: 当请求失败时
        """
        logger.debug("使用标准 requests 库发起请求")
        session = requests.Session()
        
        # 添加一些常见的浏览器头
        if "Referer" not in ctx.headers:
            ctx.headers["Referer"] = f"{ctx.url.split('//')[0]}//{ctx.url.split('//')[1].split('/')[0]}"
        
        response = session.get(
            url=ctx.url,
            headers=ctx.headers,
            timeout=ctx.timeout_seconds,
            proxies=proxies,
        )
        response.raise_for_status()
        logger.debug(f"标准请求成功: 状态码={response.status_code}")
        return response.text

    def _get_next_proxy(self) -> str:
        """获取下一个可用代理

        采用轮询策略，跳过失败次数过多的代理

        Returns:
            代理地址

        Raises:
            ValueError: 当没有可用代理时
        """
        if not self._proxy_pool:
            raise ValueError("代理池为空")

        # 尝试找到可用的代理
        attempts = 0
        max_attempts = len(self._proxy_pool)
        
        while attempts < max_attempts:
            proxy = self._proxy_pool[self._current_proxy_index]
            failure_count = self._proxy_failure_count.get(proxy, 0)
            
            # 如果代理失败次数未超过阈值，则使用
            if failure_count < self._max_proxy_failures:
                self._current_proxy_index = (self._current_proxy_index + 1) % len(self._proxy_pool)
                return proxy
            
            # 否则跳过这个代理
            logger.warning(f"代理 {proxy} 失败次数过多 ({failure_count})，跳过")
            self._current_proxy_index = (self._current_proxy_index + 1) % len(self._proxy_pool)
            attempts += 1

        raise ValueError("所有代理都已达到最大失败次数")

    def _mark_proxy_failed(self, proxy: str) -> None:
        """标记代理失败

        Args:
            proxy: 失败的代理地址
        """
        if proxy:
            current_count = self._proxy_failure_count.get(proxy, 0)
            self._proxy_failure_count[proxy] = current_count + 1
            logger.warning(f"代理 {proxy} 失败次数: {current_count + 1}/{self._max_proxy_failures}")
