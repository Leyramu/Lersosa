package command

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/status"
	client "lersosa/api/stellar/service/v1/status"
	"lersosa/app/stellar/interface/internal/client/status"
)

// StatusSaveCmdExe 星体状态保存命令执行器.
type StatusSaveCmdExe struct {
	client *status.Client
}

// NewStatusSaveCmdExe 构造星体状态保存命令执行器.
func NewStatusSaveCmdExe(client *status.Client) *StatusSaveCmdExe {
	return &StatusSaveCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *StatusSaveCmdExe) ExecuteVoid(ctx context.Context, request *v1.StatusSaveRequest) error {
	_, err := exe.client.SaveStatus(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *StatusSaveCmdExe) toClientModel(request *v1.StatusSaveRequest) *client.StatusSaveRequest {
	return &client.StatusSaveRequest{
		Score:    request.GetScore(),
		Flag:     request.GetFlag(),
		Check:    request.GetCheck(),
		CreateBy: request.GetCreateBy(),
		Remark:   request.GetRemark(),
	}
}
