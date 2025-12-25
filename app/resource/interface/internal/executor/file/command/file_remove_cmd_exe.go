package command

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/file"
	client "lersosa/api/resource/service/v1/file"
	"lersosa/app/resource/interface/internal/client/file"
)

// FileRemoveCmdExe 文件删除命令执行器.
type FileRemoveCmdExe struct {
	client *file.Client
}

// NewFileRemoveCmdExe 创建文件删除命令执行器.
func NewFileRemoveCmdExe(client *file.Client) *FileRemoveCmdExe {
	return &FileRemoveCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *FileRemoveCmdExe) ExecuteVoid(ctx context.Context, request *v1.FileRemoveRequest) error {
	_, err := exe.client.RemoveFile(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *FileRemoveCmdExe) toClientModel(request *v1.FileRemoveRequest) *client.FileRemoveRequest {
	files := make([]*client.FileRemoveRequest_File, 0, len(request.Files))
	for _, m := range request.Files {
		files = append(files, &client.FileRemoveRequest_File{
			OssId:   m.OssId,
			Version: m.Version,
		})
	}

	return &client.FileRemoveRequest{Files: files}
}
