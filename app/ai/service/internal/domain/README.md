# 领域层（Domain Layer）

领域层按限界上下文组织核心业务规则,是 DDD 架构的核心。

## 主要功能

- 领域模型定义:实体(Entity)、值对象(Value Object)、聚合根(Aggregate Root)
- 领域服务实现:封装跨实体的业务逻辑
- Gateway 接口定义:定义仓储接口,实现依赖倒置
- 业务规则验证:确保领域对象的完整性和一致性

## 与其他层的关系

- 被 `application` 层调用:应用服务编排领域服务完成业务用例
- 不直接依赖传输层 DTO 或 adapter/controller 逻辑
- 通过 Gateway 接口与 `infrastructure` 层解耦
- 新增消息会话相关业务优先收敛到 `chat` 聚合

## 包结构

- `domain` - 领域层
    - `chat` - Chat 限界上下文
        - `ability` - 领域服务
            - `chat_domain_service.go` - Chat 领域服务
        - `gateway` - 仓储接口定义
            - `eino_gateway.go` - Eino 模型网关接口
            - `message_gateway.go` - 消息仓储接口
            - `session_gateway.go` - 会话仓储接口
        - `model` - 领域模型
            - `chat_aggregate.go` - Chat 聚合根
            - `chat_errors.go` - Chat 领域错误定义
            - `chat_types.go` - Chat 类型定义
            - `inference_result.go` - 推理结果值对象
            - `message_entity.go` - 消息实体
            - `session_entity.go` - 会话实体
            - `trace_value.go` - 追踪值对象
    - `knowledge` - Knowledge 限界上下文
        - `ability` - 领域服务
            - `knowledge_domain_service.go` - Knowledge 领域服务
        - `gateway` - 仓储接口定义
            - `knowledge_es_i.go` - Elasticsearch 仓储接口
            - `knowledge_redis_i.go` - Redis 缓存接口
            - `knowledge_repo_i.go` - Knowledge 仓储接口
        - `model` - 领域模型
            - `knowledge_model.go` - Knowledge 领域模型
    - `domain.go` - 领域层依赖注入

## 领域层约束

1. 业务规则优先沉淀在聚合、实体、值对象和领域服务中
2. 不直接依赖传输层 DTO 或 controller/adapter 逻辑
3. 新增链路追踪语义统一挂在 `chat` 聚合的 trace 值对象或聚合行为中
4. 保持领域模型的纯粹性,避免技术细节污染
