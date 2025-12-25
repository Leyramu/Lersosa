# 基础设置层（Infrastructure Layer）

本包包含BFF层的基础设施配置和通用组件，为上层应用提供基础支撑。

## 主要功能

- 配置管理
- 中间件集成
- 第三方服务集成
- 基础设施初始化

## 与其他层的关系

- 为 `controller` 、`executor` 、`client` 等各层提供基础设施支持
- 集成外部中间件和服务
- 处理全局配置和环境初始化

## 包结构

- `infrastructure` - 基础设置层
    - `conf` - 配置管理
        - proto - 配置协议
            - `conf.proto` - 配置协议
        - `conf.pb.go` - 配置协议编译后的文件
