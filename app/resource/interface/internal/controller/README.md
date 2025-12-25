# 控制层 (Controller Layer)

本包包含应用的HTTP控制器，负责处理API请求和响应。

## 主要功能

- HTTP 请求接收与参数解析
- 响应数据封装与格式化
- 请求路由分发
- 异常拦截与处理

## 与其他层的关系

- 接收来自 HTTP 客户端的请求
- 调用 `executor` 层执行具体业务逻辑
- 返回处理结果给客户端

## 包结构

- `controller/` - 控制层
    - `handler/` - 处理器
    - `web` - Web Api
        - `file_controller_v1.go` - 文件相关接口
        - `oss_config_controller_v1.go` - 对象存储相关接口
