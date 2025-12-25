package query

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/ossconfig"
	client "lersosa/api/resource/service/v1/ossconfig"
	"lersosa/app/resource/interface/internal/client/ossconfig"
)

// OssConfigGetQryExe 获取资源配置查询执行器.
type OssConfigGetQryExe struct {
	client *ossconfig.Client
}

// NewOssConfigGetQryExe 创建获取资源配置查询执行器.
func NewOssConfigGetQryExe(client *ossconfig.Client) *OssConfigGetQryExe {
	return &OssConfigGetQryExe{client: client}
}

// Execute 执行命令.
func (exe *OssConfigGetQryExe) Execute(ctx context.Context, request *v1.OssConfigGetRequest) (*v1.OssConfigGetReply, error) {
	return exe.toServerModel(
		exe.client.GetOssConfig(
			ctx, exe.toClientModel(request),
		),
	)
}

// toClientModel 转换为客户端模型.
func (exe *OssConfigGetQryExe) toClientModel(request *v1.OssConfigGetRequest) *client.OssConfigGetRequest {
	return &client.OssConfigGetRequest{
		OssConfigId: request.OssConfigId,
	}
}

// toServerModel 转换为服务端模型.
func (exe *OssConfigGetQryExe) toServerModel(reply *client.OssConfigGetReply, err error) (*v1.OssConfigGetReply, error) {
	return &v1.OssConfigGetReply{
		Data: &v1.OssConfigGetReply_OssConfig{
			OssConfigId:  reply.OssConfigId,
			ConfigKey:    reply.ConfigKey,
			AccessKey:    reply.AccessKey,
			SecretKey:    reply.SecretKey,
			BucketName:   reply.BucketName,
			Prefix:       reply.Prefix,
			Endpoint:     reply.Endpoint,
			Domain:       reply.Domain,
			IsHttps:      reply.IsHttps,
			Region:       reply.Region,
			AccessPolicy: reply.AccessPolicy,
			Status:       reply.Status,
			Ext1:         reply.Ext1,
			Remark:       reply.Remark,
			Version:      reply.Version,
		},
	}, err
}
