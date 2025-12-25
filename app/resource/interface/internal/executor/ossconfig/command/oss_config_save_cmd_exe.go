package command

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/ossconfig"
	client "lersosa/api/resource/service/v1/ossconfig"
	"lersosa/app/resource/interface/internal/client/ossconfig"
)

// OssConfigSaveCmdExe 资源配置保存命令执行器.
type OssConfigSaveCmdExe struct {
	client *ossconfig.Client
}

// NewOssConfigSaveCmdExe 创建资源配置保存命令执行器.
func NewOssConfigSaveCmdExe(client *ossconfig.Client) *OssConfigSaveCmdExe {
	return &OssConfigSaveCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *OssConfigSaveCmdExe) ExecuteVoid(ctx context.Context, request *v1.OssConfigSaveRequest) error {
	_, err := exe.client.SaveOssConfig(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *OssConfigSaveCmdExe) toClientModel(request *v1.OssConfigSaveRequest) *client.OssConfigSaveRequest {
	return &client.OssConfigSaveRequest{
		ConfigKey:    request.ConfigKey,
		AccessKey:    request.AccessKey,
		SecretKey:    request.SecretKey,
		BucketName:   request.BucketName,
		Prefix:       request.Prefix,
		Endpoint:     request.Endpoint,
		Domain:       request.Domain,
		IsHttps:      request.IsHttps,
		Region:       request.Region,
		AccessPolicy: request.AccessPolicy,
		Status:       request.Status,
		Ext1:         request.Ext1,
		CreateDept:   request.CreateDept,
		CreateBy:     request.CreateBy,
		Remark:       request.Remark,
	}
}
