package command

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/ossconfig"
	client "lersosa/api/resource/service/v1/ossconfig"
	"lersosa/app/resource/interface/internal/client/ossconfig"
)

// OssConfigRemoveCmdExe 资源配置删除命令执行器.
type OssConfigRemoveCmdExe struct {
	client *ossconfig.Client
}

// NewOssConfigRemoveCmdExe 创建资源配置删除命令执行器.
func NewOssConfigRemoveCmdExe(client *ossconfig.Client) *OssConfigRemoveCmdExe {
	return &OssConfigRemoveCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *OssConfigRemoveCmdExe) ExecuteVoid(ctx context.Context, request *v1.OssConfigRemoveRequest) error {
	_, err := exe.client.RemoveOssConfig(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *OssConfigRemoveCmdExe) toClientModel(request *v1.OssConfigRemoveRequest) *client.OssConfigRemoveRequest {
	ossConfigs := make([]*client.OssConfigRemoveRequest_OssConfig, 0, len(request.OssConfigs))
	for _, m := range request.OssConfigs {
		ossConfigs = append(ossConfigs, &client.OssConfigRemoveRequest_OssConfig{
			OssConfigId: m.OssConfigId,
			Version:     m.Version,
		})
	}

	return &client.OssConfigRemoveRequest{OssConfigs: ossConfigs}
}
