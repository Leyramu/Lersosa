# 执行层（Executor Layer）

BFF 层执行器，负责协调和执行具体的业务逻辑。

## 主要功能

- 业务逻辑编排
- 服务调用协调
- 数据聚合处理
- 异常处理和回滚

## 与其他层的关系

- 接收来自 `controller` 层的请求
- 调用 `client` 层进行远程服务调用
- 使用 `pkg` 层进行通用数据处理

## 包结构

- `executor/` - 业务执行器层
    - `file` - 文件执行器
        - `command` - 命令执行器
            - `query` - 查询执行器
                - `file_get_qry_exe.go` - 文件查询命令执行器
                - `file_page_qry_exe.go` - 文件分页查询执行器
            - `file_save_cmd_exe.go` - 文件保存命令执行器
            - `file_modify_cmd_exe.go` - 文件修改命令执行器
            - `file_reomve_cmd_exe.go` - 文件删除命令执行器
            - `file_upload_cmd_exe.go` - 文件上传命令执行器
        - `file_executor.go` - 文件执行器编排
    - `ossconfig` - OSS 配置执行器
        - `command` - 命令执行器
            - `query` - 查询执行器
                - `oss_config_get_qry_exe.go` - OSS 配置查询命令执行器
                - `oss_config_page_qry_exe.go` - OSS 配置分页查询执行器
            - `oss_config_save_cmd_exe.go` - OSS 配置保存命令执行器
            - `oss_config_modify_cmd_exe.go` - OSS 配置修改命令执行器
            - `oss_config_reomve_cmd_exe.go` - OSS 配置删除命令执行器
            - `oss_config_upload_cmd_exe.go` - OSS 配置上传命令执行器
        - `oss_config_executor.go` - OSS 配置执行器编排
