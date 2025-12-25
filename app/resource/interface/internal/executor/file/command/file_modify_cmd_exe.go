package command

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/file"
	client "lersosa/api/resource/service/v1/file"
	"lersosa/app/resource/interface/internal/client/file"
)

// FileModifyCmdExe 文件修改命令执行器.
type FileModifyCmdExe struct {
	client *file.Client
}

// NewFileModifyCmdExe 创建文件修改命令执行器.
func NewFileModifyCmdExe(client *file.Client) *FileModifyCmdExe {
	return &FileModifyCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *FileModifyCmdExe) ExecuteVoid(ctx context.Context, request *v1.FileModifyRequest) error {
	_, err := exe.client.ModifyFile(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *FileModifyCmdExe) toClientModel(request *v1.FileModifyRequest) *client.FileModifyRequest {
	return &client.FileModifyRequest{
		OssId:        request.OssId,
		OriginalName: request.OriginalName,
		Ext1:         request.Ext1,
		UpdateBy:     request.UpdateBy,
		Service:      request.Service,
		Version:      request.Version,
	}
}
