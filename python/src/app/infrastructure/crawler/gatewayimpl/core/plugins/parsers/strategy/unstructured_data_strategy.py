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
import logging
from typing import TYPE_CHECKING, Any

from app.core.container.annotations import Component
from infrastructure.crawler.gatewayimpl.core.plugins.parsers import ParserStrategyI

if TYPE_CHECKING:
    from infrastructure.crawler.gatewayimpl.core.dto import CrawlerResponseData

logger = logging.getLogger(__name__)


@Component
class UnstructuredDataParserStrategy(ParserStrategyI):
    """非结构化数据解析器
    
    Notes：
        - 负责解析 JSON、CSV、PDF、Excel、Image 等文件
        - 将原始字节流转换为结构化数据
        - 支持 JSONPath 查询和嵌套字典展平
    """

    def __init__(self, config: Any = None):
        pass

    @property
    def strategy_name(self) -> str:
        return "unstructured_data"

    def parse(self, response: "CrawlerResponseData", **kwargs: Any) -> list[dict[str, Any]]:
        """解析非结构化数据
        
        Args:
            response: 响应数据对象
            **kwargs: 额外参数
                - json_path: JSONPath 表达式
                - flatten: 是否展平嵌套结构
                - ocr_language: OCR 语言（默认 eng）
                - excel_sheet: Excel 工作表名称或索引
                
        Returns:
            提取的结构化数据列表
        """
        payload = response.payload
        if not payload or "content" not in payload:
            return []

        content = payload["content"]
        file_type = payload.get("file_type", "auto")
        
        if file_type == "auto":
            file_type = self._detect_file_type_from_content(content)

        # 提取解析所需的额外参数
        json_path = kwargs.get("json_path")
        flatten = kwargs.get("flatten", False)
        ocr_language = kwargs.get("ocr_language", "eng")
        excel_sheet = kwargs.get("excel_sheet")

        try:
            if file_type == "json":
                parsed = self._parse_json(content, json_path, flatten)
            elif file_type == "csv":
                parsed = self._parse_csv(content)
            elif file_type == "pdf":
                parsed = self._parse_pdf(content)
            elif file_type == "excel":
                parsed = self._parse_excel(content, excel_sheet)
            elif file_type == "image":
                parsed = self._parse_image(content, ocr_language)
            else:
                logger.warning(f"不支持的文件类型: {file_type}")
                return [{"raw_data": str(content)}]
            
            # 统一转换为列表格式
            if isinstance(parsed, list):
                return parsed
            else:
                return [parsed] if parsed else []
        except Exception as exc:
            logger.error(f"解析失败: {exc}", exc_info=True)
            return [{"error": str(exc), "type": file_type}]

    def _parse_json(self, content: bytes, json_path: str | None, flatten: bool) -> list[dict[str, Any]]:
        """解析 JSON 数据"""
        try:
            data = json.loads(content.decode("utf-8"))
        except json.JSONDecodeError as exc:
            raise ValueError(f"JSON 解析失败: {exc}") from exc

        if isinstance(data, list):
            data_list = data
        elif isinstance(data, dict):
            if json_path:
                data_list = self._extract_by_jsonpath(data, json_path)
            else:
                data_list = [data]
        else:
            return []

        records = []
        for item in data_list:
            if flatten and isinstance(item, dict):
                item = self._flatten_dict(item)
            records.append(item)

        return records

    def _flatten_dict(self, d: dict, parent_key: str = "", sep: str = ".") -> dict:
        """展平嵌套字典"""
        items = []
        for k, v in d.items():
            new_key = f"{parent_key}{sep}{k}" if parent_key else k
            if isinstance(v, dict):
                items.extend(self._flatten_dict(v, new_key, sep).items())
            else:
                items.append((new_key, v))
        return dict(items)

    @staticmethod
    def _detect_file_type_from_content(content: bytes) -> str:
        """从内容检测文件类型"""
        if content.startswith(b'{') or content.startswith(b'['):
            try:
                json.loads(content)
                return "json"
            except (json.JSONDecodeError, TypeError):
                pass
        elif content.startswith(b'%PDF'):
            return "pdf"
        try:
            text = content.decode('utf-8', errors='ignore')
            if ',' in text and '\n' in text:
                return "csv"
        except (UnicodeDecodeError, ValueError):
            pass
        return "unknown"

    @staticmethod
    def _extract_by_jsonpath(data: dict, json_path: str) -> list[Any]:
        """使用 JSONPath 提取数据"""
        keys = json_path.strip("$.[]").split(".")
        current = data

        for key in keys:
            if isinstance(current, dict):
                current = current.get(key, {})
            else:
                return []

        if isinstance(current, list):
            return current
        else:
            return [current]


    @staticmethod
    def _parse_csv(content: bytes) -> list[dict[str, Any]]:
        """解析 CSV 文件"""
        try:
            import csv
            import io
            
            text = content.decode("utf-8")
            reader = csv.DictReader(io.StringIO(text))
            return list(reader)
        except Exception as exc:
            raise ValueError(f"CSV 解析失败: {exc}") from exc

    @staticmethod
    def _parse_pdf(content: bytes) -> dict[str, Any]:
        """解析 PDF 文件"""
        try:
            import pdfplumber
            import io
            
            with pdfplumber.open(io.BytesIO(content)) as pdf:
                pages = []
                for page in pdf.pages:
                    text = page.extract_text()
                    if text:
                        pages.append(text)
                
                return {
                    "type": "pdf",
                    "pages": len(pdf.pages),
                    "text": "\n".join(pages),
                }
        except ImportError:
            logger.warning("pdfplumber 未安装，无法解析 PDF。请运行: pip install pdfplumber")
            return {"type": "pdf", "error": "pdfplumber not installed"}
        except Exception as exc:
            raise ValueError(f"PDF 解析失败: {exc}") from exc

    @staticmethod
    def _parse_excel(content: bytes, sheet: str | int | None = None) -> list[dict[str, Any]]:
        """解析 Excel 文件"""
        try:
            import openpyxl
            import io
            
            workbook = openpyxl.load_workbook(filename=io.BytesIO(content), read_only=True, data_only=True)
            
            # 选择工作表
            if sheet is None:
                ws = workbook.active
            elif isinstance(sheet, int):
                ws = workbook.worksheets[sheet]
            else:
                ws = workbook[sheet]
            
            if ws is None:
                raise ValueError(f"无法找到指定的工作表: {sheet}")
            
            # 转换为字典列表
            rows = []
            headers = None
            for row in ws.iter_rows(values_only=True):
                if headers is None:
                    headers = [f"col_{i}" for i in range(len(row))]
                else:
                    rows.append(dict(zip(headers, row)))
            
            return rows
        except ImportError:
            logger.warning("openpyxl 未安装，无法解析 Excel。请运行: pip install openpyxl")
            return [{"type": "excel", "error": "openpyxl not installed"}]
        except Exception as exc:
            raise ValueError(f"Excel 解析失败: {exc}") from exc

    @staticmethod
    def _parse_image(content: bytes, ocr_language: str = "eng") -> dict[str, Any]:
        """解析图片文件 (OCR)"""
        try:
            import pytesseract
            from PIL import Image
            import io
            
            image = Image.open(io.BytesIO(content))
            text = pytesseract.image_to_string(image, lang=ocr_language)
            
            return {
                "type": "image",
                "text": text,
                "language": ocr_language,
            }
        except ImportError:
            logger.warning("pytesseract 或 PIL 未安装，无法进行 OCR。请运行: pip install pytesseract pillow")
            return {"type": "image", "error": "pytesseract not installed"}
        except Exception as exc:
            raise ValueError(f"OCR 识别失败: {exc}") from exc
