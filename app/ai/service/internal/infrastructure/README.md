# 基础设施层（Infrastructure Layer）

基础设施层负责数据库、缓存、搜索与外部模型调用实现,为上层提供技术支撑。

## 主要功能

- 数据库访问:使用 Ent ORM 进行类型安全的数据库操作
- 缓存管理:Redis 客户端封装,提供高性能缓存服务
- 搜索引擎:Elasticsearch 客户端集成,支持全文检索
- 外部服务调用:AI 模型推理服务(HTTP/gRPC)集成
- 数据转换器:领域模型与数据库模型之间的转换

## 与其他层的关系

- 实现 `domain` 层定义的 Gateway 接口
- 被 `application` 层通过依赖注入使用
- 依赖 `conf` 层的配置信息进行初始化
- 为整个应用提供持久化、缓存和外部服务能力

## 包结构

- `infrastructure` - 基础设施层
    - `chat` - Chat 限界上下文基础设施
        - `convertor` - 数据转换器
            - `chat_convertor.go` - Chat 数据转换器
        - `gatewayimpl` - Gateway 接口实现
            - `database/ent` - 数据库实现
                - `chat_data.go` - Chat 数据访问
            - `eino` - Eino AI 模型集成
                - `http_transport.go` - HTTP 传输层
                - `inference_stream_collector.go` - 推理流收集器
                - `openai_compatible_convertor.go` - OpenAI 兼容转换器
                - `openai_compatible_model.go` - OpenAI 兼容模型
                - `openai_compatible_types.go` - OpenAI 兼容类型定义
                - `provider_profiles.go` - 提供者配置
                - `retry_config.go` - 重试配置
            - `eino_gateway_impl.go` - Eino 网关实现
            - `message_gateway_impl.go` - 消息网关实现
            - `session_gateway_impl.go` - 会话网关实现
    - `knowledge` - Knowledge 限界上下文基础设施
        - `convertor` - 数据转换器
            - `knowledge_convertor.go` - Knowledge 数据转换器
        - `gatewayimpl` - Gateway 接口实现
            - `database/ent` - 数据库实现
                - `knowledge_data.go` - Knowledge 数据访问
            - `knowledge_es_impl.go` - Elasticsearch 实现
            - `knowledge_redis_impl.go` - Redis 缓存实现
            - `knowledge_repo_impl.go` - Knowledge 仓储实现
    - `infrastructure.go` - 基础设施层依赖注入

## 当前约束

- `chat/` 为当前主链路基础设施
- `knowledge/` 后续按独立领域接入
- 后续需要抽出 `shared/` provider,统一 DB/Redis/ES 初始化

## 迁移方向

1. 将技术初始化失败改为返回错误,而不是直接 `Fatal`
2. 收敛共享客户端初始化,减少 `*_data.go` 重复
3. 让 `gatewayimpl/` 只承担领域接口实现
