package command

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/status"
	client "lersosa/api/stellar/service/v1/status"
	"lersosa/app/stellar/interface/internal/client/status"
)

// StatusRemoveCmdExe 星体状态删除命令执行器.
type StatusRemoveCmdExe struct {
	client *status.Client
}

// NewStatusRemoveCmdExe 构造星体状态删除命令执行器.
func NewStatusRemoveCmdExe(client *status.Client) *StatusRemoveCmdExe {
	return &StatusRemoveCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *StatusRemoveCmdExe) ExecuteVoid(ctx context.Context, request *v1.StatusRemoveRequest) error {
	_, err := exe.client.RemoveStatus(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *StatusRemoveCmdExe) toClientModel(request *v1.StatusRemoveRequest) *client.StatusRemoveRequest {
	statuses := make([]*client.StatusRemoveRequest_Status, 0, len(request.GetStatuses()))
	for _, req := range request.GetStatuses() {
		statuses = append(statuses, &client.StatusRemoveRequest_Status{
			StatusId: req.GetStatusId(),
			Version:  req.GetVersion(),
		})
	}

	return &client.StatusRemoveRequest{Statuses: statuses}
}
