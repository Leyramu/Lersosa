package command

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/status"
	client "lersosa/api/stellar/service/v1/status"
	"lersosa/app/stellar/interface/internal/client/status"
)

// StatusModifyCmdExe 星体状态修改命令执行器.
type StatusModifyCmdExe struct {
	client *status.Client
}

// NewStatusModifyCmdExe 构造星体状态修改命令执行器.
func NewStatusModifyCmdExe(client *status.Client) *StatusModifyCmdExe {
	return &StatusModifyCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *StatusModifyCmdExe) ExecuteVoid(ctx context.Context, request *v1.StatusModifyRequest) error {
	_, err := exe.client.ModifyStatus(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *StatusModifyCmdExe) toClientModel(request *v1.StatusModifyRequest) *client.StatusModifyRequest {
	return &client.StatusModifyRequest{
		StatusId: request.GetStatusId(),
		Flag:     request.GetFlag(),
		Check:    request.GetCheck(),
		UpdateBy: request.GetUpdateBy(),
		Remark:   request.GetRemark(),
		Version:  request.GetVersion(),
	}
}
