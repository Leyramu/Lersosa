# AI 服务模块（AI Service Module）

本模块是 Lersosa 平台的 AI 微服务实现,基于 Kratos 框架、DDD（领域驱动设计）、CQRS（命令查询职责分离）架构构建,提供智能对话和知识管理功能。

## 技术栈

- **框架**: Kratos v2.9.2（微服务框架,支持 gRPC + HTTP/2）
- **ORM**: Ent v0.14.5（类型安全的 ORM）
- **依赖注入**: Google Wire v0.7.0（编译时依赖注入）
- **数据库**: PostgreSQL 18
- **缓存**: Redis 9
- **搜索引擎**: Elasticsearch 9
- **序列化**: Protocol Buffers v3

## 架构分层

本模块采用标准的 DDD 分层架构:

```
app/ai/service/
├── internal/
│   ├── adapter/          # 适配层 - 协议转换与 DTO 映射
│   ├── application/      # 应用层 - 用例编排与 CQRS
│   ├── domain/           # 领域层 - 核心业务规则
│   ├── infrastructure/   # 基础设施层 - 技术实现
│   ├── conf/             # 配置管理层 - 系统配置
│   └── server/           # 服务器层 - gRPC/HTTP 服务
├── cmd/server/           # 应用入口
├── configs/              # 配置文件
└── certs/                # TLS 证书
```

### 分层职责

1. **Adapter Layer（适配层）**
   - 负责 Protobuf/HTTP 请求与应用层命令之间的转换
   - 包含 Chat 和 Knowledge 两个限界上下文的适配器
   - 实现 RPC 适配器,封装 gRPC 调用

2. **Application Layer（应用层）**
   - 负责用例编排、事务边界与 CQRS 组织
   - 实现 Command（命令）和 Query（查询）执行器
   - 协调领域服务完成具体业务场景

3. **Domain Layer（领域层）**
   - 按限界上下文组织核心业务规则
   - 定义实体、值对象、聚合根和领域服务
   - 通过 Gateway 接口与基础设施层解耦

4. **Infrastructure Layer（基础设施层）**
   - 实现领域层定义的 Gateway 接口
   - 提供数据库、缓存、搜索引擎等技术服务
   - 集成外部 AI 模型推理服务

5. **Config Layer（配置层）**
   - 使用 Protocol Buffers 定义配置结构
   - 支持多源配置加载和验证

6. **Server Layer（服务器层）**
   - 启动和管理 gRPC/HTTP 服务器
   - 集成中间件（日志、追踪、恢复等）
   - 支持 TLS/mTLS 安全通信

## 限界上下文

### Chat 上下文（主链路）

提供智能对话功能:
- 会话管理（创建、删除、列表）
- 消息发送与流式响应
- 聊天历史查询
- AI 模型推理集成

### Knowledge 上下文（预留）

提供知识管理功能:
- 知识库 CRUD 操作
- 全文检索（Elasticsearch）
- 缓存优化（Redis）

## 依赖关系

```
Server → Adapter → Application → Domain ← Infrastructure
                    ↑                      ↓
                  Config ←─────────────────┘
```

- 依赖方向自上而下,核心业务逻辑不依赖外部框架
- Domain 层通过 Gateway 接口与 Infrastructure 层解耦（依赖倒置）
- 各层通过 Google Wire 进行依赖注入

## 开发规范

- **DDD 原则**: 以业务领域为核心,通过限界上下文划分模块
- **CQRS 模式**: 命令和查询分离,优化各自的数据模型
- **Clean Architecture**: 分层清晰,依赖单向流动
- **DRY/KISS/YAGNI**: 避免重复代码,保持简单,只实现必要功能
- **并发安全**: 合理使用 Goroutine 和 Channel
- **OWASP 安全**: 防范常见安全攻击

## 快速开始

### 生成代码

```bash
# 生成 Protobuf 代码
make proto

# 生成依赖注入代码
make wire

# 生成 Ent ORM 代码
make ent
```

### 运行服务

```bash
# 开发模式运行
make run

# 构建二进制文件
make build
```

### 测试

```bash
# 运行单元测试
make test

# 运行集成测试
make test-integration
```

## 目录说明

- `cmd/server/` - 应用入口和 Wire 依赖注入配置
- `configs/` - YAML 配置文件
- `certs/` - TLS 证书文件（PostgreSQL、Redis、Elasticsearch、gRPC/HTTP）
- `internal/` - 内部实现代码（不对外暴露）
- `Makefile` - 构建和开发命令
- `generate.go` - 代码生成入口
- `go.mod` / `go.sum` - Go 模块依赖管理

## 相关文档

- [适配层说明](internal/adapter/README.md)
- [应用层说明](internal/application/README.md)
- [领域层说明](internal/domain/README.md)
- [基础设施层说明](internal/infrastructure/README.md)
- [配置层说明](internal/conf/README.md)
- [服务器层说明](internal/server/README.md)
