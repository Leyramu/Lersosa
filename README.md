# 多语言混合开发框架

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
2. Python 算法端 Demo 运行使用 Python-3.12.3 测试通过
3. Java 后端 Demo 运行使用 Java-23.0.1 测试通过
4. Vue 前端 Demo 运行使用 Vite-5.2.12 测试通过

## 7. 框架结构树

``` tree
LeyramuLersosa    多语言混合开发框架
├─lersosa-java    Java 后端
│  ├─lersosa-api    微服务 API 模块
│  │  ├─lersosa-api-system    系统模块
│  │  ├─lersosa-api-permission-api    权限模块
│  │  ├─lersosa-api-student    学生模块
│  │  └─lersosa-api-user    用户模块
│  ├─lersosa-cloud    微服务核心模块
│  │  ├─lersosa-cloud-resgistry    注册中心模块
│  │  └─lersosa-cloud-seata    Seata 分布式事务模块
│  ├─lersosa-gateway    微服务网关模块
│  │  └─lersosa-gateway-api    API 网关模块
│  ├─lersosa-common    微服务公共模块
│  │  ├─lersosa-common-algorithm    算法模块
│  │  ├─lersosa-common-base    基本模块
│  │  ├─lersosa-common-core    核心模块
│  │  ├─lersosa-common-datascope    数据范围模块
│  │  ├─lersosa-common-datasource    多数据源模块
│  │  ├─lersosa-common-datascope    数据范围模块
│  │  ├─lersosa-common-dependency    业务依赖模块
│  │  ├─lersosa-common-log    消息日志模块
│  │  ├─lersosa-common-redis    数据缓存模块
│  │  ├─lersosa-common-security    数据安全模块
│  │  ├─lersosa-common-sensitive    数据脱敏模块
│  │  └─lersosa-common-swagger    Swagger 模块
│  ├─lersosa-service    微服务业务模块
│  │  ├─lersosa-service-auth    认证中心业务模块
│  │  ├─lersosa-service-file    文件业务模块
│  │  ├─lersosa-service-gen    代码生成模块
│  │  ├─lersosa-service-job    定时任务业务模块
│  │  ├─lersosa-service-system    系统业务模块
│  │  ├─lersosa-service-openai    OpenAI 业务模块
│  │  ├─lersosa-service-permission    权限业务模块
│  │  ├─lersosa-service-student    学生业务模块
│  │  ├─lersosa-service-teacher    教师业务模块
│  │  └─lersosa-service-user    用户业务模块
│  ├─lersosa-visual    微服务可视化模块
│  │  └─lersosa-visual-monitor    微服务监控模块
│  ├─lersosa-web    微服务页面渲染模块
│  │  ├─lersosa-web-search    搜索渲染模块
│  │  └─lersosa-web-page    页面渲染模块
│  └─swagger-json    API 工程规范生成器
├─lersosa-python    Python 算法端
│  └─src    源代码目录
│       ├─app    应用模块
│       │  ├─routes    路由模块
│       │  └─utils    工具模块
│       └─center    注册中心模块
└─lersosa-vue    Vue 前端
    ├─vue-admin    前端管理层模块
    └─vue-user    前端用户层模块
```

## 8. 技术栈

1. 算法端：Python、Flask、Flask-RESTful
2. 后端：Java、Spring Cloud、Spring Boot、Spring Security、Spring Data JPA、Spring Cloud Gateway、Spring Cloud OpenFeign、Spring
   Cloud Alibaba、Spring Cloud Stream、Spring Cloud Config、Spring Cloud Sleuth、Spring Cloud
   Zipkin、Redis、MySQL、Docker、Nacos、Swagger、JWT、JWT-OAuth2.0、Feign、Hystrix、RabbitMQ、Shiro、MyBatis、MyBatis-Plus、Lombok、Fastjson、Jackson
3. 前端：Vue、Vite、Element UI Plus、Vue Router、Axios、Pinia、Sass、Js-cookie、Javascript、Typescript、Swagger

## 9. 贡献

1. 后端贡献者：[Miraitowa_zcx](https://github.com/Miraitowa-zcx)

## 10. 版权声明

* Copyright (c) 2024 Leyramu. All rights reserved.
* This project (Digitalization Education), including its source code, documentation, and any associated materials, is
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
