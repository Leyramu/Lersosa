package query

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/file"
	client "lersosa/api/resource/service/v1/file"
	"lersosa/app/resource/interface/internal/client/file"
)

// FilePageQryExe 文件分页查询执行器.
type FilePageQryExe struct {
	client *file.Client
}

// NewFilePageQryExe 创建文件分页查询执行器.
func NewFilePageQryExe(client *file.Client) *FilePageQryExe {
	return &FilePageQryExe{client: client}
}

// Execute 执行命令.
func (exe *FilePageQryExe) Execute(ctx context.Context, request *v1.FilePageRequest) (*v1.FilePageReply, error) {
	return exe.toServerModel(
		exe.client.PageFile(
			ctx, exe.toClientModel(request),
		),
	)
}

// toClientModel 转换为客户端模型.
func (exe *FilePageQryExe) toClientModel(request *v1.FilePageRequest) *client.FilePageRequest {
	return &client.FilePageRequest{
		PageNum:  request.PageNum,
		PageSize: request.PageSize,
	}
}

// toServerModel 转换为服务端模型.
func (exe *FilePageQryExe) toServerModel(reply *client.FilePageReply, err error) (*v1.FilePageReply, error) {
	rows := make([]*v1.FilePageReply_File, len(reply.Rows))
	for i, item := range reply.Rows {
		rows[i] = &v1.FilePageReply_File{
			OssId:        item.OssId,
			FileName:     item.FileName,
			OriginalName: item.OriginalName,
			FileSuffix:   item.FileSuffix,
			Url:          item.Url,
			Ext1:         item.Ext1,
			Service:      item.Service,
			Version:      item.Version,
		}
	}

	return &v1.FilePageReply{
		Rows:  rows,
		Total: int64(len(rows)),
	}, err
}
