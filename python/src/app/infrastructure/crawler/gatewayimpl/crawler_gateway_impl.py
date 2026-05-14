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

import time
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path
from typing import Any, Dict

from fastapi.logger import logger

from app.common.config import CrawlerConfig
from app.core.container.annotations import Component
from app.core.container.decorators import RequiredArgsConstructor
from app.domain.crawler.gateway import CrawlerGateway
from app.domain.crawler.model import CrawlerEntity
from infrastructure.crawler.gatewayimpl.core.chain import CrawlerPipelineHandler
from infrastructure.crawler.gatewayimpl.core.dto import CrawlerRecord, CrawlerRequestContext
from infrastructure.crawler.gatewayimpl.core.handler import (
    RobotsCheckHandler,
    UserAgentRotationHandler,
    CoreExecutionHandler,
    RecoveryHandler
)
from infrastructure.crawler.gatewayimpl.core.log import CrawlerLogging
from infrastructure.crawler.gatewayimpl.core.plugins.concurrency import ConcurrencyControlStrategyI
from infrastructure.crawler.gatewayimpl.core.plugins.fetchers import FetcherStrategyI
from infrastructure.crawler.gatewayimpl.core.plugins.parsers import ParserStrategyI
from infrastructure.crawler.gatewayimpl.core.plugins.rate import RateControlStrategyI
from infrastructure.crawler.gatewayimpl.core.plugins.recovery import ExceptionRecoveryStrategyI
from infrastructure.crawler.gatewayimpl.core.plugins.robots import RobotsProtocolStrategyI
from infrastructure.crawler.gatewayimpl.core.plugins.useragent import UserAgentStrategyI
from infrastructure.crawler.gatewayimpl.core.storage import FileStorage


@Component
@RequiredArgsConstructor
class CrawlerGatewayImpl(CrawlerGateway):
    """爬虫网关实现
    
    Notes：
        - 实现 CrawlerGateway 接口
        - 负责任务的并发调度与结果聚合
        - 组装并启动责任链流水线
    """

    # 日志插件
    logging_plugin: CrawlerLogging

    # 存储插件
    file_storage: FileStorage

    def execute(self, entity: CrawlerEntity) -> Dict[str, Any]:
        """执行爬虫任务
        
        Args:
            entity: 爬虫领域实体
            
        Returns:
            执行结果摘要
        """
        logger.info("开始执行爬虫任务: project=%s, tasks=%d", entity.project, len(entity.tasks))

        try:
            # 串行执行各个任务
            all_records: Dict[str, CrawlerRecord] = {}
            error_count = 0

            for task in entity.tasks:
                if not task.enabled:
                    continue

                records, task_errors = self._execute_task(task)
                error_count += task_errors

                for record in records:
                    all_records[record.record_id] = record

                logger.info("任务完成: %s, records=%d, errors=%d", task.name, len(records), task_errors)

            # 保存结果
            output_dir = Path(CrawlerConfig.OUTPUT_DIR)
            output_paths = self.file_storage.save_records(
                records=list(all_records.values()),
                output_dir=output_dir
            )

            # 构建摘要
            summary = {
                "status": "success",
                "records": len(all_records),
                "errors": error_count,
                "jsonl_path": str(output_paths["jsonl"]),
                "csv_path": str(output_paths["csv"]),
            }

            # 记录日志统计信息
            log_stats = self.logging_plugin.get_stats()
            logger.info(
                "[日志统计] 总请求=%d, 总错误=%d, 错误率=%.2f%%",
                log_stats["total_requests"],
                log_stats["total_errors"],
                log_stats["error_rate"] * 100
            )

            logger.info("爬虫执行完成: %s", summary)
            return summary

        except Exception as e:
            logger.error("爬虫执行失败: %s", str(e), exc_info=True)
            raise RuntimeError(f"爬虫执行失败: {str(e)}") from e

    def _execute_task(self, task: CrawlerEntity.CrawlerTaskConfig) -> tuple[
        list[CrawlerRecord], int]:
        """执行单个任务
        
        Args:
            task: 当前要执行的任务配置
            
        Returns:
            (记录列表, 错误数量)
        """
        task_records: list[CrawlerRecord] = []
        errors = 0
        pages = list(range(task.fetch.pages.start, task.fetch.pages.end + 1))

        # 重置日志统计
        self.logging_plugin.reset_stats()

        # 动态选择并实例化策略
        fetcher_strategy_name = task.fetch.strategy_name or "static_page"
        parser_strategy_name = task.parse.strategy_name or "static_html"

        logger.info("任务 [%s] 策略选择: fetcher=%s, parser=%s", task.name, fetcher_strategy_name, parser_strategy_name)

        fetcher = FetcherStrategyI.get_strategy(fetcher_strategy_name, config=task.fetch)
        parser = ParserStrategyI.get_strategy(parser_strategy_name, config=task.parse)

        concurrency_control = ConcurrencyControlStrategyI.get_strategy(
            task.concurrency.strategy_name or ("adaptive" if task.concurrency.max_concurrency == 0 else "fixed"),
            config=task.concurrency
        )

        rate_control = RateControlStrategyI.get_strategy(
            task.rate.strategy_name or ("fixed_delay" if task.rate.base_delay > 0 else "adaptive_delay"),
            config=task.rate
        )

        exception_recovery = ExceptionRecoveryStrategyI.get_strategy(
            task.recovery.strategy_name or "retry",
            config=task.recovery
        )

        robots_strategy = RobotsProtocolStrategyI.get_strategy(
            task.robots.strategy_name or "smart",
            config=task.robots
        )

        ua_strategy = UserAgentStrategyI.get_strategy(
            task.useragent.strategy_name or "random_rotation",
            config=task.useragent
        )

        # 组装责任链
        head_handler = self._build_pipeline(
            task=task,
            fetcher=fetcher,
            parser=parser,
            rate_control=rate_control,
            recovery=exception_recovery,
            robots=robots_strategy,
            ua=ua_strategy,
        )

        # 分批并发执行
        index = 0
        while index < len(pages):
            batch_size = concurrency_control.get_current_concurrency()
            page_batch = pages[index:index + batch_size]
            index += batch_size

            # 执行批次并获取结果
            batch_records, batch_errors, batch_latencies = self._execute_batch(
                task=task,
                page_batch=page_batch,
                head_handler=head_handler,
                logging_plugin=self.logging_plugin,
            )

            task_records.extend(batch_records)
            errors += batch_errors

            # 批次结束后，根据统计数据动态调整并发
            avg_latency = sum(batch_latencies) / len(batch_latencies) if batch_latencies else 2.5
            error_rate = batch_errors / max(len(page_batch), 1)
            concurrency_control.update_concurrency({
                "avg_latency": avg_latency,
                "error_rate": error_rate,
            })

            # 批次间的速率控制
            time.sleep(rate_control.get_delay_seconds())

        return task_records, errors

    @staticmethod
    def _execute_batch(
            task: CrawlerEntity.CrawlerTaskConfig,
            page_batch: list[int],
            head_handler: CrawlerPipelineHandler,
            logging_plugin: CrawlerLogging,
    ) -> tuple[list[CrawlerRecord], int, list[float]]:
        """执行一个批次的页面抓取
        
        Args:
            task: 任务配置
            page_batch: 页码批次
            head_handler: 责任链头处理器
            
        Returns:
            (记录列表, 错误数量, 延迟列表)
        """
        import hashlib
        from datetime import datetime, timezone

        batch_records: list[CrawlerRecord] = []
        batch_errors = 0
        batch_latencies: list[float] = []

        with ThreadPoolExecutor(max_workers=len(page_batch)) as pool:
            # 提交所有任务
            futures = {}
            for page in page_batch:
                url = task.fetch.url_template.format(page=page)

                # 创建请求上下文
                request_ctx = CrawlerRequestContext(
                    task_name=task.name,
                    url=url,
                    headers=dict(task.headers),
                    timeout_seconds=task.fetch.timeout_seconds,
                )

                # 记录请求前日志
                logging_plugin.log_before_request(request_ctx)

                # 创建页面追踪器
                page_tracker = {
                    'page': page,
                    'url': url,
                    'task_source': task.source,
                    'generate_record_id': lambda t, src=task.source, u=url:
                    hashlib.md5(f"{src}|{u}|{t}".encode("utf-8")).hexdigest(),
                    'create_record': lambda rid, d, src=task.source, u=url:
                    CrawlerRecord.create_from_selectors(
                        record_id=rid,
                        source=src,
                        url=u,
                        crawl_time=datetime.now(timezone.utc).isoformat(),
                        extracted_data=d,
                    ),
                }

                future = pool.submit(head_handler.handle, request_ctx)
                futures[future] = page_tracker

            # 收集结果
            for future in as_completed(futures):
                page_tracker = futures[future]
                try:
                    response = future.result()
                    if response:
                        # 记录响应后日志
                        # 需要构建一个临时的 CrawlerRequestContext 用于日志记录
                        temp_ctx = CrawlerRequestContext(
                            task_name=task.name,
                            url=page_tracker['url'],
                            headers={},
                            timeout_seconds=task.fetch.timeout_seconds,
                        )
                        logging_plugin.log_after_response(temp_ctx, response)

                        batch_latencies.append(response.elapsed_seconds)
                        records_data = getattr(response, 'parsed_records', [])
                        logger.debug("页面 %d 解析结果数量: %d", page_tracker['page'], len(records_data))

                        # 转换为记录对象
                        for data in records_data:
                            title = data.get("title", "")
                            record_id = page_tracker['generate_record_id'](title)
                            record = page_tracker['create_record'](record_id, data)
                            batch_records.append(record)
                    else:
                        logger.warning("页面 %d 执行返回 None", page_tracker['page'])
                        batch_errors += 1
                except Exception as exc:
                    # 记录异常日志
                    temp_ctx = CrawlerRequestContext(
                        task_name=task.name,
                        url=page_tracker['url'],
                        headers={},
                        timeout_seconds=task.fetch.timeout_seconds,
                    )
                    logging_plugin.log_exception(temp_ctx, exc)

                    logger.error("task=%s page=%s 执行失败: %s", task.name, page_tracker['page'], exc)
                    batch_errors += 1

        return batch_records, batch_errors, batch_latencies

    @staticmethod
    def _build_pipeline(
            task: CrawlerEntity.CrawlerTaskConfig,
            fetcher: FetcherStrategyI,
            parser: ParserStrategyI,
            rate_control: RateControlStrategyI,
            recovery: ExceptionRecoveryStrategyI,
            robots: RobotsProtocolStrategyI,
            ua: UserAgentStrategyI,
    ) -> CrawlerPipelineHandler:
        """构建爬虫执行责任链

        Notes：
            - RobotsCheckHandler: 检查 Robots 协议
            - UserAgentRotationHandler: 设置 User-Agent
            - RecoveryHandler: 异常恢复管理
            - CoreExecutionHandler: 核心执行（速率控制 + 抓取 + 解析）
        """
        # 创建处理器
        ua_handler = UserAgentRotationHandler(ua)
        robots_handler = RobotsCheckHandler(robots)
        recovery_handler = RecoveryHandler(recovery, task)
        core_handler = CoreExecutionHandler(fetcher, parser, rate_control, task)

        ua_handler.set_next(robots_handler)
        robots_handler.set_next(recovery_handler)
        recovery_handler.set_next(core_handler)

        return ua_handler
