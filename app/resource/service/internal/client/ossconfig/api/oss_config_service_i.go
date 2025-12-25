package api

import (
	"context"

	"lersosa/app/resource/service/internal/client/ossconfig/dto"
	"lersosa/app/resource/service/internal/client/ossconfig/dto/co"
)

// ServiceI 资源配置客户端层接口.
type ServiceI interface {
	// Page 分页查询资源配置接口.
	Page(ctx context.Context, dto *dto.OssConfigPageQry) ([]*co.OssConfigCo, error)

	// Get 获取资源配置接口.
	Get(ctx context.Context, dto *dto.OssConfigGetQry) (*co.OssConfigCo, error)

	// GetDefault 获取默认资源配置接口.
	GetDefault(ctx context.Context, dto *dto.OssConfigGetDefaultQry) (*co.OssConfigCo, error)

	// Save 保存资源配置接口.
	Save(ctx context.Context, dto *dto.OssConfigSaveCmd) error

	// Modify 修改资源配置接口.
	Modify(ctx context.Context, dto *dto.OssConfigModifyCmd) error

	// Remove 删除资源配置接口.
	Remove(ctx context.Context, dto *dto.OssConfigRemoveCmd) error
}
