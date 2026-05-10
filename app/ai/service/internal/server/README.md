# 服务器层（Server Layer）

本包包含应用的 gRPC 和 HTTP 服务器实现,负责启动和管理网络服务,处理客户端请求。

## 主要功能

- gRPC 服务器配置与启动
- HTTP 服务器配置与启动
- TLS/mTLS 安全通信支持
- 中间件集成(日志、追踪、恢复等)
- 服务注册与发现

## 与其他层的关系

- 接收来自客户端的 gRPC/HTTP 请求
- 调用 `adapter` 层进行协议转换和业务处理
- 依赖 `infrastructure` 层提供的数据库、缓存等基础设施
- 使用 `conf` 层的配置信息进行服务器初始化

## 包结构

- `server` - 服务器层
    - `grpc.go` - gRPC 服务器实现
    - `http.go` - HTTP 服务器实现
    - `server.go` - 服务器依赖注入
