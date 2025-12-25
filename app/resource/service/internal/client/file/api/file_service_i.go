package api

import (
	"context"

	"lersosa/app/resource/service/internal/client/file/dto"
	"lersosa/app/resource/service/internal/client/file/dto/co"
)

// ServiceI 文件客户端层接口.
type ServiceI interface {
	// Page 分页查询文件接口.
	Page(ctx context.Context, dto *dto.FilePageQry) ([]*co.FileCo, error)

	// Get 获取文件接口.
	Get(ctx context.Context, dto *dto.FileGetQry) (*co.FileCo, error)

	// Save 保存文件接口.
	Save(ctx context.Context, dto *dto.FileSaveCmd) error

	// Modify 修改文件接口.
	Modify(ctx context.Context, dto *dto.FileModifyCmd) error

	// Remove 删除文件接口.
	Remove(ctx context.Context, dto *dto.FileRemoveCmd) error
}
