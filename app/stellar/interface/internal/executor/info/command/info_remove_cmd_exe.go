package command

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/info"
	client "lersosa/api/stellar/service/v1/info"
	"lersosa/app/stellar/interface/internal/client/info"
)

// InfoRemoveCmdExe 星体信息删除命令执行器.
type InfoRemoveCmdExe struct {
	client *info.Client
}

// NewInfoRemoveCmdExe 构造星体信息删除命令执行器.
func NewInfoRemoveCmdExe(client *info.Client) *InfoRemoveCmdExe {
	return &InfoRemoveCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *InfoRemoveCmdExe) ExecuteVoid(ctx context.Context, request *v1.InfoRemoveRequest) error {
	_, err := exe.client.RemoveInfo(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *InfoRemoveCmdExe) toClientModel(request *v1.InfoRemoveRequest) *client.InfoRemoveRequest {
	infos := make([]*client.InfoRemoveRequest_Info, 0, len(request.GetInfos()))
	for _, req := range request.GetInfos() {
		infos = append(infos, &client.InfoRemoveRequest_Info{
			InfoId:  req.GetInfoId(),
			Version: req.GetVersion(),
		})
	}

	return &client.InfoRemoveRequest{Infos: infos}
}
