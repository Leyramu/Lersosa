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
import os
import time
from typing import TYPE_CHECKING, Any

from playwright.sync_api import sync_playwright

from app.common.config import CrawlerConfig
from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.fetchers import FetcherStrategyI

# 设置 Playwright 浏览器路径
os.environ["PLAYWRIGHT_BROWSERS_PATH"] = CrawlerConfig.PLAYWRIGHT_BROWSERS_PATH

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRequestContext, CrawlerResponseData

logger = logging.getLogger(__name__)


@Component
class DynamicRenderStrategy(FetcherStrategyI):
    """动态渲染页面抓取器

    Notes：
        - 使用 Playwright 浏览器自动化
        - 支持等待选择器
        - 支持错误截图
        - 可配置浏览器选项
    """

    def __init__(self, config: Any = None):
        """初始化动态渲染抓取器

        Args:
            config: 领域实体中的插件配置对象，支持以下属性：
                - headless: 是否无头模式（默认True）
        """
        if config:
            self._headless = getattr(config, 'headless', True)
        else:
            self._headless = True

    @property
    def strategy_name(self) -> str:
        return "dynamic_render"

    @staticmethod
    def _is_forbidden_page(html: str) -> bool:
        """检查页面是否被 WAF 拦截
        
        Args:
            html: 页面 HTML 内容
            
        Returns:
            True 如果页面是被拦截的 Forbidden 页面
        """
        if not html:
            return True

        # 检查常见的 WAF/拦截页面特征
        forbidden_indicators = [
            "<title>Forbidden</title>",
            "<title>403 Forbidden</title>",
            "<title>Access Denied</title>",
            'content="WAF"',
            "cloudflare",
            "captcha",
            "verify you are human",
        ]

        html_lower = html.lower()
        for indicator in forbidden_indicators:
            if indicator.lower() in html_lower:
                logger.warning(f"检测到 WAF 拦截特征: {indicator}")
                return True

        # 检查页面是否太短
        if len(html) < 500:
            logger.warning(f"页面内容过短 ({len(html)} 字符)，可能被拦截")
            return True

        return False

    def fetch(
            self, ctx: CrawlerRequestContext, **kwargs: Any
    ) -> "CrawlerResponseData":
        """抓取动态渲染页面

        Args:
            ctx: 请求上下文（包含已设置好的 User-Agent）
            **kwargs: 额外参数
                - wait_selector: CSS 选择器，等待该元素出现
                - wait_timeout: 等待超时时间（毫秒，默认 30000）
                - browser_options: 浏览器配置字典
                    - viewport: 视口大小 {width, height}（默认 1920x1080）

        Returns:
            响应数据对象

        Raises:
            RuntimeError: 当抓取失败时
        """
        from infrastructure.crawler.gatewayimpl.core.dto import CrawlerResponseData

        wait_selector: str | None = kwargs.get("wait_selector")
        wait_timeout: int = kwargs.get("wait_timeout", 30000)
        browser_options: dict[str, Any] = kwargs.get("browser_options", {})

        # 解析浏览器选项
        viewport = browser_options.get("viewport", {"width": 1920, "height": 1080})

        # User-Agent
        user_agent = ctx.headers.get("User-Agent", "")

        start_time = time.time()

        try:
            with sync_playwright() as p:
                browser = p.chromium.launch(headless=self._headless)
                context = browser.new_context(
                    user_agent=user_agent,
                    viewport=viewport,
                )
                page = context.new_page()

                try:
                    # 设置额外 headers
                    if ctx.headers:
                        page.set_extra_http_headers(ctx.headers)

                    # 导航到页面
                    page.goto(ctx.url, timeout=ctx.timeout_seconds * 1000)

                    # 等待特定元素
                    if wait_selector is not None:
                        try:
                            page.wait_for_selector(wait_selector, timeout=wait_timeout)
                        except TimeoutError:
                            logger.warning(f"等待选择器 '{wait_selector}' 超时，但继续获取页面内容")

                    # 获取页面内容
                    html = page.content()

                    # 检查页面是否被拦截（WAF/Forbidden）
                    if self._is_forbidden_page(html):
                        error_msg = f"页面被 WAF 拦截，返回 Forbidden 页面: {ctx.url}"
                        logger.error(error_msg)
                        raise ConnectionError(error_msg)

                    elapsed = time.time() - start_time

                    return CrawlerResponseData(
                        url=ctx.url,
                        status_code=200,
                        elapsed_seconds=elapsed,
                        text=html,
                    )
                except Exception as page_exc:
                    # 页面级异常
                    raise page_exc
                finally:
                    page.close()
                    context.close()
                    browser.close()

        except Exception as exc:
            if isinstance(exc, ConnectionError):
                raise
            # 检查是否是超时异常
            exc_type_name = type(exc).__name__
            if 'Timeout' in exc_type_name or 'timeout' in str(exc).lower():
                raise
            raise RuntimeError(f"动态渲染抓取失败: {exc}") from exc
