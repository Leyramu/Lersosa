# 客户端层（Client Layer）

本包包含应用的外部服务客户端，负责与下游服务进行通信。

## 主要功能

- 远程服务调用
- 请求参数序列化
- 响应结果反序列化
- 服务发现与负载均衡

## 与其他层的关系

- 接收来自 `executor` 层的服务调用请求
- 调用下游微服务接口
- 返回服务调用结果给上游

## 包结构

- `client` - 客户端层
    - `file` - 文件服务客户端
        - `file_client.go` - 文件服务客户端实现
        - `file_provider.go` - 文件服务提供者
    - `ossconfig/` - OSS配置服务客户端
        - `oss_config_client.go` - OSS配置服务客户端实现
        - `oss_config_provider.go` - OSS配置服务提供者
    - `client.go` - 客户端依赖注入
