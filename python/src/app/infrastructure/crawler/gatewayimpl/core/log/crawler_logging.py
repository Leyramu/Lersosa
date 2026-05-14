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


"""爬虫日志记录插件。

提供完整的请求生命周期日志记录：
- 请求前：记录 URL、超时、代理等信息
- 响应后：记录状态码、延迟、性能评估
- 异常时：记录错误详情和建议
"""

from __future__ import annotations

import logging
from typing import TYPE_CHECKING

from app.core.container.annotations import Component

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRequestContext, CrawlerResponseData

from fastapi.logger import logger


@Component
class CrawlerLogging:
    """爬虫日志记录插件
    
    Notes：
    - 记录请求生命周期日志
    - 统计请求和错误数量
    - 提供性能评估和错误建议
    """

    def __init__(self):
        """初始化日志插件"""
        self._request_count = 0
        self._error_count = 0

    def log_before_request(self, ctx: "CrawlerRequestContext") -> None:
        """记录请求前日志
        
        Args:
            ctx: 请求上下文
        """
        try:
            self._request_count += 1

            # 构建详细的请求日志
            log_parts = [
                f"[爬取请求 #{self._request_count}]",
                f"task={ctx.task_name}",
                f"url={ctx.url}",
            ]

            # 添加超时信息
            if hasattr(ctx, 'timeout_seconds') and ctx.timeout_seconds:
                log_parts.append(f"timeout={ctx.timeout_seconds}s")

            # 添加代理信息
            if hasattr(ctx, 'proxies') and ctx.proxies:
                try:
                    proxy_info = str(ctx.proxies.get('http', '') or ctx.proxies.get('https', ''))
                    if proxy_info:
                        # 隐藏代理认证信息
                        if '@' in proxy_info:
                            proxy_info = proxy_info.split('@')[1]
                        log_parts.append(f"proxy={proxy_info}")
                except Exception:
                    pass

            logger.info(" ".join(log_parts))

            # 如果需要更详细的调试信息
            if logger.isEnabledFor(logging.DEBUG):
                debug_info = {
                    "task_name": ctx.task_name,
                    "url": ctx.url,
                    "timeout": getattr(ctx, 'timeout_seconds', None),
                    "headers_count": len(getattr(ctx, 'headers', {})),
                }
                logger.debug("[爬取请求详情] %s", debug_info)
        except Exception as exc:  # noqa: BLE001
            logger.error(f"[日志插件错误] log_before_request 失败: {exc}")

    def log_after_response(self, ctx: "CrawlerRequestContext", resp: "CrawlerResponseData") -> None:
        """记录响应后日志
        
        Args:
            ctx: 请求上下文
            resp: 响应数据
        """
        try:
            # 计算性能等级
            latency = resp.elapsed_seconds
            if latency < 0.5:
                perf_level = "优秀"
            elif latency < 1.5:
                perf_level = "良好"
            elif latency < 3.0:
                perf_level = "一般"
            else:
                perf_level = "较慢"

            # 构建响应日志
            log_parts = [
                f"[爬取响应]",
                f"task={ctx.task_name}",
                f"status={resp.status_code}",
                f"latency={latency:.3f}s",
                f"perf={perf_level}",
            ]

            # 添加响应数据大小
            if hasattr(resp, 'text') and resp.text:
                try:
                    size_kb = len(resp.text.encode('utf-8')) / 1024
                    log_parts.append(f"size={size_kb:.1f}KB")
                except Exception:
                    pass

            # 添加内容类型
            if hasattr(resp, 'headers') and resp.headers:
                try:
                    content_type = resp.headers.get('content-type', '')
                    if content_type:
                        # 简化内容类型显示
                        ct_short = content_type.split(';')[0]
                        log_parts.append(f"type={ct_short}")
                except Exception:
                    pass

            logger.info(" ".join(log_parts))

            # 记录慢请求警告
            if latency > 5.0:
                logger.warning(
                    "[慢请求警告] task=%s url=%s latency=%.3fs",
                    ctx.task_name,
                    ctx.url,
                    latency
                )

            # 记录错误状态码
            if resp.status_code >= 400:
                self._error_count += 1
                logger.warning(
                    "[HTTP错误] task=%s status=%d url=%s",
                    ctx.task_name,
                    resp.status_code,
                    ctx.url
                )
        except Exception as exc:  # noqa: BLE001
            logger.error(f"[日志插件错误] log_after_response 失败: {exc}")

    def log_exception(self, ctx: "CrawlerRequestContext", exc: Exception) -> None:
        """记录异常日志
        
        Args:
            ctx: 请求上下文
            exc: 异常对象
        """
        try:
            self._error_count += 1

            # 获取异常类型名称
            exc_type = type(exc).__name__
            exc_msg = str(exc)

            # 构建错误日志
            logger.error(
                "[爬取错误 #%d] task=%s url=%s type=%s error=%s",
                self._error_count,
                ctx.task_name,
                ctx.url,
                exc_type,
                exc_msg
            )

            # 在 DEBUG 级别记录完整堆栈
            if logger.isEnabledFor(logging.DEBUG):
                logger.debug(
                    "[爬取错误详情] task=%s url=%s exception=%s",
                    ctx.task_name,
                    ctx.url,
                    exc,
                    exc_info=True  # 包含完整堆栈跟踪
                )

            # 根据异常类型提供建议
            self._log_error_suggestion(ctx, exc)
        except Exception as log_exc:  # noqa: BLE001
            logger.error(f"[日志插件错误] log_exception 失败: {log_exc}")

    def _log_error_suggestion(self, ctx: "CrawlerRequestContext", exc: Exception) -> None:
        """根据异常类型记录解决建议
        
        Args:
            ctx: 请求上下文
            exc: 异常对象
        """
        exc_type = type(exc).__name__.lower()
        exc_msg = str(exc).lower()

        # 超时错误
        if 'timeout' in exc_type or 'timeout' in exc_msg:
            logger.warning(
                "[建议] 请求超时，考虑增加 timeout_seconds 或检查网络连接。task=%s",
                ctx.task_name
            )

        # 连接错误
        elif 'connection' in exc_type or 'connect' in exc_msg:
            logger.warning(
                "[建议] 连接失败，检查目标网站是否可访问或代理配置是否正确。task=%s",
                ctx.task_name
            )

        # HTTP 错误
        elif 'http' in exc_type or 'status' in exc_msg:
            logger.warning(
                "[建议] HTTP 错误，检查 URL 是否正确或目标网站是否需要特殊处理。task=%s",
                ctx.task_name
            )

        # 解析错误
        elif 'parse' in exc_type or 'decode' in exc_msg:
            logger.warning(
                "[建议] 解析错误，检查响应编码或内容格式是否符合预期。task=%s",
                ctx.task_name
            )

    def get_stats(self) -> dict:
        """获取日志统计信息
        
        Returns:
            包含请求计数和错误计数的字典
        """
        return {
            "total_requests": self._request_count,
            "total_errors": self._error_count,
            "error_rate": (
                self._error_count / self._request_count
                if self._request_count > 0
                else 0.0
            ),
        }

    def reset_stats(self) -> None:
        """重置统计数据"""
        self._request_count = 0
        self._error_count = 0
