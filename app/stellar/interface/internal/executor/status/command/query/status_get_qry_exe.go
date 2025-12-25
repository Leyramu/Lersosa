package query

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/status"
	client "lersosa/api/stellar/service/v1/status"
	"lersosa/app/stellar/interface/internal/client/status"
)

// StatusGetQryExe 获取星体状态查询执行器.
type StatusGetQryExe struct {
	client *status.Client
}

// NewStatusGetQryExe 构造获取星体状态查询执行器.
func NewStatusGetQryExe(client *status.Client) *StatusGetQryExe {
	return &StatusGetQryExe{client: client}
}

// Execute 执行命令.
func (exe *StatusGetQryExe) Execute(ctx context.Context, request *v1.StatusGetRequest) (*v1.StatusGetReply, error) {
	return exe.toServerModel(
		exe.client.GetStatus(
			ctx, exe.toClientModel(request),
		),
	)
}

// toClientModel 转换为客户端模型.
func (exe *StatusGetQryExe) toClientModel(request *v1.StatusGetRequest) *client.StatusGetRequest {
	return &client.StatusGetRequest{
		StatusId: request.GetStatusId(),
	}
}

// toServerModel 转换为服务端模型.
func (exe *StatusGetQryExe) toServerModel(reply *client.StatusGetReply, err error) (*v1.StatusGetReply, error) {
	return &v1.StatusGetReply{
		Data: &v1.StatusGetReply_Status{
			StatusId:   reply.GetStatusId(),
			FileUrl:    reply.GetFileUrl(),
			Score:      reply.GetScore(),
			Flag:       reply.GetFlag(),
			Check:      reply.GetCheck(),
			CreateBy:   reply.GetCreateBy(),
			CreateTime: reply.GetCreateTime(),
			UpdateBy:   reply.GetUpdateBy(),
			UpdateTime: reply.GetUpdateTime(),
			Remark:     reply.GetRemark(),
			Version:    reply.GetVersion(),
		},
	}, err
}
