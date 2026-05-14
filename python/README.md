# Lersosa 智能爬虫框架

[![Star](https://img.shields.io/badge/Star-GitHub-yellow.svg)](https://github.com/Leyramu/Lersosa)
[![Version](https://img.shields.io/badge/Version-v1.0.0-success.svg)](https://github.com/Leyramu/Lersosa)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/Leyramu/Lersosa/blob/master/LICENSE)
[![Python](https://img.shields.io/badge/Python-3.10+-blue.svg)]()
[![FastAPI](https://img.shields.io/badge/FastAPI-0.136.1-green.svg)]()
[![Build Status](https://img.shields.io/badge/Build-Passing-success.svg)](https://github.com/Leyramu/Lersosa/actions)

## 1. 概述

Lersosa 是一个基于 **DDD（领域驱动设计）** + **COLA 架构** + **依赖注入容器** 的智能爬虫框架，旨在简化合规数据采集、策略化爬取和结构化数据提取的过程。框架采用纯分层架构设计，通过策略模式实现细粒度的采集能力插件化，提供高效、灵活且可维护的开发体验。

## 2. 框架介绍

该框架归属于 Lersosa 的副产品，其核心理念是"合规优先、策略驱动、分层解耦"，通过集成多种采集策略和自动化依赖管理，实现跨场景、跨目标站点的复杂数据采集应用开发。

## 3. 框架特点

1. **合规优先**：内置 robots.txt 检查、速率限制、User-Agent 轮换等合规机制
2. **策略插件化**：通过策略模式实现 Fetcher、Parser、Rate Control 等能力的灵活切换
3. **依赖注入**：自定义 IOC 容器自动管理组件生命周期，减少样板代码
4. **分层清晰**：严格遵循 Adapter/Application/Domain/Infrastructure 六层架构
5. **配置驱动**：通过 JSON 配置文件声明采集任务，无需修改代码即可调整策略
6. **异步高性能**：基于 FastAPI + asyncio 实现高并发采集能力

## 4. 应用场景

1. **企业级数据采集中台** - 为多个业务系统提供统一采集、解析、清洗、入库能力
2. **竞品监控系统** - 定时采集竞品价格、库存、评价等关键数据
3. **舆情分析平台** - 从新闻网站、社交媒体采集公开信息进行情感分析
4. **学术研究数据采集** - 合规采集公开学术资源、专利文献等
5. **多源数据融合** - 从不同来源采集数据并进行结构化整合

## 5. 特点亮点

1. **DDD+COLA 架构**：清晰的领域边界，业务逻辑与基础设施完全解耦
2. **IOC 容器管理**：类似 Spring 的依赖注入体验，@Component 注解自动注册
3. **策略模式实践**：7 大类策略插件（Fetcher/Parser/Concurrency/Rate/Recovery/Robots/UserAgent）
4. **统一响应规范**：标准化的 API 响应结构，便于前后端协同
5. **异常恢复机制**：重试、熔断、降级、快照等多种恢复策略
6. **可扩展性强**：新增策略只需实现接口并添加 @Component 注解即可

## 6. 快速开始

### 环境要求

- Python >= 3.13
- Redis >= 7.0（可选，用于状态缓存）
- Playwright（可选，用于动态页面渲染）

### 安装依赖

```bash
# 克隆项目
git clone https://github.com/Leyramu/Lersosa.git
cd Lersosa

# 安装依赖
pip install -r requirements.txt

# 安装 Playwright 浏览器（如需动态渲染）
playwright install
```

### 运行项目

```bash
# 开发环境
export MODE=dev
python src/main.py

# 预发布环境
export MODE=staging
python src/main.py

# 生产环境
export MODE=prod
python src/main.py
```

### 执行采集任务

```bash
# 通过 API 执行采集任务
curl -X POST http://127.0.0.1:8000/crawler/execute \
  -H "Content-Type: application/json" \
  -d '{
    "task_name": "books_static",
    "config_path": "config/crawler_targets.json"
  }'
```

## 7. 项目结构

```
Lersosa    智能爬虫框架
├─config              配置文件目录
│  └─crawler_targets.json    采集任务配置
├─docs               文档目录
│  └─software_design_spec.md    软件设计说明书
├─src                源代码目录
│  ├─algo            算法模块（预留）
│  │  └─data         数据处理模块
│  ├─app             应用核心模块
│  │  ├─adapter      接口接入层（Adapter Layer）
│  │  │  └─web       Web 控制器
│  │  ├─application  应用服务层（Application Layer）
│  │  │  └─crawler   爬虫应用服务
│  │  │      ├─command    命令对象
│  │  │      ├─service    应用服务
│  │  │      └─state      状态管理
│  │  ├─domain       领域层（Domain Layer）
│  │  │  └─crawler    爬虫领域
│  │  │      ├─ability     领域服务
│  │  │      ├─gateway     网关接口
│  │  │      └─model       领域实体
│  │  ├─infrastructure 基础设施层（Infrastructure Layer）
│  │  │  └─crawler    爬虫基础设施
│  │  │      └─gatewayimpl  网关实现
│  │  │          └─core     核心实现
│  │  │              ├─chain      责任链
│  │  │              ├─handler    处理器
│  │  │              ├─plugins    策略插件
│  │  │              │  ├─concurrency  并发控制策略
│  │  │              │  ├─fetchers     抓取策略
│  │  │              │  ├─parsers      解析策略
│  │  │              │  ├─rate         速率控制策略
│  │  │              │  ├─recovery     异常恢复策略
│  │  │              │  ├─robots       Robots 协议策略
│  │  │              │  └─useragent    User-Agent 策略
│  │  │              ├─storage    存储实现
│  │  │              └─log        日志记录
│  │  ├─client       客户端模块（RPC/HTTP Client）
│  │  ├─common       通用模块
│  │  │  ├─config    配置管理
│  │  │  ├─domain    通用领域对象
│  │  │  ├─nacos     Nacos 集成（预留）
│  │  │  └─utils     工具类
│  │  ├─core         核心容器模块
│  │  │  ├─container IOC 容器
│  │  │  │  ├─annotations  注解定义
│  │  │  │  ├─core         容器核心
│  │  │  │  └─decorators   装饰器
│  │  │  ├─exception 异常处理
│  │  │  ├─middleware 中间件
│  │  │  ├─model     数据模型
│  │  │  ├─redis     Redis 集成
│  │  │  ├─rpc       RPC 模块（gRPC）
│  │  │  └─state     生命周期管理
│  │  ├─bootstrap.py 应用引导类
│  │  └─__init__.py  包初始化
│  ├─model           模型文件（预留）
│  ├─proto           协议定义（gRPC Proto）
│  │  └─rpc          RPC 协议
│  ├─main.py         应用入口
│  ├─.env.development  开发环境变量
│  ├─.env.staging      预发布环境变量
│  └─.env.production   生产环境变量
├─ms-playwright      Playwright 浏览器缓存
├─.venv              虚拟环境
├─requirements.txt   Python 依赖
├─Dockerfile         Docker 构建文件
├─environment.yml    Conda 环境配置
└─SUPERIOR_README.md 多语言框架总览
```

## 8. 技术栈

### 核心技术

| 组件名称 | 版本 | 核心作用 |
|---------|------|---------|
| **FastAPI** | 0.136.1 | 异步 Web 框架，提供高性能 HTTP 服务 |
| **Uvicorn** | 0.46.0 | ASGI 服务器，支持异步请求处理 |
| **Pydantic** | 2.13.3 | 数据校验与序列化，定义请求/响应模型 |
| **Playwright** | 1.58.0 | 浏览器自动化，支持动态页面渲染 |
| **BeautifulSoup4** | 4.14.3 | HTML 解析器，静态页面数据提取 |
| **Requests** | 2.33.0 | HTTP 客户端，基础请求发送 |
| **Curl-CFFI** | 0.15.0 | HTTP 客户端，TLS 指纹伪装，反爬对抗 |
| **Redis** | 5.3.1 | 缓存与状态管理，任务队列支持 |
| **CircuitBreaker** | 2.1.3 | 熔断器模式，异常恢复策略 |

### 数据处理

| 组件名称 | 版本 | 功能说明 |
|---------|------|---------|
| **NumPy** | 2.4.4 | 数值计算库 |
| **Matplotlib** | 3.10.9 | 数据可视化 |
| **Pillow** | 12.2.0 | 图像处理库 |
| **pdfplumber** | 0.11.9 | PDF 文档解析 |
| **openpyxl** | 3.1.5 | Excel 文件处理 |
| **pytesseract** | 0.3.13 | OCR 文字识别 |

### 工具库

| 组件名称 | 版本 | 功能说明 |
|---------|------|---------|
| **python-dotenv** | 1.2.2 | 环境变量加载 |
| **colorlog** | 6.10.1 | 彩色日志输出 |
| **python-multipart** | 0.0.20 | 文件上传支持 |
| **python-jose** | 3.5.0 | JWT Token 处理 |
| **protobuf** | 6.33.6 | Protocol Buffers 序列化 |
| **grpcio** | 1.80.0 | gRPC 通信框架 |
| **websockets** | 16.0 | WebSocket 支持 |

### 基础设施

| 分类 | 组件名称 | 版本 | 功能说明 |
|-----|---------|------|---------|
| **服务发现** | Nacos SDK | 2.0.11 | 服务注册与配置中心（预留） |
| **缓存** | Redis | 7.4.2 | 分布式缓存与状态管理 |
| **搜索引擎** | Elasticsearch | 8.16.0 | 全文检索与分析（预留） |
| **消息队列** | RabbitMQ | 4.0.4 | 异步任务队列（预留） |
| **云原生** | Docker | 28.0.4 | 容器化部署 |
| **云原生** | Kubernetes | 1.32.2 | 容器编排（预留） |

## 9. 核心架构

### 分层架构

```mermaid
flowchart TB
    Client[客户端] --> Adapter[Adapter 层<br/>Web Controller]
    Adapter --> Application[Application 层<br/>Command + Service + State]
    Application --> Domain[Domain 层<br/>Entity + Gateway + DomainService]
    Domain -.依赖倒置.-> Infrastructure[Infrastructure 层<br/>GatewayImpl + Strategy Plugins]
    Infrastructure --> Storage[(File System / Redis)]
    
    subgraph Core["Core Container"]
        IOC[IOC 容器<br/>BeanContainerManager]
        Annotation[@Component 注解扫描]
        DI[依赖注入]
    end
    
    Infrastructure -.自动注册.-> IOC
    Domain -.自动注入.-> IOC
    Application -.自动注入.-> IOC
    Adapter -.自动注入.-> IOC
```

### 策略模式

框架提供 7 大类策略插件，每类支持多种实现：

| 策略类型 | 接口 | 实现示例 | 配置名称 |
|---------|------|---------|---------|
| **Fetcher** | FetcherStrategyI | StaticPageFetcher, PlaywrightFetcher | `static_page`, `dynamic_render` |
| **Parser** | ParserStrategyI | HtmlParser, JsonParser, AntiScrapingParser | `static_html`, `json_api`, `anti_scraping` |
| **Concurrency** | ConcurrencyControlStrategyI | TokenBucket, Semaphore, FixedConcurrency | `token_bucket`, `semaphore`, `fixed_concurrency` |
| **Rate** | RateControlStrategyI | FixedDelay, ExponentialBackoff, SlidingWindow | `fixed_delay`, `exponential_backoff`, `sliding_window` |
| **Recovery** | ExceptionRecoveryStrategyI | RetryRecovery, CircuitBreaker, Fallback | `retry_recovery`, `circuit_breaker`, `fallback` |
| **Robots** | RobotsProtocolStrategyI | SmartRobots, DisabledRobots | `smart`, `disabled` |
| **UserAgent** | UserAgentStrategyI | RandomRotation, BrowserFingerprint | `random_rotation`, `browser_fingerprint` |

## 10. 配置示例

### 采集任务配置（config/crawler_targets.json）

```json
{
  "project": "public_content_intelligence",
  "tasks": [
    {
      "name": "books_static",
      "enabled": true,
      "source": "books.toscrape.com",
      "scenario": "static_page",
      "fetch": {
        "strategy_name": "static_page",
        "mode": "html",
        "url_template": "https://books.toscrape.com/catalogue/page-{page}.html",
        "pages": {
          "start": 1,
          "end": 12
        },
        "timeout_seconds": 15
      },
      "parse": {
        "strategy_name": "static_html",
        "selectors": {
          "item": "article.product_pod",
          "title": "h3 a",
          "price": "p.price_color",
          "rating": "p.star-rating"
        }
      },
      "concurrency": {
        "strategy_name": "fixed_concurrency",
        "max_concurrency": 5
      },
      "rate": {
        "strategy_name": "fixed_delay",
        "base_delay": 1.0
      },
      "recovery": {
        "strategy_name": "retry_recovery",
        "max_retries": 3
      },
      "robots": {
        "strategy_name": "smart",
        "crawl_delay": 0.0
      }
    }
  ]
}
```

## 11. API 文档

启动服务后访问：http://127.0.0.1:8000/docs

### 核心接口

| 接口 | 方法 | 说明 |
|-----|------|------|
| `/crawler/execute` | POST | 执行采集任务 |
| `/crawler/status/{task_id}` | GET | 查询任务状态 |
| `/crawler/result/{task_id}` | GET | 获取采集结果 |
| `/crawler/stop/{task_id}` | POST | 停止采集任务 |

### 请求示例

```bash
# 执行采集任务
curl -X POST http://127.0.0.1:8000/crawler/execute \
  -H "Content-Type: application/json" \
  -d '{
    "task_name": "books_static",
    "config_path": "config/crawler_targets.json"
  }'

# 查询任务状态
curl http://127.0.0.1:8000/crawler/status/task_20260425_001
```

## 12. 扩展开发

### 新增采集策略

1. 实现策略接口：

```python
from app.core.container import Component
from infrastructure.crawler.gatewayimpl.core.plugins.rate import RateControlStrategyI

@Component
class MyCustomRateStrategy(RateControlStrategyI):
    
    @property
    def strategy_name(self) -> str:
        return "my_custom_rate"
    
    async def wait(self, task_id: str) -> None:
        # 自定义速率控制逻辑
        await asyncio.sleep(2.0)
```

2. 在配置中使用：

```json
{
  "rate": {
    "strategy_name": "my_custom_rate"
  }
}
```

3. IOC 容器自动注册，无需手动 wiring！

### 新增业务域

1. 在 `src/app/domain/` 创建新领域目录
2. 定义 Entity、Gateway、DomainService
3. 在 `src/app/application/` 创建应用服务
4. 在 `src/app/adapter/web/` 创建 Controller
5. 使用 `@Component`、`@Service`、`@RestController` 注解自动注册

## 13. 贡献指南

我们欢迎任何形式的贡献！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 代码规范

- 遵循 PEP 8 编码规范
- 使用类型注解（Type Hints）
- 编写单元测试
- 保持分层架构清晰

## 14. 贡献者

1. 框架架构师：[Miraitowa_zcx](https://github.com/Miraitowa-zcx)
2. 核心开发者：[Miraitowa_zcx](https://github.com/Miraitowa-zcx)
3. 研发所属团队：[Leyramu Team](https://github.com/Leyramu)

## 15. 参考资料

**COLA 架构**  
`v5.0.0` | 作者：Alibaba | 协议：LGPL-2.1 license | 仓库：https://github.com/alibaba/COLA  
介绍：COLA 架构定义良好的应用结构，提供最佳应用架构实践，通过分层治理混乱的业务应用系统。

**FastAPI**  
`v0.136.1` | 作者：Sebastián Ramírez | 协议：MIT License | 仓库：https://github.com/tiangolo/fastapi  
介绍：现代、快速（高性能）的 Web 框架，基于标准 Python 类型提示构建 API。

## 16. 许可证

- Copyright (c) 2020-2026 Leyramu Group. All rights reserved.
- Licensed under the Apache License, Version 2.0

详见 [LICENSE](LICENSE) 文件。

## 17. 免责声明

The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.

By using this project, users acknowledge and agree to abide by these terms and conditions.

---

**⭐ 如果这个项目对你有帮助，请给一个 Star！**
