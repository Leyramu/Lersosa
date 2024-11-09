## 1. 概述

本项目是一个开源的、跨平台的开发框架，旨在简化构建复杂应用程序的过程，支持多种编程语言和库的无缝集成。它的核心理念是让你能够在同一个项目中自由地使用
Java, Python, Go 等不同语言的优势，提供了一种高效且灵活的开发体验。

## 2. 框架介绍

该框架的核心思想是使用一种或多种编程语言，通过集成各种库，实现跨平台、跨语言、跨数据库的复杂应用程序开发。

## 3. 框架特点

1. 跨平台：该框架支持多种操作系统，可以轻松实现跨平台的应用开发。
2. 跨语言：该框架支持多种编程语言，可以轻松实现跨语言的应用开发。
3. 跨数据库：该框架支持多种数据库，可以轻松跨数据库的应用开发。

## 4. 应用场景

1. 微服务架构 - 在微服务环境中，你可以根据每个服务的需求选择最合适的语言。
2. 数据科学与机器学习 - 结合 Python 的数据处理能力和 Java 或 Go 的高性能计算。
3. 前端与后端集成 - 使用 JavaScript/TypeScript 进行前端开发，Java 或 Go 处理后端逻辑。
4. 实验性项目 - 快速尝试新语言或库，而不影响整个项目的结构。

## 5. 特点亮点

1. 多语言融合：允许你在同一项目中使用多种语言，充分利用每种语言的特点。
2. 高效运行时：通过优化的 IR 层和 VM，保证了代码执行的效率。
3. 组件化设计：可重用的组件使代码组织更加清晰，易于维护。
4. 统一的 API：无论使用哪种语言，都能享受一致的开发体验。
5. 跨平台支持：可在 Linux, macOS, Windows 等操作系统上运行。

## 6. 运行

1. 运行前请先安装好多语言开发框架的依赖库。
2. Python 算法端 Demo 运行使用 Python-3.11.0 测试通过
3. Java 后端 Demo 运行使用 Java-22.0.2 测试通过
4. Vue 前端 Demo 运行使用 Vite-5.4.10 测试通过

## 3. 框架结构树

``` tree
Lersosa    DevelopTalentRank
├─lersosa-java    Java 后端
│  ├─lersosa-api    微服务 API 模块
│  │  ├─lersosa-api-bom    API 依赖模块
│  │  ├─lersosa-api-resource    资源模块
│  │  ├─lersosa-api-workflow    工作流模块
│  │  └─lersosa-api-system    系统模块
│  ├─lersosa-gateway    微服务网关模块
│  │  └─lersosa-gateway-api    API 网关模块
│  ├─lersosa-common    微服务公共模块
│  │  ├─lersosa-common-alibaba-bom   阿里云依赖模块
│  │  ├─lersosa-common-bom    公共依赖模块
│  │  ├─lersosa-common-bus    消息总线模块
│  │  ├─lersosa-common-dependencies   业务依赖模块
│  │  ├─lersosa-common-core    核心模块
│  │  ├─lersosa-common-encrypt    加密模块
│  │  ├─lersosa-common-excel    Excel 模块
│  │  ├─lersosa-common-dict    字典模块
│  │  ├─lersosa-common-doc    接口文档模块
│  │  ├─lersosa-common-dubbo    Dubbo 模块
│  │  ├─lersosa-common-elasticsearch    Elasticsearch 模块
│  │  ├─lersosa-common-idempotent    幂等模块
│  │  ├─lersosa-common-job    定时任务模块
│  │  ├─lersosa-common-json  Json 序列化模块
│  │  ├─lersosa-common-loadbalancer    负载均衡模块
│  │  ├─lersosa-common-logstash    日志追踪模块
│  │  ├─lersosa-common-log    消息日志模块
│  │  ├─lersosa-common-mail    邮件模块
│  │  ├─lersosa-common-mybatis    MyBatis 模块
│  │  ├─lersosa-common-nacos     配置中心模块
│  │  ├─lersosa-common-ratelimiter    限流模块
│  │  ├─lersosa-common-satoken    OAuth2 模块
│  │  ├─lersosa-common-oss   对象存储模块
│  │  ├─lersosa-common-seata    Seata 分布式事务模块
│  │  ├─lersosa-common-sentinel    Sentinel 模块
│  │  ├─lersosa-common-redis    数据缓存模块
│  │  ├─lersosa-common-security    数据安全模块
│  │  ├─lersosa-common-sensitive    数据脱敏模块
│  │  ├─lersosa-common-skylog    日志收集模块
│  │  ├─lersosa-common-sms    短信模块
│  │  ├─lersosa-common-social    社交模块
│  │  ├─lersosa-common-sse    服务端事件模块
│  │  ├─lersosa-common-tenant    租户模块
│  │  ├─lersosa-common-translation    翻译模块
│  │  ├─lersosa-common-web     Web 模块
│  │  └─lersosa-common-websocket    WebSocket 模块
│  ├─lersosa-example    微服务示例模块
│  │  ├─lersosa-example-demo  示例业务模块
│  │  ├─lersosa-example-test-mq  测试消息模块
│  ├─lersosa-service    微服务业务模块
│  │  ├─lersosa-service-auth    认证中心业务模块
│  │  ├─lersosa-service-gen    代码生成模块
│  │  ├─lersosa-service-job    定时任务业务模块
│  │  ├─lersosa-service-system    系统业务模块
│  │  ├─lersosa-service-resource    资源模块
│  │  ├─lersosa-service-workflow    工作流业务模块
│  │  └─lersosa-service-openai    OpenAI 业务模块
│  ├─lersosa-visual    微服务可视化模块
│  │  ├─lersosa-visual-nacos    注册中心模块
│  │  ├─lersosa-visual-seata    分布式事务模块
│  │  ├─lersosa-visual-snailjob   任务调度模块
│  │  ├─lersosa-visual-sentinel    限流熔断模块
│  │  └─lersosa-visual-monitor    微服务监控模块
│  └─lersosa-web    微服务页面渲染模块
│      ├─lersosa-web-search    搜索渲染模块
│      └─lersosa-web-page    页面渲染模块
├─lersosa-python    Python 算法端
│  └─src    源代码目录
│       ├─algo    算法模块
│       │  └─ultimate    模型模块
│       ├─app    应用模块
│       │  ├─base    基础模块
│       │  ├─common    通用模块
│       │  │  ├─config    配置模块
│       │  │  └─nacos     配置中心模块
│       │  ├─model    模型模块
│       │  │  ├─entity    实体模块
│       │  │  └─enum     枚举模块
│       │  ├─service    服务模块
│       │  ├─controller    控制器模块
│       │  ├─core    核心模块
│       │  └─exception    异常模块
│       └─model    模型模块
└─lersosa-vite    Vite 前端
    ├─lersosa-vite-admin    前端管理层模块
    └─lersosa-vite-user    前端用户层模块
```

## 4. 技术栈

1. 算法端：Python、FastAPI、Nacos
2. 后端：Java、Spring Cloud、Spring Boot、Spring Security、Spring Data JPA、Spring Cloud Gateway、Spring Cloud OpenFeign、Spring
   Cloud Alibaba、Spring Cloud Stream、Spring Cloud Config、Spring Cloud Sleuth、Spring Cloud
   Zipkin、Redis、MySQL、Docker、Nacos、Swagger、JWT、JWT-OAuth2.0、Hystrix、RabbitMQ、Shiro、MyBatis、MyBatis-Plus、Lombok、Fastjson、Jackson
3. 前端：Vue、Vite、Element UI Plus、Vue Router、Axios、Pinia、Sass、Js-cookie、Javascript、Typescript、Swagger

## 5. 贡献

1. 框架贡献者(后端)：[Miraitowa_zcx](https://github.com/Miraitowa-zcx)
2. 框架贡献者(前端)：[Chenhb](https://github.com/yuyeyuyy)
3. 算法贡献者：[DangTianqi](https://github.com/DangTianqi)

## 6. 版权声明

* Copyright (c) 2024 Leyramu. All rights reserved.
* This project (Lersosa), including its source code, documentation, and any associated materials, is
  the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any
  form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior
  written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical
  reviews and certain other noncommercial uses permitted by copyright law.
* For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at
  2038322151@qq.com.
* The author disclaims all warranties, express or implied, including but not limited to the warranties of
  merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any
  special, incidental, indirect, or consequential damages arising from the use of this software.
* By using this project, users acknowledge and agree to abide by these terms and conditions.
