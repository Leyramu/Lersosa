package api

import (
	"context"

	"lersosa/app/stellar/service/internal/client/status/dto"
	"lersosa/app/stellar/service/internal/client/status/dto/co"
)

// ServiceI 星体状态服务客户端接口.
type ServiceI interface {
	// Page 分页查询星体状态接口.
	Page(ctx context.Context, dto *dto.StatusPageQry) ([]*co.StatusCo, error)

	// List 列表查询星体状态接口.
	List(ctx context.Context, dto *dto.StatusListQry) ([]*co.StatusCo, error)

	// Get 获取星体状态接口.
	Get(ctx context.Context, dto *dto.StatusGetQry) (*co.StatusCo, error)

	// Save 保存星体状态接口.
	Save(ctx context.Context, dto *dto.StatusSaveCmd) error

	// Modify 修改星体状态接口.
	Modify(ctx context.Context, dto *dto.StatusModifyCmd) error

	// Remove 删除星体状态接口.
	Remove(ctx context.Context, dto *dto.StatusRemoveCmd) error
}
