package query

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/ossconfig"
	client "lersosa/api/resource/service/v1/ossconfig"
	"lersosa/app/resource/interface/internal/client/ossconfig"
)

// OssConfigPageQryExe 资源配置分页查询执行器.
type OssConfigPageQryExe struct {
	client *ossconfig.Client
}

// NewOssConfigPageQryExe 创建资源配置分页查询执行器.
func NewOssConfigPageQryExe(client *ossconfig.Client) *OssConfigPageQryExe {
	return &OssConfigPageQryExe{client: client}
}

// Execute 执行命令.
func (exe *OssConfigPageQryExe) Execute(ctx context.Context, request *v1.OssConfigPageRequest) (*v1.OssConfigPageReply, error) {
	return exe.toServerModel(
		exe.client.PageOssConfig(
			ctx, exe.toClientModel(request),
		),
	)
}

// toClientModel 转换为客户端模型.
func (exe *OssConfigPageQryExe) toClientModel(request *v1.OssConfigPageRequest) *client.OssConfigPageRequest {
	return &client.OssConfigPageRequest{
		PageNum:    request.PageNum,
		PageSize:   request.PageSize,
		ConfigKey:  request.ConfigKey,
		BucketName: request.BucketName,
		Status:     request.Status,
	}
}

// toServerModel 转换为服务端模型.
func (exe *OssConfigPageQryExe) toServerModel(reply *client.OssConfigPageReply, err error) (*v1.OssConfigPageReply, error) {
	rows := make([]*v1.OssConfigPageReply_OssConfig, len(reply.Rows))
	for i, item := range reply.Rows {
		rows[i] = &v1.OssConfigPageReply_OssConfig{
			OssConfigId:  item.OssConfigId,
			ConfigKey:    item.ConfigKey,
			AccessKey:    item.AccessKey,
			SecretKey:    item.SecretKey,
			BucketName:   item.BucketName,
			Prefix:       item.Prefix,
			Endpoint:     item.Endpoint,
			Domain:       item.Domain,
			IsHttps:      item.IsHttps,
			Region:       item.Region,
			AccessPolicy: item.AccessPolicy,
			Status:       item.Status,
			Ext1:         item.Ext1,
			Remark:       item.Remark,
			Version:      item.Version,
		}
	}

	return &v1.OssConfigPageReply{
		Rows:  rows,
		Total: int64(len(rows)),
	}, err
}
