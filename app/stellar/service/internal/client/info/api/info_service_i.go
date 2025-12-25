package api

import (
	"context"

	"lersosa/app/stellar/service/internal/client/info/dto"
	"lersosa/app/stellar/service/internal/client/info/dto/co"
)

// ServiceI 星体信息服务客户端接口.
type ServiceI interface {
	// Page 分页查询星体信息接口.
	Page(ctx context.Context, dto *dto.InfoPageQry) ([]*co.InfoCo, error)

	// List 查询星体信息列表接口.
	List(ctx context.Context, dto *dto.InfoListQry) ([]*co.InfoCo, error)

	// Get 获取星体信息接口.
	Get(ctx context.Context, dto *dto.InfoGetQry) (*co.InfoCo, error)

	// Save 保存星体信息接口.
	Save(ctx context.Context, dto *dto.InfoSaveCmd) error

	// Modify 修改星体信息接口.
	Modify(ctx context.Context, dto *dto.InfoModifyCmd) error

	// Remove 删除星体信息接口.
	Remove(ctx context.Context, dto *dto.InfoRemoveCmd) error
}
