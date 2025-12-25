package query

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/file"
	client "lersosa/api/resource/service/v1/file"
	"lersosa/app/resource/interface/internal/client/file"
)

// FileGetQryExe 获取文件查询执行器.
type FileGetQryExe struct {
	client *file.Client
}

// NewFileGetQryExe 创建获取文件查询执行器.
func NewFileGetQryExe(client *file.Client) *FileGetQryExe {
	return &FileGetQryExe{client: client}
}

// Execute 执行命令.
func (exe *FileGetQryExe) Execute(ctx context.Context, request *v1.FileGetRequest) (*v1.FileGetReply, error) {
	return exe.toServerModel(
		exe.client.GetFile(
			ctx, exe.toClientModel(request),
		),
	)
}

// toClientModel 转换为客户端模型.
func (exe *FileGetQryExe) toClientModel(request *v1.FileGetRequest) *client.FileGetRequest {
	return &client.FileGetRequest{
		OssId: request.OssId,
	}
}

// toServerModel 转换为服务端模型.
func (exe *FileGetQryExe) toServerModel(reply *client.FileGetReply, err error) (*v1.FileGetReply, error) {
	return &v1.FileGetReply{
		Data: &v1.FileGetReply_File{
			OssId:        reply.OssId,
			FileName:     reply.FileName,
			OriginalName: reply.OriginalName,
			FileSuffix:   reply.FileSuffix,
			Url:          reply.Url,
			Ext1:         reply.Ext1,
			Service:      reply.Service,
			Version:      reply.Version,
		},
	}, err
}
