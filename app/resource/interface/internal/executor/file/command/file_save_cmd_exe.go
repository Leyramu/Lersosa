package command

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/file"
	client "lersosa/api/resource/service/v1/file"
	"lersosa/app/resource/interface/internal/client/file"
)

// FileSaveCmdExe 文件保存命令执行器.
type FileSaveCmdExe struct {
	client *file.Client
}

// NewFileSaveCmdExe 创建文件保存命令执行器.
func NewFileSaveCmdExe(client *file.Client) *FileSaveCmdExe {
	return &FileSaveCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *FileSaveCmdExe) ExecuteVoid(ctx context.Context, request *v1.FileSaveRequest) error {
	_, err := exe.client.SaveFile(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *FileSaveCmdExe) toClientModel(request *v1.FileSaveRequest) *client.FileSaveRequest {
	return &client.FileSaveRequest{
		FileName:     request.FileName,
		OriginalName: request.OriginName,
		FileSuffix:   request.FileSuffix,
		Url:          request.Url,
		Ext1:         request.Ext1,
		CreateDept:   request.CreateDept,
		CreateBy:     request.CreateBy,
		Service:      request.Service,
	}
}
