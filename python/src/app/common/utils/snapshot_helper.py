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

import json
import time
from pathlib import Path
from typing import Any

from fastapi.logger import logger

from app.common.config import CrawlerConfig


class SnapshotHelper:
    """快照保存助手

        统一处理异常快照的保存逻辑，支持
    
    Notes：
        - 自动创建快照目录
        - 智能截断过大的 HTML 内容
        - 可序列化的上下文信息保存
        - 安全的文件名生成
    """

    @staticmethod
    def save_snapshot(
        context: dict[str, Any],
        error: str,
        snapshot_type: str = "error",
        extra_data: dict[str, Any] | None = None,
    ) -> str:
        """保存异常快照到文件
        
        Args:
            context: 异常上下文，包含 url、task_name、headers、html 等信息
            error: 错误信息
            snapshot_type: 快照类型前缀（如 "retry_error", "circuit_breaker", "fallback"）
            extra_data: 额外的快照数据（可选）
            
        Returns:
            快照文件路径
        """
        snapshot_dir = Path(CrawlerConfig.SNAPSHOT_DIR)
        snapshot_dir.mkdir(parents=True, exist_ok=True)

        timestamp = int(time.time())
        task_name = context.get("task_name", "unknown")
        url = context.get("url", "unknown")

        # 生成安全的文件名
        safe_url = url
        for char in ['/', ':', '?', '*', '"', '<', '>', '|', '\\']:
            safe_url = safe_url.replace(char, '_')
        safe_url = safe_url[:50]  # 限制长度
        filename = f"{snapshot_type}_{task_name}_{safe_url}_{timestamp}.json"
        filepath = snapshot_dir / filename

        # 构建快照数据
        snapshot_data = {
            "timestamp": time.strftime("%Y-%m-%d %H:%M:%S"),
            "error": error,
            "task_name": task_name,
            "url": url,
        }

        # 添加额外数据
        if extra_data:
            snapshot_data.update(extra_data)

        # 保存 Headers
        if "headers" in context:
            snapshot_data["headers"] = context["headers"]

        # 保存 HTML
        if "html" in context:
            html_content = context["html"]
            if len(html_content) > 100000:
                snapshot_data["html_truncated"] = True
                snapshot_data["html_preview"] = html_content[:10000]
                snapshot_data["html_size"] = len(html_content)
            else:
                snapshot_data["html"] = html_content

        # 保存其他可序列化的上下文信息
        for key, value in context.items():
            if key not in ["headers", "html"]:
                try:
                    json.dumps(value)  # 测试是否可序列化
                    snapshot_data[key] = value
                except (TypeError, ValueError):
                    # 不可序列化则转为字符串
                    snapshot_data[f"{key}_str"] = str(value)

        # 写入文件
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(snapshot_data, f, ensure_ascii=False, indent=2, default=str)

        logger.info(f"异常快照已保存: {filepath}")
        return str(filepath)

    @staticmethod
    def save_redirect_snapshot(
        url: str,
        task_name: str,
        status_code: int,
        redirect_location: str,
        headers: dict[str, str],
        html_content: str,
    ) -> tuple[str, str]:
        """保存重定向快照
        
        Args:
            url: 原始URL
            task_name: 任务名称
            status_code: HTTP状态码
            redirect_location: 重定向目标
            headers: 响应头
            html_content: HTML内容
            
        Returns:
            (html_path, metadata_path) 元组
        """
        snapshot_dir = Path(CrawlerConfig.SNAPSHOT_DIR)
        snapshot_dir.mkdir(parents=True, exist_ok=True)

        timestamp = int(time.time())
        safe_task_name = task_name.replace("/", "_").replace(":", "_")
        base_filename = f"redirect_{safe_task_name}_{timestamp}"

        # 保存 HTML 内容
        html_path = snapshot_dir / f"{base_filename}.html"
        with open(html_path, 'w', encoding='utf-8') as f:
            f.write(f"<!-- 重定向调试快照 -->\n")
            f.write(f"<!-- 原始 URL: {url} -->\n")
            f.write(f"<!-- 状态码: {status_code} -->\n")
            f.write(f"<!-- 重定向目标: {redirect_location} -->\n")
            f.write(f"<!-- 时间戳: {timestamp} -->\n\n")
            f.write(html_content)

        # 保存元数据 JSON
        metadata = {
            "url": url,
            "status_code": status_code,
            "redirect_location": redirect_location,
            "headers": headers,
            "timestamp": timestamp,
            "task_name": task_name,
        }
        metadata_path = snapshot_dir / f"{base_filename}.json"
        with open(metadata_path, 'w', encoding='utf-8') as f:
            json.dump(metadata, f, ensure_ascii=False, indent=2)

        logger.info(f"重定向快照已保存:")
        logger.info(f"  - HTML: {html_path}")
        logger.info(f"  - Metadata: {metadata_path}")

        return str(html_path), str(metadata_path)
