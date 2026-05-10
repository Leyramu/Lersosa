# 适配层（Adapter Layer）

适配层负责 Protobuf / HTTP 请求与应用层命令之间的转换,是微服务架构中的重要组成部分。

## 主要功能

- 协议转换:将外部 API 请求转换为内部命令/查询对象
- DTO 映射:在不同层级间转换数据传输对象
- RPC 适配器实现:封装 gRPC 调用,提供类型安全的服务接口
- 视图模型构建:为不同客户端构建定制化的响应数据

## 与其他层的关系

- 接收来自 `server` 层的 gRPC/HTTP 请求
- 调用 `application` 层执行具体业务逻辑
- 使用 `domain` 层的领域模型进行数据转换
- 依赖 `infrastructure` 层提供的配置和基础设施

## 包结构

- `adapter` - 适配层
    - `chat` - Chat 限界上下文适配器
        - `api` - API 接口定义
            - `chat_service_i.go` - Chat 服务接口
        - `dto` - 数据传输对象
            - `co` - 命令对象(Command Objects)
                - `chat_co.go` - Chat 相关命令对象
            - `create_session_cmd.go` - 创建会话命令
            - `delete_session_cmd.go` - 删除会话命令
            - `get_chat_history_qry.go` - 获取聊天历史查询
            - `list_sessions_qry.go` - 列出会话查询
            - `send_message_cmd.go` - 发送消息命令
        - `rpc` - RPC 适配器实现
            - `chat_adapter_v1.go` - Chat 适配器 V1 版本
    - `knowledge` - Knowledge 限界上下文适配器
        - `api` - API 接口定义
            - `knowledge_service_i.go` - Knowledge 服务接口
        - `dto` - 数据传输对象
            - `co` - 命令对象(Command Objects)
                - `knowledge_co.go` - Knowledge 相关命令对象
            - `knowledge_get_qry.go` - 知识查询
            - `knowledge_modify_cmd.go` - 知识修改命令
            - `knowledge_page_qry.go` - 知识分页查询
            - `knowledge_remove_cmd.go` - 知识删除命令
            - `knowledge_save_cmd.go` - 知识保存命令
        - `rpc` - RPC 适配器实现
            - `knowledge_adapter_v1.go` - Knowledge 适配器 V1 版本
    - `adapter.go` - 适配器依赖注入

## 当前约束

- `chat/` 为当前运行时主适配器
- `knowledge/` 为独立子域预留目录
- 适配层可以依赖应用层,但应用层不应反向依赖适配层 DTO
- 面向前端的协议裁剪应优先迁往 `app/ai/interface` BFF 模块
