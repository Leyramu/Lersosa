package command

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/ossconfig"
	client "lersosa/api/resource/service/v1/ossconfig"
	"lersosa/app/resource/interface/internal/client/ossconfig"
)

// OssConfigModifyCmdExe 资源配置修改命令执行器.
type OssConfigModifyCmdExe struct {
	client *ossconfig.Client
}

// NewOssConfigModifyCmdExe 创建资源配置修改命令执行器.
func NewOssConfigModifyCmdExe(client *ossconfig.Client) *OssConfigModifyCmdExe {
	return &OssConfigModifyCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *OssConfigModifyCmdExe) ExecuteVoid(ctx context.Context, request *v1.OssConfigModifyRequest) error {
	_, err := exe.client.ModifyOssConfig(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *OssConfigModifyCmdExe) toClientModel(request *v1.OssConfigModifyRequest) *client.OssConfigModifyRequest {
	return &client.OssConfigModifyRequest{
		OssConfigId:  request.OssConfigId,
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
		UpdateBy:     request.UpdateBy,
		Remark:       request.Remark,
		Version:      request.Version,
	}
}
