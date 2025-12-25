# 多语言混合开发框架

[![Star](https://img.shields.io/badge/Star-GitHub-yellow.svg)](https://github.com/Leyramu/Lersosa)
[![Version](https://img.shields.io/badge/Version-v5.0.0-success.svg)](https://https://github.com/Leyramu/Lersosa)
[![License](https://img.shields.io/badge/License-Apache2-blue.svg)](https://https://github.com/Leyramu/Lersosa/blob/master/LICENSE)
[![使用Go Lang开发维护](https://img.shields.io/badge/Go%20Lang-提供支持-blue.svg)](https://www.jetbrains.com/?from=RuoYi-Cloud-Plus)
<br/>
[![DDD + CQRS + COLA + BFF + Clean Architecture](https://img.shields.io/badge/DDD%20+%20CQRS%20+%20COLA%20+%20BFF%20+%20Clean%20Architecture-Design-red.svg)]()
<br/>
[![Go Kratos](https://img.shields.io/badge/Go%20Kratos-2.9.2-blue.svg)]()
![Go](https://img.shields.io/badge/Go-1.25.4-green.svg)
[![Build Status](https://img.shields.io/badge/Build-Passing-successsvg)](https://https://github.com/Leyramu/Lersosa/actions)

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
2. 组件化设计：可重用的组件使代码组织更加清晰，易于维护。
3. 统一的 API：无论使用哪种语言，都能享受一致的开发体验。
4. 跨平台支持：可在 Linux, macOS, Windows 等操作系统上运行。

## 6. 运行

1. 运行前请先安装好多语言开发框架的依赖库。
2. Python 算法端 Demo 运行使用 Python-3.11.0 测试通过
3. Java 后端 Demo 运行使用 Java-25.0.0 测试通过
4. Go 后端 Demo 运行使用 Go-1.25.4 测试通过
5. React 前端 Demo 运行使用 Vite-6.0.0 测试通过

## 7. 框架结构树

``` tree
Lersosa    多语言混合开发框架
├─.idea    IDEA 配置文件
├─api      API 接口定义
├─app      应用
├─pkg      公共模块
├─docker    Docker 镜像
├─third_party    第三方依赖库
├─bin      可执行文件
├─doc    文档
├─.gitignore    git 忽略文件
├─LICENSE    开源协议
└─README.md    项目介绍
```

## 4. 技术栈

### 参考源代码

**RuoYi Cloud**  
`v3.6.5` | 作者：yangzongzhuan | 协议：MIT License | 仓库：https://github.com/yangzongzhuan/RuoYi-Cloud <br/>
介绍：RuoYi-Cloud 是一个基于SpringBoot 3.x + MyBatis Plus + Vue3 + Element Plus的权限管理系统。

**RuoYi Cloud Plus**
`v2.2.0` | 作者：dromara | 协议：MIT License | 仓库：https://gitee.com/dromara/RuoYi-Cloud-Plus <br/>
介绍：微服务管理系统 重写RuoYi-Cloud所有功能 整合 SpringCloudAlibaba、Dubbo3.0、Sa-Token、Mybatis-Plus、MQ、Flowable、ES、Docker
全方位升级 定期同步。

**COLA**
`v5.0.0` | 作者：Alibaba | 协议：LGPL-2.1 license | 仓库：https://github.com/alibaba/COLA <br/>
介绍：COLA架构就是为此而生，其核心职责就是定义良好的应用结构，提供最佳应用架构的最佳实践。
通过不断探索，我们发现良好的分层结构，良好的包结构定义，可以帮助我们治理混乱不堪的业务应用系统。

**Go Kratos**
`v2.9.2` | 作者：BiliBili | 协议：MIT License | 仓库：https://github.com/go-kratos/kratos <br/>
介绍：Kratos 是由 golang 实现的面向微服务的治理框架，基于 Go 语言开发，使用 Protobuf 作为通信协议，
提供便捷的功能，帮助你快速从零开始构建一个坚不可摧的应用， 并支持 HTTP/2、gRPC 等多种通信协议。

---

### 主要组件说明（v4.0.0）

| 组件名称                     | 版本/实现方案                                     | 核心作用                                                           |
|--------------------------|---------------------------------------------|----------------------------------------------------------------|
| **Vue**                  | 3.5.13                                      | 渐进式前端框架，用于构建用户界面                                               |
| **Vite**                 | 6.2.2                                       | 快速、轻量、modern前端开发工具                                             |
| **ORM**                  | MyBatis 3.5.16                              | ORM框架，用于持久化数据                                                  |
| **Mybatis Plus**         | 3.5.8 (`mybatis-plus-spring-boot3-starter`) | MyBatis增强工具，提供通用CRUD、分页插件、代码生成器等特性                             |
| **Swagger**              | SpringDoc 2.6.0                             | 通过`springdoc-openapi`实现API文档自动化生成与管理                           |
| **Spring Boot**          | 3.3.5                                       | 快速构建独立运行的Spring应用，提供自动配置和嵌入式容器支持                               |
| **Spring Cloud**         | 2023.0.3                                    | 微服务架构核心框架，集成Nacos等服务治理组件                                       |
| **Spring Cloud Alibaba** | 2023.0.1.2                                  | Spring Cloud Alibaba是Spring Cloud Alibaba团队提供的一套Spring Cloud规范 |
| **Dubbo**                | 3.2.14                                      | 轻量级分布式RPC框架，支持Spring Cloud集成                                   |
| **gRPC**                 | 1.68.0                                      | 高性能RPC框架，支持Spring Cloud集成                                      |
| **Nginx Purge**          | 3.2.0                                       | Nginx缓存清除工具，用于清除缓存                                             |
| **Caffeine**             | 6.1.14                                      | 本地缓存框架，通过`caffeine-spring-boot-starter`集成                      |
| **Loadbalancer**         | 4.14                                        | 负载均衡组件，用于实现服务发现和负载均衡                                           |
| **Python FastAPI**       | 0.115.11                                    | Python Web框架，通过`nacos-sdk`集成Nacos服务发现和配置管理                     |
| **Python Psrcat**        | 0.1.0                                       | 脉冲星源数据处理工具                                                     |          
| **Maven**                | 3.9.9                                       | 项目构建和依赖管理工具                                                    |
| **Git**                  | 2.49.0                                      | 版本控制工具，用于管理源代码                                                 |
| **Docker**               | 28.0.4                                      | 容器化应用部署和运行环境                                                   |

---

### 次要组件说明（v4.0.0）

| 分类        | 组件名称            | 版本      | 功能说明                                      |
|-----------|-----------------|---------|-------------------------------------------|
| **前端**    | Element-Plus    | 2.9.6   | Vue组件库                                    |
|           | Vue-Router      | 4.5.0   | 前端路由管理                                    |
|           | Axios           | 1.8.3   | 基于Promise的HTTP库                           |
|           | Unocss          | 0.58.4  | 快速、轻量、高性能的CSS框架                           |
|           | Pinia           | 2.3.1   | Vue状态管理                                   |
|           | Echarts         | 5.6.0   | 基于JavaScript的图表库，支持多种图表类型                 |
|           | Js Cookie       | 3.0.5   | Cookie管理工具                                |
| **工具**    | Lombok          | 1.18.36 | 通过注解自动生成getter/setter等方法                  |
|           | EasyExcel       | 4.0.3   | 阿里云高效Excel处理工具                            |
|           | Apache Commons  | 2.15.0  | 常用工具类库，通过`commons-lang3`集成                |
|           | Apache Velocity | 2.3     | 模板引擎，通过`velocity-engine-core`集成           |
|           | Fastjson        | 2.0.53  | JSON解析工具，采用 Alibaba 官方序列化                 |
|           | AWS             | 2.28.22 | 云存储工具，支持 七牛、阿里、腾讯 等一切支持S3协议的厂家            |
|           | Redisson        | 3.37.0  | Redis客户端 基于Netty的客户端工具                    | 
| **安全**    | Sa-Token        | 1.39.0  | 轻量级权限认证框架，支持Spring Boot 3集成               |
|           | Jasypt          | 3.0.5   | 密码加密解密工具，通过`jasypt-spring-boot-s.arter`集成 |
| **租户**    | Tenant          | 3.5.8   | 多租户解决方案，实现数据隔离                            |
| **短信**    | Sms4j           | 3.3.3   | 短信发送工具，通过`sms4j-spring-boot-starter`集成    |
| **SQL监控** | P6spy           | 3.9.1   | SQL监控工具，采用 p6spy 可输出完整SQL与执行时间监控          |

---

### 基础设施（v4.0.0）

| 分类          | 组件名称                | 版本                                | 功能说明                                              |
|-------------|---------------------|-----------------------------------|---------------------------------------------------|
| **服务注册中心**  | Nacos               | 2.5.0                             | 服务注册中心与配置中心，实现动态服务发现和配置管理                         |
| **分布式事务**   | Seata               | 1.7.1                             | 分布式事务框架，通过`seata-spring-boot-starter`集成           |
| **分布式限流熔断** | Sentinel            | 1.8.8                             | 分布式限流框架，通过`sentinel-datasource-nacos`实现动态规则管理     |
| **分布式任务调度** | Snailjob            | 1.1.2                             | 采用 SnailJob 天生支持分布式 统一的管理中心 支持多种数据库 支持分片重试DAG任务流等 |
| **数据库**     | MySQL               | 9.2.0                             | 关系型数据库，用于存储和检索数据                                  |
| **分库分表功能**  | ShardingSpere Proxy | 5.5.2                             | 分库分表中间件，用于代理数据库请求，解决数据库负载均衡和容错能力                  |
|             | Dynamic Datasource  | 4.3.1                             | 动态数据源管理，支持多租户场景                                   |
| **高性能缓存**   | Redis               | 7.4.2                             | 内存数据库，通过`redisson-spring-boot-starter`实现分布式锁和缓存管理 |
| **分布式搜索引擎** | Elasticsearch       | 8.16.0                            | 分布式搜索分析引擎，集成`easy-es-boot-starter`简化操作            |
| **分布式消息队列** | RocketMQ            | 2.3.0                             | 分布式消息队列，通过`rocketmq-spring-boot-starter`集成        |
|             | Rabbitmq            | 4.0.4-management                  |                                                   |
|             | Kafka               | 3.5.1                             | 分布式消息队列，通过`kafka-spring-boot-starter`集成           |
| **文件存储**    | Minio               | RELEASE.<br/>2025-03-12T18-04-18Z | 对象存储服务，通过`minio-spring-boot-starter`集成            |
| **链路追踪**    | SkyWalking          | 10.1.0                            | 分布式系统性能监控，集成`apm-toolkit-logback`实现链路追踪           |
| **数据可视化**   | Kibana              | 8.16.0                            | 数据分析和可视化工具                                        |
| **告警**      | Prometheus          | 2.47.0                            | 分布式监控系统                                           |
|             | Grafana             | 9.4.1                             | 监控数据可视化工具                                         |
| **日志**      | Logstash            | 8.16.0                            | 日志收集和转发工具，通过`logstash-spring-boot-starter`集成      |
| **云原生**     | Kubernetes          | 1.32.2                            | 用于管理云平台中多个主机上的容器化的应用                              |

## 5. 贡献

1. 框架贡献者(后端)：[Miraitowa_zcx](https://github.com/Miraitowa-zcx)
2. 框架贡献者(前端)：[Chenhb](https://github.com/yuyeyuyy)
3. 算法贡献者：[DangTianqi](https://github.com/DangTianqi)

## 6. 版权声明

* This project is derived from [go-kratos](https://github.com/go-kratos/kratos).
* Original work: Copyright (c) 2020 go-kratos, licensed under the **MIT License**.
* Modifications and new code: Copyright 2024 Leyramu Group, licensed under the **Apache License 2.0**.
* See the [LICENSE](./LICENSE) file for full details.
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
