# 应用层（Application Layer）

应用层负责用例编排、事务边界与 CQRS 组织,不承载协议适配职责。

## 主要功能

- 业务用例编排:协调领域服务完成具体业务场景
- CQRS 模式实现:命令(Command)和查询(Query)分离
- 事务边界管理:定义业务操作的事务范围
- 异常处理:统一处理业务异常并转换为 appropriate 错误类型

## 与其他层的关系

- 接收来自 `adapter` 层的调用请求
- 调用 `domain` 层的领域服务执行核心业务逻辑
- 依赖 `infrastructure` 层提供的仓储实现
- 不直接依赖传输层 DTO 或 controller/adapter 逻辑

## 包结构

- `application` - 应用层
    - `chat` - Chat 限界上下文应用服务
        - `command` - 命令执行器
            - `query` - 查询执行器
                - `get_chat_history_qry_exe.go` - 获取聊天历史查询执行器
                - `list_sessions_qry_exe.go` - 列出会话查询执行器
            - `create_session_cmd_exe.go` - 创建会话命令执行器
            - `delete_session_cmd_exe.go` - 删除会话命令执行器
            - `send_message_cmd_exe.go` - 发送消息命令执行器
        - `services` - 应用服务实现
            - `chat_service_impl.go` - Chat 应用服务实现
    - `knowledge` - Knowledge 限界上下文应用服务
        - `command` - 命令执行器
            - `query` - 查询执行器
                - `knowledge_get_qry_exe.go` - 知识查询执行器
                - `knowledge_page_qry_exe.go` - 知识分页查询执行器
            - `knowledge_modify_cmd_exe.go` - 知识修改命令执行器
            - `knowledge_remove_cmd_exe.go` - 知识删除命令执行器
            - `knowledge_save_cmd_exe.go` - 知识保存命令执行器
        - `services` - 应用服务实现
            - `knowledge_service_impl.go` - Knowledge 应用服务实现
    - `application.go` - 应用层依赖注入

## 当前约束

- `chat/` 是当前主用例目录
- `knowledge/` 为独立子域预留目录
- 面向前端的聚合响应迁移到 `app/ai/interface` BFF 模块
