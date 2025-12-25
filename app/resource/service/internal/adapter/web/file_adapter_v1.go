package web

import (
	"context"
	v1 "lersosa/api/resource/service/v1/file"
	file "lersosa/app/resource/service/internal/client/file/api"
	"lersosa/app/resource/service/internal/client/file/dto"
	"lersosa/app/resource/service/internal/client/file/dto/co"
)

// FileAdapter 文件适配层.
type FileAdapter struct {
	// v1.UnimplementedFileServer 未实现的文件适配层接口.
	v1.UnimplementedFileServer

	// service 文件客户端层接口.
	service file.ServiceI
}

// NewFileAdapter 构造文件适配层.
func NewFileAdapter(service file.ServiceI) *FileAdapter {
	return &FileAdapter{service: service}
}

// PageFile 实现分页查询文件方法.
func (adapter *FileAdapter) PageFile(ctx context.Context, request *v1.FilePageRequest) (*v1.FilePageReply, error) {
	page, err := adapter.service.Page(ctx, dto.NewFilePageQry(request))

	if err != nil {
		return &v1.FilePageReply{Rows: nil}, err
	}

	var rows []*v1.FilePageReply_File
	for _, item := range page {
		rows = append(rows, co.NewFilePageCo(item))
	}

	return &v1.FilePageReply{Rows: rows}, nil
}

// GetFile 实现查询文件方法.
func (adapter *FileAdapter) GetFile(ctx context.Context, request *v1.FileGetRequest) (*v1.FileGetReply, error) {
	get, err := adapter.service.Get(ctx, dto.NewFileGetQry(request))

	if err != nil {
		return &v1.FileGetReply{}, err
	}

	return co.NewFileGetCo(get), nil
}

// SaveFile 实现保存文件方法.
func (adapter *FileAdapter) SaveFile(ctx context.Context, request *v1.FileSaveRequest) (*v1.FileSaveReply, error) {
	return &v1.FileSaveReply{}, adapter.service.Save(ctx, dto.NewFileSaveCmd(request))
}

// ModifyFile 实现修改文件方法.
func (adapter *FileAdapter) ModifyFile(ctx context.Context, request *v1.FileModifyRequest) (*v1.FileModifyReply, error) {
	return &v1.FileModifyReply{}, adapter.service.Modify(ctx, dto.NewFileModifyCmd(request))
}

// RemoveFile 实现删除文件方法.
func (adapter *FileAdapter) RemoveFile(ctx context.Context, request *v1.FileRemoveRequest) (*v1.FileRemoveReply, error) {
	return &v1.FileRemoveReply{}, adapter.service.Remove(ctx, dto.NewFileRemoveCmd(request))
}
