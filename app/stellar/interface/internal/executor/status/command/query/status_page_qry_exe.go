package query

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/status"
	client "lersosa/api/stellar/service/v1/status"
	"lersosa/app/stellar/interface/internal/client/status"
)

// StatusPageQryExe 星体状态分页查询执行器.
type StatusPageQryExe struct {
	client *status.Client
}

// NewStatusPageQryExe 构造星体状态分页查询执行器.
func NewStatusPageQryExe(client *status.Client) *StatusPageQryExe {
	return &StatusPageQryExe{client: client}
}

// Execute 执行命令.
func (exe *StatusPageQryExe) Execute(ctx context.Context, request *v1.StatusPageRequest) (*v1.StatusPageReply, error) {
	return exe.toServerModel(
		exe.client.PageStatus(
			ctx, exe.toClientModel(request),
		),
	)
}

// toClientModel 转换为客户端模型.
func (exe *StatusPageQryExe) toClientModel(request *v1.StatusPageRequest) *client.StatusPageRequest {
	return &client.StatusPageRequest{
		PageNum:  request.PageNum,
		PageSize: request.PageSize,
		Flag:     request.Flag,
		Check:    request.Check,
		CreateBy: request.CreateBy,
		UpdateBy: request.UpdateBy,
	}
}

// toServerModel 转换为服务端模型.
func (exe *StatusPageQryExe) toServerModel(reply *client.StatusPageReply, err error) (*v1.StatusPageReply, error) {
	rows := make([]*v1.StatusPageReply_Status, len(reply.GetRows()))
	for i, item := range reply.GetRows() {
		rows[i] = &v1.StatusPageReply_Status{
			StatusId:   item.GetStatusId(),
			FileUrl:    item.GetFileUrl(),
			Score:      item.GetScore(),
			Flag:       item.GetFlag(),
			Check:      item.GetCheck(),
			CreateBy:   item.GetCreateBy(),
			CreateTime: item.GetCreateTime(),
			UpdateBy:   item.GetUpdateBy(),
			UpdateTime: item.GetUpdateTime(),
			Remark:     item.GetRemark(),
			Version:    item.GetVersion(),
		}
	}

	return &v1.StatusPageReply{
		Rows:  rows,
		Total: int64(len(rows)),
	}, err
}
