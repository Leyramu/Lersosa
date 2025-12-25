package gateway

import (
	"context"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"

	"github.com/google/uuid"
)

// RepoI 资源配置基础设施层仓储接口.
type RepoI interface {
	// Page 分页查询资源配置接口.
	Page(ctx context.Context, condition *model.Condition) ([]*model.Entity, error)

	// List 查询所有资源配置接口.
	List(ctx context.Context, condition *model.Condition) ([]*model.Entity, error)

	// Get 获取资源配置接口.
	Get(ctx context.Context, id uuid.UUID) (*model.Entity, error)

	// Save 保存资源配置接口.
	Save(ctx context.Context, entity *model.Entity) error

	// Modify 修改资源配置接口.
	Modify(ctx context.Context, entity *model.Entity) error

	// Remove 删除资源配置接口.
	Remove(ctx context.Context, entities *[]model.Entity) error
}
